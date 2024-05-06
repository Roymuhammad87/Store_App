package com.adrammedia.storenet.frontend.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.adrammedia.storenet.R
import com.adrammedia.storenet.backend.data.categories.Category
import com.adrammedia.storenet.databinding.ActivityAddToolBinding
import com.adrammedia.storenet.frontend.adapters.IntentImageAdapter
import com.adrammedia.storenet.frontend.viewmodels.CategoriesViewModel
import com.adrammedia.storenet.frontend.viewmodels.ToolsViewModel
import com.adrammedia.storenet.utils.Constants
import com.adrammedia.storenet.utils.RealPathUtil
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class AddToolActivity : AppCompatActivity() {
    private  val TAG = "AddToolActivity"
    private lateinit var binding: ActivityAddToolBinding
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: Editor
    private val toolsViewModel by viewModels<ToolsViewModel>()
    private val categoryViewModel by viewModels<CategoriesViewModel>()
    private var files = mutableListOf<File>()
    private var categories = listOf<Category>()
    private var uris = listOf<Uri>()
    @Inject
    lateinit var intentImageAdapter: IntentImageAdapter

    companion object {
        var apiToken =""
    }

    private val imageRl = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        it.let { intent ->
            try {
                val items = intent.data?.clipData?.itemCount!!
                Log.d(TAG, "items: $items ")
                for (i in 0..<items) {
                    val uri: Uri = intent.data?.clipData?.getItemAt(i)!!.uri
                    val path = RealPathUtil.getRealPath(this, uri)
                    val file = File(path)
                    files += file
                    uris+= uri
                 }
                intentImageAdapter.differ.submitList(uris)
            } catch (e:Exception) {
                Log.d("TAG", "msg: ${e.message}")
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("CommitPrefEdits", "IntentReset")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddToolBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
           if (!checkMemoryPermissions())
               requestMemoryPermission()
        sharedPreferences = getSharedPreferences("token", MODE_PRIVATE)
        setupRecyclerView()

        sharedPreferences = getSharedPreferences("token", MODE_PRIVATE)
        editor = sharedPreferences.edit()
        val categories = getCategories()
        val categoriesAdapter = ArrayAdapter(this,R.layout.category_item,categories)
        val stateAdapter = ArrayAdapter(this, R.layout.category_item, Constants.TOOL_AVAILABILITY)
        binding.etMainAvailability.setAdapter(stateAdapter)

        binding.etMainCategId.setAdapter(categoriesAdapter)


        binding.tvMainAddToolImages.setOnClickListener {
            getToolImages()
        }

        binding.btnMainStoreTool.setOnClickListener {
               storeTool()
           }
    }


    /**
     *  Logic Business Functions
     */

    //Get Tool images from intent
    @SuppressLint("IntentReset")
    private fun getToolImages() {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        imageRl.launch(intent)
    }


    //Store a Tool
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun storeTool() {
        apiToken = sharedPreferences.getString("userToken", "").toString()
        val name = binding.etMainName.text.toString()
        val desc = binding.etMainDescription.text.toString()
        val price = binding.etMainPrice.text.toString()
        val state = binding.etMainAvailability.text.toString()
        val stateBoolean = when (state) {
            Constants.AVAILABLE -> 1
            Constants.NOT_AVAILABLE ->0
            else->Unit
        }
        val userId = sharedPreferences.getString("id", "")
        val categoryName = binding.etMainCategId.text.toString()
        val images = mutableListOf<MultipartBody.Part>()
        val requestName: RequestBody = name.toRequestBody("text/plain".toMediaTypeOrNull())
        val requestDesc: RequestBody = desc.toRequestBody("text/plain".toMediaTypeOrNull())
        val requestPrice: RequestBody = price.toRequestBody("text/plain".toMediaTypeOrNull())
        val requestState: RequestBody =
            stateBoolean.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val requestUserId: RequestBody =
            userId!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val requestCategoryName: RequestBody =
            categoryName.toRequestBody("text/plain".toMediaTypeOrNull())

        files.forEach { file ->
            Log.d("TAG", "path:${Uri.fromFile(file)} ")
            val body = prepareImagePart(file.path)
            images += body
        }
        toolsViewModel.storeTool(requestName, requestDesc, requestPrice,requestState, requestUserId, requestCategoryName, *images.toTypedArray())
        lifecycleScope.launch {
            toolsViewModel.toolsStateFlow.collect { response ->
                if (response?.status == 201) {
                    Toast.makeText(this@AddToolActivity, response.message, Toast.LENGTH_SHORT).show()
                    Log.d("TAG", "tool profile: ${response.data.toolImages}")
                    clearFields()
                    finish()
                } else if (response?.status == 400){
                    Toast.makeText(this@AddToolActivity, response.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //Prepare Tool images for sending it to the server
    private fun prepareImagePart(path:String, partName:String ="images[]"):MultipartBody.Part {
        val file = File(path)
        val requestBody:RequestBody = file.asRequestBody(
            contentResolver.getType(Uri.fromFile(file))
                ?.toMediaTypeOrNull()
        )
        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }

    //Get Categories List
    private fun getCategories():List<String>{
        val categoriesName = mutableListOf<String>()
        categoryViewModel.getCategories()
        lifecycleScope.launch {
            categoryViewModel.categoriesStateFlow.collect{
                it?.let { categoriesResponse->
                    if(categoriesResponse.status == 200){
                        categories = categoriesResponse.data
                        for (i in categories.indices){
                            val name = categories[i].categoryName
                            categoriesName += name

                        }
                    }
                }
            }
        }
        return categoriesName
    }

    //Clear Fields After successfully storing the tool
    private fun clearFields(){
        binding.apply {
            etMainName.text.clear()
            etMainDescription.text.clear()
            etMainPrice.text.clear()
            etMainAvailability.text.clear()
            etMainCategId.text.clear()
        }
        files.clear()
        intentImageAdapter.differ.submitList(emptyList())
    }

    //Display images in recycler view after getting them from intent
    private fun setupRecyclerView() {
        binding.rvMain.apply {
            layoutManager = LinearLayoutManager(this@AddToolActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = intentImageAdapter
        }
    }

    //Permissions
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkMemoryPermissions():Boolean{
        return (ContextCompat.checkSelfPermission(this,
            Constants.STORAGE_PERMISSION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
            Constants.AUDIO_PERMISSION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
            Constants.VIDEO_PERMISSION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
            Constants.WRITE_PERMISSION) == PackageManager.PERMISSION_GRANTED)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestMemoryPermission(){
        ActivityCompat.requestPermissions(this,
            Constants.PERMISSIONS_ARRAY, 100
        )

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED
                && grantResults[3] == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "Memory Permissions granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Memory Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}