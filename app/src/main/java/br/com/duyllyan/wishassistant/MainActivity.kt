package br.com.duyllyan.wishassistant

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment

private const val KEY = "index"

class MainActivity : AppCompatActivity() {

    private var dataFragment = WishFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
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
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        supportFragmentManager.putFragment(outState, KEY, dataFragment)
    }
}