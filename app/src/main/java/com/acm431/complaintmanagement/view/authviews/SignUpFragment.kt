package com.acm431.complaintmanagement.view.authviews

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.acm431.complaintmanagement.BaseFragment
import com.acm431.complaintmanagement.MainActivity
import com.acm431.complaintmanagement.R
import com.acm431.complaintmanagement.model.User
import com.acm431.complaintmanagement.viewmodel.AuthViewModel
import kotlinx.android.synthetic.main.fragment_signup.*

class SignUpFragment : BaseFragment() {
    private lateinit var viewModel: AuthViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AuthViewModel::class.java)
    }

    override fun onResume() {
        super.onResume()

        tv_already_button.setOnClickListener {
            val action = SignUpFragmentDirections.actionSignUpFragmentToLogInFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_signup, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        register_button.setOnClickListener {
            registerBtn()
        }
    }

    private fun makeShortTost(message: String) {
        Toast.makeText(
            this.context,
            message,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun registerBtn() {
        val username = et_username.text.toString()
        val email = et_email.text.toString()
        val password = et_password.text.toString()
        val passAgain = et_password_again.text.toString()
        val citizenID = et_citizen_id.text.toString()
        val user = User(
            username = username,
            email = email,
            password = password,
            identityNumber = citizenID
        )

        if (username.isEmpty() && email.isEmpty()
            && password.isEmpty() && passAgain.isEmpty() && citizenID.isEmpty())
            makeShortTost("Lütfen boş bıraktığınız alan olmadığından emin olun")
        else if (passAgain != password)
            makeShortTost("Girdiğiniz şifreler uyuşmuyor")
        else if (!cb_terms_and_condition.isChecked)
            makeShortTost("Lütfen kullanım koşullarını kabul edin !")
        else{
            viewModel.register(user)
            viewModel.registrationSucces.observe(viewLifecycleOwner) { registrationSuccess ->
                if (registrationSuccess) {
                    makeShortTost("Hesabınız oluşturuldu !")

                    val intent = Intent(requireContext(),MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }

                else {
                    viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
                        if (errorMessage != null) {
                            showErrorSnackBar(errorMessage,true)
                        }
                    }
                }
            }
        }

/*        viewModel.registerLoading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                showProgressBar(getString(R.string.please_wait))
            } else {
                hideProgressBar()
            }

        })

        viewModel.registerError.observe(viewLifecycleOwner, Observer { error ->
            if (error) {
                //showErrorSnackBar(getString(R.string.an_error_occured), true)
            }
        })*/
    }
}