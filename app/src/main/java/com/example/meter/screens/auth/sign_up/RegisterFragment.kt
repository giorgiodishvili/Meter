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
                binding.emailRegInput.error = "შეიყვანე სწორი ელ-ფოსტა"
            if (password.length < 6)
                binding.passRegInput.error = "პაროლი უნდა შეიცავდეს მინიმუმ 6 სიმბოლოს"
            if (password != passwordConfirmed)
                binding.passRegConfInput.error = "პაროლები არ ემთხვევა"
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
                popDialog(R.layout.dialog_item_error, R.id.errorMsg, "მოხვდა რაღაც შეცდომა")
        })
    }
}