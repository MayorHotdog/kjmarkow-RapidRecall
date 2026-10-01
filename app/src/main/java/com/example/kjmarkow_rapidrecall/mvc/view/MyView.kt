package com.example.kjmarkow_rapidrecall.mvc.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.kjmarkow_rapidrecall.mvc.model.MyModel
import com.example.kjmarkow_rapidrecall.mvc.model.ViewObserver

class MyView : ViewObserver<MyModel> {
    override fun update(model: MyModel) {
        TODO("Not yet implemented")
        // Update the UI using model state
    }

    //private val controller: MyController

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//
//
//    }
}