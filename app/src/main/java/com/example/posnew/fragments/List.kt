package com.example.posnew.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.posnew.EXTRA_SCAN
import com.example.posnew.Room.Product
import com.example.posnew.Room.ProductViewModel
import com.example.posnew.ItemView
import com.example.posnew.PREF_NAME
import com.facebook.shimmer.ShimmerFrameLayout
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import com.example.posnew.R
import com.example.posnew.Room.ProductAdapter
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class List : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var scannedResult: String = ""

    private lateinit var ShimmerView: ShimmerFrameLayout
    private val vm: ProductViewModel by viewModels()
    private val SELECT_PICTURE = 1
    private var selectedImagePath: String? = null
    private lateinit var imageSource: String
    private var inputData: ByteArray? = null
    var filename: String? = ""
    private lateinit var notificationManager: NotificationManagerCompat

    lateinit var dialog: AlertDialog
    lateinit var service: Intent
    var JobSchedulerId = 5
    var query: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)

        ShimmerView = view.findViewById(R.id.shimmerFrameLayout)

        query = arguments?.getString("query")
        val input = view.findViewById<SearchView>(R.id.input)
        input.setQuery(query, false)
        input.clearFocus()

        Handler(Looper.getMainLooper()).postDelayed({
            ShimmerView.stopShimmer()
            ShimmerView.visibility = View.GONE
        }, 3000)

        val productRecyclerView = view.findViewById<RecyclerView>(R.id.productRecyclerView)

        productAdapter = ProductAdapter(query, vm)
        productRecyclerView.adapter = productAdapter
        productRecyclerView.layoutManager = LinearLayoutManager(context)

        vm.getAllData().observe(viewLifecycleOwner, Observer {
            productAdapter.setData(it)
        })

        val addbutton = view.findViewById<Button>(R.id.bt_addStock)

        addbutton.setOnClickListener {
            val addView = View.inflate(context, R.layout.layout_pop_up, null)

            val builder = AlertDialog.Builder(context)
            builder.setView(addView)
            dialog = builder.create()

            val browseBtn = addView.findViewById<Button>(R.id.browse_btn)
            browseBtn.setOnClickListener {
                val intent = Intent()
                intent.type = "image/*"
                intent.action = Intent.ACTION_OPEN_DOCUMENT
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE)
                val imgFileName = addView.findViewById<TextView>(R.id.image_file_name)
                imgFileName.text = filename
            }

            val Barcodeid = addView.findViewById<Button>(R.id.bt_AddID)

            Barcodeid.setOnClickListener {
                run {
                    IntentIntegrator.forSupportFragment(this).initiateScan()
                }
            }

            val confirmBtn = addView.findViewById<Button>(R.id.bt_add_to_conf)
            val cancelBtn = addView.findViewById<Button>(R.id.bt_cancel)
            val name = addView.findViewById<EditText>(R.id.inputName)
            val qty = addView.findViewById<EditText>(R.id.inputQty)
            val price = addView.findViewById<EditText>(R.id.inputPrice)
            dialog.show()
            var hasil = ""

            confirmBtn.setOnClickListener {
                doAsync {
                    try {
                        var productTmp = Product()
                        productTmp.ProductName = name.text.toString()
                        productTmp.Quantity = qty.text.toString().toInt()
                        productTmp.Price = price.text.toString().toInt()
                        productTmp.ProductPic = imageSource
                        vm.insert(productTmp)

                        var data = vm.getAllData()

                    } catch (e: Exception) {
                        uiThread {
                            Log.e("ERROR DB", "$e")
                            Toast.makeText(view.context, "$e", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                dialog.dismiss()
            }
        }

        return view
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode === Activity.RESULT_OK) {
            if (requestCode === SELECT_PICTURE) {
                val selectedImageUri: Uri? = data!!.data
                selectedImagePath = getPath(selectedImageUri)
                Log.d(ContentValues.TAG, "onActivityResult: $selectedImagePath")
                filename = selectedImagePath?.substring(selectedImagePath?.lastIndexOf(
                        "/")!! + 1)
                val view = layoutInflater.inflate(R.layout.layout_pop_up, null, true)
                val imgFileName = view.findViewById<TextView>(R.id.image_file_name)
                imgFileName.text = filename

                if(data != null){
                    imageSource = selectedImageUri.toString()
                    var inputStream = context?.contentResolver?.openInputStream(selectedImageUri!!)
                    Log.d(ContentValues.TAG, "onActivityResult: imagesource $imageSource")
                }
            }

        }


        val view = layoutInflater.inflate(R.layout.layout_pop_up, null, true)
        var result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        Log.i(TAG, "onActivityResult: ${result}")
        if(result != null){
            if(result.contents != null){
                scannedResult = result.contents
                Log.i(TAG, "onActivityResult: ${result.contents}")
            } else {
                Log.i(TAG, "onActivityResult: ${result.contents}")
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
            Log.i(TAG, "onActivityResult: nulll")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.putString("scannedResult", scannedResult)
        super.onSaveInstanceState(outState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        savedInstanceState?.let {
            scannedResult = it.getString("scannedResult").toString()
        }
    }
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//       onRestoreInstanceState(savedInstanceState)
//
//        savedInstanceState?.let {
//            val idbarang = view?.findViewById<TextView>(R.id.barcodeID)
//            scannedResult = it.getString("scannedResult").toString()
//            idbarang?.text = scannedResult
//        }
//    }



    @Throws(IOException::class)
    fun getBytes(inputStream: InputStream): ByteArray? {
        val byteBuffer = ByteArrayOutputStream()
        val bufferSize = 1024
        val buffer = ByteArray(bufferSize)
        var len = 0
        while (inputStream.read(buffer).also { len = it } != -1) {
            byteBuffer.write(buffer, 0, len)
        }
        return byteBuffer.toByteArray()
    }

    fun getPath(uri: Uri?): String? {
        if (uri == null) {
            return null
        }
        var filePath: String? = null
        val fileId: String = DocumentsContract.getDocumentId(uri)
        val id = fileId.split(":".toRegex()).toTypedArray()[1]
        val column = arrayOf(MediaStore.Images.Media.DATA)
        val selector = MediaStore.Images.Media._ID + "=?"
        val cursor = context?.contentResolver?.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                column, selector, arrayOf(id), null)
        if (cursor != null){
            val columnIndex = cursor?.getColumnIndex(column[0])
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex)
            }
            cursor.close()
            return filePath
        }
        return uri.path
    }

    private fun resizeImg(img: ByteArray?): ByteArray? {
        var img = img
        while (img?.size!! > 500000) {
            val bitmap: Bitmap = BitmapFactory.decodeByteArray(img, 0, img!!.size)
            val resized = Bitmap.createScaledBitmap(bitmap,
                    (bitmap.width * 0.8).toInt(), (bitmap.height * 0.8).toInt(), true)
            val stream = ByteArrayOutputStream()
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream)
            img = stream.toByteArray()
        }
        return img
    }

    private fun startMyJob() {
        var serviceComponent = ComponentName(requireActivity(), ItemView::class.java)
        var mJobInfo = JobInfo.Builder(JobSchedulerId, serviceComponent)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
            .setRequiresDeviceIdle(false)
            .setRequiresCharging(false)
            .setMinimumLatency(1)
            .setOverrideDeadline(1)
        //            .setPeriodic(15 * 60 * 1000, 5 * 60 * 1000)

        var JobItem = context?.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        JobItem.schedule(mJobInfo.build())
        Toast.makeText(context, "Job Scheduler Start", Toast.LENGTH_SHORT).show()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as InterfaceFragment
    }

    companion object {
        lateinit var dataPasser: InterfaceFragment
        lateinit var productAdapter: ProductAdapter

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            List().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}