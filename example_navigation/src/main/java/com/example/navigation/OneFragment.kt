package com.example.navigation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_one.*

class OneFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_one, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text.text = "One"
        text.setOnClickListener {

            var bundle = Bundle()
            bundle.putString("username", "simple")
            bundle.putInt("age", 25)
            Navigation.findNavController(it)
                    .navigate(R.id.twoFragment, bundle)

        }

        btn_navActivity.setOnClickListener {
            startActivity(Intent(activity, NavActivity::class.java))
        }
    }
}