package br.com.duyllyan.wishassistant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import br.com.duyllyan.wishassistant.model.WishRepository
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val KEY_INDEX = "index"

class WishFragment : Fragment() {

    private lateinit var currentIndex: TextView
    private lateinit var pityIndex: TextView
    private lateinit var softPityIndex: TextView
    private lateinit var button1: Button
    private lateinit var button10: Button
    private lateinit var buttonGotIt: Button
    private lateinit var menuBottomNavigation: BottomNavigationView
    private lateinit var wishViewModel: WishViewModel

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
        pityIndex = activity!!.findViewById(R.id.pity_index)
        pityIndex.text = wishViewModel.pity.toString()

        buttonGotIt = activity!!.findViewById(R.id.button_got_it)
        buttonGotIt.setOnClickListener {
            wishViewModel.apply {
                resetWishes()
                updateTextView(currentIndex, softPityIndex, pityIndex)
                updatePity()
            }
        }

        menuBottomNavigation = activity!!.findViewById(R.id.menu_bottom_navigation)
        menuBottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.ic_common -> wishViewModel.apply {
                    currentValue = commonWish
                    currentKey = br.com.duyllyan.wishassistant.model.COMMON_INDEX
                    updatePity()
                }
                R.id.ic_weapon -> wishViewModel.apply {
                    currentValue = weaponWish
                    currentKey = br.com.duyllyan.wishassistant.model.WEAPON_INDEX
                    updatePity()
                }
                R.id.ic_character -> wishViewModel.apply {
                    currentValue = characterWish
                    currentKey = br.com.duyllyan.wishassistant.model.CHAR_INDEX
                    updatePity()
                }
            }
            wishViewModel.updateTextView(currentIndex, softPityIndex, pityIndex)
            true
        }

        button1 = activity!!.findViewById(R.id.button_1)
        button1.setOnClickListener {
            wishViewModel.apply {
                addWishes(1, requireContext())
                updatePity()
                updateTextView(currentIndex, softPityIndex, pityIndex)
            }
        }

        button10 = activity!!.findViewById(R.id.button_10)
        button10.setOnClickListener {
            wishViewModel.apply {
                addWishes(10, requireContext())
                updatePity()
                updateTextView(currentIndex, softPityIndex, pityIndex)
            }
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_INDEX, wishViewModel.currentValue)
    }
}