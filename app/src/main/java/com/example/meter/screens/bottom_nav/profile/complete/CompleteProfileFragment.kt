package com.example.meter.screens.bottom_nav.profile.complete

import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.meter.R
import com.example.meter.base.BaseFragment
import com.example.meter.base.SharedViewModel
import com.example.meter.databinding.CompleteProfileFragmentBinding
import com.example.meter.entity.UserDetails
import com.example.meter.extensions.*
import com.example.meter.network.Resource
import com.example.meter.pushnotifications.MyFirebaseMessagingService
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import com.example.meter.repository.firebase.RealtimeDbRepImpl
import com.example.meter.screens.bottom_nav.profile.myposts.commPosts.MyCommPostsViewModel
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
    @Inject
    lateinit var db: RealtimeDbRepImpl

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (firebaseAuthImpl.getUserId() == null) {
            findNavController().navigate(R.id.action_navigation_profile_to_main_auth)
        } else {
            viewModel.readUserInfo
        }
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        navigationBarSetup()
        init()
    }

    private fun init() {

        val externalUid = arguments?.getString("uid")

        if (externalUid != null  && firebaseAuthImpl.getUserId() != externalUid) {
            showOtherProfile(externalUid)
        } else {
            showCurrentProfile()
        }

        db.createNode(firebaseAuthImpl.getUserId().toString(), uid)
        db.createReversedNode(uid, firebaseAuthImpl.getUserId().toString())

        uid.let { viewModel.getUserInfo(it) }
        listeners()
        observers()
    }

    private fun observers() {
        viewModel.readUserInfo.observe(viewLifecycleOwner, { user ->
            val d = d("userinfo", "${user.data?.url}")
            d("userinfo", "$user")
            when (user.status) {
                Resource.Status.SUCCESS -> {
                    d("loglog", "$user")
                    val name = user.data?.name

                    user.data?.let { it1 -> binding.profilePic.loadImg(it1.url, false) }
                    binding.msgButton.setOnClickListener {
                        user.data?.let { it1 -> openChat(it1) }
                    }

                    val arr = name?.split(" ".toRegex(), 2)?.toTypedArray()
                    if (arr != null) {
                        displayName(arr)
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
            MyFirebaseMessagingService.getToken(requireContext())?.let { it1 ->
                sharedViewModel.deleteUserFromToken(
                    it1
                )
            }
            firebaseAuthImpl.signOut()
            findNavController().navigate(R.id.action_navigation_profile_to_navigation_home)
        }

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_profile_to_navigation_home)
        }

    }

    private fun openChat(model: UserDetails) {
        val bundle = bundleOf("userInfo" to model)

        findNavController().navigate(R.id.action_navigation_profile_to_chatFragment, bundle)
    }

    private fun navigationBarSetup() {
        val navController =
            (childFragmentManager.findFragmentById(R.id.postsHostFragment) as NavHostFragment).navController

        val popupMenu = PopupMenu(requireActivity(), null)
        popupMenu.inflate(R.menu.post_menu)
        val menu = popupMenu.menu
        binding.bottomBar.setupWithNavController(menu, navController)
    }

    private fun showOtherProfile(externalUid: String) {
        sharedViewModel.saveUserId(externalUid)
        uid = externalUid

        binding.msgButton.show()
        binding.editProfileButton.setGone()
        binding.logOutButton.setGone()
    }

    private fun showCurrentProfile() {
        binding.msgButton.setGone()
        uid = firebaseAuthImpl.getUserId().toString()
        authorisedWithGoogle = firebaseAuthImpl.getUser()?.photoUrl != null
        MyFirebaseMessagingService.getToken(requireContext())?.let {
            sharedViewModel.saveToken(
                uid,
                it
            )
        }
        if (authorisedWithGoogle)
            binding.profilePic.loadImgUri(firebaseAuthImpl.getUser()?.photoUrl)
    }

    private fun displayName(arr: Array<String>) {
        if (arr.size == 2) {
            binding.nameTv.text = arr[0]
            binding.surnameTv.text = arr[1]
        } else {
            if (arr.size == 1) {
                binding.nameTv.text = arr[0]
            } else {
                binding.nameTv.text = arr[0]
                binding.surnameTv.text = arr.drop(1).joinToString(" ")
            }

        }

    }

}