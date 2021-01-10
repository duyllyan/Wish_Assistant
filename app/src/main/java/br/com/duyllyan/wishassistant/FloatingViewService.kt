package br.com.duyllyan.wishassistant

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import br.com.duyllyan.wishassistant.model.WishRepository
import kotlin.math.roundToInt

class FloatingViewService : Service(){

    var LAYOUT_FLAG = 0

    private var currentIndex = 0

    private lateinit var wishViewModel: WishViewModel
    lateinit var floatingView: View
    lateinit var manager: WindowManager
    lateinit var params: WindowManager.LayoutParams
    private lateinit var collapsedLayout: View
    private lateinit var expandedLayout: View
    private lateinit var collapsedMenu: View
    private lateinit var backButton: View
    private lateinit var wishIcon: ImageView
    private lateinit var buttonRight: ImageButton
    private lateinit var button1: Button
    private lateinit var button10: Button
    private lateinit var buttonGotIt: View
    private lateinit var currentWish: TextView
    private lateinit var closeButton: ImageView

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        LAYOUT_FLAG = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }
        val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
        )
        this.params = params
        params.gravity = Gravity.TOP or Gravity.START
        params.x = 0
        params.y = 100

        manager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        floatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null)
        manager.addView(floatingView, params)

        wishViewModel = WishViewModel(WishRepository(baseContext))
        collapsedLayout = floatingView.findViewById(R.id.layoutCollapsed)
        expandedLayout = floatingView.findViewById(R.id.layoutExpanded)
        collapsedMenu = floatingView.findViewById(R.id.collapsedMenu)
        backButton = floatingView.findViewById(R.id.btn_back)
        wishIcon = floatingView.findViewById(R.id.wish_icon)
        buttonRight = floatingView.findViewById(R.id.button_right)
        button1 = floatingView.findViewById(R.id.btn_plus_1)
        button10= floatingView.findViewById(R.id.btn_plus_10)
        buttonGotIt = floatingView.findViewById(R.id.btn_got_it)
        currentWish = floatingView.findViewById(R.id.current_index)
        currentWish.text = wishViewModel.currentValue.toString()
        closeButton = floatingView.findViewById(R.id.btn_close)

        button1.setOnClickListener {
            wishViewModel.apply {
                addWishes(1, baseContext)
                updateTextView()
            }
        }

        button10.setOnClickListener {
            wishViewModel.apply {
                addWishes(10, baseContext)
                updateTextView()
            }
        }

        buttonGotIt.setOnClickListener {
            wishViewModel.apply {
                resetWishes()
                updateTextView()
            }
        }

        buttonRight.setOnClickListener {
            currentIndex = (currentIndex + 1) % 3
            updateWishIcon()
        }

        closeButton.setOnClickListener {
            stopSelf()
        }

        hideExpandedLayout()

        floatingView.findViewById<View>(R.id.relativeLayoutParent)?.setOnTouchListener(object : View.OnTouchListener {

            var initialX: Int? = null
            var initialY: Int? = null
            var initialTouchX: Float? = null
            var initialTouchY: Float? = null

            override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {

                when (motionEvent!!.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params.x
                        initialY = params.y
                        initialTouchX = motionEvent.rawX
                        initialTouchY = motionEvent.rawY
                        return true
                    }
                    MotionEvent.ACTION_UP -> {
                        val xDiff = (motionEvent.rawX - initialTouchX!!)
                        val yDiff = (motionEvent.rawY - initialTouchY!!)
                        if (xDiff < 10 && yDiff < 10) {
                            if (isViewCollapsed()) {
                                collapsedLayout.visibility = View.GONE
                                expandedLayout.visibility = View.VISIBLE
                            }
                        }
                        return true
                    }
                    MotionEvent.ACTION_MOVE -> {
                        params.x = initialX!!.plus((motionEvent.rawX - initialTouchX!!)).roundToInt()
                        params.y = initialY!!.plus((motionEvent.rawY - initialTouchY!!)).roundToInt()
                        manager.updateViewLayout(floatingView, params)
                        return true
                    }
                }
                return false
            }
        })
        return START_NOT_STICKY
    }


    private fun WishViewModel.updateTextView() {
        currentWish.text = currentValue.toString()
    }

    private fun updateWishIcon() {
        when (currentIndex) {
            0 -> {
                wishIcon.setImageResource(R.drawable.ic_common)
                wishViewModel.apply {
                    currentValue = commonWish
                    currentKey = br.com.duyllyan.wishassistant.model.COMMON_INDEX
                    updateTextView()
                }
            }
            1 -> {
                wishIcon.setImageResource(R.drawable.ic_weapon)
                    wishViewModel.apply {
                        currentValue = weaponWish
                        currentKey = br.com.duyllyan.wishassistant.model.WEAPON_INDEX
                        updateTextView()
                    }
            }
            2 -> {
                wishIcon.setImageResource(R.drawable.ic_character)
                    wishViewModel.apply {
                        currentValue = characterWish
                        currentKey = br.com.duyllyan.wishassistant.model.CHAR_INDEX
                        updateTextView()
                    }
            }
        }
    }

    private fun hideExpandedLayout() {
        backButton.setOnClickListener {
            collapsedLayout.visibility = View.VISIBLE
            expandedLayout.visibility = View.GONE
        }
    }

    private fun isViewCollapsed(): Boolean {
        return collapsedLayout.visibility == View.VISIBLE
    }

    override fun onDestroy() {
        super.onDestroy()
        manager.removeView(floatingView)
        isRunning = null
        stopSelf()
    }

    companion object {
        var isRunning: Int? = null
    }
}