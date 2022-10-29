package com.example.cryptoappfinalproject.presentation.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.databinding.FragmentLoginBinding
import com.example.cryptoappfinalproject.presentation.ui.registration.RegistrationViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var binding: FragmentLoginBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginListeners()

    }


    private fun loginListeners() {
        binding?.btnLogin?.setOnClickListener {
            if(FirebaseAuth.getInstance().currentUser == null) {
            val email = binding?.etEmail?.text.toString()
            val password = binding?.etPassword?.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty() &&  FirebaseAuth.getInstance().currentUser == null
            ) {
                viewLifecycleOwner.lifecycleScope.launch {
                    try {
                        binding!!.pbLogin.visibility = View.VISIBLE
                        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                            email,
                            password
                        ).addOnCompleteListener {

                            if (it.isSuccessful) {
                                binding!!.pbLogin.visibility = View.GONE
                                Toast.makeText(requireContext(), getString(R.string.you_are_logged_in), Toast.LENGTH_SHORT).show()
                                findNavController()
                                    .navigate(LoginFragmentDirections.actionLoginFragmentToMainActivity())
                            } else {
                                binding!!.pbLogin.visibility = View.GONE
                                Toast.makeText(
                                    requireContext(),
                                    it.exception.toString(),
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        }


                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
                        }

                    }
                }

            }

            if(FirebaseAuth.getInstance().currentUser != null) {
                Toast.makeText(requireContext(), getString(R.string.already_logged_in), Toast.LENGTH_SHORT).show()

            }
            if(email.isEmpty() || password.isEmpty() ) {
                Toast.makeText(requireContext(), getString(R.string.fill_every_field), Toast.LENGTH_SHORT).show()

            }

        }
            else {
                Toast.makeText(requireContext(), getString(R.string.already_logged_in), Toast.LENGTH_SHORT).show()

            }
        }

        binding?.btnBack?.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
        }

        binding?.btnRegister?.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
        }

    }




    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}