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
import com.acm431.complaintmanagement.ComplaintActivity
import com.acm431.complaintmanagement.R
import com.acm431.complaintmanagement.viewmodel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*

class LogInFragment : BaseFragment() {
    private lateinit var viewModel: AuthViewModel
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        if(auth.currentUser!=null){

            val intent = Intent(requireContext(), ComplaintActivity::class.java)
            intent.putExtra("toProfile", 1)
            startActivity(intent)
            requireActivity().finish()

        }
    }

    override fun onResume() {
        super.onResume()

        register_button_login.setOnClickListener {
            val action = LogInFragmentDirections.actionLogInFragmentToSignUpFragment()
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        login_button.setOnClickListener {
            loginBtn()
        }

        btn_forgot.setOnClickListener {
            forgotBtn()
        }
    }

    private fun forgotBtn() {
        val email = et_email.text.toString()

        if (email.isNotEmpty())
            auth.sendPasswordResetEmail(email)
        else
            Toast.makeText(
                this.context,
                "Lütfen şifresini sıfırlamak istediğiniz e-maili girin",
                Toast.LENGTH_LONG
            ).show()
    }

    private fun loginBtn() {
        val email = et_email.text.toString()
        val password = et_password.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty())
            viewModel.login(email, password)
        else
            Toast.makeText(
                this.context,
                "Lütfen email ve password alanlarını boş bırakmayın",
                Toast.LENGTH_SHORT
            ).show()

        viewModel.loginResult.observe(viewLifecycleOwner, Observer { result ->
            if (result) {
                val intent = Intent(requireContext(), ComplaintActivity::class.java)
                intent.putExtra("toProfile", 1)
                startActivity(intent)
                requireActivity().finish()
            }
        })

/*       viewModel.loginLoading.observe(viewLifecycleOwner, Observer { loading->
            if(loading){
                showProgressBar(getString(R.string.please_wait))
            }
            else {
                println("loading stopped")
                hideProgressBar()
            }
            println("Loading:" + loading)
        })*/

        viewModel.loginError.observe(viewLifecycleOwner, Observer { error->
            println("Error :" + error)
            if(error){
                viewModel.errorMessage.observe(viewLifecycleOwner) {
                    showErrorSnackBar(it,true)
                }
            }
        })
    }
}