package com.example.posnew.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo(name = "Barcode_ID") var BarcodeID: String = "",
    @ColumnInfo(name = "PRODUCT_NAME") var ProductName: String = "",
    @ColumnInfo(name = "PRODUCT_QTY") var Quantity: Int = 0,
    @ColumnInfo(name = "PRODUCT_PRICE") var Price: Int = 0,
    @ColumnInfo(name = "PRODUCT_IMG") var ProductPic: String = ""
)