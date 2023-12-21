package com.utad.ideas.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.utad.ideas.R
import com.utad.ideas.database.DataStoreManager
import com.utad.ideas.databinding.FragmentRegisterBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private lateinit var _binding: FragmentRegisterBinding
    private val binding: FragmentRegisterBinding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.setOnClickListener {
            createUser()
        }
    }

    private fun createUser() {
        val userName = binding.etName.text.toString()
        val password = binding.etPassword.text.toString()
        val passwordRepeat = binding.etRepeatPassword.text.toString()



        if (!userName.isNullOrEmpty() && !password.isNullOrEmpty() && !passwordRepeat.isNullOrEmpty()) {
            if (password == passwordRepeat) {
                lifecycleScope.launch(Dispatchers.IO) { // Accedo al ciclo de vida de la vista, luego depende de la accion accedo a un hilo u otro aca IO
                    DataStoreManager.saveUser(
                        requireContext(),
                        userName,
                        password
                    ) // Accedo al DataStore y llamo a la funcion, le paso el contexto y los datos
                }
                Toast.makeText(requireContext(), "Usuario creado", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack() // Regreso a la ventana anterior para hacer login una vez que se creo el usuario
            } else {
                Toast.makeText(requireContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(requireContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show()
        }
    }
}