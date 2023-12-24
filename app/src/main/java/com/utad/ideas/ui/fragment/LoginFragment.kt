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
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class LoginFragment : Fragment() {

    private lateinit var _binding: FragmentLoginBinding
    private val binding: FragmentLoginBinding get() = _binding


    private var userLogged: Boolean = false

    private var isLoginButtonClickedUser = false
    private var isLoginButtonClickedPasswd = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkUser()

        binding.btnGoToCreate.setOnClickListener {
            goToRegister()
        }
        binding.btnLoginAcceder.setOnClickListener {
            isLoginButtonClickedUser = true
            isLoginButtonClickedPasswd = true
            doLogin()
        }
    }


    private fun checkUser() {
        lifecycleScope.launch(Dispatchers.IO) {
            DataStoreManager.getIsUserLogged(requireContext()).collect { isUserLogged ->
                if (isUserLogged) {
                    userLogged = true
                    withContext(Dispatchers.Main) {
                        goToHome()
                    }
                }
            }
        }
    }

    private fun doLogin() {
        var name: String? = binding.etLoginName.text.toString().trim()
        var passwordValue: String? = binding.etLoginPassword.text.toString().trim()

        if (!name.isNullOrEmpty() && !passwordValue.isNullOrEmpty()) {

            var isNameValid: Boolean? = null
            var isPasswordValid: Boolean? = null
            // En esta corrutina escucho el nombre de usuario, solo una cosa puede escuchar
            lifecycleScope.launch(Dispatchers.IO) {
                DataStoreManager.getUser(requireContext()).collect { user ->
                    isNameValid = (user == name) // Si el nombre coincide me dara true
                    checkCredentials(isNameValid, isPasswordValid)
                    name = null
                    showMessageUser(isNameValid!!)
                }
            }
            lifecycleScope.launch(Dispatchers.IO) {
                DataStoreManager.getPassword(requireContext()).collect { password ->
                    isPasswordValid =
                        (password == passwordValue) // Si el nombre coincide me dara true
                    checkCredentials(isNameValid, isPasswordValid)
                    passwordValue = null
                    showMessagePasswd(isPasswordValid!!)
                }
            }
        } else {
            Toast.makeText(requireContext(), "Rellenar todos los campos", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun showMessageUser(isNameValid: Boolean) {
        if ((isNameValid != null && !isNameValid!!) && (!userLogged) && isLoginButtonClickedUser) {
            lifecycleScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    requireContext(),
                    "Nombre no válido",
                    Toast.LENGTH_SHORT
                ).show()
            }
            userLogged = false
        }
    }

    private fun showMessagePasswd(isPasswordValid: Boolean) {
        if ((isPasswordValid != null && !isPasswordValid!!) && (!userLogged) && isLoginButtonClickedPasswd) {
            lifecycleScope.launch(Dispatchers.Main) {
                Toast.makeText(
                    requireContext(),
                    "Contraseña no válida",
                    Toast.LENGTH_SHORT
                ).show()
            }
            userLogged = false
        }
    }

    // Chequeo las credenciales que sean true

    private fun checkCredentials(isNameValid: Boolean?, isPasswordValid: Boolean?) {
        if (isNameValid == true && isPasswordValid == true) {
            setUserLogged(true) // guardo que el usuario esta logeado
            userLogged = true
            goToHome()
        }

    }

    private fun goToRegister() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
        findNavController().navigate(action)
        isLoginButtonClickedPasswd = false
        isLoginButtonClickedUser = false
    }

    // Guardo la sesion que el usuario esta logeado
    private fun setUserLogged(isLogged: Boolean) {
        lifecycleScope.launch(Dispatchers.IO) {
            DataStoreManager.setUserLogged(requireContext(), isLogged)
        }
    }

    private fun goToHome() {
        val intent = Intent(requireContext(), IdeasActivity::class.java)
        startActivity(intent)

    }

}