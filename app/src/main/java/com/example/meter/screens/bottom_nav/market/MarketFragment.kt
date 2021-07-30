package com.example.meter.screens.bottom_nav.market

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meter.R
import com.example.meter.adapter.carpost.CarPostRecyclerAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.MarketFragmentBinding
import com.example.meter.extensions.makePhoneCall
import com.example.meter.extensions.setGone
import com.example.meter.paging.loadstate.LoaderStateAdapter
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MarketFragment : BaseFragment<MarketFragmentBinding, MarketViewModel>(
    MarketFragmentBinding::inflate,
    MarketViewModel::class.java
) {

    private lateinit var adapter: CarPostRecyclerAdapter

    @Inject
    lateinit var firebaseAuthImpl: FirebaseRepositoryImpl

    fun observe() {
        viewModel.getMarketPosts().observe(viewLifecycleOwner, { resource ->
            lifecycleScope.launch {
                binding.swipeRefresh.isRefreshing = false
                adapter.submitData(resource)
            }
        })

        viewModel.readUserInfo.observe(viewLifecycleOwner, { data ->
            data.data?.number?.let { requireActivity().makePhoneCall(it) }
        })
    }

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        initRecycler()
        listeners()
        observe()
        setListeners()
    }

    fun setListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getMarketPosts()
            observe()
        }

        binding.searchBar.doOnTextChanged { text, _, _, count ->
            if (text!!.length >= 2) {
                Log.d("piifi", "$text $count")
                getSeachResultWithDelay(text.toString())
            } else if (text.isEmpty()) {
                viewModel.getMarketPosts()
                observe()
            }
        }

    }

    private fun listeners() {
        adapter.onCallClick = { uid ->
            viewModel.getUserInfo(uid)
        }

        binding.include5.userProfile.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_marketPosts_to_navigation_profile)
        }
        binding.include5.chatfragments.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_marketPosts_to_chatRequestsFragment)
        }

    }

    private fun initRecycler() {

        binding.recyclerMarketPosts.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        adapter =
            CarPostRecyclerAdapter {
                findNavController().navigate(
                    R.id.action_navigation_marketPosts_to_singleCarSellPostFragment,
                    bundleOf("postId" to it)
                )
            }
        binding.recyclerMarketPosts.adapter = adapter.withLoadStateFooter(LoaderStateAdapter())

    }

    private fun getSeachResultWithDelay(text: String) {
        CoroutineScope(Dispatchers.Main).launch {
            delay(500)
            viewModel.searchPost(text, "", null, null, null, null).observe(viewLifecycleOwner, {
                lifecycleScope.launch {
                    if (binding.progressCircular.isVisible) {
                        binding.progressCircular.setGone()
                    }
                    adapter.submitData(it)
                    adapter.notifyDataSetChanged()
                }
            })
        }
    }
}