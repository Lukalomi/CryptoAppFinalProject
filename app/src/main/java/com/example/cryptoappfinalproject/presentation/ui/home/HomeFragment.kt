package com.example.cryptoappfinalproject.presentation.ui.home

import android.app.AlertDialog

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities


import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.cardview.widget.CardView
import android.view.Gravity
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.common.BaseFragment
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.data.local.Crypto
import com.example.cryptoappfinalproject.data.local.Exchanges
import com.example.cryptoappfinalproject.databinding.FragmentHomeBinding
import com.example.cryptoappfinalproject.domain.model.CryptoCoinsModel
import com.example.cryptoappfinalproject.domain.model.CryptoExchangesModel
import com.example.cryptoappfinalproject.domain.model.CryptoSearchModel
import com.example.cryptoappfinalproject.domain.model.VideoTitleModel

import com.example.cryptoappfinalproject.presentation.MainActivity
import com.example.cryptoappfinalproject.presentation.ui.adapters.CoinsHomeAdapter
import com.example.cryptoappfinalproject.presentation.ui.adapters.CoinsSearchAdapter
import com.example.cryptoappfinalproject.presentation.ui.adapters.ExchangesAdapter
import com.example.cryptoappfinalproject.presentation.ui.adapters.MovieLoadStateAdapter

import com.example.cryptoappfinalproject.presentation.ui.adapters.*

