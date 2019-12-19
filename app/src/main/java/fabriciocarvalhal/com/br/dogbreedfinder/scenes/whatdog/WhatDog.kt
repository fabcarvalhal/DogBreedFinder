package fabriciocarvalhal.com.br.dogbreedfinder.scenes.whatdog

import android.graphics.Bitmap

interface WhatDog {
    interface View {
        fun displayProbability(info: String)
    }

    interface Presenter {
        fun runModelInference(forImage: Bitmap)
    }
}