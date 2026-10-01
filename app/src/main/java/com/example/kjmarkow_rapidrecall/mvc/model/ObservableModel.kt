package com.example.kjmarkow_rapidrecall.mvc.model

interface ViewObserver<M> {
    fun update( model: M)
}

open class ObservableModel<M> {
    private val observers = mutableListOf< ViewObserver<M> >()

    fun addObserver( o: ViewObserver<M> ) {
        if (o !in observers) {
            observers.add(o)
        }
    }
    fun removeObserver( o: ViewObserver <M> ) {
        observers.remove(o)
    }

    protected fun notifyObservers( model: M ) {
        observers.forEach { observer ->
            observer.update( model )
        }

    }
}