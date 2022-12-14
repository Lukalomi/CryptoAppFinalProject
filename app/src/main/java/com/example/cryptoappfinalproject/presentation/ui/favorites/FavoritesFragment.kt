package com.example.cryptoappfinalproject.presentation.ui.favorites

import android.app.AlertDialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.common.BaseFragment
import com.example.cryptoappfinalproject.data.local.Crypto
import com.example.cryptoappfinalproject.data.local.Exchanges
import com.example.cryptoappfinalproject.databinding.FragmentFavoritesBinding
import com.example.cryptoappfinalproject.presentation.ui.adapters.FavoritesAdapter
import com.example.cryptoappfinalproject.presentation.ui.adapters.FavoritesExchangesAdapter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FragmentFavoritesBinding, FavoritesViewModel>(
    FragmentFavoritesBinding::inflate,
    FavoritesViewModel::class.java
) {

    private lateinit var adapter: FavoritesAdapter
    private lateinit var adapterExchanges: FavoritesExchangesAdapter

    private var count = 0
    private var isCoin: Boolean = true


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

            binding.apply {
                linearNotAvailable.visibility = View.GONE
                svFavorites.visibility = View.VISIBLE
                tvCoins.visibility = View.VISIBLE
                tvExchanges.visibility = View.VISIBLE
                btnSort.visibility = View.VISIBLE
            }
        }
        else {
            binding.apply {
                linearNotAvailable.visibility = View.VISIBLE
                svFavorites.visibility = View.GONE
                tvExchanges.visibility = View.GONE
                tvCoins.visibility = View.GONE
                btnSort.visibility = View.GONE
            }
            navigateToAuth()

        }
    }

    private fun btnSortListener() {
        binding.btnSort.setOnClickListener {
            if (count == 0 && isCoin) {
                sortCoinsByDesc()
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_up)
                binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                count ++
            } else if(count != 0 && isCoin) {
                sortCoinsByAsc()
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_down)
                binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                count --
            }
            else if(count == 0 && !isCoin) {
                sortExByAsc()
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_up)
                binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                count ++
            }
            else if(count != 0 && !isCoin) {
                sortExByDesc()
                val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_down)
                binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
                count --
            }
        }
    }

    private fun setOnCoinsListener() {
        binding.tvCoins.setOnClickListener {
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_down)
            binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            isCoin = true
            getAllFavCoins()
            searchCryptos()
            binding.tvCoins.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
            binding.tvExchanges.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.grey
                )
            );
            binding.svFavorites.setQuery("", true)
            binding.svFavorites.clearFocus()

        }
    }

    private fun setOnExchangesListener() {
        binding.tvExchanges.setOnClickListener {
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.arrow_up)
            binding.btnSort.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null)
            isCoin = false
            getAllExchanges()
            searchExchanges()
            binding.tvCoins.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
            binding.tvExchanges.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.white
                )
            );
            binding.svFavorites.setQuery("", true)
            binding.svFavorites.clearFocus()
        }
    }

    private fun getAllFavCoins() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.readAllData().collect {
                setFavAdapter()
                adapter.submitList(it)
            }
        }
    }

    private fun openDrawer() {
        binding.btnAuth.setOnClickListener {
            val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer)
            drawer.openDrawer(Gravity.LEFT)

        }
    }


    private fun getAllExchanges() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.readAllExchanges().collect {
                setFavExchangeAdapter()
                adapterExchanges.submitList(it)
            }
        }
    }

    private fun sortCoinsByDesc() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.readAllData().collect {
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
            viewModel.readAllData().collect {
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
            viewModel.readAllExchanges().collect {
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
            viewModel.readAllExchanges().collect {
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
        binding.rvFavCryptoAssets.layoutManager = LinearLayoutManager(activity)
        binding.rvFavCryptoAssets.adapter = adapter
        adapter.onClickListener = {
            val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
            builder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                viewModel.deleteCrypto(it)
                findNavController().navigate(FavoritesFragmentDirections.actionReloadFavFragment())


            }
            builder.setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
            builder.setTitle("${resources.getString(R.string.remove)} ${it.originalTitle}?")
            builder.setMessage("${resources.getString(R.string.sure_to_remove)} ${it.originalTitle} ${resources.getString(R.string.from_cryptos)}?")
            builder.create().show()
        }
    }


    private fun setSortedAscCoinsFavAdapter() {
        adapter = FavoritesAdapter(requireContext())
        binding.rvFavCryptoAssets.layoutManager = LinearLayoutManager(activity)
        binding.rvFavCryptoAssets.adapter = adapter
        adapter.onClickListener = {
            val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
            builder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                viewModel.deleteCrypto(it)
                findNavController().navigate(FavoritesFragmentDirections.actionReloadFavFragment())
            }
            builder.setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
            builder.setTitle("${resources.getString(R.string.remove)} ${it.originalTitle}?")
            builder.setMessage("${resources.getString(R.string.sure_to_remove)} ${it.originalTitle} ${resources.getString(R.string.from_cryptos)}?")
            builder.create().show()
        }
    }


    private fun setFavAdapter() {
        adapter = FavoritesAdapter(requireContext())
        binding.rvFavCryptoAssets.layoutManager = LinearLayoutManager(activity)
        binding.rvFavCryptoAssets.adapter = adapter
        adapter.onClickListener = {
            val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
            builder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                viewModel.deleteCrypto(it)
                findNavController().navigate(FavoritesFragmentDirections.actionReloadFavFragment())

            }
            builder.setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
            builder.setTitle("${resources.getString(R.string.remove)} ${it.originalTitle}?")
            builder.setMessage("${resources.getString(R.string.sure_to_remove)} ${it.originalTitle} ${resources.getString(R.string.from_cryptos)}?")
            builder.create().show()
        }
    }

    private fun setFavExAscAdapter() {
        adapterExchanges = FavoritesExchangesAdapter(requireContext())
        binding.rvFavCryptoAssets.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavCryptoAssets.adapter = adapterExchanges
        adapterExchanges.onClickListener = {
            val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
            builder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                viewModel.deleteExchange(it)
                sortExByAsc()
            }
            builder.setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
            builder.setTitle("${resources.getString(R.string.remove)} ${it.title}?")
            builder.setMessage("${resources.getString(R.string.sure_to_remove)} ${it.title} ${resources.getString(R.string.from_cryptos)}?")
            builder.create().show()
        }
    }

    private fun setFavExDescAdapter() {
        adapterExchanges = FavoritesExchangesAdapter(requireContext())
        binding.rvFavCryptoAssets.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavCryptoAssets.adapter = adapterExchanges
        adapterExchanges.onClickListener = {
            val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
            builder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                viewModel.deleteExchange(it)
                sortExByDesc()
            }
            builder.setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
            builder.setTitle("${resources.getString(R.string.remove)} ${it.title}?")
            builder.setMessage("${resources.getString(R.string.sure_to_remove)} ${it.title} ${resources.getString(R.string.from_cryptos)}?")
            builder.create().show()
        }
    }

    private fun setFavExchangeAdapter() {
        adapterExchanges = FavoritesExchangesAdapter(requireContext())
        binding.rvFavCryptoAssets.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavCryptoAssets.adapter = adapterExchanges
        adapterExchanges.onClickListener = {
            val builder = AlertDialog.Builder(requireContext(), R.style.MyDialogTheme)
            builder.setPositiveButton(resources.getString(R.string.yes)) { _, _ ->
                viewModel.deleteExchange(it)
                getAllExchanges()
            }
            builder.setNegativeButton(resources.getString(R.string.no)) { _, _ -> }
            builder.setTitle("${resources.getString(R.string.remove)} ${it.title}?")
            builder.setMessage("${resources.getString(R.string.sure_to_remove)} ${it.title} ${resources.getString(R.string.from_cryptos)}?")
            builder.create().show()
        }
    }

    private fun searchCryptos() {

        var displayList: MutableList<Crypto> = mutableListOf()
        viewLifecycleOwner.lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.readAllData().collect {

                    binding.svFavorites.setOnQueryTextListener(object :
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
                viewModel.readAllExchanges().collect {

                    binding.svFavorites.setOnQueryTextListener(object :
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
    override fun onResume() {
        super.onResume()
        binding!!.svFavorites.setQuery("", true)
    }

    private fun navigateToAuth(){
        binding.btnRegisterFav.setOnClickListener{
            findNavController().navigate(FavoritesFragmentDirections.actionFavoritesFragmentToRegistrationFragment())
        }
        binding.btnLoginFav.setOnClickListener{
            findNavController().navigate(FavoritesFragmentDirections.actionFavoritesFragmentToLoginFragment())
        }
    }


}