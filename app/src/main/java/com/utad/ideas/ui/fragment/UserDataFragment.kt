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
import androidx.datastore.dataStore
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class UserDataFragment : Fragment() {

    private lateinit var _binding: FragmentUserDataBinding
    private val binding: FragmentUserDataBinding get() = _binding

    private lateinit var user: String
    private lateinit var passwd: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        saveDataUser()
        listenToExit()
        listenToDelete()
    }

    private fun saveDataUser() {
        lifecycleScope.launch(Dispatchers.IO) {
            user = DataStoreManager.getUser(requireContext()).first()
            passwd = "Contraseña " +  DataStoreManager.getPassword(requireContext()).first()

            // Actualizar la interfaz de usuario en el hilo principal
            withContext(Dispatchers.Main) {
                setText()
            }
        }
    }

    private fun setText() {
        binding.tvHomeName.text = user
        binding.tvHomePasswd.text = passwd
    }

    private fun listenToDelete() {
        binding.btnHomeDelete.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                DataStoreManager.deleteUser(requireContext())
                withContext(Dispatchers.Main) {
                    goToLogin()
                }
                lifecycleScope.launch(Dispatchers.IO) {

                    DataStoreManager.deleteLogin(requireContext())
                    withContext(Dispatchers.Main) {
                        goToLogin()
                    }
                }
            }
        }
    }

    private fun listenToExit() {
        binding.btnHomeExit.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                DataStoreManager.deleteLogin(requireContext())
                withContext(Dispatchers.Main) {
                    goToLogin()
                }
            }
        }
    }

    private fun goToLogin() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)

    }

}