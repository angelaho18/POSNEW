package com.example.posnew.Room

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.posnew.ImageBitmapString
import org.jetbrains.anko.doAsync
import java.io.IOException


@Database(entities = [Product::class], version = 1)
@TypeConverters(ImageBitmapString::class)
abstract class ProductDBHelper : RoomDatabase() {

    abstract fun productDao(): ProductDAO

    companion object {
        private var INSTANCE: ProductDBHelper? = null

        @Synchronized
        fun getInstance(context: Context): ProductDBHelper? {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance
            synchronized(this){
                val instance = Room
                    .databaseBuilder(context.applicationContext,
                        ProductDBHelper::class.java,
                        "productdbex.db")
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }


    fun populateInitialData(context: Context) {
        try {
            doAsync {
                if (productDao().count() === 0) {
                    runInTransaction {
                        Runnable() {
                            var product = Product()
                            product.ProductName = "Hulala"
                            product.Quantity = 1
                            product.Price = 5000
                            productDao().insertAll(product)
                            Log.d(TAG, "populateInitialData: $product")
                        }
                    }
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "populateInitialData: $e")
        }
    }

}




