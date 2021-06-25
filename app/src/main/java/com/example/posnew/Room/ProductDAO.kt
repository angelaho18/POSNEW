package com.example.posnew.Room

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDAO {
    @Query("Select count(*) from Product")
    fun count(): Int

    @Query("Select * from Product order by PRODUCT_NAME asc")
    fun getAllData(): LiveData<List<Product>>

    @Insert
    fun insertAll(vararg product: Product)

    @Update
    fun updateData(product: Product)

    @Delete
    fun deleteData(product: Product)

    @Query("Delete from Product where id=:id")
    fun deleteById(id: Int)

    @Query("Select * from Product where PRODUCT_QTY <= 5 order by PRODUCT_QTY asc")
    fun getDataForWidget(): List<Product>
}
//abstract class ProductDAO {
////    @Transaction
////    abstract fun insertUser(product: Product)
//
//    @Query("Select * from Product order by PRODUCT_NAME asc")
//    abstract fun getAllData(): List<Product>
//
//    @Query("Delete from Product where PRODUCT_NAME=:name")
//    abstract fun deleteByName(name: String)
//
//    @Insert
//    abstract fun insertAll(vararg product: Product)
//
//    @Update
//    abstract fun updateData(product: Product)
//}