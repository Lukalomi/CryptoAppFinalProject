package com.example.cryptoappfinalproject.presentation.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptoappfinalproject.*
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.data.local.Crypto
import com.example.cryptoappfinalproject.data.local.Exchanges
import com.example.cryptoappfinalproject.databinding.FragmentHomeBinding
import com.example.cryptoappfinalproject.domain.model.CryptoCoinsModel
import com.example.cryptoappfinalproject.domain.model.CryptoExchangesModel
import com.example.cryptoappfinalproject.domain.model.CryptoSearchModel
import com.example.cryptoappfinalproject.presentation.ui.adapters.*
import com.example.cryptoappfinalproject.presentation.ui.favorites.FavoritesViewModel
import com.example.cryptoappfinalproject.presentation.ui.registration.RegistrationViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var binding: FragmentHomeBinding? = null


    private lateinit var homeAdapter: CoinsHomeAdapter
    private lateinit var searchAdapter: CoinsSearchAdapter
    private lateinit var adapterExchanges: ExchangesAdapter
    private val viewModelReg: RegistrationViewModel by viewModels()
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
        setUpBottomNavigation()
        getAllCoinsPager()
        searchCryptos()
        addCoinsToFavList()
//        populateProfilePicture()
        openDrawer()
        recyclerScrollState()


        binding!!.tvExchanges.setOnClickListener {
            getAllExchanges()
            searchExchanges()
            addExchangesToFavList()
        }
        binding!!.tvCryptoAssets.setOnClickListener {
            getAllCoinsPager()
            searchCryptos()
            addCoinsToFavList()
            binding!!.tvExchanges.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey
                )
            )
            binding!!.tvCryptoAssets.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            )

        }


    }

    private fun recyclerScrollState() {
        binding!!.rvHomeCryptoAssets.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    binding!!.rvScrollUp.apply {
                        visibility = View.VISIBLE
                        setOnClickListener {
                            binding!!.rvHomeCryptoAssets.smoothScrollToPosition(0)
                        }
                    }
                } else {
                    binding!!.rvScrollUp.visibility = View.GONE

                }
            }


        }
        )
    }

    private fun openDrawer() {
        binding!!.btnAuth.setOnClickListener {
            val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer)
            drawer.openDrawer(Gravity.LEFT)
        }
    }

    private fun addCoinsToFavList() {
        lifecycleScope.launch {
            viewModelFav.readAllData().collect {
                it.forEach {
                    favList.add(it)
                }
            }
        }
    }


    private fun addExchangesToFavList() {
        lifecycleScope.launch {
            viewModelFav.readAllExchanges().collect {
                it.forEach {
                    favExchanges.add(it)
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
                            binding!!.pbHome.visibility = View.GONE
                        }
                        else -> {
                            binding!!.pbHome.visibility = View.VISIBLE
                        }
                    }
                }

                homeAdapter.submitData(it)


            }

        }

    }


    private fun getAllExchanges() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getExchanges().collect {
                when (it) {
                    is Resource.Success -> {
                        setExchangesAdapter()
                        adapterExchanges.submitList(it)
                        binding!!.tvExchanges.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.white
                            )
                        )
                        binding!!.tvCryptoAssets.setTextColor(
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
        binding!!.rvHomeCryptoAssets.layoutManager = LinearLayoutManager(activity)
        binding!!.rvHomeCryptoAssets.adapter = homeAdapter

        binding!!.rvHomeCryptoAssets.adapter = homeAdapter.withLoadStateFooter(
            footer= MovieLoadStateAdapter{homeAdapter.retry()}
        )
        homeAdapter.onFavListener = {
            favoritesListener(it)
        }


    }


    private fun setSearchAdapter() {
        searchAdapter = CoinsSearchAdapter(requireContext())
        binding!!.rvHomeCryptoAssets.layoutManager = LinearLayoutManager(requireContext())
        binding!!.rvHomeCryptoAssets.adapter = searchAdapter
        searchAdapter.onClickListener = {
            favoritesSearchListener(it)
        }
    }


    private fun setExchangesAdapter() {
        adapterExchanges = ExchangesAdapter(requireContext())
        binding!!.rvHomeCryptoAssets.layoutManager = LinearLayoutManager(requireContext())
        binding!!.rvHomeCryptoAssets.adapter = adapterExchanges
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

            builder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    Toast.makeText(
                        requireContext(),
                        "${crypto.originalTitle}  ${resources.getString(R.string.already_added)}",
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
                        "${crypto.originalTitle}  ${resources.getString(R.string.been_added)}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        builder.setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
        builder.setTitle("${resources.getString(R.string.add)}  ${crypto.originalTitle}?")
        builder.setMessage("${resources.getString(R.string.sure_to_add)} ${crypto.originalTitle}  ${resources.getString(R.string.to_cryptos)}?")
        builder.create().show()


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
        builder.setMessage("${resources.getString(R.string.sure_to_add)} ${exchange.title} ${resources.getString(R.string.to_cryptos)}?")
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
        builder.setMessage("${resources.getString(R.string.sure_to_add)} ${crypto.originalTitle} ${resources.getString(R.string.to_cryptos)}?")
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

    private fun searchExchanges() {
        var displayList: MutableList<CryptoExchangesModel.CryptoExchangesModelItem> =
            mutableListOf()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getExchanges().collect {
                    when (it) {
                        is Resource.Success -> {
                            binding!!.svHome.setOnQueryTextListener(object :
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


    private fun setUpBottomNavigation() {
        val navHostFragment =
            activity!!.supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView: BottomNavigationView =
            activity!!.findViewById(R.id.bottomNavigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }
//
//    private fun populateProfilePicture() {
//
//        lifecycleScope.launch {
//            viewModelReg.readAllUserInfo().collect {
//                val profilePicture = activity!!.findViewById<ImageView>(R.id.ivUserPhoto)
//                val drawerProfileName = activity!!.findViewById<TextView>(R.id.tvUsernameHeader)
//
//                if (Firebase.auth.currentUser != null) {
//                    it.forEach {
//                        Glide.with(requireContext())
//                            .load(it.image)
//                            .error(R.drawable.ic_launcher_background)
//                            .into(profilePicture)
//                        drawerProfileName.text = it.name + " " + it.surname
//
//                    }
//
//                } else {
//                    Glide.with(requireContext())
//                        .load(R.drawable.ic_person)
//                        .error(R.drawable.ic_launcher_background)
//                        .into(profilePicture)
//                    drawerProfileName.text = ""
//
//                }
//
//            }
//        }
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        favList.clear()
        favExchanges.clear()
    }

}



