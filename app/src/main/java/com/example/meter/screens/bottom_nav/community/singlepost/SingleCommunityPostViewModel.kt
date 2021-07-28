package com.example.meter.screens.bottom_nav.community.singlepost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meter.entity.Comment
import com.example.meter.entity.UserDetails
import com.example.meter.entity.community.post.Content
import com.example.meter.entity.push_notification.PushNotificationRequest
import com.example.meter.entity.push_notification.PushNotificationResponse
import com.example.meter.network.Resource
import com.example.meter.repository.comment.CommentRepository
import com.example.meter.repository.firebase.FirebaseMessagingRepo
import com.example.meter.repository.post.community.post.CommunityPostRepository
import com.example.meter.repository.userInfo.UserInfoRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SingleCommunityPostViewModel @Inject constructor(
    private val communityPostRepository: CommunityPostRepository,
    private val commentRepository: CommentRepository,
    private val userInfo: UserInfoRepositoryImpl,
    private val firebaseMessagingRepo: FirebaseMessagingRepo
) : ViewModel() {

    private val _post = MutableLiveData<Resource<Content>>()

    val post: LiveData<Resource<Content>>
        get() = _post

    private val _deletePost = MutableLiveData<Resource<Content>>()

    val deletePost: LiveData<Resource<Content>>
        get() = _deletePost


    private val _createLike = MutableLiveData<Resource<Boolean>>()

    val createLike: LiveData<Resource<Boolean>>
        get() = _createLike

    private val _dislike = MutableLiveData<Resource<Boolean>>()

    val dislike: LiveData<Resource<Boolean>>
        get() = _dislike

    private val _comments = MutableLiveData<Resource<List<Comment>>>()

    val comments: LiveData<Resource<List<Comment>>>
        get() = _comments

    private val _createComment = MutableLiveData<Resource<Comment>>()

    val createComment: LiveData<Resource<Comment>>
        get() = _createComment

    private val _deleteComment = MutableLiveData<Resource<Comment>>()

    val deleteComment: LiveData<Resource<Comment>>
        get() = _deleteComment

    private val _sendPush = MutableLiveData<Resource<PushNotificationResponse>>()

    val sendPush: LiveData<Resource<PushNotificationResponse>>
        get() = _sendPush

    private var _readUserInfo = MutableLiveData<Resource<UserDetails>>()
    val readUserInfo: LiveData<Resource<UserDetails>> = _readUserInfo

    fun getPost(postId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _post.postValue(communityPostRepository.getSinglePost(postId))
            }
        }
    }

    fun getUserInfo(uid: String) {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                val result = userInfo.getUserPersonalInfo(uid)
                _readUserInfo.postValue(result)
            }
        }
    }

    fun getComments(postId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _comments.postValue(commentRepository.getComments(postId))
            }
        }
    }

    fun createComment(
        postId: Long,
        userId: String,
        description: String,
        contentUserId: String
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _createComment.postValue(
                    commentRepository.createComment(
                        postId,
                        userId,
                        description
                    )
                )
            }
        }
        if (contentUserId != userId) {
            sendPush(
                contentUserId, pushNotificationRequest = PushNotificationRequest(
                    data = mapOf(
                        "comment" to description,
                        "name" to userId,
                        "postId" to postId.toString()
                    ),
                    message = "has commented on your post",
                    title = "Your Message",
                    token = "",
                    topic = "Comment"
                )
            );
        }
    }

    private fun sendPush(userId: String, pushNotificationRequest: PushNotificationRequest) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _sendPush.postValue(
                    firebaseMessagingRepo.sendPushNotification(
                        userId, pushNotificationRequest
                    )
                )
            }
        }
    }

    fun deleteComment(
        commentId: Long
    ) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _deleteComment.postValue(
                    commentRepository.deleteComment(
                        commentId
                    )
                )
            }
        }
    }

    fun createLike(userId: String, postId: Long, contentUserId: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _createLike.postValue(
                    communityPostRepository.createLike(
                        postId, userId
                    )
                )

                if (contentUserId != userId) {
                    sendPush(
                        contentUserId, pushNotificationRequest = PushNotificationRequest(
                            data = mapOf(
                                "comment" to "liked",
                                "name" to userId,
                                "postId" to postId.toString()
                            ),
                            message = "has commented on your post",
                            title = "Your Message",
                            token = "",
                            topic = "Comment"
                        )
                    );
                }
            }
        }
    }

    fun deleteLike(userId: String, postId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _dislike.postValue(
                    communityPostRepository.deleteLike(
                        postId, userId
                    )
                )
            }
        }
    }

    fun deletePost(postId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _deletePost.postValue(
                    communityPostRepository.deletePost(
                        postId
                    )
                )
            }
        }
    }


}