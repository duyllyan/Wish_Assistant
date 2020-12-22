package br.com.duyllyan.wishassistant.model

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.text.Editable
import br.com.duyllyan.wishassistant.WishViewModel
import kotlinx.coroutines.channels.Channel

private const val KEY = "br.com.duyllyan.wishassistant.wishes"
const val WEAPON_INDEX = "weapon_index"
const val COMMON_INDEX = "common_index"
const val CHAR_INDEX = "char_index"

class WishRepository(context: Context) {

    private val appContext = context.applicationContext

    private val sharedPreferences : SharedPreferences?
        get() = appContext?.getSharedPreferences(KEY, Context.MODE_PRIVATE)

    fun setWishes(common: Int, weapon: Int, character: Int) {
        sharedPreferences?.edit()?.apply {
            putInt(COMMON_INDEX, common)
            putInt(WEAPON_INDEX, weapon)
            putInt(CHAR_INDEX, character)
        }?.apply()
    }

    fun getWish(keyIndex: String): Int {
        return sharedPreferences?.getInt(keyIndex, 0) ?: 0
    }
}