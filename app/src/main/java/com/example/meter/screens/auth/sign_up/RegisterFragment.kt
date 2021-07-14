package com.example.meter.screens.auth.sign_up

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.meter.R
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.RegisterFragmentBinding
import com.example.meter.extensions.isEmail
import com.example.meter.extensions.isNotEmail
import com.example.shualeduri.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<RegisterFragmentBinding, RegisterViewModel>(
    RegisterFragmentBinding::inflate,
    RegisterViewModel::class.java
) {

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        init()
    }

    private fun init() {

        binding.registrationRegButton.setOnClickListener {
            register()
        }

        binding.toLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

    }

    private fun register() {
        val email = binding.emailRegInput.text.toString()
        val password = binding.passRegInput.text.toString()
        val passwordConfirmed = binding.passRegConfInput.text.toString()
        validate(email, password, passwordConfirmed)

    }

    private fun validate(email: String, password: String, passwordConfirmed: String) {
        if (email.isEmpty() || password.isEmpty() || passwordConfirmed.isEmpty()) {
            requireActivity().showToast("fill the fields")
        } else {
            if (email.isNotEmail())
                binding.emailRegInput.error = "enter valid email"
            if (password.length < 6)
                binding.passRegInput.error = "password must be at least 6 char"
            if (password != passwordConfirmed)
                binding.passRegConfInput.error = "passwords does't match"
            if (password.length >= 6 && password == passwordConfirmed && email.isEmail())
                viewModel.registerStart(email, password)
                observer()
        }
    }

    private fun observer() {
        viewModel.registerStatus.observe(viewLifecycleOwner, { regStatus ->
            if (regStatus)
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            else
                requireActivity().showToast("Error")
        })
    }
}