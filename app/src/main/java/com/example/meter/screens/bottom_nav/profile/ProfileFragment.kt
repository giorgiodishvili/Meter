package com.example.meter.screens.bottom_nav.profile

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.navigation.fragment.findNavController
import com.example.meter.R
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.ProfileFragmentBinding
import com.example.meter.entity.UserDetails
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (firebaseAuthImpl.getUserId() == null) {
            findNavController().navigate(R.id.action_navigation_profile_to_main_auth)
        }
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        init()
        observers()
    }

    private fun init() {

        firebaseAuthImpl.getUserId()?.let {
            viewModel.loadUserInfo()
        }

        authorisedWithGoogle = firebaseAuthImpl.getUser()?.photoUrl != null

        if (authorisedWithGoogle)
            googleUserLogin()
        else



        if (binding.tvUserName.text.isNullOrBlank()) {
            binding.status.setGone()
            binding.tvUserName.setGone()
        }

        binding.saveButton.setOnClickListener {
            if (authorisedWithGoogle)
                uploadGoogleInfo()
            else
                saveToDB()
        }

        binding.addImage.setOnClickListener {
            getUri()
        }

        binding.logOutButton.setOnClickListener {
            firebaseAuthImpl.signOut()
            findNavController().navigate(R.id.action_navigation_profile_to_main_auth)
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
//            val model = UserDetails(name, number, emailInfo, true)

            if (!this::imageUri.isInitialized) {
                viewModel.uploadUserInfo(emailInfo!!, name, number, true, null, false)
                d("tagtag", "hee")
            } else {
                viewModel.uploadUserInfo(emailInfo!!, name, number, true, imageUri, true)
            }
        } else
            requireActivity().showToast("Fill fields correctly")

    }


    private fun observers() {
        viewModel.readUserInfo.observe(viewLifecycleOwner, { status ->
            when (status.status){
                Resource.Status.SUCCESS -> {
                    status.data?.name?.let {
                        setVerified(it)
                    }
                }
                Resource.Status.ERROR -> {
                    requireActivity().showToast("ErrorRetrieving")
                    d("errorTAG", "${status.message}")
                }
                Resource.Status.LOADING -> {}
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
                d("loglog", "$imageUri")
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
            val model = UserDetails(name, number, email, true)
            firebaseAuthImpl.getUserId()?.let { uid ->
                viewModel.uploadUserInfo(email!!, name!!, number, true)
            }
        } else
            requireActivity().showToast("Fields are blank")
    }

    private fun clearInputs() {
        binding.nameInput.text?.clear()
        binding.numberInput.text?.clear()
    }

}