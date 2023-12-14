package com.utad.ideas.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.utad.ideas.database.DataStoreManager
import com.utad.ideas.databinding.FragmentLoginBinding
import com.utad.ideas.ui.activities.IdeasActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class LoginFragment : Fragment() {

    private lateinit var _binding: FragmentLoginBinding
    private val binding: FragmentLoginBinding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkIsUserLogged()

        binding.btnGoToCreate.setOnClickListener {
            goToRegister()
        }
        binding.btnLoginAcceder.setOnClickListener {
            doLogin()
        }
    }

    private fun checkIsUserLogged() {
        lifecycleScope.launch(Dispatchers.IO) {
            DataStoreManager.getIsUserLogged(requireContext()).collect { isUserLogged ->
                if (isUserLogged) {
                    with(Dispatchers.Main) {
                        goToHome()
                    }
                }
            }
        }
    }

    private fun doLogin() {
        val name = binding.etLoginName.text.toString().trim()
        val password = binding.etLoginPassword.text.toString().trim()

        if (!name.isNullOrEmpty() && !password.isNullOrEmpty()) {
            obtainUserAndPassword(name, password)
        } else {
            Toast.makeText(requireContext(), "Rellenar todos los campos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToRegister() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(action)
    }


    //  Recupero el nombre y la contraseña  de mi BD y compruebo si son iguales paso los valores a otra funcion

    private fun obtainUserAndPassword(name: String, passwordParam: String) {

        var isNameValid: Boolean? = null
        var isPasswordValid: Boolean? = null

        // En esta corrutina escucho el nombre de usuario, solo una cosa puede escuchar
        lifecycleScope.launch(Dispatchers.IO) {
            DataStoreManager.getUser(requireContext()).collect { user ->
                isNameValid = user == name // Si el nombre coincide me dara true
                checkCredentials(isNameValid, isPasswordValid)
            }
        }
        lifecycleScope.launch(Dispatchers.IO) {
            DataStoreManager.getPassword(requireContext()).collect { password ->
                isPasswordValid = password == passwordParam // Si el nombre coincide me dara true
                checkCredentials(isNameValid, isPasswordValid)
            }
        }
    }

    // Chequeo las credenciales que sean true

    private fun checkCredentials(isNameValid: Boolean?, isPasswordValid: Boolean?) {
        if (isNameValid == true && isPasswordValid == true) {
            setUserLogged() // guardo que el usuario esta logeado
            goToHome()
        } else if (isNameValid != null && isNameValid!!) {
            Toast.makeText(requireContext(), "Nombre invalido", Toast.LENGTH_SHORT).show()
        } else if (isPasswordValid != null && isPasswordValid!!) {
            Toast.makeText(requireContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT)
                .show()

        }
    }

    // Guardo la sesion que el usuario esta logeado
    private fun setUserLogged() {
        lifecycleScope.launch(Dispatchers.IO) {
            DataStoreManager.setUserLogged(requireContext())
        }
    }

    private fun goToHome() {
        val intent = Intent(requireContext(), IdeasActivity::class.java)
        startActivity(intent)

    }


}