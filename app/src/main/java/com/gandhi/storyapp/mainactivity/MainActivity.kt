package com.gandhi.storyapp.mainactivity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gandhi.storyapp.R
import com.gandhi.storyapp.ViewModelFactory
import com.gandhi.storyapp.addstoryactivity.AddStoryActivity
import com.gandhi.storyapp.authentication.LoginActivity
import com.gandhi.storyapp.databinding.ActivityMainBinding
import com.gandhi.storyapp.detailactivity.DetailActivity
import com.gandhi.storyapp.detailactivity.StoryAdapter
import com.gandhi.storyapp.map.MapsActivity
import com.gandhi.storyapp.model.UserPreference
import com.gandhi.storyapp.pager3.LoadingStateAdapter
import com.gandhi.storyapp.retrofit.ListStoryItem

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupButton()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        getList()
    }

    private fun setupButton() {
        binding.fab.setOnClickListener {
            startActivity(Intent(this@MainActivity, AddStoryActivity::class.java))
        }
    }

    private fun setupViewModel() {
        mainViewModel = ViewModelProvider(
            this,
            ViewModelFactory(UserPreference.getInstance(dataStore), this)
        )[MainViewModel::class.java]
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
        binding.rvStory.setHasFixedSize(true)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvStory.addItemDecoration(itemDecoration)
    }

    private fun getList() {
        val storyAdapter = StoryAdapter()
        binding.rvStory.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )
        mainViewModel.getUser().observe(this) { token ->

            mainViewModel.getStoryPager(token).observe(this) { result ->
                storyAdapter.submitData(lifecycle, result)
                storyAdapter.setOnClickCallBack(object : StoryAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: ListStoryItem) {
                        val intent = Intent(this@MainActivity, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.EXTRA_ID, data)
                        startActivity(
                            intent,
                            ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity as Activity)
                                .toBundle()
                        )
                    }

                })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout_menu -> {
                mainViewModel.logout()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
                true
            }
            R.id.map_menu -> {
                startActivity(Intent(this@MainActivity, MapsActivity::class.java))
                true
            }
            else -> true
        }
    }
}