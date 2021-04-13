package com.example.hashtag.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.hashtag.databinding.FragmentHomeBinding
import com.example.hashtag.utils.DataState
import com.example.hashtag.viewmodels.ActivityViewModel
import com.example.localdatabase.RoomDao
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    @Inject
    lateinit var roomDao: RoomDao
    private val activityViewModel: ActivityViewModel by viewModels()
    lateinit var fragmentHomeBinding: FragmentHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeSuccessStatusAfterStoringInLocalDatabse()
        fragmentHomeBinding.btDownload.setOnClickListener {
            loadImage()
        }
    }

    fun loadImage() {
        roomDao.getImagePath().observe(viewLifecycleOwner, Observer {
            // Called  when image is stored in the Roomdatabase
            if (it.isNotEmpty()) {
                Glide.with(requireActivity()).load(it.get(0).image_path).into(fragmentHomeBinding.ivDownloaded)
            }
            // Api called first time when image is not stored in the Roomdatabase
            else {
                activityViewModel.getImagePath(ActivityViewModel.MainStateEvent.GetApiResponse)
            }
        })
    }

    fun observeSuccessStatusAfterStoringInLocalDatabse() {
        activityViewModel.apiResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DataState.Success<Boolean> -> {
                    fragmentHomeBinding.progressbar.visibility = View.GONE
                    Toast.makeText(requireActivity(), "Image stored successfully.", Toast.LENGTH_LONG).show()
                }
                is DataState.Error -> {
                    Toast.makeText(requireActivity(), "Something went wrong", Toast.LENGTH_LONG).show()
                    fragmentHomeBinding.progressbar.visibility = View.GONE
                }
                is DataState.Loading -> {
                    if (!fragmentHomeBinding.progressbar.isVisible)
                        fragmentHomeBinding.progressbar.visibility = View.VISIBLE
                }
            }
        })
    }

}