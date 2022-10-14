package com.example.cryptoappfinalproject.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import android.widget.Toast

import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoappfinalproject.data.local.Crypto
import com.example.cryptoappfinalproject.databinding.FragmentHomeBinding
import com.example.cryptoappfinalproject.ui.adapters.CoinsHomeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.example.cryptoappfinalproject.*
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.domain.CryptoCoinsModel
import com.example.cryptoappfinalproject.domain.CryptoSearchModel
import com.example.cryptoappfinalproject.ui.adapters.CoinsSearchAdapter
import com.example.cryptoappfinalproject.ui.favorites.FavoritesViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null

    private lateinit var adapter: CoinsHomeAdapter
    private lateinit var searchAdapter: CoinsSearchAdapter

    private val viewModel: HomeViewModel by viewModels()
    private val viewModelFav: FavoritesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllCoinsPager()
        setUpBottomNavigation()
        addCoinsToFavList()
        searchCryptos()

    }

    private fun addCoinsToFavList() {
        lifecycleScope.launch {
            viewModelFav.readAllData().collect {
                it.forEach {
                    favList.add(it)
                }
                Log.d("roomCrypto", favList.size.toString())


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
        adapter.onFavListener = {
            favoritesListener(it)
        }

        binding!!.pbHome.visibility = View.GONE

    }

    private fun setSearchAdapter() {
        searchAdapter = CoinsSearchAdapter(requireContext())
        binding!!.rvHomeCryptoAssets.layoutManager = LinearLayoutManager(activity)
        binding!!.rvHomeCryptoAssets.adapter = searchAdapter
        searchAdapter.onClickListener = {
            favoritesSearchListener(it)
        }
    }


    private fun favoritesListener(singleItem: CryptoCoinsModel.CryptoCoinsModelItem) {
        val crypto = Crypto(
            uid = 0,
            image = singleItem.image!!,
            originalTitle = singleItem.name!!,
            marketCapRank = singleItem.marketCapRank!!,
            currentPrice = singleItem.currentPrice!!,
            priceChangePercentage24h = singleItem.priceChangePercentage24h!!
        )

        val builder = AlertDialog.Builder(requireContext())

        if (favList.size == 0) {
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
        }
        if (favList.toString().contains(crypto.originalTitle)) {

            builder.setPositiveButton("Yes") { _, _ ->

                viewLifecycleOwner.lifecycleScope.launch {
                    Toast.makeText(
                        requireContext(),
                        " ${crypto.originalTitle}, Is Already Added To Favorites",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
        if (!favList.toString().contains(crypto.originalTitle)) {

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
        }

        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Add ${crypto.originalTitle}?")
        builder.setMessage("Are You Sure You Want To Add ${crypto.originalTitle} To Favorite Cryptos?")
        builder.create().show()


    }

    private fun favoritesSearchListener(singleItem: CryptoSearchModel.Coin) {
        val crypto = Crypto(
            uid = 0,
            image = singleItem.thumb!!,
            originalTitle = singleItem.name!!,
            marketCapRank = singleItem.marketCapRank!!,
            currentPrice = null,
            priceChangePercentage24h = null
        )

        val builder = AlertDialog.Builder(requireContext())

        if (favList.size == 0) {
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
        }
        if (favList.toString().contains(crypto.originalTitle)) {

            builder.setPositiveButton("Yes") { _, _ ->

                viewLifecycleOwner.lifecycleScope.launch {
                    Toast.makeText(
                        requireContext(),
                        " ${crypto.originalTitle}, Is Already Added To Favorites",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
        if (!favList.toString().contains(crypto.originalTitle)) {

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
        }

        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Add ${crypto.originalTitle}?")
        builder.setMessage("Are You Sure You Want To Add ${crypto.originalTitle} To Favorite Cryptos?")
        builder.create().show()

    }

    private fun searchCryptos() {
        var displayList: MutableList<CryptoSearchModel.Coin> = mutableListOf()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.newState.collect {
                    when (it) {
                        is Resource.Success -> {
                            binding!!.svHome.setOnQueryTextListener(object :
                                SearchView.OnQueryTextListener {
                                override fun onQueryTextSubmit(query: String?): Boolean {
                                    return true
                                }

                                override fun onQueryTextChange(newText: String?): Boolean {
                                    viewModel.getSearchedCryptos(query = newText.toString())
                                    if (newText!!.isNotEmpty()) {
                                        displayList.clear()
                                        val search = newText.lowercase(Locale.getDefault())
                                        it.data.forEach {
                                            if (it.name!!.lowercase(Locale.getDefault())
                                                    .contains(search)
                                            ) {
                                                displayList.add(it)
                                            }
                                        }
                                        setSearchAdapter()
                                        searchAdapter.submitList(Resource.Success(displayList))
                                    } else {
                                        getAllCoinsPager()
                                    }

                                    return true

                                }

                            })
                        }
                        is Resource.Error -> {
                            Log.d("error", it.errorMsg)
                        }
                        is Resource.Loader -> {
                            Log.d("loader", "loading")
                        }
                    }
                }
            }
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


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        favList.clear()

    }

}




