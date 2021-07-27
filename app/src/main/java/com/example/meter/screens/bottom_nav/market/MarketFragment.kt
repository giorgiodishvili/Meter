package com.example.meter.screens.bottom_nav.market

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.meter.R
import com.example.meter.adapter.carpost.CarPostRecyclerAdapter
import com.example.meter.base.BaseFragment
import com.example.meter.databinding.MarketFragmentBinding
import com.example.meter.paging.loadstate.LoaderStateAdapter
import com.example.meter.repository.firebase.FirebaseRepositoryImpl
import dagger.hilt.android.AndroidEntryPoint
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
    }

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        initRecycler()
        observe()
        setListeners()
    }

    fun setListeners(){
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getMarketPosts()
            observe()
        }
    }

    private fun initRecycler() {

        binding.recyclerMarketPosts.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL, false
        )
        var userId = firebaseAuthImpl.getUserId()

        if (userId.isNullOrEmpty()) {
            userId = ""
        }

        adapter =
            CarPostRecyclerAdapter {
                findNavController().navigate(
                    R.id.action_navigation_marketPosts_to_singleCarSellPostFragment,
                    bundleOf("postId" to it)
                )
            }
        binding.recyclerMarketPosts.adapter = adapter.withLoadStateFooter(LoaderStateAdapter())

    }


}