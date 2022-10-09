package com.example.cryptoappfinalproject.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast


import androidx.core.view.GravityCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoappfinalproject.data.local.Crypto
import com.example.cryptoappfinalproject.databinding.FragmentHomeBinding
import com.example.cryptoappfinalproject.ui.adapters.CoinsHomeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.example.cryptoappfinalproject.*
import com.example.cryptoappfinalproject.ui.favorites.FavoritesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    private lateinit var adapter: CoinsHomeAdapter

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
        setUpBottomNavigation()
        drawerListener()
        drawerNavigation()


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
                originalTitle = it.name!!,
                marketCapRank = it.marketCapRank!!,
                currentPrice = it.currentPrice!!,
                priceChangePercentage24h = it.priceChangePercentage24h!!
            )
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModelFav.insertCrypto(crypto)
                    favList.add(crypto)
                    Toast.makeText(
                        requireContext(),
                        " ${crypto.originalTitle}, Has Been Added To Favorites",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.setTitle("Add ${crypto.originalTitle}?")
            builder.setMessage("Are You Sure You Want To Add ${crypto.originalTitle} To Favorite Cryptos?")
            builder.create().show()

        }

    }

    private fun setUpBottomNavigation() {
        val navHostFragment =
            activity!!.supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView: BottomNavigationView =
            activity!!.findViewById(R.id.bottomNavigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }


    private fun drawerListener() {
        binding?.drawer?.setOnTouchListener(object : Swipe(requireContext()) {

            override fun onSwipeRight() {
                binding?.drawer?.openDrawer(
                    GravityCompat.START, true
                )
            }

            override fun onSwipeLeft() {
                binding?.drawer?.closeDrawer(
                    GravityCompat.END, true
                )
            }
        }
        )
    }
    

    private fun drawerNavigation() {
        binding?.btnConverter?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToConverterFragment())
        }

        binding?.btnSettings?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment())
        }

        binding?.btnUser?.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToUserInfoFragment())
        }


    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }

}