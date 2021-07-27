package com.example.meter.screens.bottom_nav.market

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
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
                adapter.submitData(resource)
            }
        })
    }

    override fun setUp(inflater: LayoutInflater, container: ViewGroup?) {
        initRecycler()
        observe()
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
            CarPostRecyclerAdapter()
        binding.recyclerMarketPosts.adapter = adapter.withLoadStateFooter(LoaderStateAdapter())
    }


}