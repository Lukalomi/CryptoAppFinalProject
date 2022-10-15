package com.example.cryptoappfinalproject.ui.registration

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.cryptoappfinalproject.databinding.FragmentRegistrationBinding
import com.google.firebase.auth.FirebaseAuth

class RegistrationFragment : Fragment() {

    private var binding: FragmentRegistrationBinding? = null
//
//    var pickedPhoto : Uri? = null
//    var pickedBitMap : Bitmap? = null
//

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
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}

//
//    private fun pickPhoto () {
//        if (ContextCompat.checkSelfPermission(
//                requireContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE)
//        != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(
//                requireActivity(), arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
//        } else {
//            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//            startActivityForResult(galleryIntent, 2)
//        }
//    }
//
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == 1) {
//            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                startActivityForResult(galleryIntent, 2)
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == 2 && resultCode == Activity.RESULT_OK && data != null) {
//            pickedPhoto = data.data
//            if (pickedPhoto != null) {
//                if (Build.VERSION.SDK_INT >= 28) {
//                    val source =
//                        ImageDecoder.createSource(requireContext().contentResolver, pickedPhoto!!)
//                    pickedBitMap = ImageDecoder.decodeBitmap(source)
//                    binding?.ivProfilePhoto?.setImageBitmap(pickedBitMap)
//                } else {
//                    pickedBitMap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, pickedPhoto)
//                    binding?.ivProfilePhoto?.setImageBitmap(pickedBitMap)
//                }
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data)
//    }
//


