package com.example.meter.screens.bottom_nav.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.meter.R
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.ProfileFragmentBinding
import com.example.meter.entity.UserDetails
import com.example.meter.extensions.*
import com.example.meter.repository.FirebaseRepository
import com.example.shualeduri.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileFragmentBinding, ProfileViewModel>(
    ProfileFragmentBinding::inflate,
    ProfileViewModel::class.java
) {

    @Inject
    lateinit var firebaseAuth: FirebaseRepository

    private lateinit var imageUri: Uri

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (firebaseAuth.getUserId() == null) {
            findNavController().navigate(R.id.action_navigation_profile_to_main_auth)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {

        init()
        observers()
    }

    private fun init() {

        val authorisedWithGoogle = firebaseAuth.getUser()?.photoUrl != null

        if (authorisedWithGoogle)
            googleUser()
        else
            readFromDb()


        if (binding.tvUserName.text.isNullOrBlank()) {
            binding.tvUserName.setGone()
            binding.status.setGone()
        }

        binding.saveButton.setOnClickListener {
            if (!authorisedWithGoogle && inputCheck())
                saveToDB()
            else
                requireActivity().showToast("Fill fields correctly")
        }

        binding.addImage.setOnClickListener {
            getUri()
        }

        binding.logOutButton.setOnClickListener {
            firebaseAuth.signOut()
            findNavController().navigate(R.id.action_navigation_profile_to_main_auth)
        }

    }

    private fun saveToDB() {
        val name = binding.nameInput.text.toString().trim()
        val number = binding.numberInput.text.toString().trim()
        val emailInfo = binding.emailInput.text.toString().trim()

        val model = UserDetails(name, number, emailInfo)
        viewModel.uploadUserInfo(firebaseAuth.getUserId()!!, model, imageUri)
    }

    private fun readFromDb() {
        viewModel.readFromDb(firebaseAuth.getUser()!!.uid)
    }

    private fun observers() {
        viewModel.writeInDbStatus.observe(viewLifecycleOwner, { status ->
            if (status) {
                setVerified()
            } else
                requireActivity().showToast("Error")

        })

        viewModel.uploadImageStatus.observe(viewLifecycleOwner, { status ->
            if (!status)
                requireActivity().showToast("Failed")
        })

        viewModel.readFormDbStatus.observe(viewLifecycleOwner, { result ->
            Log.d("tagtag", "$result")
            if (result != null) {
                binding.tvUserName.show()
                binding.status.show()
                binding.tvUserName.text = result.name
                disableInputs()
            }
        })

        viewModel.readImageStatus.observe(viewLifecycleOwner, { image ->
            binding.userImage.loadImgUri(image)
            imageUri = image
        })
    }

    private fun getUri() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == RESULT_OK)
            imageUri = data?.data!!
        binding.userImage.loadImgUri(imageUri)
    }

    private fun disableInputs() {
        binding.nameInput.setReadOnly(true)
        binding.numberInput.setReadOnly(true)
        binding.emailInput.setReadOnly(true)
    }

    private fun inputCheck(): Boolean {

        return !binding.nameInput.text.isNullOrBlank() && !binding.numberInput.text.isNullOrBlank() && binding.emailInput.text.toString()
            .isEmail() && binding.numberInput.text!!.length == 9
    }

    private fun setVerified() {
        val name = binding.nameInput.text.toString().trim()
        binding.tvUserName.show()
        binding.status.show()
        binding.tvUserName.text = name
        binding.tvInfoTitle.text = getString(R.string.edit_info)
        requireActivity().showToast("Successfully saved")
    }

    private fun googleUser() {
        binding.addImage.setGone()
        binding.userImage.loadImgUri(firebaseAuth.getUser()!!.photoUrl)
        binding.tvUserName.text = firebaseAuth.getUser()!!.displayName
        binding.tvInfoTitle.text = getString(R.string.edit_info)
        disableInputs()
    }

}