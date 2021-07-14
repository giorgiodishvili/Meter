package com.example.meter.screens.auth.sign_in

import android.content.Intent
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.meter.R
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.LoginFragmentBinding
import com.example.meter.extensions.isEmail
import com.example.meter.extensions.isNotEmail
import com.example.shualeduri.extensions.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginFragmentBinding, LoginViewModel>(
    LoginFragmentBinding::inflate,
    LoginViewModel::class.java
) {

    companion object {
        const val RC_SIGN_IN = 13
    }

    private lateinit var googleSignInClient: GoogleSignInClient
    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mAuth.currentUser != null) {
            val navController = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            navController.navController.navigate(R.id.action_global_navigation_profile)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        init()
    }

    private fun init() {
        googleServiceInit()

        binding.loginButton.setOnClickListener {
            login()
        }

        binding.loginGoogle.setOnClickListener {
            signInGoogle()
        }

        binding.registerButton.setOnClickListener {
            registration()
        }

        binding.resetButton.setOnClickListener {
            reset()
        }
    }

    private fun validate(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            requireActivity().showToast("fill the fields")
        } else {
            if (email.isNotEmail())
                binding.loginInput.error = "enter valid email"
            if (password.length < 6)
                binding.passInput.error = "password must be at least 6 char"
            if (password.length >= 6 && email.isEmail())
                viewModel.loginStart(email, password)
                observer()
        }
    }

    private fun login() {
        val email = binding.loginInput.text.toString()
        val password = binding.passInput.text.toString()
        validate(email, password)
    }

    private fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun googleServiceInit() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

    }

    private fun observer() {
        viewModel.loginStatus.observe(viewLifecycleOwner, { loginStatus ->
            if (loginStatus) {
                val navController = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                navController.navController.navigate(R.id.action_global_navigation_profile)
            } else {
                requireActivity().showToast("Error")
            }
        })

        viewModel.loginGoogleStatus.observe(viewLifecycleOwner, { loginStatus ->
            d("SignInActivity", "$loginStatus")

            if (loginStatus) {
                val navController = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                navController.navController.navigate(R.id.action_global_navigation_profile)
            } else {
                requireActivity().showToast("Error")
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception = task.exception
            if (task.isSuccessful) {
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    viewModel.firebaseAuthWithGoogle(account.idToken!!)
                    observer()
                } catch (e: ApiException) {
                    d("SignInActivity", "Google sign in failed", e)
                }
            } else {
                d("SignInActivity", exception.toString())
            }
        }
    }

    private fun registration() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun reset() {
        findNavController().navigate(R.id.action_loginFragment_to_resetFragment)
    }

}