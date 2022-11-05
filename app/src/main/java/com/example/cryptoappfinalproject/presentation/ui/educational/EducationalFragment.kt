package com.example.cryptoappfinalproject.presentation.ui.educational

import android.app.Dialog
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.databinding.FragmentEducationalBinding
import com.example.cryptoappfinalproject.domain.model.VideoTitleModel
import com.example.cryptoappfinalproject.presentation.ui.adapters.EducationVideosAdapter
import com.example.cryptoappfinalproject.presentation.ui.registration.RegistrationViewModel
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class EducationalFragment : Fragment(), Player.Listener {


    private var binding: FragmentEducationalBinding? = null
    private val viewModel: EducationalViewModel by viewModels()


    private lateinit var learnAdapter: EducationVideosAdapter
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
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
        setAdapter()

    }


    private fun setAdapter() {
        lifecycleScope.launch {
            learnAdapter = EducationVideosAdapter(requireContext())
            binding!!.rvEducation.layoutManager = GridLayoutManager(requireContext(), 2)
            binding!!.rvEducation.adapter = learnAdapter
            viewModel.getVideos()
            learnAdapter.submitList(viewModel.getVideos())
            learnAdapter.onClickListener = { adapterItem ->

                when (adapterItem.id) {
                    1 -> {
                        setVideoDialog()
                        extractYoutubeVid(player!!, adapterItem.videoUrl!!)
                    }
                    2 -> {
                        setVideoDialog()
                        extractYoutubeVid(player!!, adapterItem.videoUrl!!)
                    }
                    3 -> {
                        setVideoDialog()
                        extractYoutubeVid(player!!, adapterItem.videoUrl!!)
                    }
                    4 -> {
                        setVideoDialog()
                        extractYoutubeVid(player!!, adapterItem.videoUrl!!)
                    }
                    5 -> {
                        setVideoDialog()
                        extractYoutubeVid(player!!, adapterItem.videoUrl!!)
                    }
                    6 -> {
                        setVideoDialog()
                        extractYoutubeVid(player!!, adapterItem.videoUrl!!)
                    }
                    7 -> {
                        setVideoDialog()
                        extractYoutubeVid(player!!, adapterItem.videoUrl!!)
                    }
                    8 -> {
                        setVideoDialog()
                        extractYoutubeVid(player!!, adapterItem.videoUrl!!)
                    }


                }

            }

        }

    }

    private fun setVideoDialog() {
        val dialogBinding = layoutInflater.inflate(R.layout.video_dialog, null)
        val dialog = Dialog(requireContext())
        player = SimpleExoPlayer.Builder(requireContext()).build()
        dialogBinding.findViewById<PlayerView>(R.id.playerViewDialog).player = player

        dialog.setContentView(dialogBinding)
        dialog.setCancelable(true)
        dialog.setOnDismissListener {
            releasePlayer()
        }
        dialog.show()
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


    private fun releasePlayer() {
        if (player != null) {
            playWhenReady = player!!.playWhenReady
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            player!!.release()
            player = null
        }
    }

//    private fun hideSystemUi() {
//        binding!!.playerView.systemUiVisibility =
//            (View.SYSTEM_UI_FLAG_LOW_PROFILE or
//                    View.SYSTEM_UI_FLAG_FULLSCREEN or
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
//                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
//                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
//    }

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





