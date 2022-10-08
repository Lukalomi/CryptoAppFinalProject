package com.example.cryptoappfinalproject.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.cryptoappfinalproject.FavoritesViewModel
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.Swipe
import com.example.cryptoappfinalproject.data.local.Crypto
import com.example.cryptoappfinalproject.databinding.FragmentHomeBinding
import com.example.cryptoappfinalproject.domain.CryptoCoinsModel
import com.example.cryptoappfinalproject.ui.adapters.CoinsHomeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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
        drawerListener()
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
            val byId = layoutInflater.inflate(R.layout.single_crypto, null)
            val name = byId.findViewById<TextView>(R.id.tvCryptoName)
            var image: ImageView = byId.findViewById(R.id.ivCrypto)
            val crypto = Crypto(
                uid = 0,
                image = it.image!!,
                originalTitle = it.name!!
            )
            name.text = it.name
            Glide.with(requireContext())
                .load(it.image)
                .into(image)
            viewLifecycleOwner.lifecycleScope.launch {
                viewModelFav.insertCrypto(crypto)
                Toast.makeText(requireContext()," ${crypto.originalTitle}, Has Been Added To Favorites", Toast.LENGTH_SHORT).show()
            }

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



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null

    }

}