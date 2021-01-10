package br.com.duyllyan.wishassistant

import android.content.Context
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.duyllyan.wishassistant.model.WishRepository

private const val WEAPON_INDEX = "weapon_index"
private const val COMMON_INDEX = "common_index"
private const val CHAR_INDEX = "char_index"
private const val softPityDifference = 14

class WishViewModel(private val repository: WishRepository): ViewModel() {

    var commonWish = repository.getWish(COMMON_INDEX)
    var weaponWish = repository.getWish(WEAPON_INDEX)
    var characterWish = repository.getWish(CHAR_INDEX)

    var currentKey = COMMON_INDEX
    var currentValue = commonWish
    var softPity = getSoftPity()
    var pity = getPity()

    fun resetWishes() {
        when (currentKey) {
            COMMON_INDEX -> commonWish = 0
            WEAPON_INDEX -> weaponWish = 0
            CHAR_INDEX -> characterWish = 0
        }
        currentValue = 0
        softPity = getSoftPity()
        pity = getPity()
        repository.setWishes(commonWish, weaponWish, characterWish)

    }

    fun addWishes(quantity: Int, context: Context) {
        when (currentKey) {
            COMMON_INDEX -> commonWish += verifyPity(quantity, 90)
            WEAPON_INDEX -> weaponWish += verifyPity(quantity, 80)
            CHAR_INDEX -> characterWish += verifyPity(quantity, 90)
        }
/*        if (pity == 0) {
            Toast.makeText(context, "Teste", Toast.LENGTH_SHORT).show()
        }*/
        repository.setWishes(commonWish, weaponWish, characterWish)
    }

    private fun verifyPity(quantity: Int, pityValue: Int) = if (currentValue + quantity >= pityValue) {
        currentValue += quantity - pityValue
        softPity = getPityValue(pityValue)
        pity = getPityValue(pityValue)
        quantity - pityValue
    } else {
        currentValue += quantity
        softPity = getPityValue(pityValue)
        pity = getPityValue(pityValue)
        quantity
    }

    fun updateTextView(currentWish: TextView, currentSoftPity: TextView, currentPity: TextView) {
        currentWish.text = currentValue.toString()
        currentSoftPity.text = softPity.toString()
        currentPity.text = pity.toString()
    }

    private fun getPityValue(pityValue : Int) : Int {
        return if (pityValue - currentValue > 0) {
            pityValue - currentValue
        } else {
            0
        }
    }

    fun updatePity() {
        pity = getPityValue(verifyPity())
        softPity = getPityValue(verifySoftPity())
    }

    @JvmName("getSoftPity1")
    fun getSoftPity() : Int {
        return getPityValue(verifySoftPity())
    }

    private fun verifySoftPity() = verifyPity() - softPityDifference

    @JvmName("getPity1")
    fun getPity() : Int {
        return getPityValue(verifyPity())
    }

    private fun verifyPity() = if (currentKey == WEAPON_INDEX) {
        80
    } else {
        90
    }

    class WishViewModelFactory(private val repository: WishRepository)  : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return WishViewModel(repository) as T
        }
    }
}