package com.vedant.youtubeshorts.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.util.MimeTypes
import com.vedant.youtubeshorts.R
import com.vedant.youtubeshorts.databinding.SingleVideoRowBinding
import com.vedant.youtubeshorts.model.PostsItem

class SingleVideoAdapter(private val context: Context) :
    PagingDataAdapter<PostsItem, SingleVideoAdapter.ViewHolder>(COMPARATOR) {


    class ViewHolder(private val binding: SingleVideoRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var isFav = false
        private var player: ExoPlayer? = null
        private val isPlaying get() = player?.isPlaying ?: false
        fun bind(posts: PostsItem, context: Context) {
            val videoUrl = posts.submission.mediaUrl

            initializePlayer(videoUrl, context)

//            binding.txtVideoTitle.text = posts.submission.title
//            binding.txtVideoDescription.text = posts.submission.description
//            Glide.with(binding.root).load(posts.creator.pic).into(binding.profilePic)
//
//
//            binding.fav.setOnClickListener {
//                isFav = if (!isFav) {
//                    binding.fav.setImageResource(R.drawable.ic_baseline_favorite_24)
//                    true
//                } else {
//                    binding.fav.setImageResource(R.drawable.ic_baseline_favorite_border_24)
//                    false
//                }
//            }
        }

        private fun initializePlayer(videoUri: String, context: Context) {
            player = ExoPlayer.Builder(context)
                .build()
            val mediaItem = MediaItem.Builder()
                .setUri(videoUri)
                .setMimeType(MimeTypes.APPLICATION_MP4)
                .build()

            val mediaSource = ProgressiveMediaSource.Factory(
                DefaultDataSource.Factory(context) // <- context
            )
                .createMediaSource(mediaItem)

            player!!.apply {
                setMediaSource(mediaSource)
//                playWhenReady = true
                seekTo(0, 0L)
                prepare()
            }.also {

                binding.videoView.player = it
            }
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item, context)
        } else {
            Toast.makeText(context, "NULL", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder =
        ViewHolder(
            SingleVideoRowBinding.inflate(
                LayoutInflater.from(
                    parent
                        .context
                )
            )
        )


    companion object {
        private val COMPARATOR = object : DiffUtil.ItemCallback<PostsItem>() {
            override fun areItemsTheSame(oldItem: PostsItem, newItem: PostsItem): Boolean {
                return oldItem.postId == newItem.postId
            }

            override fun areContentsTheSame(oldItem: PostsItem, newItem: PostsItem): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }
}