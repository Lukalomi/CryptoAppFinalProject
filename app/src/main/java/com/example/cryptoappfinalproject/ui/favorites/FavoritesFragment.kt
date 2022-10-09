package com.example.cryptoappfinalproject.ui.favorites

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoappfinalproject.databinding.FragmentFavoritesBinding
import com.example.cryptoappfinalproject.favList
import com.example.cryptoappfinalproject.ui.adapters.CoinsHomeAdapter
import com.example.cryptoappfinalproject.ui.adapters.FavoritesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}