package br.com.duyllyan.wishassistant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import br.com.duyllyan.wishassistant.model.WishRepository
import com.google.android.material.bottomnavigation.BottomNavigationMenu
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val KEY_INDEX = "index"

class WishFragment() : Fragment() {

    lateinit var currentIndex: TextView
    lateinit var pityIndex: TextView
    lateinit var softPityIndex: TextView
    lateinit var button1: Button
    lateinit var button10: Button
    lateinit var menuBottomNavigation: BottomNavigationView
    lateinit var wishViewModel: WishViewModel

    companion object {
        fun newInstance() = WishFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.wish_fragment, container, false).apply {
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        wishViewModel = ViewModelProvider(
                this,
                WishViewModel.WishViewModelFactory(WishRepository(requireContext())))
                .get(WishViewModel::class.java)

        val recoveryData = savedInstanceState?.getInt(KEY_INDEX, wishViewModel.currentValue) ?: wishViewModel.currentValue

        currentIndex = activity!!.findViewById(R.id.current_index)
        currentIndex.text = recoveryData.toString()
        softPityIndex = activity!!.findViewById(R.id.soft_pity_index)
        softPityIndex.text = wishViewModel.softPity.toString()
        menuBottomNavigation = activity!!.findViewById(R.id.menu_bottom_navigation)
        menuBottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_common -> wishViewModel.apply {
                    currentValue = commonWish
                    softPity = if (76 - currentValue > 0) {
                        76 - currentValue
                    } else {
                        0
                    }
                    currentKey = br.com.duyllyan.wishassistant.model.COMMON_INDEX
                }
                R.id.ic_weapon -> wishViewModel.apply {
                    currentValue = weaponWish
                    softPity = if (76 - currentValue > 0) {
                        76 - currentValue
                    } else {
                        0
                    }
                    currentKey = br.com.duyllyan.wishassistant.model.WEAPON_INDEX
                }
                R.id.ic_character -> wishViewModel.apply {
                    currentValue = characterWish
                    softPity = if (76 - currentValue > 0) {
                    76 - currentValue
                } else {
                    0
                }
                    currentKey = br.com.duyllyan.wishassistant.model.CHAR_INDEX
                }
            }
            updateTextView()
            true
        }

        button1 = activity!!.findViewById(R.id.button_1)
        button1.setOnClickListener {
            wishViewModel.addWishes(1)
            updateTextView()
        }

        button10 = activity!!.findViewById(R.id.button_10)
        button10.setOnClickListener {
            wishViewModel.addWishes(10)
            updateTextView()
        }

    }

    private fun updateTextView() {
        currentIndex.text = wishViewModel.currentValue.toString()
        softPityIndex.text = wishViewModel.softPity.toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_INDEX, wishViewModel.currentValue)
    }
}