package com.utad.ideas.ui.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.utad.ideas.R
import com.utad.ideas.database.DataStoreManager
import com.utad.ideas.database.dataStore
import com.utad.ideas.databinding.FragmentIdeasListBinding
import com.utad.ideas.databinding.FragmentUserDataBinding
import com.utad.ideas.ui.activities.IdeasActivity
import com.utad.ideas.ui.activities.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UserDataFragment : Fragment() {

    private lateinit var _binding: FragmentUserDataBinding
    private val binding: FragmentUserDataBinding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenToExit()
        listenToDelete()
    }

    private fun listenToDelete() {
        binding.btnHomeDelete.setOnClickListener {
            //lifecycleScope.launch(Dispatchers.IO) {

            //booleanPreferencesKey("user_logged")
            // DataStoreManager.setUserLogged(requireContext())
            // TODO ir al login y eliminar el usuario isLogged DATA STORE
        }
    }

    //goToLogin()
    /*  lifecycleScope.launch(Dispatchers.IO) {
            DataStoreManager.setUserLogged(requireContext())
            }
        }    */


private fun listenToExit() {
    binding.btnHomeExit.setOnClickListener {
        //goToLogin()
        // TODO ir al login
    }
}


/*private fun goToLogin(){
    val intent = Intent(requireContext(), MainActivity::class.java)
    startActivity(intent)
}*/


}