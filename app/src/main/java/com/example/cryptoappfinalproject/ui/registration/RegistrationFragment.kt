package com.example.cryptoappfinalproject.ui.registration

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.data.local.UserInfo
import com.example.cryptoappfinalproject.databinding.FragmentRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var binding: FragmentRegistrationBinding? = null

    private val registrationViewModel: RegistrationViewModel by viewModels()

    companion object {
        val IMAGE_REQUEST_CODE = 100
    }

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
        chooseProfilePicture()


    }


    private fun registrationListener() {

    }

    private fun chooseProfilePicture() {
        binding?.btnProfilePhoto?.setOnClickListener {
            pickPhoto()
        }
    }

    private fun pickPhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
           binding?.ivProfilePhoto?.setImageURI(data?.data)

            val profileBitMap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, data?.data)

            binding?.btnRegister?.setOnClickListener {

                val name = binding?.etName?.text.toString()
                val surname = binding?.etSurname?.text.toString()
                val email = binding?.etEmail?.text.toString()
                val password = binding?.etPassword?.text.toString()
                val repeatPassword = binding?.etRepeatPassword?.text.toString()

                val user = UserInfo(
                    uid = 0,
                    image = profileBitMap,
                    name = name,
                    surname = surname,
                    email = email,
                    password = password
                )



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
                    .addOnSuccessListener { _ ->
                        registrationViewModel.insertUserInfo(user)


                        findNavController()
                            .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToHomeFragment())
                    }
                    .addOnFailureListener { _ ->
                        Toast.makeText(requireContext(), "Password should contain at least 6 characters", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
            binding?.btnBack?.setOnClickListener {
                findNavController()
                    .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
            }


        }
    }






    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
