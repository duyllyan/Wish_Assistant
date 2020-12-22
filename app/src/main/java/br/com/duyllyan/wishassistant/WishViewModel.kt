package br.com.duyllyan.wishassistant

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.duyllyan.wishassistant.model.WishRepository

private const val KEY = "br.com.duyllyan.wishassistant.wishes"
private const val WEAPON_INDEX = "weapon_index"
private const val COMMON_INDEX = "common_index"
private const val CHAR_INDEX = "char_index"

class WishViewModel(private val repository: WishRepository): ViewModel() {

    var commonWish = repository.getWish(COMMON_INDEX)
    var weaponWish = repository.getWish(WEAPON_INDEX)
    var characterWish = repository.getWish(CHAR_INDEX)

    var currentKey = COMMON_INDEX
    var currentValue = commonWish
    var softPity = if (76 - currentValue > 0) {
        76 - currentValue
    } else {
        0
    }

    fun addWishes(quantity: Int) {
        when (currentKey) {
            COMMON_INDEX -> commonWish += quantity
            WEAPON_INDEX -> weaponWish += quantity
            CHAR_INDEX -> characterWish += quantity
        }
        currentValue += quantity
        softPity = if (76 - currentValue > 0) {
            76 - currentValue
        } else {
            0
        }
        repository.setWishes(commonWish, weaponWish, characterWish)
    }

    class WishViewModelFactory(private val repository: WishRepository)  : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return WishViewModel(repository) as T
        }
    }
}