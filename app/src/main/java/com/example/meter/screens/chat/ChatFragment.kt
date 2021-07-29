package com.example.meter.screens.chat

import android.os.Build
import android.os.Bundle
import android.util.Log.d
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meter.adapter.chat.ChatRecyclerAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.ChatFragmentBinding
import com.example.meter.entity.Chat
import com.example.meter.entity.UserDetails
import com.example.meter.entity.push_notification.PushNotificationRequest
import com.example.meter.extensions.showToast
import com.example.meter.network.Resource
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import com.example.meter.repository.firebase.RealtimeDbRepImpl
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : BaseFragment<ChatFragmentBinding, ChatViewModel>(
    ChatFragmentBinding::inflate,
    ChatViewModel::class.java
) {
    @Inject
    lateinit var currentUser: FirebaseRepositoryImpl
    @Inject
    lateinit var db: RealtimeDbRepImpl

    private lateinit var adapter: ChatRecyclerAdapter
    private lateinit var nodeForCurrent: DatabaseReference
    private lateinit var nodeForOther: DatabaseReference

    private lateinit var otherUser: UserDetails

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getOtherUserInfo()
        nodeForCurrent = db.createNode(currentUser.getUserId().toString(), otherUser.id.toString())
        nodeForOther = db.createNode(otherUser.id.toString(), currentUser.getUserId().toString())

    }

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        init()
    }

    private fun init() {

        viewModel.getUserInfo(currentUser.getUserId().toString())
        setUpChatRecycler()
        observers()
        listeners()
    }

    private fun getOtherUserInfo() {

        val otherUser = arguments?.getParcelable<UserDetails>("userInfo")

        if (otherUser != null)
            this.otherUser = otherUser
    }

    private fun observers() {
        viewModel.readUserInfo.observe(viewLifecycleOwner, { user ->
            when (user.status) {
                Resource.Status.SUCCESS -> {
                    user.data?.let {
                        adapter.loadInfo(currentUser.getUserId().toString(), user.data.url, otherUser.url)
                        listenForMessage()
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

    private fun setUpChatRecycler() {
        adapter = ChatRecyclerAdapter()
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
    }

    private fun listeners() {

        binding.commentBTN.setOnClickListener {
            val text = binding.commentET.text.trim().toString()
            if (text.isNotBlank())
                sendMessage(text)
        }
    }


    private fun sendMessage(text: String) {
        val msgForCurrent = nodeForCurrent.push()
        val msgForOther = nodeForOther.push()

        val message = Chat( currentUser.getUserId().toString(), msgForCurrent.key.toString(), text, getCurrentTimeStamp().toString(), otherUser.id.toString())

        msgForCurrent.setValue(message)
        msgForOther.setValue(message)
        binding.commentET.text.clear()

    }

    private fun listenForMessage() {
        var isLoaded : Boolean = false;
        nodeForCurrent.get().addOnSuccessListener { snapshot ->
            d("tagtag", "HEREE")
            nodeForCurrent.addChildEventListener(object: ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val messageItem = snapshot.getValue(Chat::class.java)
                    d("userIdLog123", "$messageItem")
                    if (messageItem != null) {
                        adapter.addItems(messageItem)

                        if(isLoaded){
                            viewModel.sendPush(messageItem.toUid,PushNotificationRequest(
                                data = mapOf(
                                    "comment" to "მოგწერათ",
                                    "name" to messageItem.fromUid,
                                    "postId" to "",
                                    "to" to messageItem.toUid,
                                    "from" to messageItem.fromUid,
                                    "type" to "message"
                                ),
                                message = "დააკომენტარა თქვენს პოსტზე",
                                title = "Mater",
                                token = "",
                                topic = "Comment"
                            ))
                        }
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    d("userIdLog123change", "change")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    d("userIdLog123", "remove")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    d("userIdLog123", "move")
                }

                override fun onCancelled(error: DatabaseError) {
                    d("userIdLog123", "canceled")
                }

            })
            isLoaded=true
        }

    }



    private fun getCurrentTimeStamp(): String? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ISO_INSTANT.format(Instant.now())
        } else {
            Timestamp(System.currentTimeMillis()).toString()
        }
    }
}


