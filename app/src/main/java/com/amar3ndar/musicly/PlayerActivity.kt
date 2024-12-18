package com.amar3ndar.musicly

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.amar3ndar.musicstream.R
import com.amar3ndar.musicstream.databinding.ActivityPlayerBinding
import com.bumptech.glide.Glide

class PlayerActivity : AppCompatActivity() {

    lateinit var binding: ActivityPlayerBinding
    lateinit var exoPlayer: ExoPlayer

    private var handler = Handler(Looper.getMainLooper())
    private var playerListener = object : Player.Listener {
        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            updatePlayPauseButton(isPlaying) // Update play/pause button dynamically
        }
    }

    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            // Get current position and duration
            val currentPosition = exoPlayer.currentPosition
            val duration = exoPlayer.duration

            // Format time to MM:SS
            val elapsedTime = formatTime(currentPosition)
            val remainingTime = formatTime(duration - currentPosition)

            // Update the UI with elapsed and remaining time
            binding.elapsedTimeText.text = elapsedTime
            binding.remainingTimeText.text = remainingTime

            // Update the seekbar progress
            binding.seekBar.progress = currentPosition.toInt()
            binding.seekBar.max = duration.toInt()

            // Post again after 1 second
            handler.postDelayed(this, 1000)
        }
    }

    @OptIn(UnstableApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MyExoplayer.getCurrentSong()?.apply {
            binding.songTitleTextView.text = title
            binding.songSubtitleTextView.text = subtitle
            Glide.with(binding.songCoverImage).load(coverUrl)  // Load cover image
                .into(binding.songCoverImage)


            exoPlayer = MyExoplayer.getInstance()!!
            binding.playerView.player = exoPlayer
            binding.playerView.showController()
            exoPlayer.addListener(playerListener)

            // Start updating the time
            handler.post(updateTimeRunnable)

            // Test setting shuffle mode directly
            exoPlayer.shuffleModeEnabled = true
            Log.d("PlayerActivity", "Shuffle mode enabled directly: ${exoPlayer.shuffleModeEnabled}")
        }

        // Initialize button actions
        setupButtonActions()

        // SeekBar change listener
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    exoPlayer.seekTo(progress.toLong())
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.removeListener(playerListener)
        handler.removeCallbacks(updateTimeRunnable) // Stop updating time
    }

    private fun formatTime(milliseconds: Long): String {
        val minutes = (milliseconds / 1000) / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun updatePlayPauseButton(isPlaying: Boolean) {
        val btnPlayPause = binding.playerView.findViewById<ImageButton>(R.id.btn_play_pause)
        if (isPlaying) {
            btnPlayPause.setImageResource(R.drawable.ic_pause) // Show pause icon
        } else {
            btnPlayPause.setImageResource(R.drawable.ic_play_arrow) // Show play icon
        }
    }

    private fun setupButtonActions() {

        val btnSkipNext = binding.playerView.findViewById<ImageButton>(R.id.btn_skip_next) //skip next
        val btnSkipBack = binding.playerView.findViewById<ImageButton>(R.id.btn_skip_back) //skip back
        val btnPlayPause = binding.playerView.findViewById<ImageButton>(R.id.btn_play_pause) //Play and pause
        val btnSkipTimerBack = binding.playerView.findViewById<ImageButton>(R.id.skip_back) //10sec back
        val btnSkipTimerForward = binding.playerView.findViewById<ImageButton>(R.id.skip_forward) //10sec forward
        val btnShuffle = binding.playerView.findViewById<ImageButton>(R.id.shuffle_button) //shuffle
        val btnLoop = binding.playerView.findViewById<ImageButton>(R.id.loop_button) //loop

        // Skip to next song
        btnSkipNext.setOnClickListener {
            Log.d("PlayerActivity", "Skip Next clicked")
            exoPlayer.seekToNext()
        }

        // Skip to previous song
        btnSkipBack.setOnClickListener {
            Log.d("PlayerActivity", "Skip Back clicked")
            exoPlayer.seekToPrevious()
        }

        // Shuffle button click listener
        btnShuffle.setOnClickListener {
            Log.d("PlayerActivity", "Shuffle clicked")
            val shuffleModeEnabled = !exoPlayer.shuffleModeEnabled
            exoPlayer.shuffleModeEnabled = shuffleModeEnabled
            Log.d("PlayerActivity", "Shuffle mode enabled: $shuffleModeEnabled")
            btnShuffle.setImageResource(if (shuffleModeEnabled) R.drawable.ic_on_shuffle else R.drawable.ic_shuffle)
        }

        // Loop current song
        btnLoop.setOnClickListener {
            Log.d("PlayerActivity", "Loop clicked")
            exoPlayer.repeatMode = when (exoPlayer.repeatMode) {
                Player.REPEAT_MODE_OFF -> {
                    btnLoop.setImageResource(R.drawable.ic_on_loop)
                    Player.REPEAT_MODE_ONE
                }
                Player.REPEAT_MODE_ONE -> {
                    btnLoop.setImageResource(R.drawable.ic_on_loop)
                    Player.REPEAT_MODE_ALL
                }
                Player.REPEAT_MODE_ALL -> {
                    btnLoop.setImageResource(R.drawable.ic_loop)
                    Player.REPEAT_MODE_OFF
                }
                else -> {
                    btnLoop.setImageResource(R.drawable.ic_loop)
                    Player.REPEAT_MODE_OFF
                }
            }
        }

        // Play/Pause button click listener
        btnPlayPause.setOnClickListener {
            Log.d("PlayerActivity", "Play/Pause clicked")
            if (exoPlayer.isPlaying) {
                exoPlayer.pause()
                btnPlayPause.setImageResource(R.drawable.ic_play_arrow) // Change to play icon
            } else {
                exoPlayer.play()
                btnPlayPause.setImageResource(R.drawable.ic_pause) // Change to pause icon
            }
        }

        // back 10 sec
        btnSkipTimerBack.setOnClickListener {
            Log.d("PlayerActivity", "Skip Back clicked")
            val newPosition = (exoPlayer.currentPosition - 10000).coerceAtLeast(0)
            exoPlayer.seekTo(newPosition) // Skip back 10 seconds
        }

        // forward 10 sec
        btnSkipTimerForward.setOnClickListener {
            Log.d("PlayerActivity", "Skip Forward clicked")
            val newPosition = (exoPlayer.currentPosition + 10000).coerceAtMost(exoPlayer.duration)
            exoPlayer.seekTo(newPosition) // Skip forward 10 seconds
        }
    }
}