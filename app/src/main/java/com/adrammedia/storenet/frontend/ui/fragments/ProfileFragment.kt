package com.adrammedia.storenet.frontend.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import com.adrammedia.storenet.R
import com.adrammedia.storenet.databinding.FragmentProfileBinding
import com.adrammedia.storenet.frontend.ui.activities.HomeActivity
import com.adrammedia.storenet.frontend.viewmodels.StoreProfileViewModel
import com.adrammedia.storenet.utils.Constants
import com.adrammedia.storenet.utils.DataStatus
import com.adrammedia.storenet.utils.RealPathUtil
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import kotlin.properties.Delegates


@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private val storeProfileViewModel by viewModels<StoreProfileViewModel>()
    private var path:String  =""



    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
          val uri = it.data?.data
           path = RealPathUtil.getRealPath(context, uri)
        binding.profileImage.setImageURI(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("IntentReset")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUserProfile()

        binding.profileImage.setOnClickListener {
                val intent =    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                activityResultLauncher.launch(intent)
        }

        binding.profileFragBtnSave.setOnClickListener {
            storeUserInfo()
        }
    }

    private fun getUserProfile() {
        storeProfileViewModel.getUserProfile(HomeActivity.userId.toInt())
        lifecycleScope.launch {
            storeProfileViewModel.getProfileStateFlow.collect{
                when(it.status) {
                    DataStatus.Status.LOADING->{
                        binding.profileFragPb.visibility = View.VISIBLE
                    }
                    DataStatus.Status.SUCCESS->{
                        binding.profileFragPb.visibility = View.GONE
                       it.data?.data?.let{data->
                           Log.d("TAG", "image: ${Constants.IMAGES_BASE_URL+ data.image}")
                           binding.profileFragBtnSave.isEnabled = false
                            binding.profileFragEtPhone.setText(data.phone)
                            binding.profileFragEtEmail.setText(data.email)
                           Glide.with(requireContext()).load(Constants.IMAGES_BASE_URL+ data.image)
                               .into(binding.profileImage)
                            binding.profileFragEtName.setText(it.data.data.name)
                        }

                    }
                    DataStatus.Status.ERROR->{
                        binding.profileFragPb.visibility = View.GONE
                    }
                }
            }
        }
    }


    private fun storeUserInfo() {
            val userId = HomeActivity.userId.toInt()
            val phone = binding.profileFragEtPhone.text.toString()
            val image = prepareImagePart(path)
            storeProfileViewModel.saveUserInfo(userId,phone,HomeActivity.longitude.toDouble(), HomeActivity.latitude.toDouble(), image)
            lifecycleScope.launch {
                storeProfileViewModel.storeProfileStateFlow.collect{
                    when(it.status) {
                        DataStatus.Status.LOADING->{
                            binding.profileFragPb.visibility = View.VISIBLE
                        }
                        DataStatus.Status.SUCCESS->{
                            binding.profileFragPb.visibility = View.GONE
                            Toast.makeText(context, it.data?.message, Toast.LENGTH_SHORT).show()
                        }
                        DataStatus.Status.ERROR->{
                            binding.profileFragPb.visibility = View.GONE
                            Toast.makeText(context, it.errorMsg, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }


    }
    private fun prepareImagePart(path:String, partName:String ="image"): MultipartBody.Part {
        val file = File(path)
        val requestBody: RequestBody = file.asRequestBody(
            requireContext().contentResolver.getType(Uri.fromFile(file))
                ?.toMediaTypeOrNull()
        )
        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }

}



