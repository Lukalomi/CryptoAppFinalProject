package com.example.cryptoappfinalproject.presentation.ui.registration

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.data.local.UserInfo
import com.example.cryptoappfinalproject.databinding.FragmentRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

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
        chooseProfilePicture()
        goBack()

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

            val profileBitMap =
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, data?.data)

            binding?.btnRegister?.setOnClickListener {
                if (data?.data == null) {
                    Toast.makeText(requireContext(), "Please upload a picture", Toast.LENGTH_SHORT)
                        .show()
                }

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



                if (name.isNotEmpty() && surname.isNotEmpty() &&
                    email.isNotEmpty() && password.isNotEmpty() && repeatPassword.isNotEmpty()
                ) {

                    try {
                        binding!!.pbRegister.visibility = View.VISIBLE
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                            email, password
                        ).addOnCompleteListener {

                            if (it.isSuccessful) {
                                checkLoggedInstance()
                                registrationViewModel.insertUserInfo(user)
                                binding!!.pbRegister.visibility = View.GONE

                                findNavController()
                                    .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToHomeFragment())
                            } else {
                                binding!!.pbRegister.visibility = View.GONE
                                Toast.makeText(
                                    requireContext(),
                                    it.exception.toString(),
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        }

                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()

                    }


                }

                if (name.isEmpty() || surname.isEmpty() ||
                    email.isEmpty() || password.isEmpty() || repeatPassword.isEmpty()
                ) {

                    Toast.makeText(requireContext(), getString(R.string.fill_every_field), Toast.LENGTH_SHORT)
                        .show()

                }

                if (password != repeatPassword) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.pass_dont_match),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

            }


        }
    }

    private fun goBack() {
        binding?.btnBack?.setOnClickListener {
            findNavController()
                .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
        }
    }

    private fun checkLoggedInstance() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            Toast.makeText(requireContext(), getString(R.string.havent_registered), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), getString(R.string.you_are_registered), Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}
