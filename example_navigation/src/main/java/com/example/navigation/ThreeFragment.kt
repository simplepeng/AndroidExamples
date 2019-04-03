package com.example.navigation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_three.*

class ThreeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_three, container, false)
    }

//    val args: ThreeFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(Color.BLUE)

        val username = ThreeFragmentArgs.fromBundle(arguments!!).username
        val age  = ThreeFragmentArgs.fromBundle(arguments!!).age

        tv_arguments.text = "username == $username ---- age == $age"
    }
}