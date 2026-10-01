package com.example.kjmarkow_rapidrecall

import com.example.kjmarkow_rapidrecall.mvc.control.MyController
import com.example.kjmarkow_rapidrecall.mvc.model.MyModel
import com.example.kjmarkow_rapidrecall.mvc.view.MyView

fun main() {
    val model = MyModel()
    val view = MyView()
    val controller = MyController(model)
    model.addObserver(view)
}