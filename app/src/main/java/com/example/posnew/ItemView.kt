package com.example.posnew

import android.app.job.JobParameters
import android.app.job.JobService
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.ParsedRequestListener
import com.example.pointofsale.model.Reqres
import com.example.pointofsale.model.ReqresItem
import com.example.posnew.fragments.List
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header

class ItemView: JobService() {
    val TAG = "JOB SCHEDULER"
    var handler: Handler = Handler()
    lateinit var runnable: Runnable
    override fun onStartJob(params: JobParameters?): Boolean {
        Log.d(TAG, "onStartJob: START JOB")
        runnable = Runnable() {
            getItemList(params)
           List.productAdapter.notifyDataSetChanged()
        }
        runnable.run()
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        Log.d(TAG, "onStopJob: STOP JOB")
        handler.removeCallbacks(runnable)
        return true
    }

    private fun getItemList(jobParameters: JobParameters?) {
        var client = AsyncHttpClient()
        var url = "https://www.cheapshark.com/api/1.0/deals"
        var handler = object : AsyncHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                AndroidNetworking.initialize(applicationContext)
                AndroidNetworking.get(url)
                    .build()
                    .getAsObject(Reqres::class.java, object : ParsedRequestListener<Reqres> {
                        override fun onResponse(response: Reqres) {
                            Data.addAll(response)
                            List.productAdapter.notifyDataSetChanged()
                            Log.d(TAG, "onResponse: $response")
                        }

                        override fun onError(anError: ANError?) {
                            Log.d(TAG, "onError: ERROR")
                            Toast.makeText(
                                applicationContext,
                                "Jaringan anda sedang tidak stabil",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    })
                jobFinished(jobParameters, false)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.d(TAG, "Gagalll")
                Toast.makeText(applicationContext, "Fail to Load", Toast.LENGTH_SHORT).show()
                jobFinished(jobParameters, true)
            }
        }
        client.get(url, handler)
    }

    companion object{
        var Data: MutableList<ReqresItem> = mutableListOf()
    }
}