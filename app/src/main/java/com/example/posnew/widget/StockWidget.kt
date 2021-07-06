package com.example.posnew.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast
import com.example.posnew.ActivityFragment
import com.example.posnew.EXTRA_ITEM
import com.example.posnew.EXTRA_NOTIF
import com.example.posnew.R

private const val TAG = "STOCK_WIDGET"

class StockWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        Log.d(TAG, "onUpdate: ")
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created

    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.d(TAG, "onReceive: ${intent?.action}")
        val action = intent?.action
        if (ACTION_REFRESH == action) {
            Log.d(TAG, "onReceive: refresh")
            Toast.makeText(context, "Sync Product to Aplikasi Kasir Ku", Toast.LENGTH_SHORT).show()
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val componentName = ComponentName(context!!.packageName, javaClass.name)
            val appWidgetIds = appWidgetManager.getAppWidgetIds(componentName)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list)
            onUpdate(context, appWidgetManager, appWidgetIds)
        } else if (ACTION_CLICK == action) {
            Log.d(TAG, "onReceive: $ACTION_CLICK")

            var intent = Intent(context, ActivityFragment::class.java)
            intent.apply {
                putExtra(EXTRA_NOTIF, true)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            context?.startActivity(intent)
            val clickedPosition = intent.getIntExtra(EXTRA_ITEM, 0)
        }
    }

    companion object {
        private const val ACTION_CLICK = "actionClick"
        private const val ACTION_REFRESH = "actionRefresh"

        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val widgetText = context.getString(R.string.appwidget_text)

            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.stock_widget)
            views.setTextViewText(R.id.appwidget_text, widgetText)

            val intent = Intent(context, StockWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME))

            views.setRemoteAdapter(R.id.widget_list, intent)

            val fragment = Intent(context, StockWidget::class.java)
            fragment.action = ACTION_CLICK
            val pendingIntent = PendingIntent.getBroadcast(context,
                0,
                fragment,
                0)

            val refreshIntent = Intent(context, StockWidget::class.java)
            refreshIntent.action = ACTION_REFRESH
            val refreshPendingIntent = PendingIntent.getBroadcast(context,
                0,
                refreshIntent,
                PendingIntent.FLAG_UPDATE_CURRENT)

            views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent)
            views.setOnClickPendingIntent(R.id.refresh_btn, refreshPendingIntent)
            views.setPendingIntentTemplate(R.id.widget_list, pendingIntent)

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_list);
            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

