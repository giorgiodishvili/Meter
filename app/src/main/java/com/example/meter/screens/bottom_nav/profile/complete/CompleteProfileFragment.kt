package com.example.meter.screens.bottom_nav.profile.complete

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.meter.R
import com.example.meter.base.BaseFragment
import com.example.meter.base.SharedViewModel
import com.example.meter.databinding.CompleteProfileFragmentBinding
import com.example.meter.extensions.loadImg
import com.example.meter.extensions.loadImgUri
import com.example.meter.extensions.setGone
import com.example.meter.network.Resource
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import com.example.meter.screens.bottom_nav.profile.myposts.commPosts.MyCommPostsViewModel
import com.example.shualeduri.extensions.showToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class CompleteProfileFragment :
    BaseFragment<CompleteProfileFragmentBinding, MyCommPostsViewModel>(
        CompleteProfileFragmentBinding::inflate,
        MyCommPostsViewModel::class.java
    ) {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var authorisedWithGoogle: Boolean = false
    private lateinit var uid: String

    @Inject
    lateinit var firebaseAuthImpl: FirebaseRepositoryImpl

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
        navigationBarSetup()
        init()
    }

    private fun init() {

        val externalUid = arguments?.getString("uid")

//        val navController = (childFragmentManager.findFragmentById(R.id.postsHostFragment) as NavHostFragment?)?.navController
//        navController?.navigate(R.id.action_global_navigation_commPosts, bundleOf("uid2" to externalUid.toString()))

        if (externalUid != null) {
            sharedViewModel.saveUserId(externalUid)
            uid = externalUid

            binding.editProfileButton.setGone()
            binding.logOutButton.setGone()
        } else {
            uid = firebaseAuthImpl.getUserId().toString()
            authorisedWithGoogle = firebaseAuthImpl.getUser()?.photoUrl != null

            if (authorisedWithGoogle)
                binding.profilePic.loadImgUri(firebaseAuthImpl.getUser()?.photoUrl)

        }


        uid.let { viewModel.getUserInfo(it) }
        listeners()
        observers()
    }

    private fun observers() {
        viewModel.readUserInfo.observe(viewLifecycleOwner, {
            val d = d("userinfo", "${it.data?.url}")
            d("userinfo", "$it")
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    d("loglog", "$it")
                    val name = it.data?.name
                    it.data?.let { it1 -> binding.profilePic.loadImg(it1.url) }
                    val arr = name?.split(" ".toRegex(), 2)?.toTypedArray()

                    if (arr != null) {
                        if (arr.size == 2) {
                            binding.nameTv.text = arr[0]
                            binding.surnameTv.text = arr[1]
                        } else {
                            binding.nameTv.text = arr[0]
                            binding.surnameTv.text = arr.drop(1).joinToString(" ")
                        }
                    }
                }
                Resource.Status.ERROR -> {
                    requireContext().showToast("error loading user info")
                }
                Resource.Status.LOADING -> {

                }
            }
        })

    }

    private fun listeners() {
        binding.editProfileButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_navigation_completeProfileInfo)
        }

        binding.logOutButton.setOnClickListener {
            firebaseAuthImpl.signOut()
            findNavController().navigate(R.id.action_navigation_profile_to_navigation_home)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_navigation_home)
        }

    }

    private fun navigationBarSetup() {
        val navController = (childFragmentManager.findFragmentById(R.id.postsHostFragment) as NavHostFragment).navController

        val popupMenu = PopupMenu(requireActivity(), null)
        popupMenu.inflate(R.menu.post_menu)
        val menu = popupMenu.menu
        binding.bottomBar.setupWithNavController(menu, navController)
    }

}