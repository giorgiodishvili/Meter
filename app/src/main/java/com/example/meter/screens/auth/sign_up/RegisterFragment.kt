package com.example.meter.screens.auth.sign_up

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.meter.R
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.RegisterFragmentBinding
import com.example.shualeduri.extensions.showToast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

class RegisterFragment : BaseFragment<RegisterFragmentBinding, RegisterViewModel>(
    RegisterFragmentBinding::inflate,
    RegisterViewModel::class.java
) {

    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        init()
    }

    private fun init() {

        binding.registrationRegButton.setOnClickListener {
            register()
        }

        binding.toLogin.setOnClickListener {
            findNavController().navigate(R.id.action_resetFragment_to_loginFragment)
        }

    }

    private fun register() {
        val email = binding.emailRegInput.text.toString()
        val password = binding.passRegInput.text.toString()
        val passwordConfirmed = binding.passRegConfInput.text.toString()

        if (email.isEmpty() || password.isEmpty() || passwordConfirmed.isEmpty() || password != passwordConfirmed) {
            requireActivity().showToast("Fill fields")
        } else {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { process ->
                if (process.isSuccessful) {
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    requireActivity().showToast("You are now registered")
                } else {
                    requireActivity().showToast("Error check the internet connection")
                }
            }
        }
    }
}