package br.com.tairoroberto.starwars.principal

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.RelativeLayout
import android.widget.Toast
import br.com.tairoroberto.starwars.R
import br.com.tairoroberto.starwars.base.isConnected
import br.com.tairoroberto.starwars.model.Person
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import java.util.*


class HomeActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private val REQUEST_PERMISSIONS = 2
    private val RESULT_QR_OK = 1
    private val MY_PERMISSIONS_REQUEST = 1
    private val permissions = ArrayList<String>()
    private var presenter: IPrincipalPresenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        presenter = PrincipalPresenterImpl(this)

        fab.setOnClickListener {
            if (mayRequestSdCardPermissions()) {
                initCamera()
            }
        }

        activity_principal_lv_person.adapter = presenter?.loadPersonAdapter()
        activity_principal_lv_person.onItemClickListener = this
    }


    override fun onResume() {
        super.onResume()
        if (isConnected()) {
            fab.visibility = FloatingActionButton.VISIBLE
        } else {
            fab.visibility = FloatingActionButton.GONE
        }

        activity_principal_lv_person.adapter = presenter?.loadPersonAdapter()

        val relList = this.findViewById<View>(R.id.activity_principal_rl_list) as RelativeLayout
        val relNodata = this.findViewById<View>(R.id.activity_principal_rl_no_data) as RelativeLayout

        if (presenter?.sizeAdapterPersons() as Int > 0) {
            relList.visibility = RelativeLayout.VISIBLE
            relNodata.visibility = RelativeLayout.GONE
        } else {
            relList.visibility = RelativeLayout.GONE
            relNodata.visibility = RelativeLayout.VISIBLE
        }

    }

    override fun onItemClick(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
        val person = adapterView.getItemAtPosition(i) as Person
        presenter?.sendDetailActivity(person)
        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_slide_in_bottom, android.support.v7.appcompat.R.anim.abc_slide_out_top)

    }

    fun showToastMsg(msg: String) {
        val snackbar = Snackbar.make(this.findViewById(R.id.activity_rl_root), msg, Snackbar.LENGTH_LONG)
        snackbar.show()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                presenter?.processCaptureQRCode(result.contents)
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

        if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }

        try {
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                builder.setTitle(getString(R.string.atention))
                builder.setMessage(getString(R.string.rational_permission))
                builder.setNegativeButton(getString(R.string.not), { dialog, _ ->
                    dialog.dismiss()
                })
                builder.setPositiveButton(getString(R.string.yes), { dialog, _ ->
                    dialog.dismiss()
                    mayRequestSdCardPermissions()
                })
                builder.create()
                builder.show()
            } else {

                val info = packageManager.getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
                if (info.requestedPermissions != null) {

                    info.requestedPermissions.filterTo(permissions) { ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED && !ActivityCompat.shouldShowRequestPermissionRationale(this, it) }

                    ActivityCompat.requestPermissions(this, permissions.toTypedArray(), REQUEST_PERMISSIONS)
                }
            }


        } catch (e: Exception) {
            Log.e(HomeActivity::class.java.name, e.message)
        }

        return false
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
                                            grantResults: IntArray) {
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initCamera()
            }
        }
    }

    fun initCamera() {
        val integrator = IntentIntegrator(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES)
        integrator.setPrompt("Scan STAR WARS barcode")
        integrator.setCameraId(0)  // Use a specific camera of the device
        integrator.setBeepEnabled(false)
        integrator.setBarcodeImageEnabled(true)
        integrator.initiateScan()
    }

    fun getContext(): Context {
        return  this
    }
}
