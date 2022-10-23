package com.example.cryptoappfinalproject.ui.settings

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cryptoappfinalproject.App
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.data.local.UserInfo
import com.example.cryptoappfinalproject.databinding.FragmentSettingsBinding
import com.example.cryptoappfinalproject.ui.registration.RegistrationFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SettingsFragment() : Fragment() {


    private val settingsViewModel: SettingsViewModel by viewModels()
    private var binding: FragmentSettingsBinding? = null

    private var count = 0
    val context =  App.appContext

    private val appSettingPrefs: SharedPreferences =
        context.getSharedPreferences("AppSettingPrefs", 0)
    val sharedPrefEdit: SharedPreferences.Editor = appSettingPrefs.edit()
    val isDayMode: Boolean = appSettingPrefs.getBoolean("DayMode", false)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goBack()
        populateUserInfo()
        chooseProfilePicture()
        updatePass()
        updateEmail()
        handlingNightAndDayModes()
        setDayMode()
    }


    private fun handlingNightAndDayModes() {
        if (isDayMode) {
            binding!!.switchNightMode.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            requireActivity().setTheme(R.style.Theme_CryptoAppFinalProject)

        //when dark mode is enabled, we use the dark theme
        } else {
            binding!!.switchNightMode.isChecked = false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            requireActivity().setTheme(R.style.Theme_CryptoAppFinalProject)  //default app theme
        }
    }


    private fun setDayMode() {

        binding!!.switchNightMode.setOnClickListener {
            if (binding!!.switchNightMode.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefEdit.putBoolean("DayMode", true)
                sharedPrefEdit.apply()

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefEdit.putBoolean("DayMode", false)
                sharedPrefEdit.apply()
            }
        }
    }


    private fun populateUserInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.readAllUserInfo().collect {
                it.forEach {
                    binding!!.apply {
                        if (Firebase.auth.currentUser != null) {
                            tvUserName.text = it.name
                            tvUserSurname.text = it.surname
                            tvUserPass.text = "******"
                            tvUserEmail.text = it.email
                            binding!!.showPass.visibility = View.VISIBLE

                            Glide.with(requireContext())
                                .load(it.image)
                                .error(R.drawable.ic_launcher_background)
                                .into(ivUserSettings)
                        }
                    }


                }
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewLifecycleOwner.lifecycleScope.launch {
            if (requestCode == RegistrationFragment.IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
                binding?.ivUserSettings?.setImageURI(data?.data)

                val profileBitMap =
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, data?.data)
                settingsViewModel.readAllUserInfo().collect {
                    it.forEach {
                        val name = it.name
                        val surname = it.surname
                        val email = it.email
                        val password = it.password
                        val updatedUser = UserInfo(
                            name = name,
                            surname = surname,
                            email = email,
                            password = password,
                            uid = it.uid,
                            image = profileBitMap
                        )

                        settingsViewModel.updateUserInfo(updatedUser)
                        Toast.makeText(
                            requireContext(),
                            "Your Profile Picture Has Been Changed Successfully $name",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }
            }
        }
    }

    private fun updateEmail() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {

                settingsViewModel.readAllUserInfo().collect {
                    it.forEach {
                        var name = it.name
                        var surname = it.surname
                        var email = it.email
                        var password = it.password
                        var updatedUser = UserInfo(
                            name = name,
                            surname = surname,
                            email = email,
                            password = password,
                            uid = it.uid,
                            image = it.image
                        )

                        binding!!.tvUserEmail.setOnLongClickListener {
                            val dialogBinding =
                                layoutInflater.inflate(R.layout.user_profile_dialog, null)
                            val dialog = Dialog(requireContext())
                            val oldEmail =
                                dialogBinding.findViewById<EditText>(R.id.etUserInfoConfirm)
                            val newEmail = dialogBinding.findViewById<EditText>(R.id.etUserInfo)
                            oldEmail.hint = "Enter Your Old Email"
                            newEmail.hint = "Enter Your New Email"

                            dialog.setContentView(dialogBinding)
                            dialog.setCancelable(true)
                            dialog.show()


                            val btnSaveUpdatedEmail =
                                dialogBinding.findViewById<AppCompatButton>(R.id.btnUserInfoSave)
                            val btnCancelUpdatedEmail =
                                dialogBinding.findViewById<AppCompatButton>(R.id.btnUserInfoCancel)

                            btnSaveUpdatedEmail.setOnClickListener {
                                if (oldEmail.text.toString() == email) {

                                    Firebase.auth.signInWithEmailAndPassword(email, password)
                                        .addOnCompleteListener {
                                            if (it.isSuccessful) {
                                                Firebase.auth.currentUser!!.updateEmail(newEmail.text.toString())
                                                updatedUser.email = newEmail.text.toString()
                                                binding!!.tvUserEmail.text = updatedUser.email
                                                settingsViewModel.updateUserInfo(updatedUser)
                                                Toast.makeText(
                                                    requireContext(),
                                                    "Congrats,Email Has Been Updated $name!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            } else {
                                                Toast.makeText(
                                                    requireContext(),
                                                    it.exception.toString(),
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }


                                    dialog.dismiss()
                                } else {
                                    Toast.makeText(
                                        requireContext(),
                                        "Old Email is Incorrect",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            btnCancelUpdatedEmail.setOnClickListener {
                                dialog.dismiss()

                            }
                            true
                        }


                    }
                }

            }

        }

    }

    private fun updatePass() {
        viewLifecycleOwner.lifecycleScope.launch {
            settingsViewModel.readAllUserInfo().collect {
                it.forEach {
                    var name = it.name
                    var surname = it.surname
                    var email = it.email
                    var password = it.password
                    var updatedUser = UserInfo(
                        name = name,
                        surname = surname,
                        email = email,
                        password = password,
                        uid = it.uid,
                        image = it.image
                    )
                    binding!!.showPass.setOnClickListener {
                        if (count == 0) {
                            binding!!.tvUserPass.text = password
                            count++
                        } else {
                            binding!!.tvUserPass.text = "******"
                            count--

                        }
                    }

                    binding!!.tvUserPass.setOnLongClickListener {
                        val dialogBinding =
                            layoutInflater.inflate(R.layout.user_profile_dialog, null)
                        val dialog = Dialog(requireContext())
                        dialog.setContentView(dialogBinding)
                        dialog.setCancelable(true)
                        dialog.show()

                        val oldPass = dialogBinding.findViewById<EditText>(R.id.etUserInfoConfirm)
                        val newPass = dialogBinding.findViewById<EditText>(R.id.etUserInfo)
                        val btnSaveUpdatedPass =
                            dialogBinding.findViewById<AppCompatButton>(R.id.btnUserInfoSave)
                        val btnCancelUpdatedPass =
                            dialogBinding.findViewById<AppCompatButton>(R.id.btnUserInfoCancel)

                        btnSaveUpdatedPass.setOnClickListener {
                            if (oldPass.text.toString() == password) {
                                Firebase.auth.signInWithEmailAndPassword(email!!, password)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            Firebase.auth.currentUser!!.updatePassword(newPass.text.toString())
                                            Toast.makeText(
                                                requireContext(),
                                                "Congrats,Password Has Been Updated $name!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            Toast.makeText(
                                                requireContext(),
                                                it.exception.toString(),
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                updatedUser.password = newPass.text.toString()
                                settingsViewModel.updateUserInfo(updatedUser)
                                binding!!.tvUserPass.text = updatedUser.password
                                dialog.dismiss()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "Old Password is Incorrect",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        btnCancelUpdatedPass.setOnClickListener {
                            dialog.dismiss()
                        }

                        true
                    }

                }
            }

        }
    }


    private fun chooseProfilePicture() {
        if (Firebase.auth.currentUser != null) {
            binding?.ivUserSettings?.setOnLongClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, RegistrationFragment.IMAGE_REQUEST_CODE)
                true
            }
        }


    }



    private fun goBack() {
        binding!!.ivBackSettings.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToHomeFragment())
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }
}