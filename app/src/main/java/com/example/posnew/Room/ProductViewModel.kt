package com.example.posnew.Room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class ProductViewModel (application: Application): AndroidViewModel(application){
    private val repository = ProductRepository(application)
    val products = repository.getData()
    
    fun insert(product: Product){
        repository.insert(product)
    }
    fun update(product: Product){
        repository.update(product)
    }
    fun delete(id: Int){
        repository.delete(id)
    }
    fun getAllData(): LiveData<List<Product>>{
        return products
    }
}