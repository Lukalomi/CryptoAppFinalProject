package com.example.cryptoappfinalproject.ui.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.databinding.FragmentLoginBinding
import com.example.cryptoappfinalproject.databinding.FragmentRegistrationBinding
import com.google.firebase.auth.FirebaseAuth

class RegistrationFragment : Fragment() {

    private var binding: FragmentRegistrationBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding!!.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registrationListener()

    }


    private fun registrationListener() {
        binding?.btnRegister?.setOnClickListener {
            val name = binding?.etName?.text.toString()
            val surname = binding?.etSurname?.text.toString()
            val email = binding?.etEmail?.text.toString()
            val password = binding?.etPassword?.text.toString()
            val repeatPassword = binding?.etRepeatPassword?.text.toString()

            if (name.isEmpty() || surname.isEmpty() ||
                email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()) {

                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

            if (password != repeatPassword) {
                Toast.makeText(
                    requireContext(),
                    "Passwords do not match",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { task ->
                    findNavController()
                        .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToHomeFragment())
                }
                .addOnFailureListener { task ->
                    Toast.makeText(requireContext(), "Password should contain at least 6 characters", Toast.LENGTH_SHORT)
                        .show()
                }
        }
        binding?.btnBack?.setOnClickListener {
            findNavController()
                .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}