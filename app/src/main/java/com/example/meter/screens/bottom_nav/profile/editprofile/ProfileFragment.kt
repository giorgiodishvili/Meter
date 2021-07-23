package com.example.meter.screens.bottom_nav.profile.editprofile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.util.Log.d
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.meter.R
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.ProfileFragmentBinding
import com.example.meter.extensions.*
import com.example.meter.network.Resource
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import com.example.shualeduri.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class ProfileFragment : BaseFragment<ProfileFragmentBinding, ProfileViewModel>(
    ProfileFragmentBinding::inflate,
    ProfileViewModel::class.java
) {

    @Inject
    lateinit var firebaseAuthImpl: FirebaseRepositoryImpl


    private lateinit var imageUri: Uri
    private var authorisedWithGoogle: Boolean = false

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        init()
        observers()
    }

    private fun init() {

        authorisedWithGoogle = firebaseAuthImpl.getUser()?.photoUrl != null

        if (authorisedWithGoogle) {
            googleUserLogin()
            d("uriuri", "${firebaseAuthImpl.getUser()?.photoUrl}")
            firebaseAuthImpl.getUserId()?.let { viewModel.loadUserInfo(it, false) }
        } else {
            firebaseAuthImpl.getUserId()?.let { viewModel.loadUserInfo(it, true) }
        }


        if (binding.tvUserName.text.isNullOrBlank()) {
            binding.status.setGone()
            binding.tvUserName.setGone()
        }

        listeners()
    }

    private fun listeners() {

        binding.saveButton.setOnClickListener {
            if (authorisedWithGoogle)
                uploadGoogleInfo()
            else
                saveToDB()
        }

        binding.addImage.setOnClickListener {
            getUri()
        }

        binding.backbtn.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_completeProfileInfo_to_navigation_profile)
        }

        binding.nameInput.doOnTextChanged { _, _, _, count ->
            if (count != 0)
                binding.nameInput.removeDrawableEnd()
        }

        binding.numberInput.doOnTextChanged { _, _, _, count ->
            if (count != 0)
                binding.numberInput.removeDrawableEnd()
        }
    }

    private fun saveToDB() {

        val name = binding.nameInput.text.toString().trim()
        val number = binding.numberInput.text.toString().trim()
        val emailInfo = firebaseAuthImpl.getUser()?.email

        if (inputCheck()) {

            if (!this::imageUri.isInitialized) {
                val defaultImage = "https://i.ibb.co/0rPxJQ0/profile-pic.png"
                viewModel.uploadUserInfo(emailInfo!!, name, number, defaultImage, true, null, false)
                d("tagtag", "hee")
            } else {
                viewModel.uploadUserInfo(emailInfo!!, name, number,
                    imageUri.toString(), true, imageUri, true)
            }
        } else
            requireActivity().showToast("Fill fields correctly")

    }


    private fun observers() {
        viewModel.readUserInfo.observe(viewLifecycleOwner, { status ->
            when (status.status) {
                Resource.Status.SUCCESS -> {
                    status.data?.name?.let {
                        setVerified(it)
                    }
                }
                Resource.Status.ERROR -> {
                    d("errorTAG", "${status.message}")
                }
                Resource.Status.LOADING -> {
                }
            }
        })

        viewModel.uploadImageStatus.observe(viewLifecycleOwner, { status ->
            if (!status)
                requireActivity().showToast("Failed")
        })

        viewModel.postUserInfo.observe(viewLifecycleOwner, { result ->
            when (result.status) {
                Resource.Status.SUCCESS -> {
                    d("errorTAG", "${result.data}")
                    requireActivity().showToast("success")

                    if (result != null && result.data?.verified == true) {
                        binding.tvUserName.show()
                        binding.status.show()
                        binding.tvUserName.text = result.data.name
                        showInputForVerified()
                    }
                }
                Resource.Status.ERROR -> {
                    d("errorTAG", "${result.message}")
                }
                Resource.Status.LOADING -> {

                }
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
            if (data != null) {
                imageUri = data.data!!
                binding.userImage.loadImgUri(imageUri)
            }
    }

    private fun showInputForVerified() {
        binding.nameInput.setDrawableEnd(requireContext(), R.drawable.ic_checked)
        binding.numberInput.setDrawableEnd(requireContext(), R.drawable.ic_checked)
    }

    private fun inputCheck(): Boolean {
        return !binding.nameInput.text.isNullOrBlank() && !binding.numberInput.text.isNullOrBlank() && binding.numberInput.text!!.length == 9
    }

    private fun setVerified(name: String) {
        d("errorTAG", "hwl")

        binding.tvUserName.show()
        binding.status.show()
        binding.tvInfoTitle.text = getString(R.string.edit_info)

        if (!authorisedWithGoogle) {
            d("errorTAG", "normal login")
            binding.tvUserName.text = name
        } else {
            d("errorTAG", "logined from google")
            binding.tvUserName.text = firebaseAuthImpl.getUser()?.displayName
        }

        showInputForVerified()
        clearInputs()

    }

    private fun googleUserLogin() {

        binding.addImage.setGone()
        binding.status.setGone()

        binding.nameInput.setReadOnly(true)
        binding.nameInput.setDrawableEnd(requireContext(), R.drawable.ic_checked)

        binding.userImage.loadImgUri(firebaseAuthImpl.getUser()!!.photoUrl)
        binding.tvUserName.text = firebaseAuthImpl.getUser()!!.displayName
        binding.tvInfoTitle.text = getString(R.string.add_phone)

    }

    private fun uploadGoogleInfo() {
        val name = firebaseAuthImpl.getUser()!!.displayName
        val number = binding.numberInput.text.toString().trim()
        val email = firebaseAuthImpl.getUser()!!.email

        if (number.isNotBlank()) {
            firebaseAuthImpl.getUserId()?.let { uid ->
                viewModel.uploadUserInfo(email!!, name!!, number,
                    firebaseAuthImpl.getUser()!!.photoUrl.toString(), true)
            }
        } else
            requireActivity().showToast("Fields are blank")
    }

    private fun clearInputs() {
        binding.nameInput.text?.clear()
        binding.numberInput.text?.clear()
    }

}