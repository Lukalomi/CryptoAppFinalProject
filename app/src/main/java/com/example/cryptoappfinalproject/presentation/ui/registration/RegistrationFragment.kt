package com.example.cryptoappfinalproject.presentation.ui.registration

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.common.BaseFragment
import com.example.cryptoappfinalproject.data.local.UserInfo
import com.example.cryptoappfinalproject.databinding.FragmentRegistrationBinding
import com.example.cryptoappfinalproject.domain.model.FirebaseUserModel
import com.example.cryptoappfinalproject.presentation.ui.login.LoginFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegistrationFragment : BaseFragment<FragmentRegistrationBinding, RegistrationViewModel>(
    FragmentRegistrationBinding::inflate,
    RegistrationViewModel::class.java
) {

    companion object {
        val IMAGE_REQUEST_CODE = 100
    }

    private val db = Firebase.firestore


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chooseProfilePicture()
        goBack()

    }


    private fun chooseProfilePicture() {
        binding.btnProfilePhoto.setOnClickListener {
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
            binding.ivProfilePhoto.setImageURI(data?.data)

            val profileBitMap =
                MediaStore.Images.Media.getBitmap(requireContext().contentResolver, data?.data)

            binding.btnRegister.setOnClickListener {
                if (data?.data == null) {
                    Toast.makeText(requireContext(), "Please upload a picture", Toast.LENGTH_SHORT)
                        .show()
                }

                val name = binding.etName.text.toString()
                val surname = binding.etSurname.text.toString()
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                val repeatPassword = binding.etRepeatPassword.text.toString()

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
                        binding.pbRegister.visibility = View.VISIBLE
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                            email, password
                        ).addOnCompleteListener {

                            if (it.isSuccessful) {
                                checkLoggedInstance()

                                registrationViewModel.insertUserInfo(user)
                                binding!!.pbRegister.visibility = View.GONE

                                val userId = FirebaseAuth.getInstance().currentUser!!.uid
                                db.collection("users").document(userId)
                                    .set(FirebaseUserModel(name, email, userId))
                                    .addOnSuccessListener {
                                        Toast.makeText(
                                            requireContext(),
                                            "success!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }.addOnFailureListener {
                                        Toast.makeText(
                                            requireContext(),
                                            "loser!!!",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                findNavController()
                                    .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToMainActivity())
                            } else {
                                binding.pbRegister.visibility = View.GONE
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
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.fill_every_field),
                        Toast.LENGTH_SHORT
                    )
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
        binding.btnBack.setOnClickListener {
            findNavController()
                .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToLoginFragment())
        }
    }

    private fun checkLoggedInstance() {
        if (FirebaseAuth.getInstance().currentUser == null) {
            Toast.makeText(
                requireContext(),
                getString(R.string.havent_registered),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.you_are_registered),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}
