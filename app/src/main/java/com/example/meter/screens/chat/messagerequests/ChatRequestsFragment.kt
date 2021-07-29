package com.example.meter.screens.chat.messagerequests

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meter.R
import com.example.meter.adapter.chat.ChatRequestsRecyclerAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.ChatRequestsFragmentBinding
import com.example.meter.network.Resource
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import com.example.meter.repository.firebase.RealtimeDbRepImpl
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ChatRequestsFragment : BaseFragment<ChatRequestsFragmentBinding, ChatRequestsViewModel>(
    ChatRequestsFragmentBinding::inflate,
    ChatRequestsViewModel::class.java
) {

    @Inject
    lateinit var currentUser: FirebaseRepositoryImpl
    @Inject
    lateinit var db: RealtimeDbRepImpl

    private lateinit var nodeForCurrent: DatabaseReference

    private lateinit var adapter: ChatRequestsRecyclerAdapter

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        init()

    }

    private fun init() {
        hideOnToolbar(binding.include2.chatfragments, binding.include2.userProfile)
        nodeForCurrent = db.incomingChat(currentUser.getUserId().toString())
        initAdapter()
        loadUsers()
        observers()
    }

    private fun initAdapter() {
        adapter = ChatRequestsRecyclerAdapter()
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
    }

    private fun loadUsers() {

        nodeForCurrent.get().addOnSuccessListener { snapshot ->
            val listOfUsers = mutableListOf<String>()
            snapshot.children.forEach {
                listOfUsers.add(it.key.toString())
            }
            Log.d("childrencound", "$listOfUsers")
            viewModel.getUsersForChat(listOfUsers)
        }
    }

    private fun observers() {
        viewModel.usersForChat.observe(viewLifecycleOwner, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    Log.d("childrencound", "success")
                    it.data?.let { it1 -> adapter.addItems(it1) }
                    adapter.onUserClick = { userPosition ->
                        val bundle = bundleOf("userInfo" to it.data?.get(userPosition))
                        findNavController().navigate(R.id.action_chatRequestsFragment_to_chatFragment, bundle)
                    }
                }
                Resource.Status.ERROR -> {
                    Log.d("childrencound", "${it.message}")
                }
                Resource.Status.LOADING -> {}
            }
        })
    }


}