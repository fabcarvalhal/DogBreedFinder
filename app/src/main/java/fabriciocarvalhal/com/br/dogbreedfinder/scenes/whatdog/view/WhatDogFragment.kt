package fabriciocarvalhal.com.br.dogbreedfinder.scenes.whatdog.view


import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_what_dog.*
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.app.Activity.RESULT_OK
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import fabriciocarvalhal.com.br.dogbreedfinder.scenes.whatdog.presenter.WhatDogPresenter
import kotlinx.coroutines.Dispatchers
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import androidx.lifecycle.Lifecycle
import fabriciocarvalhal.com.br.dogbreedfinder.scenes.whatdog.WhatDog

/**
 * A simple [Fragment] subclass.
 */
class WhatDogFragment : Fragment(), WhatDog.View {
    override fun displayProbability(info: String) {
        resultTxtView?.text = info
    }

    private val presenter: WhatDogPresenter by lazy {
        WhatDogPresenter(this, resources.assets.list("")?.firstOrNull { it.endsWith(".tflite") } ?: "", resources.assets.open("output_labels.txt"))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(fabriciocarvalhal.com.br.dogbreedfinder.R.layout.fragment_what_dog, container, false)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when (requestCode) {
            0 -> if (resultCode == RESULT_OK) {
                intent?.data?.let { image ->
                    getBitmap(image)?.let {
                        dogImageView?.setImageBitmap(it)
                        presenter.runModelInference(it)
                    }

                }
            }
        }
    }

    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir =
            context?.filesDir

        return File.createTempFile(
            imageFileName, //prefix
            ".jpg", //suffix
            storageDir       //directory
        )
    }

    private fun getBitmap(uri: Uri): Bitmap? {
        context?.run {
            val bitmap: Bitmap
            bitmap = if(Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(
                    this.contentResolver,
                    uri
                )
            } else {
                val source = ImageDecoder.createSource(this.contentResolver, uri)
                ImageDecoder.decodeBitmap(source)
            }

            return bitmap
        }

        return null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectImageBtn.setOnClickListener {
            val permissions = arrayOf(Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            verifyPermissions(* permissions)
        }
    }

    private fun verifyPermissions(vararg permissions: String) {
        context?.run {
            val areAllPermissionsGranted = permissions.map {
                ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
            }.all { it }
            if (!areAllPermissionsGranted) {
                ActivityCompat.requestPermissions(requireActivity(), permissions, MY_PERMISSION_READ)
            } else {
                openLibrary()
            }

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSION_READ && (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            openLibrary()
        }
    }

    private fun openLibrary() {
        context?.let {
            val takePicture = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            val photoURI = FileProvider.getUriForFile(it,
                "fabriciocarvalhal.com.br.dogbreedfinder.fileprovider",
                createImageFile())
            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            startActivityForResult(takePicture, 0)
        }

    }

    companion object {
        const val MY_PERMISSION_READ = 1
    }
}
