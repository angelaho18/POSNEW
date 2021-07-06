package com.example.posnew

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItem (var NamaProduk : String, var GambarProduk : String, var Harga : Int, var JumlahProduk : Int): Parcelable {
}