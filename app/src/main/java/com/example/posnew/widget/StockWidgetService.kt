package com.example.posnew.widget

import android.app.Activity
import android.content.ContentProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.pointofsale.Room.Product
import com.example.pointofsale.Room.ProductDBHelper
import com.example.posnew.EXTRA_ITEM
import com.example.posnew.R
import kotlinx.android.synthetic.main.stock_widget.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

private const val TAG = "STOCK_WIDGET"
class StockWidgetService: RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return StockRemoteViewsFactory(this, intent)
    }
}

internal class StockRemoteViewsFactory(private val context: Context, intent: Intent?): RemoteViewsService.RemoteViewsFactory {
    private var db = ProductDBHelper.getInstance(context)
    private var list = ArrayList<Product>()

    override fun onCreate() {
        Log.d(TAG, "onCreate: ")
    }

    override fun onDataSetChanged() {
//        list.add(Product(1, "Indonmie", 1))
//        list.add(Product(2, "Supermie", 2))
//        list.add(Product(3, "Sprite", 5))
//        list.add(Product(4, "Fanta", 4))
        list.clear()
        var datas = db?.productDao()?.getDataForWidget()
        for (data in datas!!){
            list.add(data)
        }
//        SystemClock.sleep(3000)
        Log.d(TAG, "onDataSetChanged: ")
    }

    override fun onDestroy() {
        list.clear()
    }

    override fun getCount(): Int {
        var count = list.size
        return count
    }

    override fun getViewAt(position: Int): RemoteViews {
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_item)
        val item = list[position]

        remoteViews.setTextViewText(R.id.product, item.ProductName)
        remoteViews.setTextViewText(R.id.remains, item.Quantity.toString())

        val bundle = Bundle()
        bundle.putInt(EXTRA_ITEM, position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(bundle)

        remoteViews.setOnClickFillInIntent(R.id.list_widget_item, fillInIntent)

        Log.d(TAG, "getViewAt: ")
        return remoteViews
    }

    override fun getLoadingView(): RemoteViews? {
        return null
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

}
