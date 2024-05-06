package com.adrammedia.storenet.frontend.ui.fragments

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrammedia.storenet.databinding.FragmentMyToolsBinding
import com.adrammedia.storenet.frontend.adapters.ToolsAdapter
import com.adrammedia.storenet.frontend.ui.activities.AddToolActivity
import com.adrammedia.storenet.frontend.ui.activities.HomeActivity
import com.adrammedia.storenet.frontend.viewmodels.ToolsViewModel
import com.adrammedia.storenet.utils.DataStatus
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MyToolsFragment : Fragment() {
    private lateinit var binding: FragmentMyToolsBinding
    private val toolsViewModel by viewModels<ToolsViewModel>()
    @Inject
    lateinit var toolsAdapter: ToolsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyToolsBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        populateData()
    }

    private fun populateData() {
        val userId = HomeActivity.userId.toInt()
        toolsViewModel.getUserTools(userId)
        lifecycleScope.launch {
            toolsViewModel.userToolsStateFlow.collect {
                when(it.status){
                    DataStatus.Status.LOADING->{
                        binding.myToolFragPb.visibility =View.VISIBLE
                    }
                    DataStatus.Status.SUCCESS->{
                        binding.myToolFragPb.visibility =View.GONE
                        if (it.data?.isEmpty() == true){
                            showingSnackBar()
                        } else {
                            toolsAdapter.differ.submitList(it.data)
                        }
                    }
                    DataStatus.Status.ERROR->{
                        binding.myToolFragPb.visibility =View.GONE
                        Toast.makeText(activity, it.errorMsg, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showingSnackBar() {
        Snackbar.make(
            binding.myToolFragPb,
            "OPS!! you didn't publish any tool yet", Snackbar.LENGTH_SHORT).setAction("Add tool now") {
                startActivity(Intent(activity, AddToolActivity::class.java))
            }.setActionTextColor(Color.BLUE)
            .setBackgroundTint(Color.WHITE)
            .setTextColor(Color.DKGRAY)
                .show()
    }

    private fun setUpRecyclerView() {
        binding.myToolsFragRv.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = toolsAdapter
        }
    }

    override fun onStart() {
        super.onStart()
       populateData()
    }

}