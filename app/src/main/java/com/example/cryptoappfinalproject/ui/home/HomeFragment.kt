package com.example.cryptoappfinalproject.ui.home

import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.cryptoappfinalproject.FavoritesViewModel
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.Swipe
import com.example.cryptoappfinalproject.data.local.Crypto
import com.example.cryptoappfinalproject.databinding.FragmentHomeBinding
import com.example.cryptoappfinalproject.domain.CryptoCoinsModel
import com.example.cryptoappfinalproject.favList
import com.example.cryptoappfinalproject.ui.adapters.CoinsHomeAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import androidx.navigation.ui.NavigationUI.setupWithNavController

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null
    private lateinit var adapter: CoinsHomeAdapter
    private lateinit var navController: NavController

    private val viewModel: HomeViewModel by viewModels()
    private val viewModelFav: FavoritesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllCoinsPager()

        bottomNavigation()
    }

        drawerListener()
        lifecycleScope.launch {
            viewModelFav.readAllData().collect {
                it.forEach {
                    favList.add(it)
                }
                Log.d("roommovie", favList.size.toString())


            }
        }
    }

    private fun getAllCoinsPager() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.coinsPager.collect {
                setAllCoinsAdapter()
                adapter.submitData(it)
            }
        }
    }

    private fun setAllCoinsAdapter() {

        adapter = CoinsHomeAdapter(requireContext())
        binding!!.rvHomeCryptoAssets.layoutManager = LinearLayoutManager(activity)
        binding!!.rvHomeCryptoAssets.adapter = adapter
       favoritesListener()
        binding!!.pbHome.visibility = View.GONE

    }




    private fun favoritesListener() {
        adapter.onFavListener = {
            val crypto = Crypto(
                uid = 0,
                image = it.image!!,
                originalTitle = it.name!!
            )
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModelFav.insertCrypto(crypto)
                    favList.add(crypto)
                    Toast.makeText(requireContext()," ${crypto.originalTitle}, Has Been Added To Favorites", Toast.LENGTH_SHORT).show()

                }
            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.setTitle("Add ${crypto.originalTitle}?")
            builder.setMessage("Are You Sure You Want To Add ${crypto.originalTitle} To Favorite Movies?")
            builder.create().show()

        }

    }



    private fun drawerListener() {
        binding?.drawer?.setOnTouchListener(object: Swipe(requireContext()) {

            override fun onSwipeRight() {
                startDrawer()
            }

            override fun onSwipeLeft() {
                endDrawer()
            }
        }
        )
    }


    private fun startDrawer() {
        binding?.drawer?.openDrawer(
            GravityCompat.START, true
        )

    }

    private fun endDrawer() {
        binding?.drawer?.closeDrawer(
            GravityCompat.END, true
        )

    }


    
    private fun bottomNavigation() {

        val navHostFragment = activity?.supportFragmentManager?.findFragmentById(R.id.nav_host_fragment)
                as NavHostFragment

        navController = navHostFragment.navController
        val bottomNavigationView = binding?.bottomNavigation
        if (bottomNavigationView != null) {
            setupWithNavController(bottomNavigationView, navController)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }

}