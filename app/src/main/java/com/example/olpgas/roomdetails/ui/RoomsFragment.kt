package com.example.olpgas.roomdetails.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.olpgas.databinding.FragmentRoomsBinding
import com.example.olpgas.roomdetails.adapter.RoomRecyclerAdapter


class RoomsFragment : Fragment() {

    private var _binding: FragmentRoomsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {


        _binding = FragmentRoomsBinding.inflate(inflater, container, false)

        val roomData: Array<Array<String>> = arrayOf(
            arrayOf(
                "Sun View Apartment",
                "Station Road, Anand Nagar, Surat, Gujarat",
                "+91 9876543210",
                "7000/-",
                "15000/-",
                "2024-03-15"
            ), arrayOf(
                "Green Meadows",
                "Ring Road, Rajkot, Gujarat",
                "+91 8765432190",
                "4500/-",
                "10000/-",
                "2024-03-14"
            ), arrayOf(
                "Royal Heights",
                "Bypass Road, Vadodara, Gujarat",
                "+91 7896543211",
                "6000/-",
                "12000/-",
                "2024-03-11"
            )
        )


        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        val roomRecyclerAdapter = RoomRecyclerAdapter(roomData, requireContext())
        binding.rv.adapter = roomRecyclerAdapter

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}