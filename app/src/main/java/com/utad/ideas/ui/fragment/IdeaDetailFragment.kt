package com.utad.ideas.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.utad.ideas.R
import com.utad.ideas.databinding.ActivityCreateBinding
import com.utad.ideas.databinding.FragmentIdeaDetailBinding
import com.utad.ideas.databinding.FragmentIdeasListBinding


class IdeaDetailFragment : Fragment() {

    private lateinit var _binding: FragmentIdeaDetailBinding
    private val binding: FragmentIdeaDetailBinding get() = _binding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIdeaDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //idTitle = recoverData()
        //setFragment(idTitle)
    }


}