package br.com.tairoroberto.starwars.principal

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import br.com.tairoroberto.starwars.R
import br.com.tairoroberto.starwars.base.isConnected
import br.com.tairoroberto.starwars.model.Person
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.content_home.*
import java.util.*
import kotlin.collections.ArrayList


class HomeActivity : AppCompatActivity(), HomeContract.View, OnClick {

    private val REQUEST_PERMISSIONS = 2
    private val permissions = ArrayList<String>()
    private var presenter: HomeContract.Preseter? = null
    private val listPerson: ArrayList<Person> = ArrayList()

    var adapter: HomePersonAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)

        presenter = HomePresenter()
        presenter?.attachView(this)

        fab.setOnClickListener {
            if (mayRequestSdCardPermissions()) {
                initCamera()
            }
        }

        adapter = HomePersonAdapter(this, listPerson, this)

        val layoutManageer = LinearLayoutManager(this)
        layoutManageer.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManageer
        recyclerView.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
    }

    override fun onItemClick(person: Person) {
        presenter?.sendDetailActivity(person)
        overridePendingTransition(android.support.v7.appcompat.R.anim.abc_slide_in_bottom, android.support.v7.appcompat.R.anim.abc_slide_out_top)
    }

    override fun onResume() {
        super.onResume()
        if (isConnected()) {
            fab.visibility = VISIBLE
        } else {
            fab.visibility = GONE
        }

        presenter?.loadPerson()

        if (listPerson.size > 0) {
            content_list.visibility = VISIBLE
            content_no_data.visibility = GONE
        } else {
            content_list.visibility = GONE
            content_no_data.visibility = VISIBLE
        }
    }

    override fun updateAdapter(listPerson: ArrayList<Person>) {
        this.listPerson.clear()
        this.listPerson.addAll(listPerson)
        adapter?.update(this.listPerson)
    }

    override fun showToastMsg(msg: String?) {
        val snackbar = Snackbar.make(this.findViewById(R.id.activity_rl_root), msg as String, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                //Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
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

    override fun getContext(): Context {
        return this
    }

    override fun getActivity(): Activity {
        return this
    }
}
