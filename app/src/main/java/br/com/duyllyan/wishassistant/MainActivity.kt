package br.com.duyllyan.wishassistant

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.service.quicksettings.TileService
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import br.com.duyllyan.wishassistant.model.MyTileService
import com.google.android.material.floatingactionbutton.FloatingActionButton

private const val KEY = "index"

class MainActivity : AppCompatActivity() {

    companion object {
        private const val DRAW_OVERLAYS_PERMISSION_REQUEST_CODE = 1
    }

    private var dataFragment = WishFragment.newInstance()
    private lateinit var fab : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        stopService(Intent(this, FloatingViewService::class.java))
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        if (savedInstanceState != null) {
            dataFragment = supportFragmentManager.getFragment(savedInstanceState, KEY) as WishFragment
        }
        fragmentTransaction.apply {
            replace(R.id.fragment_container, dataFragment)
            commit()
        }
        fab = findViewById(R.id.fab)
        fab.setOnClickListener {
            if (FloatingViewService.isRunning == null) {
                finish()
//                stopService(Intent(this, FloatingViewService::class.java))
                startFloatingWidget()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        supportFragmentManager.putFragment(outState, KEY, dataFragment)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DRAW_OVERLAYS_PERMISSION_REQUEST_CODE && isDrawOverlaysAllowed()) {
            Toast.makeText(this, "Granted permission", Toast.LENGTH_SHORT).show()
            startFloatingWidget()
        }
    }

    private fun isDrawOverlaysAllowed(): Boolean = Settings.canDrawOverlays(this)

    private fun startFloatingWidget() {
        if (isDrawOverlaysAllowed()) {
            startService(Intent(this@MainActivity, FloatingViewService::class.java))
            FloatingViewService.isRunning = 1
            return
        }
        requestForDrawingAppsPermission()
    }

    private fun requestForDrawingAppsPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
        startActivityForResult(intent, DRAW_OVERLAYS_PERMISSION_REQUEST_CODE)
    }
}