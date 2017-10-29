package br.com.tairoroberto.starwars

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {

    private val REQUEST_SDCARD_PERMISSIONS = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            if (mayRequestSdCardPermissions()){
                initCamera()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun mayRequestSdCardPermissions(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true
        }

        if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.atention))
            builder.setMessage(getString(R.string.rational_permission))
            builder.setNegativeButton(getString(R.string.not), { dialog, _ ->
                dialog.dismiss()
            })
            builder.setPositiveButton(getString(R.string.yes), { dialog, _ ->
                dialog.dismiss()
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA), REQUEST_SDCARD_PERMISSIONS)
            })
            builder.create()
            builder.show()
        } else {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA), REQUEST_SDCARD_PERMISSIONS)
        }
        return false
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_SDCARD_PERMISSIONS) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initCamera()
            }
        }
    }

    fun initCamera() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
        integrator.setPrompt("Scan a barcode")
        integrator.setCameraId(0)  // Use a specific camera of the device
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(true)
        integrator.initiateScan()
    }
}
