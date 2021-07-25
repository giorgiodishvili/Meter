package com.example.meter.screens.bottom_nav.community.singlepost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meter.entity.Comment
import com.example.meter.entity.community.post.Content
import com.example.meter.network.Resource
import com.example.meter.repository.comment.CommentRepository
import com.example.meter.repository.post.community.post.CommunityPostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SingleCommunityPostViewModel @Inject constructor(
    private val communityPostRepository: CommunityPostRepository,
    private val commentRepository: CommentRepository
) : ViewModel() {

    private val _post = MutableLiveData<Resource<Content>>()

    val post: LiveData<Resource<Content>>
        get() = _post

    private val _comments = MutableLiveData<Resource<List<Comment>>>()

    val comments: LiveData<Resource<List<Comment>>>
        get() = _comments

    private val _createComment = MutableLiveData<Resource<Comment>>()

    val createComment: LiveData<Resource<Comment>>
        get() = _createComment

    private val _deleteComment = MutableLiveData<Resource<Comment>>()

    val deleteComment: LiveData<Resource<Comment>>
        get() = _deleteComment

    fun getPost(postId: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _post.postValue(communityPostRepository.getSinglePost(postId))
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
        description: String
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
    }

    fun deleteComment(
        commentId: Long
    ){
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

}