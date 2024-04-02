package com.example.olpgas.browse_rooms.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olpgas.R
import com.example.olpgas.core.util.ConnectivityObserver
import com.example.olpgas.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewModel: BrowseRoomsViewModel by viewModels()
    private lateinit var binding: FragmentHomeBinding
    private var isCacheRefreshed = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        binding = FragmentHomeBinding.bind(view)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerViewAdapter()

        onSwipeRefresh()

        observeNetworkConnection()
    }

    private fun setRecyclerViewAdapter() {
        val adapter = RoomsRecyclerViewAdapter(emptyList(), requireContext())
        binding.rvListRooms.apply {
            this.layoutManager = LinearLayoutManager(requireContext())
            this.adapter = adapter
        }

        if(viewModel.connectionStatus.value == ConnectivityObserver.State.Available) {
            viewModel.onEvent(BrowseRoomsEvent.OnCreate)
            isCacheRefreshed = true
        }
        viewModel.allRoomDetailsState.observe(viewLifecycleOwner) {
            it?.let {
                adapter.roomsData = it
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun onSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            if(viewModel.connectionStatus.value == ConnectivityObserver.State.Available) {
                binding.swipeRefresh.isRefreshing = true
                viewModel.onEvent(BrowseRoomsEvent.OnRefresh)
                isCacheRefreshed = true
            }
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun observeNetworkConnection() {

        viewModel.connectionStatus.observe(viewLifecycleOwner) {
            Log.d("Network Connection", it.toString())

            if(it == ConnectivityObserver.State.Available && !isCacheRefreshed) {
                viewModel.onEvent(BrowseRoomsEvent.OnRefresh)
                isCacheRefreshed = true
            }
        }
    }
}