package com.utad.ideas.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.utad.ideas.database.dataStore
import com.utad.ideas.databinding.FragmentIdeasListBinding
import com.utad.ideas.room.model.Ideas
import com.utad.ideas.ui.adapters.IdeasAdapter
import com.utad.ideas.ui.application.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IdeasListFragment : Fragment() {

    private lateinit var _binding: FragmentIdeasListBinding
    private val binding: FragmentIdeasListBinding get() = _binding

    private val adapter = IdeasAdapter(
        { item: Ideas -> deleteItem(item) },
        { itemId: Int -> goToDetail(itemId) }
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIdeasListBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        getIdeasFromDataBase()
    }


    private fun setRecyclerView() {
        binding.rvIdeasList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvIdeasList.adapter = adapter
    }

    private fun getIdeasFromDataBase() {
        lifecycleScope.launch(Dispatchers.Main) {
            val application = requireActivity().application as MyApplication
            application.dataBase.ideasDao().getAllIdeaList().collect { ideasList ->
                adapter.submitList(ideasList)
            }
        }
    }

    private fun goToDetail(itemId: Int) {
        val action = IdeasListFragmentDirections.actionIdeasListFragmentToIdeaDetailFragment(itemId)
        findNavController().navigate(action)
    }

    private fun deleteItem(item: Ideas) {
        val application = requireActivity().application as MyApplication
        lifecycleScope.launch(Dispatchers.IO) {
            application.dataBase.ideasDao().deleteList(item)
        }
    }

}




