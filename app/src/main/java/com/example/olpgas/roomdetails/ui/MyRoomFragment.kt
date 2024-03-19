package com.example.olpgas.roomdetails.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.olpgas.databinding.FragmentMyRoomBinding


class MyRoomFragment : Fragment() {

    private var _binding: FragmentMyRoomBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyRoomBinding.inflate(inflater, container, false)

        return binding.root
    }

}