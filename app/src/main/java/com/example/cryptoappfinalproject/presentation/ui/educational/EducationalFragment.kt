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
import androidx.recyclerview.widget.GridLayoutManager
import at.huber.youtubeExtractor.VideoMeta
import at.huber.youtubeExtractor.YouTubeExtractor
import at.huber.youtubeExtractor.YtFile
import com.example.cryptoappfinalproject.R
import com.example.cryptoappfinalproject.databinding.FragmentEducationalBinding
import com.example.cryptoappfinalproject.domain.model.VideoTitleModel
import com.example.cryptoappfinalproject.presentation.ui.adapters.EducationVideosAdapter
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.MergingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EducationalFragment : Fragment(), Player.Listener {


    private var binding: FragmentEducationalBinding? = null


    private lateinit var learnAdapter: EducationVideosAdapter
    private var player: SimpleExoPlayer? = null
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition: Long = 0
    private var videosList: MutableList<VideoTitleModel> = mutableListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {


        binding = FragmentEducationalBinding.inflate(inflater, container, false)

        return binding!!.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        submitVideos()
        openDrawer()
        setAdapter()

    }


    private fun setAdapter() {
        learnAdapter = EducationVideosAdapter(requireContext())
        binding!!.rvEducation.layoutManager = GridLayoutManager(requireContext(), 2)
        binding!!.rvEducation.adapter = learnAdapter
        learnAdapter.submitList(videosList)
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

    private fun submitVideos() {
        videosList.add(
            VideoTitleModel(
                id = 1,
                title = getString(R.string.video_one),
                "https://i.ytimg.com/vi/SSo_EIwHSd4/hqdefault.jpg",
                "https://www.youtube.com/watch?v=SSo_EIwHSd4&t=1s&ab_channel=SimplyExplained"
            )
        )
        videosList.add(
            VideoTitleModel(
                id = 2,
                title = getString(R.string.video_two),
                "https://i.ytimg.com/vi/ZE2HxTmxfrI/hqdefault.jpg",
                "https://www.youtube.com/watch?v=ZE2HxTmxfrI&ab_channel=SimplyExplained"
            )
        )
        videosList.add(
            VideoTitleModel(
                id = 3,
                title = getString(R.string.video_three),
                "https://i.ytimg.com/vi/M3EFi_POhps/hqdefault.jpg",
                "https://www.youtube.com/watch?v=M3EFi_POhps&ab_channel=SimplyExplained"
            )
        )
        videosList.add(
            VideoTitleModel(
                id = 4,
                title = getString(R.string.video_four),
                "https://i.ytimg.com/vi/Pl8OlkkwRpc/hqdefault.jpg",
                "https://www.youtube.com/watch?v=Pl8OlkkwRpc&t=3s&ab_channel=TED"
            )
        )
        videosList.add(
            VideoTitleModel(
                id = 5,
                title = getString(R.string.video_five),
                "https://i.ytimg.com/vi/RplnSVTzvnU/hqdefault.jpg",
                "https://www.youtube.com/watch?v=RplnSVTzvnU&ab_channel=TED"
            )
        )
        videosList.add(
            VideoTitleModel(
                id = 6,
                title = getString(R.string.video_six),
                "https://i.ytimg.com/vi/97ufCT6lQcY/hqdefault.jpg",
                "https://www.youtube.com/watch?v=97ufCT6lQcY&ab_channel=TEDxTalks"
            )
        )
        videosList.add(
            VideoTitleModel(
                id = 7,
                title = getString(R.string.video_sevem),
                "https://i.ytimg.com/vi/PXSjbdOXgUE/hqdefault.jpg",
                "https://www.youtube.com/watch?v=PXSjbdOXgUE&ab_channel=99Bitcoins"
            )
        )
        videosList.add(
            VideoTitleModel(
                id = 8,
                title = getString(R.string.video_eight),
                "https://i.ytimg.com/vi/3ZplgUKlDgI/hqdefault.jpg",
                "https://www.youtube.com/watch?v=3ZplgUKlDgI&ab_channel=MoneyZG"
            )
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}





