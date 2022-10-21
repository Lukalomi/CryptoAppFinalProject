package com.example.cryptoappfinalproject.ui.favorites

import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.data.local.Crypto
import com.example.cryptoappfinalproject.data.local.Exchanges
import com.example.cryptoappfinalproject.databinding.FragmentFavoritesBinding
import com.example.cryptoappfinalproject.ui.adapters.FavoritesAdapter
import com.example.cryptoappfinalproject.ui.adapters.FavoritesExchangesAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class FavoritesFragment : Fragment() {


    private var binding: FragmentFavoritesBinding? = null
    private lateinit var adapter: FavoritesAdapter
    private lateinit var adapterExchanges: FavoritesExchangesAdapter

    private val viewModelFav: FavoritesViewModel by activityViewModels()
    private var count = 0

    private var isCoin: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isUserAvailable()
        openDrawer()
    }


    private fun isUserAvailable() {
        if(Firebase.auth.currentUser != null) {
            getAllFavCoins()
            searchCryptos()
            setOnExchangesListener()
            setOnCoinsListener()
            btnSortListener()

            binding!!.linearNotAvailable.visibility = View.GONE
        }
        else {
            binding!!.linearNotAvailable.visibility = View.VISIBLE

        }
    }

    private fun btnSortListener() {
        binding!!.btnSort.setOnClickListener {
            if (count == 0 && isCoin) {
                sortCoinsByDesc()
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_up)
                binding!!.btnSort.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                count ++
            } else if(count != 0 && isCoin) {
                sortCoinsByAsc()
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_down)
                binding!!.btnSort.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                count --
            }
            else if(count == 0 && !isCoin) {
                sortExByAsc()
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_up)
                binding!!.btnSort.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                count ++
            }
            else if(count != 0 && !isCoin) {
                sortExByDesc()
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_down)
                binding!!.btnSort.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                count --
            }
        }
    }

    private fun setOnCoinsListener() {
        binding!!.tvCoins.setOnClickListener {
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_down)
            binding!!.btnSort.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            isCoin = true
            getAllFavCoins()
            searchCryptos()
            binding!!.tvCoins.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            binding!!.tvExchanges.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey
                )
            );


        }
    }

    private fun setOnExchangesListener() {
        binding!!.tvExchanges.setOnClickListener {
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_up)
            binding!!.btnSort.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            isCoin = false
            getAllExchanges()
            searchExchanges()
            binding!!.tvCoins.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
            binding!!.tvExchanges.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            );

        }
    }

    private fun getAllFavCoins() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelFav.readAllData().collect {
                setFavAdapter()
                adapter.submitList(it)
            }
        }
    }

    private fun openDrawer() {
        binding!!.btnAuth.setOnClickListener {
            val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer)
            drawer.openDrawer(Gravity.LEFT)

        }
    }


    private fun getAllExchanges() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelFav.readAllExchanges().collect {
                setFavExchangeAdapter()
                adapterExchanges.submitList(it)
            }
        }
    }

    private fun sortCoinsByDesc() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelFav.readAllData().collect {
                it.sortByDescending {
                    it.marketCapRank
                }
                setSortedDescCoinsFavAdapter()
                adapter.submitList(it)

            }
        }
    }

    private fun sortCoinsByAsc() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelFav.readAllData().collect {
                it.sortedBy {
                    it.marketCapRank
                }
                setSortedAscCoinsFavAdapter()
                adapter.submitList(it)

            }
        }
    }

    private fun sortExByDesc() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelFav.readAllExchanges().collect {
                it.sortByDescending {
                    it.rank
                }
                setFavExDescAdapter()
                adapterExchanges.submitList(it)
            }
        }
    }

    private fun sortExByAsc() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelFav.readAllExchanges().collect {
                it.sortedBy {
                    it.rank
                }
                setFavExAscAdapter()
                adapterExchanges.submitList(it)
            }
        }
    }


    private fun setSortedDescCoinsFavAdapter() {
        adapter = FavoritesAdapter(requireContext())
        binding!!.rvFavCryptoAssets.layoutManager = LinearLayoutManager(activity)
        binding!!.rvFavCryptoAssets.adapter = adapter
        adapter.onClickListener = {
            val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
            builder.setPositiveButton("Yes") { _, _ ->
                viewModelFav.deleteCrypto(it)
                findNavController().navigate(FavoritesFragmentDirections.actionReloadFavFragment())


            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.setTitle("Remove ${it.originalTitle}?")
            builder.setMessage("Are You Sure You Want To Remove ${it.originalTitle} From Favorite Cryptos?")
            builder.create().show()
        }
    }


    private fun setSortedAscCoinsFavAdapter() {
        adapter = FavoritesAdapter(requireContext())
        binding!!.rvFavCryptoAssets.layoutManager = LinearLayoutManager(activity)
        binding!!.rvFavCryptoAssets.adapter = adapter
        adapter.onClickListener = {
            val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
            builder.setPositiveButton("Yes") { _, _ ->
                viewModelFav.deleteCrypto(it)
                findNavController().navigate(FavoritesFragmentDirections.actionReloadFavFragment())
            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.setTitle("Remove ${it.originalTitle}?")
            builder.setMessage("Are You Sure You Want To Remove ${it.originalTitle} From Favorite Cryptos?")
            builder.create().show()
        }
    }


    private fun setFavAdapter() {
        adapter = FavoritesAdapter(requireContext())
        binding!!.rvFavCryptoAssets.layoutManager = LinearLayoutManager(activity)
        binding!!.rvFavCryptoAssets.adapter = adapter
        adapter.onClickListener = {
            val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
            builder.setPositiveButton("Yes") { _, _ ->
                viewModelFav.deleteCrypto(it)
                findNavController().navigate(FavoritesFragmentDirections.actionReloadFavFragment())

            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.setTitle("Remove ${it.originalTitle}?")
            builder.setMessage("Are You Sure You Want To Remove ${it.originalTitle} From Favorite Cryptos?")
            builder.create().show()
        }
    }

    private fun setFavExAscAdapter() {
        adapterExchanges = FavoritesExchangesAdapter(requireContext())
        binding!!.rvFavCryptoAssets.layoutManager = LinearLayoutManager(requireContext())
        binding!!.rvFavCryptoAssets.adapter = adapterExchanges
        adapterExchanges.onClickListener = {
            val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
            builder.setPositiveButton("Yes") { _, _ ->
                viewModelFav.deleteExchange(it)
                sortExByAsc()
            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.setTitle("Remove ${it.title}?")
            builder.setMessage("Are You Sure You Want To Remove ${it.title} From Favorite Cryptos?")
            builder.create().show()
        }
    }

    private fun setFavExDescAdapter() {
        adapterExchanges = FavoritesExchangesAdapter(requireContext())
        binding!!.rvFavCryptoAssets.layoutManager = LinearLayoutManager(requireContext())
        binding!!.rvFavCryptoAssets.adapter = adapterExchanges
        adapterExchanges.onClickListener = {
            val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
            builder.setPositiveButton("Yes") { _, _ ->
                viewModelFav.deleteExchange(it)
                sortExByDesc()
            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.setTitle("Remove ${it.title}?")
            builder.setMessage("Are You Sure You Want To Remove ${it.title} From Favorite Cryptos?")
            builder.create().show()
        }
    }

    private fun setFavExchangeAdapter() {
        adapterExchanges = FavoritesExchangesAdapter(requireContext())
        binding!!.rvFavCryptoAssets.layoutManager = LinearLayoutManager(requireContext())
        binding!!.rvFavCryptoAssets.adapter = adapterExchanges
        adapterExchanges.onClickListener = {
            val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
            builder.setPositiveButton("Yes") { _, _ ->
                viewModelFav.deleteExchange(it)
                getAllExchanges()
            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.setTitle("Remove ${it.title}?")
            builder.setMessage("Are You Sure You Want To Remove ${it.title} From Favorite Cryptos?")
            builder.create().show()
        }
    }

    private fun searchCryptos() {

        var displayList: MutableList<Crypto> = mutableListOf()
        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelFav.readAllData().collect {

                    binding!!.svFavorites.setOnQueryTextListener(object :
                        SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {

                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {

                            if (newText!!.isNotEmpty()) {
                                displayList.clear()
                                val search = newText.lowercase(Locale.getDefault())
                                it.forEach {
                                    if (it.originalTitle.lowercase(Locale.getDefault())
                                            .contains(search)
                                    ) {
                                        displayList.add(it)
                                    }
                                }
                                setFavAdapter()
                                adapter.submitList(displayList)

                            } else {
                                getAllFavCoins()
                            }

                            return true
                        }
                    })


                }

            }
        }
    }

    private fun searchExchanges() {
        var displayList: MutableList<Exchanges> = mutableListOf()
        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelFav.readAllExchanges().collect {

                    binding!!.svFavorites.setOnQueryTextListener(object :
                        SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {

                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {

                            if (newText!!.isNotEmpty()) {
                                displayList.clear()
                                val search = newText.lowercase(Locale.getDefault())
                                it.forEach {
                                    if (it.title.lowercase(Locale.getDefault())
                                            .contains(search)
                                    ) {
                                        displayList.add(it)
                                    }
                                }
                                setFavExchangeAdapter()
                                adapterExchanges.submitList(displayList)

                            } else {
                                getAllExchanges()
                            }

                            return true
                        }
                    })


                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        count = 0
    }

}