package com.gandhi.storyapp.detailactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.gandhi.storyapp.databinding.ActivityDetailBinding
import com.gandhi.storyapp.retrofit.ListStoryItem

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    companion object {
        const val EXTRA_ID = "extra_id"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStory()

    }

    private fun setStory() {
        val data = intent.getParcelableExtra<ListStoryItem>(EXTRA_ID)
        val name = data?.name
        val image = data?.photoUrl
        val desc = data?.description
        Glide.with(this@DetailActivity).load(image).into(binding.imageDetail)
        binding.nameDetail.text = name
        binding.descDetail.setText("$desc")
    }


}