import com.example.cryptoappfinalproject.presentation.ui.favorites.*
import com.example.cryptoappfinalproject.presentation.ui.registration.RegistrationViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    FragmentHomeBinding::inflate,
    HomeViewModel::class.java
) {

    private lateinit var homeAdapter: CoinsHomeAdapter
    private lateinit var searchAdapter: CoinsSearchAdapter
    private lateinit var adapterExchanges: ExchangesAdapter

    private val viewModelReg: RegistrationViewModel by viewModels()
    private val viewModelFav: FavoritesViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpBottomNavigation()
        cryptoAssetsListener()
        exchangesListener()

        getAllCoinsPager()

        searchCryptos()
        addCoinsToFavList()
        populateProfilePicture()
        openDrawer()
        recyclerScrollState()


    }


    private fun cryptoAssetsListener(){
        binding.tvCryptoAssets.setOnClickListener {
            favExchanges.clear()
            getAllCoinsPager()
            searchCryptos()
            addCoinsToFavList()
            setTextColors()
        }
    }


    private fun setTextColors(){
        binding.tvExchanges.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.grey
            )

        )
        binding.svHome.setQuery("", true)
        binding.svHome.clearFocus()

        binding.tvCryptoAssets.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.white
            )
        )
    }


    private fun exchangesListener() {
        binding.tvExchanges.setOnClickListener {
            favList.clear()
            getAllExchanges()
            searchExchanges()
            addExchangesToFavList()
        }
    }


    private fun recyclerScrollState() {
        binding.rvHomeCryptoAssets.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    binding.rvScrollUp.apply {
                        visibility = View.VISIBLE
                        setOnClickListener {
                            binding.rvHomeCryptoAssets.smoothScrollToPosition(0)
                        }
                    }
                } else {
                    binding.rvScrollUp.visibility = View.GONE

                }
            }


        }
        )
    }

    private fun openDrawer() {
        binding.btnAuth.setOnClickListener {
            val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer)
            drawer.openDrawer(Gravity.LEFT)
        }
    }

    private fun addCoinsToFavList() {
        lifecycleScope.launch {
            viewModelFav.readAllData().collect {
                it.forEach {
                    favList.add(it)
                    favCoinTitle.add(it.originalTitle)
                }
            }
        }
    }


    private fun addExchangesToFavList() {
        lifecycleScope.launch {
            viewModelFav.readAllExchanges().collect {
                it.forEach {
                    favExchanges.add(it)
                    favExTitle.add(it.title)
                }
            }
        }
    }

    private fun getAllCoinsPager() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.coinsPager.collect {
                setAllCoinsAdapter()
                homeAdapter.addLoadStateListener {
                    when (it.source.refresh) {
                        is LoadState.NotLoading -> {
                            binding.pbHome.visibility = View.GONE
                        }
                        else -> {
                            binding.pbHome.visibility = View.VISIBLE
                        }
                    }
                }

                homeAdapter.submitData(it)


            }

        }

    }


    private fun getAllExchanges() {
        viewModel.getExchanges()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.exchangesState.collect {
                when (it) {
                    is Resource.Success -> {
                        setExchangesAdapter()
                        adapterExchanges.submitList(it)
                        binding.tvExchanges.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                        binding.tvCryptoAssets.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.grey
                            )
                        )
                    }

                    is Resource.Error -> {

                    }
                    is Resource.Loader -> {

                    }
                }

            }
        }
    }

    private fun setAllCoinsAdapter() {
        homeAdapter = CoinsHomeAdapter(requireContext())
        binding.rvHomeCryptoAssets.layoutManager = LinearLayoutManager(activity)
        binding.rvHomeCryptoAssets.adapter = homeAdapter


        binding.rvHomeCryptoAssets.adapter = homeAdapter.withLoadStateFooter(
            footer = CoinLoadStateAdapter { homeAdapter.retry() }

        )
        homeAdapter.onFavListener = {
            favoritesListener(it)
        }

    }




    private fun setSearchAdapter() {
        searchAdapter = CoinsSearchAdapter(requireContext())
        binding.rvHomeCryptoAssets.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHomeCryptoAssets.adapter = searchAdapter
        searchAdapter.onClickListener = {
            favoritesSearchListener(it)
        }

    }


    private fun setExchangesAdapter() {
        adapterExchanges = ExchangesAdapter(requireContext())
        binding.rvHomeCryptoAssets.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHomeCryptoAssets.adapter = adapterExchanges
        adapterExchanges.onClickListener = {
            favoritesExchangesListener(it)

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

        val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)

        if (favList.size == 0) {

            builder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->

                viewLifecycleOwner.lifecycleScope.launch {
                    viewModelFav.insertCrypto(crypto)
                    favList.add(crypto)

                    Toast.makeText(
                        requireContext(),
                        "${crypto.originalTitle}  ${resources.getString(R.string.been_added)}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        if (favList.toString().contains(crypto.originalTitle)) {

            Toast.makeText(
                requireContext(),
                "${crypto.originalTitle}  ${resources.getString(R.string.already_added)}",
                Toast.LENGTH_SHORT
            ).show()

        }
        if (!favList.toString().contains(crypto.originalTitle)) {

            builder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {

                    viewModelFav.insertCrypto(crypto)
                    favList.add(crypto)

                    Toast.makeText(
                        requireContext(),
                        "${crypto.originalTitle}  ${resources.getString(R.string.been_added)}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            builder.setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
            builder.setTitle("${resources.getString(R.string.add)}  ${crypto.originalTitle}?")
            builder.setMessage(
                "${resources.getString(R.string.sure_to_add)} ${crypto.originalTitle}  ${
                    resources.getString(
                        R.string.to_cryptos
                    )
                }?"
            )
            builder.create().show()

        }


    }

    private fun favoritesExchangesListener(singleItem: CryptoExchangesModel.CryptoExchangesModelItem) {
        val exchange = Exchanges(
            uid = 0,
            image = singleItem.image!!,
            title = singleItem.name!!,
            rank = singleItem.trustScoreRank!!,
            volume = singleItem.tradeVolume24hBtc,
        )

        val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)

        if (favExchanges.size == 0) {
            builder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->

                viewLifecycleOwner.lifecycleScope.launch {

                    viewModelFav.insertExchange(exchange)
                    favExchanges.add(exchange)
                    Toast.makeText(
                        requireContext(),
                        "${exchange.title}  ${resources.getString(R.string.been_added)}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        if (favExchanges.toString().contains(exchange.title)) {

            builder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->

                viewLifecycleOwner.lifecycleScope.launch {
                    Toast.makeText(
                        requireContext(),
                        "${exchange.title}  ${resources.getString(R.string.already_added)}",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
        if (!favExchanges.toString().contains(exchange.title)) {

            builder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {

                    viewModelFav.insertExchange(exchange)
                    favExchanges.add(exchange)
                    Toast.makeText(
                        requireContext(),
                        "${exchange.title}  ${resources.getString(R.string.been_added)}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        builder.setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
        builder.setTitle("${resources.getString(R.string.add)}  ${exchange.title}?")
        builder.setMessage(
            "${resources.getString(R.string.sure_to_add)} ${exchange.title} ${
                resources.getString(
                    R.string.to_cryptos
                )
            }?"
        )
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

        val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)

        if (favList.size == 0) {
            builder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->

                viewLifecycleOwner.lifecycleScope.launch {

                    viewModelFav.insertCrypto(crypto)
                    favList.add(crypto)
                    Toast.makeText(
                        requireContext(),
                        "${crypto.originalTitle} ${resources.getString(R.string.been_added)}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        if (favList.toString().contains(crypto.originalTitle)) {

            builder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->

                viewLifecycleOwner.lifecycleScope.launch {
                    Toast.makeText(
                        requireContext(),
                        " ${crypto.originalTitle} ${resources.getString(R.string.already_added)}",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
        }
        if (!favList.toString().contains(crypto.originalTitle)) {

            builder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {

                    viewModelFav.insertCrypto(crypto)
                    favList.add(crypto)

                    Toast.makeText(
                        requireContext(),
                        "${crypto.originalTitle} ${resources.getString(R.string.been_added)}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        builder.setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
        builder.setTitle("${resources.getString(R.string.add)} ${crypto.originalTitle}?")
        builder.setMessage(
            "${resources.getString(R.string.sure_to_add)} ${crypto.originalTitle} ${
                resources.getString(
                    R.string.to_cryptos
                )
            }?"
        )
        builder.create().show()

    }

    private fun searchCryptos() {
        var displayList: MutableList<CryptoSearchModel.Coin> = mutableListOf()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchState.collect {
                    when (it) {
                        is Resource.Success -> {
                            binding.svHome.setOnQueryTextListener(object :
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

    private fun searchExchanges() {
        var displayList: MutableList<CryptoExchangesModel.CryptoExchangesModelItem> =
            mutableListOf()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.exchangesState.collect {
                    when (it) {
                        is Resource.Success -> {
                            binding.svHome.setOnQueryTextListener(object :
                                SearchView.OnQueryTextListener {
                                override fun onQueryTextSubmit(query: String?): Boolean {
                                    return true
                                }

                                override fun onQueryTextChange(newText: String?): Boolean {
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
                                        setExchangesAdapter()
                                        adapterExchanges.submitList(Resource.Success(displayList))
                                    } else {
                                        getAllExchanges()
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


    override fun onResume() {
        super.onResume()
        binding.svHome.setQuery("", true)


    }


    private fun setUpBottomNavigation() {
        val navHostFragment =
            activity!!.supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView: BottomNavigationView =
            activity!!.findViewById(R.id.bottomNavigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }

    private fun populateProfilePicture() {

        lifecycleScope.launch {
            viewModelReg.readAllUserInfo().collect {
                val profilePicture = activity?.findViewById<ImageView>(R.id.ivUserPhoto)
                val drawerProfileName = activity?.findViewById<TextView>(R.id.tvUsernameHeader)

                if (Firebase.auth.currentUser != null) {
                    it.forEach {
                        if (profilePicture != null) {
                            Glide.with(requireContext())
                                .load(it.image)
                                .error(R.drawable.ic_launcher_background)
                                .into(profilePicture)
                            requireActivity().findViewById<ProgressBar>(R.id.pbProfilePic).visibility =
                                View.GONE
                            requireActivity().findViewById<CardView>(R.id.cvUserPhoto).visibility =
                                View.VISIBLE
                        }
                        if (drawerProfileName != null) {
                            drawerProfileName.text = it.name + " " + it.surname
                            requireActivity().findViewById<ProgressBar>(R.id.pbProfilePic).visibility =
                                View.GONE
                            requireActivity().findViewById<CardView>(R.id.cvUserPhoto).visibility =
                                View.VISIBLE
                        }

                    }

                } else {
                    if (profilePicture != null) {
                        Glide.with(requireContext())
                            .load(R.drawable.ic_person)
                            .error(R.drawable.ic_launcher_background)
                            .into(profilePicture)
                    }
                    if (drawerProfileName != null) {
                        drawerProfileName.text = ""
                        requireActivity().findViewById<ProgressBar>(R.id.pbProfilePic).visibility =
                            View.GONE
                        requireActivity().findViewById<CardView>(R.id.cvUserPhoto).visibility =
                            View.VISIBLE
                    }

                }

            }
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        favList.clear()
        favExchanges.clear()
        favCoinTitle.clear()
        favExTitle.clear()
    }

}




