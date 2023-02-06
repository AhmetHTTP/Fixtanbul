package com.acm431.complaintmanagement.view.complaintviews


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.acm431.complaintmanagement.BaseFragment
import com.acm431.complaintmanagement.LoadGlide
import com.acm431.complaintmanagement.MainActivity
import com.acm431.complaintmanagement.R
import com.acm431.complaintmanagement.database.GlobalValues
import com.acm431.complaintmanagement.model.Complaint
import com.acm431.complaintmanagement.viewmodel.AddComplaintViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_complaint.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.*
import java.io.File
import java.util.*
import kotlin.collections.HashMap


class AddComplaintFragment : BaseFragment() {

    private var locationLoading = MutableLiveData<Boolean>()
    private val locationManager by lazy {
        requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager?
    }
    private lateinit var viewModel: AddComplaintViewModel
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private var selectedPicture: Uri? = null
    private var addressString: String? = null
    private var latLongMap = hashMapOf <String, Double>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main)


    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // Precise location access granted.
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // Only approximate location access granted.
            }
            else -> {

            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_complaint, container, false)
    }
    override fun onResume() {
        super.onResume()

        iv_complaint_image.setOnClickListener {
            imageViewClicked()
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[AddComplaintViewModel::class.java]


        requestLocationPermission()

        registerLauncher()

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        et_complaint_location.visibility = View.GONE
        til_location.visibility = View.GONE
        runWait()

        locationLoading.observe(viewLifecycleOwner) {
            if (it == true) {
                showProgressBar("Konum alınıyor lütfen bekleyin")
            }
            else
                hideProgressBar()
        }

        btn_make_complaint.setOnClickListener {
            val givenAddress = et_complaint_location.text.toString()
            val complaintContent = et_complaint.text.toString()
            val complaintLocation: String? = addressString
            val complaint = Complaint(
                userName = GlobalValues.userName.value.toString(),
                content = complaintContent,
                location = complaintLocation ?: givenAddress,
                status = "Ekipler yönlendirildi.",
                urgency = "Not urgent",
                latitude = latLongMap["latitude"] ?: 0.0,
                longitude = latLongMap["longitude"] ?: 0.0,
            )
            viewModel.saveImageToStorage(selectedPicture!!, complaint)

        }
    }

    private fun runWait() {
        coroutineScope.launch {
            wait()
        }
    }

    private suspend fun wait() {
        delay(10000L)
        hideProgressBar()
        if(addressString == null){
            showErrorSnackBar(getString(R.string.location_not_found),true)
            et_complaint_location.visibility = View.VISIBLE
            til_location.visibility = View.VISIBLE
        }

    }


    private fun imageViewClicked() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.CAMERA
                )
            ) {
                Snackbar.make(
                    requireView(),
                    "Permission needed for camera",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(getString(R.string.give_permission)) {
                    //request permission
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }.show()
            } else {
                //request permission
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        } else {
            val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val fileName = "new-photo-name.jpg"
            val values = ContentValues()
            values.put(MediaStore.Images.Media.TITLE, fileName)
            values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera")
            selectedPicture =
                requireContext().contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    values
                )
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedPicture)
            activityResultLauncher.launch(takePhotoIntent)

        }
    }

    private fun registerLauncher() {
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        val bitmap = MediaStore.Images.Media.getBitmap(
                            requireContext().contentResolver,
                            selectedPicture
                        )
                        iv_complaint_image.setImageBitmap(bitmap)
                    }
                }
            }

        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    //permission granted
                    val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val fileName = "new-photo-name.jpg"
                    val values = ContentValues()
                    values.put(MediaStore.Images.Media.TITLE, fileName)
                    values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera")
                    selectedPicture =
                        requireContext().contentResolver.insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            values
                        )
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, selectedPicture)
                    activityResultLauncher.launch(takePhotoIntent)
                } else {
                    //permission denied
                    Toast.makeText(
                        requireActivity(),
                        "Permission needed for camera",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun requestLocationPermission() {

         when {
             ContextCompat.checkSelfPermission(
                 requireContext(),
                 Manifest.permission.ACCESS_COARSE_LOCATION
             ) == PackageManager.PERMISSION_GRANTED -> {

                 locationLoading.value = true
                 locationManager!!.requestSingleUpdate(
                     LocationManager.GPS_PROVIDER,
                     object : LocationListener {
                         override fun onLocationChanged(location: Location) {
                             val geocoder = Geocoder(requireContext(), Locale.getDefault())
                             val addresses =
                                 geocoder.
                                 getFromLocation(location!!.latitude, location.longitude, 1)
                             val address = addresses[0]
                             addressString = address.locality
                             //latLongMap = hashMapOf()
                             val latitude = address.latitude
                             val longitude = address.longitude
                             latLongMap["latitude"] = latitude
                             latLongMap["longitude"] = longitude
                             println(addressString)
                             locationLoading.value = false
                         }

                         override fun onStatusChanged(
                             provider: String?,
                             status: Int,
                             extras: Bundle?
                         ) {
                         }

                         override fun onProviderEnabled(provider: String) {
                             println("gps enabled")
                         }

                         override fun onProviderDisabled(provider: String) {
                             println("gps disabled")

                         }

                     },
                     Looper.getMainLooper()

                 )

             }

             else -> {
                 locationPermissionRequest.launch(
                     arrayOf(
                         Manifest.permission.ACCESS_FINE_LOCATION,
                         Manifest.permission.ACCESS_COARSE_LOCATION
                     )
                 )
             }
         }
    }
}

