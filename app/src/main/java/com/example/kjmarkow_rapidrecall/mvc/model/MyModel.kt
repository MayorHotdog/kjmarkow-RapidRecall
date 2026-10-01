package com.example.kjmarkow_rapidrecall.mvc.model

class MyModel : ObservableModel<MyModel>() {
    fun update() {
        // Do some data stuff
        notifyObservers(this)
    }


}