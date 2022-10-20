package com.example.cryptoappfinalproject.ui.educational

import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.example.cryptoappfinalproject.databinding.FragmentEducationalBinding
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EducationalFragment : Fragment(), Player.Listener {


var someList = mutableListOf<String>()
    private var binding: FragmentEducationalBinding? = null
    var ytVideoURL =
        "https://www.youtube.com/watch?v=ERBEWmfz6h0&list=PLSrm9z4zp4mEWwyiuYgVMWcDFdsebhM-r&ab_channel=Stevdza-San"
var videoCryptoURL = "https://www.youtube.com/watch?v=Yb6825iv0Vk&t=787s&ab_channel=BrianJung"

    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0

    private lateinit var viewModel: EducationalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {


        binding = FragmentEducationalBinding.inflate(inflater, container, false)

        return binding!!.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        openDrawer()

        initPlayer()
        someList.add(ytVideoURL)
        someList.add(videoCryptoURL)

    }


    private fun initPlayer() {
        player = SimpleExoPlayer.Builder(requireContext()).build()
        binding!!.playerView.player = player

        val mediaItem = MediaItem.fromUri(
            videoCryptoURL
        )
        player!!.addMediaItem(mediaItem)




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
                    player!!.setMediaSource(
                        MergingMediaSource(true, videoSource),
                        true
                    )
                    player!!.playWhenReady = playWhenReady
                    player!!.seekTo(currentWindow,playbackPosition)
                }


            }

        }.extract(videoCryptoURL.toString(),false,true)


    }




    override fun onResume() {
        super.onResume()
            initPlayer()
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



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}






















//
//    private fun openDrawer() {
//        binding!!.btnAuth.setOnClickListener {
//            val drawer = requireActivity().findViewById<DrawerLayout>(R.id.drawer)
//            drawer.openDrawer(Gravity.LEFT)
//
//        }
//    }



