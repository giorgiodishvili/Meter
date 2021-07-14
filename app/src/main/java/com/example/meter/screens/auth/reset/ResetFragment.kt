package com.example.meter.screens.auth.reset

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.meter.R
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.ResetFragmentBinding
import com.example.shualeduri.extensions.showToast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

class ResetFragment : BaseFragment<ResetFragmentBinding, ResetViewModel>(
    ResetFragmentBinding::inflate,
    ResetViewModel::class.java
) {

    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        init()
    }

    private fun init() {
        reset()
    }

    private fun reset() {
        binding.resetButton.setOnClickListener {

            val email = binding.editTextTextEmailAddress.text.toString()
            if (email.isNotEmpty()) {
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener { process ->

                    if (process.isSuccessful) {
                        findNavController().navigate(R.id.action_resetFragment_to_loginFragment)
                    } else {
                        requireActivity().showToast("Error")
                    }
                }
            } else {
                requireActivity().showToast("Empty fields")
            }
        }
    }

}