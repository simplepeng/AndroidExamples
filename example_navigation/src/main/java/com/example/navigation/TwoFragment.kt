package com.example.navigation

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_two.*

class TwoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_two, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setBackgroundColor(Color.RED)

        var username = arguments?.getString("username")
        var age = arguments?.getInt("age")
        tv_arguments.text = "username == $username --- age == $age"

//        text.text = "Two"
        text.setOnClickListener {
            //            Navigation.findNavController(it).navigateUp()
            Navigation.findNavController(it).popBackStack()
//            Navigation.findNavController(it).navigate(R.id.threeFragment)
        }

        nav.setOnClickListener {
            val action = TwoFragmentDirections.actionTwoFragmentToThreeFragment("chenpeng", 25)
            Navigation.findNavController(it).navigate(action)
        }
    }
}