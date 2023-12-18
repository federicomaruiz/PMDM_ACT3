package com.utad.ideas.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.utad.ideas.databinding.FragmentIdeasListBinding
import com.utad.ideas.room.model.Ideas
import com.utad.ideas.ui.adapters.IdeasAdapter

class IdeasListFragment : Fragment() {

    private lateinit var _binding: FragmentIdeasListBinding
    private val binding: FragmentIdeasListBinding get() = _binding

    private val adapter : IdeasAdapter = IdeasAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIdeasListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvIdeasList.layoutManager =
            LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)

        binding.rvIdeasList.adapter = adapter

        val list  = listOf( Ideas(1,"mercadona"))

        adapter.submitList(list)



    }




}