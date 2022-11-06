package com.example.cryptoappfinalproject.presentation.ui.settings

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.cryptoappfinalproject.App
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.common.BaseFragment
import com.example.cryptoappfinalproject.common.DayNightModeSettings
import com.example.cryptoappfinalproject.common.LanguageSettings
import com.example.cryptoappfinalproject.data.local.UserInfo
import com.example.cryptoappfinalproject.databinding.FragmentSettingsBinding
import com.example.cryptoappfinalproject.presentation.ui.registration.RegistrationFragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class SettingsFragment() : BaseFragment<FragmentSettingsBinding, SettingsViewModel>(
    FragmentSettingsBinding::inflate,
    SettingsViewModel::class.java
) {

    private var count = 0
    val context = App.appContext

    private val appSettingPrefs: SharedPreferences =
        context.getSharedPreferences(DayNightModeSettings.SCREEN_PREF, 0)
    private val sharedPrefEdit: SharedPreferences.Editor = appSettingPrefs.edit()
    private val isDayMode: Boolean = appSettingPrefs.getBoolean(DayNightModeSettings.SCREEN_KEY, DayNightModeSettings.IS_FALSE)

    private val languagePref: SharedPreferences =
        context.getSharedPreferences(LanguageSettings.LANG_PREF, 0)
    val langEdit: SharedPreferences.Editor = languagePref.edit()

      private val customList = mutableListOf(
        "Choose", "En", "Geo", "Amh"
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goBack()
        populateUserInfo()
        chooseProfilePicture()
        updatePass()
        updateEmail()
        handlingNightAndDayModes()
        setDayMode()
        setSpinner()


        val state = requireActivity().getSharedPreferences(LanguageSettings.LANG_PREF, 0).getString(LanguageSettings.LANG_KEY, LanguageSettings.ENG)
        if(state ==LanguageSettings.ENG)
            customList[0] = "En"

        if(state == LanguageSettings.GEO) {
            customList[0] = "Geo"
        }

        if(state == LanguageSettings.AMHARIC) {
            customList[0] = "Amh"
        }
    }


    private fun setSpinner() {
        val state = requireActivity().getSharedPreferences(LanguageSettings.LANG_PREF, 0).getString(LanguageSettings.LANG_KEY, LanguageSettings.ENG)

        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            customList
        )
        binding.spinnerSettings.adapter = spinnerAdapter


        binding.spinnerSettings.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    customList[0] = "Choose"

                    when (position) {
                        0 -> {

                        }
                        1 -> {

                            if(state != LanguageSettings.ENG) {
                                langEdit.putString(LanguageSettings.LANG_KEY, LanguageSettings.ENG)
                                langEdit.apply()
                                changeLanguage(LanguageSettings.ENG)
                                val i: Intent? = context.getPackageManager()
                                    .getLaunchIntentForPackage(context.getPackageName())
                                if (i != null) {
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                }
                                activity?.finish()
                                startActivity(i)
                            }
                            else{
                                Toast.makeText(requireContext(),getString(R.string.lang_already_set),Toast.LENGTH_SHORT).show()


                            }


                        }

                        2 -> {

                            if(state != LanguageSettings.GEO) {
                                langEdit.putString(LanguageSettings.LANG_KEY, LanguageSettings.GEO)
                                langEdit.apply()
                                changeLanguage(LanguageSettings.GEO)

                                val i: Intent? = context.getPackageManager()
                                    .getLaunchIntentForPackage(context.getPackageName())
                                if (i != null) {
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                }
                                activity?.finish()
                                startActivity(i)

                            }
                            else{
                                Toast.makeText(requireContext(),getString(R.string.lang_already_set),Toast.LENGTH_SHORT).show()

                            }


                        }
                        3 -> {


                            if(state != LanguageSettings.AMHARIC) {
                                langEdit.putString(LanguageSettings.LANG_KEY, LanguageSettings.AMHARIC)
                                langEdit.apply()
                                changeLanguage(LanguageSettings.AMHARIC)

                                val i: Intent? = context.getPackageManager()
                                    .getLaunchIntentForPackage(context.getPackageName())
                                if (i != null) {
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                }
                                activity?.finish()
                                startActivity(i)
                            }
                            else {
                                Toast.makeText(requireContext(),getString(R.string.lang_already_set),Toast.LENGTH_SHORT).show()

                            }

                        }
                    }


                }


                override fun onNothingSelected(parent: AdapterView<*>?) {


                }

            }
    }


    private fun changeLanguage(language: String) {
        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(
            config,
            resources.displayMetrics
        )
    }


    private fun handlingNightAndDayModes() {
        if (isDayMode) {
            binding.switchNightMode.isChecked = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            requireActivity().setTheme(R.style.Theme_CryptoAppFinalProject)

            //when dark mode is enabled, we use the dark theme
        } else {
            binding.switchNightMode.isChecked = false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            requireActivity().setTheme(R.style.Theme_CryptoAppFinalProject)  //default app theme
        }
    }


    private fun setDayMode() {

        binding.switchNightMode.setOnClickListener {
            if (binding.switchNightMode.isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefEdit.putBoolean(DayNightModeSettings.SCREEN_KEY, DayNightModeSettings.IS_TRUE)
                sharedPrefEdit.apply()

            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefEdit.putBoolean(DayNightModeSettings.SCREEN_KEY, DayNightModeSettings.IS_FALSE)
                sharedPrefEdit.apply()
            }
        }
    }


    private fun populateUserInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.readAllUserInfo().onStart {
            }.collect {
                it.forEach {
                    binding.apply {
                        if (Firebase.auth.currentUser != null) {
                            tvUserName.text = it.name
                            tvUserSurname.text = it.surname
                            tvUserPass.text = "******"
                            tvUserEmail.text = it.email
                            binding.showPass.visibility = View.VISIBLE

                            Glide.with(requireContext())
                                .load(it.image)
                                .error(R.drawable.ic_launcher_background)
                                .into(ivUserSettings)
                            binding.pbSettings.visibility = View.GONE

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
                binding.ivUserSettings?.setImageURI(data?.data)

                val profileBitMap =
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, data?.data)
                viewModel.readAllUserInfo().collect {
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

                        viewModel.updateUserInfo(updatedUser)
                        Toast.makeText(
                            requireContext(),
                            "${resources.getString(R.string.pass_updated)} + $name",
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

                viewModel.readAllUserInfo().collect {
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

                        binding.tvUserEmail.setOnLongClickListener {
                            val dialogBinding =
                                layoutInflater.inflate(R.layout.user_profile_dialog, null)
                            val dialog = Dialog(requireContext())
                            val oldEmail =
                                dialogBinding.findViewById<EditText>(R.id.etUserInfoConfirm)
                            val newEmail = dialogBinding.findViewById<EditText>(R.id.etUserInfo)
                            oldEmail.hint = getString(R.string.enter_old_email)
                            newEmail.hint = getString(R.string.enter_old_email)

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
                                                binding.tvUserEmail.text = updatedUser.email
                                                viewModel.updateUserInfo(updatedUser)
                                                Toast.makeText(
                                                    requireContext(),
                                                    "${resources.getString(R.string.email_update)} + $name!",
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
                                        getString(R.string.old_email_incorrect),
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
            viewModel.readAllUserInfo().collect {
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
                    binding.showPass.setOnClickListener {
                        if (count == 0) {
                            binding.tvUserPass.text = password
                            count++
                        } else {
                            binding.tvUserPass.text = "******"
                            count--

                        }
                    }

                    binding.tvUserPass.setOnLongClickListener {
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
                                                "${resources.getString(R.string.pass_updated)}+ $name!",
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
                                viewModel.updateUserInfo(updatedUser)
                                binding.tvUserPass.text = updatedUser.password
                                dialog.dismiss()
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    getString(R.string.old_pass_incorrect),
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
            binding.ivUserSettings?.setOnLongClickListener {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, RegistrationFragment.IMAGE_REQUEST_CODE)
                true
            }
        }


    }


    private fun goBack() {
        binding.ivBackSettings.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToHomeFragment())
        }
    }

}