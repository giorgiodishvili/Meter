package com.example.meter.screens.auth.reset

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.meter.R
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.ResetFragmentBinding
import com.example.meter.extensions.isEmail
import com.example.meter.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetFragment : BaseFragment<ResetFragmentBinding, ResetViewModel>(
    ResetFragmentBinding::inflate,
    ResetViewModel::class.java
) {

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        init()
    }

    private fun init() {
        binding.resetButton.setOnClickListener {
            reset()
        }
    }

    private fun reset() {
        val email = binding.editTextTextEmailAddress.text.toString()

        if (email.isNotEmpty() && email.isEmail()) {
            viewModel.resetStart(email)
            observer()
        } else {
            requireActivity().showToast("Empty fields")
        }
    }

    private fun observer() {
        viewModel.resetStatus.observe(viewLifecycleOwner, { resetStatus ->
            if (resetStatus) {
                popDialog(
                    R.layout.dialog_item_sent,
                    R.id.errorMsg,
                    "წერილი გაიგზავნა თქვენს ელ-ფოსტაზე",
                    true
                )
                findNavController().navigate(R.id.action_resetFragment_to_loginFragment)
            } else
                popDialog(R.layout.dialog_item_error, R.id.errorMsg, "მოხვდა რაღაც შეცდომა")
        })
    }

}