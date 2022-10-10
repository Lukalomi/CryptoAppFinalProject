package com.example.cryptoappfinalproject.ui.favorites

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoappfinalproject.common.Resource
import com.example.cryptoappfinalproject.data.local.Crypto
import com.example.cryptoappfinalproject.databinding.FragmentFavoritesBinding
import com.example.cryptoappfinalproject.domain.CryptoCoinsModel
import com.example.cryptoappfinalproject.domain.CryptoSearchModel
import com.example.cryptoappfinalproject.favList
import com.example.cryptoappfinalproject.ui.adapters.CoinsHomeAdapter
import com.example.cryptoappfinalproject.ui.adapters.FavoritesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class FavoritesFragment : Fragment() {


    private var binding: FragmentFavoritesBinding? = null
    private lateinit var adapter: FavoritesAdapter

    private val viewModelFav: FavoritesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAllFavCoins()
        searchCryptos()
    }

    private fun getAllFavCoins() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModelFav.readAllData().collect {
                setFavAdapter()
                adapter.submitList(it)
            }
        }
    }


    private fun setFavAdapter() {
        adapter = FavoritesAdapter(requireContext())
        binding!!.rvFavCryptoAssets.layoutManager = LinearLayoutManager(activity)
        binding!!.rvFavCryptoAssets.adapter = adapter
        adapter.onClickListener = {
            val builder = AlertDialog.Builder(requireContext())
            builder.setPositiveButton("Yes") { _, _ ->
                viewModelFav.deleteCrypto(it)
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModelFav.readAllData().collect {
                        setFavAdapter()
                        adapter.submitList(it)
                    }
                }

            }
            builder.setNegativeButton("No") { _, _ -> }
            builder.setTitle("Remove ${it.originalTitle}?")
            builder.setMessage("Are You Sure You Want To Remove ${it.originalTitle} From Favorite Cryptos?")
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}