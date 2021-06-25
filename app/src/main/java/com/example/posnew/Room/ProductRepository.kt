package com.example.posnew.Room

import android.app.Application
import androidx.lifecycle.LiveData
class ProductRepository (application: Application){
    private var productDao: ProductDAO? = null
    private val db = ProductDBHelper.getInstance(application)
    private var readAllData: LiveData<List<Product>>? = null

    init{
        productDao = db?.productDao()
        readAllData = productDao?.getAllData()
    }

    fun insert(product: Product) {
        db?.runInTransaction {
            productDao?.insertAll(product)
        }
    }

    fun update(product: Product) {
        db?.runInTransaction {
            productDao?.updateData(product)
        }
    }

    fun delete(id: Int){
        db?.runInTransaction {
            productDao?.deleteById(id)
        }
    }

    fun getData(): LiveData<List<Product>>{
        return readAllData!!
    }


}