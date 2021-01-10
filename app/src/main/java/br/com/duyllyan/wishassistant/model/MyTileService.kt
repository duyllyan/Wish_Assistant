package br.com.duyllyan.wishassistant.model

import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi
import br.com.duyllyan.wishassistant.FloatingViewService

@RequiresApi(Build.VERSION_CODES.N)
class MyTileService: TileService() {

    override fun onClick() {
        super.onClick()
        val wishIntent = Intent(baseContext, FloatingViewService::class.java)
        wishIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        wishIntent.action = "startWishActivity"
        if (isDrawerOverlaysAllowed() && FloatingViewService.isRunning == null) {
            startService(wishIntent)
            collapseNotificationBar()
            FloatingViewService.isRunning = 1
//        } else {
//            startActivityAndCollapse(Intent(baseContext, MainActivity::class.java).apply {
//                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                action = "startMainActivity"
//            })
        }
    }

    private fun isDrawerOverlaysAllowed(): Boolean = Settings.canDrawOverlays(this)

    private fun collapseNotificationBar() {
        val collapse = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        baseContext.sendBroadcast(collapse)
    }
}