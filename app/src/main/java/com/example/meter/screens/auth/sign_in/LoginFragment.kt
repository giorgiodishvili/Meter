package com.example.meter.screens.auth.sign_in

import android.os.Bundle
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
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<LoginFragmentBinding, LoginViewModel>(
    LoginFragmentBinding::inflate,
    LoginViewModel::class.java
) {
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
        binding.loginButton.setOnClickListener {
            login()
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

    private fun observer() {
        viewModel.loginStatus.observe(viewLifecycleOwner, { loginStatus ->
            if (loginStatus) {
                val navController = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                navController.navController.navigate(R.id.action_global_navigation_profile)
            } else {
                requireActivity().showToast("Error")
            }
        })
    }

    private fun registration() {
        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    private fun reset() {
        findNavController().navigate(R.id.action_loginFragment_to_resetFragment)
    }

}