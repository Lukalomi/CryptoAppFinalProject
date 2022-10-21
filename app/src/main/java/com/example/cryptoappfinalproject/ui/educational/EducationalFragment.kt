package com.example.cryptoappfinalproject.ui.educational

import android.R.string
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.databinding.FragmentEducationalBinding
import com.example.cryptoappfinalproject.domain.VideoTitleModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.apache.commons.io.IOUtils
import org.json.JSONObject
import java.net.URL


@AndroidEntryPoint
class EducationalFragment : Fragment(), Player.Listener {


    private var binding: FragmentEducationalBinding? = null
    private var blockChain = "https://www.youtube.com/watch?v=SSo_EIwHSd4&t=1s&ab_channel=SimplyExplained"
    private var smartContracts = "https://www.youtube.com/watch?v=ZE2HxTmxfrI&ab_channel=SimplyExplained"
    private val pOsPoW = "https://www.youtube.com/watch?v=M3EFi_POhps&ab_channel=SimplyExplained"


    var position = 0
    private var nextClicked: Boolean = false


    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    private  val viewModel: EducationalViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {


        binding = FragmentEducationalBinding.inflate(inflater, container, false)

        return binding!!.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        openDrawer()
        getYtTitle ()

    }


    private fun getYtTitle () {
        lifecycleScope.launch {
            player = SimpleExoPlayer.Builder(requireContext()).build()
            binding!!.playerView.player = player
            extractYoutubeVid(player!!, blockChain)
            viewModel.getYTTitle1().collect {
                binding!!.tvTitle.text = it.title
            }
                requireActivity().findViewById<ImageButton>(R.id.next).setOnClickListener {
                    if(position == 0 ) {
                        nextClicked = true
                        extractYoutubeVid(player!!, smartContracts)
                        position++
                        lifecycleScope.launch {
                            viewModel.getYTTitle2().collect {
                                binding!!.tvTitle.text = it.title
                            }

                            }

                    }
                    else{
                        nextClicked = true
                        extractYoutubeVid(player!!, pOsPoW)
                        lifecycleScope.launch {
                            viewModel.getYTTitles3().collect {
                                binding!!.tvTitle.text = it.title
                            }
                        }
                        position--
                    }
            }
            requireActivity().findViewById<ImageButton>(R.id.back).setOnClickListener {
                if(position == 0 && nextClicked) {
                    extractYoutubeVid(player!!, blockChain)
                    lifecycleScope.launch {
                        viewModel.getYTTitle1().collect {
                            binding!!.tvTitle.text = it.title
                        }
                    }
                }
                if(position == 1) {
                    extractYoutubeVid(player!!, smartContracts)
                    lifecycleScope.launch {
                        viewModel.getYTTitle2().collect {
                            binding!!.tvTitle.text = it.title
                        }
                    }
                    position --
                }
            }

            }
        }



    private fun extractYoutubeVid(vidPlayer: SimpleExoPlayer, URL: String) {
        object : YouTubeExtractor(requireContext()) {
            override fun onExtractionComplete(
                ytFiles: SparseArray<YtFile>?,
                videoMeta: VideoMeta?
            ) {
                if (ytFiles != null) {
                    val itag = 18
                    val videoUrl = ytFiles[itag].url
                    val videoSource: MediaSource =
                        ProgressiveMediaSource.Factory(DefaultHttpDataSource.Factory())
                            .createMediaSource(MediaItem.fromUri(videoUrl))
                    vidPlayer!!.setMediaSource(
                        MergingMediaSource(true, videoSource),
                        true
                    )
                    vidPlayer!!.playWhenReady = playWhenReady
                    vidPlayer!!.seekTo(currentWindow, playbackPosition)

                }


            }


        }.extract(URL, false, true)
    }


    override fun onResume() {
        super.onResume()
//        initPlayer()
        getYtTitle()
        hideSystemUi()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }

    private fun hideSystemUi() {
        binding!!.playerView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

    //
    private fun openDrawer() {
        binding!!.btnAuth.setOnClickListener {
            val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer)
            drawer.openDrawer(GravityCompat.START)

        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}





