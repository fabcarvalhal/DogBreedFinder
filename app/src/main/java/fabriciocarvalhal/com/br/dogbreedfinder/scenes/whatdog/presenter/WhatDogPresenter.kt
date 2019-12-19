package fabriciocarvalhal.com.br.dogbreedfinder.scenes.whatdog.presenter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.google.firebase.ml.common.FirebaseMLException
import com.google.firebase.ml.common.modeldownload.FirebaseLocalModel
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions
import com.google.firebase.ml.common.modeldownload.FirebaseModelManager
import com.google.firebase.ml.common.modeldownload.FirebaseRemoteModel
import com.google.firebase.ml.custom.*
import fabriciocarvalhal.com.br.dogbreedfinder.scenes.whatdog.WhatDog
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.coroutines.resumeWithException
import kotlin.experimental.and
import kotlin.math.max
import kotlin.math.min

class WhatDogPresenter(private val view: WhatDog.View, private val interpreterModelPath: String, private val labelsFilePath: InputStream): WhatDog.Presenter {
    override fun runModelInference(forImage: Bitmap) {
        selectedImage = forImage.copy(Bitmap.Config.RGBA_F16, true)

        runModelInference()
    }


    private val modelInterpreter: FirebaseModelInterpreter? by lazy {
            FirebaseCustomLocalModel.Builder().setAssetFilePath(interpreterModelPath).build().run {
                FirebaseModelInterpreter.getInstance(
                    FirebaseModelInterpreterOptions.Builder(this).build())
            }
    }



    lateinit var selectedImage: Bitmap

    private val labelList by lazy {
        BufferedReader(InputStreamReader(labelsFilePath)).lineSequence().toList()
    }

    private val inputOutputOptions by lazy {
        val inputDims = arrayOf(DIM_BATCH_SIZE, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y, DIM_PIXEL_SIZE)
        val outputDims = arrayOf(DIM_BATCH_SIZE, labelList.size)
        FirebaseModelInputOutputOptions.Builder()
            .setInputFormat(0, FirebaseModelDataType.BYTE, inputDims.toIntArray())
            .setOutputFormat(0, FirebaseModelDataType.BYTE, outputDims.toIntArray())
            .build()
    }


    private val imageBuffer = IntArray(DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y)

    data class LabelConfidence(val label: String, val confidence: Float)


    private fun runModelInference() = selectedImage?.let { image ->

        // Create input data.
        val imgData = bitmapToInputArray(image)

        try {
            // Create model inputs from our image data.
            val modelInputs = FirebaseModelInputs.Builder().add(imgData).build()
            // Perform inference using our model interpreter.
            modelInterpreter?.run(modelInputs, inputOutputOptions)?.continueWith {
                val inferenceOutput = it.result?.getOutput<Array<ByteArray>>(0)!!
                val topLabels = getTopLabels(inferenceOutput)

                topLabels.forEach{
                    Log.d("SERA: ", it)
                }
                view.displayProbability(topLabels[0])
            }

        } catch (exc: FirebaseMLException) {
            val msg = "Error running model inference"
            Log.e("TAG", msg, exc)
        }
    }



    @Synchronized
    private fun bitmapToInputArray(bitmap: Bitmap): ByteBuffer {
        val imgData = ByteBuffer.allocateDirect(
            DIM_BATCH_SIZE * DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE).apply {
            order(ByteOrder.nativeOrder())
            rewind()
        }
        val scaledBitmap =
            Bitmap.createScaledBitmap(bitmap, DIM_IMG_SIZE_X, DIM_IMG_SIZE_Y, true)
        scaledBitmap.getPixels(
            imageBuffer, 0, scaledBitmap.width, 0, 0, scaledBitmap.width, scaledBitmap.height)
        // Convert the image to int points.
        var pixel = 0
        for (i in 0 until DIM_IMG_SIZE_X) {
            for (j in 0 until DIM_IMG_SIZE_Y) {
                val `val` = imageBuffer[pixel++]
                imgData.put((`val` shr 16 and 0xFF).toByte())
                imgData.put((`val` shr 8 and 0xFF).toByte())
                imgData.put((`val` and 0xFF).toByte())
            }
        }
        return imgData
    }



    @Synchronized
    private fun getTopLabels(inferenceOutput: Array<ByteArray>): List<String> {
        // Since we ran inference on a single image, inference output will have a single row.
        val imageInference = inferenceOutput.first()

        // The columns of the image inference correspond to the confidence for each label.
        return labelList.mapIndexed { idx, label ->
            LabelConfidence(label, (imageInference[idx] and 0xFF.toByte()) / 255.0f)
            // Sort the results in decreasing order of confidence and return only top 3.
        }.sortedBy {
            it.confidence }.reversed().map { "${it.label}:${it.confidence}" }
            .subList(0, min(labelList.size, RESULTS_TO_SHOW))
    }

    companion object {
        private const val RESULTS_TO_SHOW = 1
        /** Dimensions of inputs. */
        private const val DIM_BATCH_SIZE = 1
        private const val DIM_PIXEL_SIZE = 3
        private const val DIM_IMG_SIZE_X = 224
        private const val DIM_IMG_SIZE_Y = 224
    }
}