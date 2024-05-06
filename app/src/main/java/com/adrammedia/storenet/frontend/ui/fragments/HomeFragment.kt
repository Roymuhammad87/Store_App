package com.adrammedia.storenet.frontend.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrammedia.storenet.R
import com.adrammedia.storenet.databinding.FragmentHomeBinding
import com.adrammedia.storenet.frontend.adapters.ToolsAdapter
import com.adrammedia.storenet.frontend.ui.activities.AddToolActivity
import com.adrammedia.storenet.frontend.viewmodels.ToolsViewModel
import com.adrammedia.storenet.utils.DataStatus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val toolsViewModel by viewModels<ToolsViewModel>()
    @Inject
    lateinit var toolsAdapter: ToolsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        populateData()
        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(activity, AddToolActivity::class.java))
        }
    }

    private fun populateData() {
        toolsViewModel.getAllTools()
        lifecycleScope.launch {
            toolsViewModel.allToolsStateFlow.collect {
                when(it.status){
                    DataStatus.Status.LOADING->{
                       binding.homeFragPb.visibility =View.VISIBLE
                    }
                    DataStatus.Status.SUCCESS->{
                        toolsAdapter.differ.submitList(it.data)
                        binding.homeFragPb.visibility =View.GONE
                    }
                    DataStatus.Status.ERROR->{
                        Toast.makeText(context, it.errorMsg, Toast.LENGTH_SHORT).show()
                        binding.homeFragPb.visibility =View.GONE
                    }
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.homeFragRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = toolsAdapter
        }
    }

    override fun onStart() {
        super.onStart()
        populateData()
    }

}