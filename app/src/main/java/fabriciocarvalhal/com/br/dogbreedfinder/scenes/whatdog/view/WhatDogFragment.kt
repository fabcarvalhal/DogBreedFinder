package fabriciocarvalhal.com.br.dogbreedfinder.scenes.whatdog.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fabriciocarvalhal.com.br.dogbreedfinder.R

/**
 * A simple [Fragment] subclass.
 */
class WhatDogFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_what_dog, container, false)
    }


}
