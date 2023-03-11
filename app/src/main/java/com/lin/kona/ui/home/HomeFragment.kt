package com.lin.kona.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.lin.kona.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private val viewModel by lazy { ViewModelProvider(this)[HomeViewModel::class.java] }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // val textView: TextView = binding.textHome
        // homeViewModel.text.observe(viewLifecycleOwner) {
        //     textView.text = it
        // }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}