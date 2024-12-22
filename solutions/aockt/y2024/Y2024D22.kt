package aockt.y2024

import io.github.jadarma.aockt.core.Solution

object Y2024D22 : Solution {

    override fun partOne(input: String): Long =
        input.trim().lines().map { it.toLong() }.sumOf { simulateSecretNumber(it) }

    override fun partTwo(input: String): Long = findBestSequence(input.trim().lines().map { it.toLong() })

    private fun simulateSecretNumber(initialSecret: Long, steps: Int = 2000): Long {
        var secret = initialSecret
        repeat(steps) {
            secret = mangleSecret(secret)
        }
        return secret
    }

    @JvmInline
    value class PriceChange(private val change: Int) {
        init {
            require(change in -9..9) { "Price change must be between -9 and 9" }
        }
    }

    data class ChangeSequence(val c1: PriceChange, val c2: PriceChange, val c3: PriceChange, val c4: PriceChange) {
        companion object {
            fun fromList(list: List<Int>): ChangeSequence {
                require(list.size == 4) { "List must have 4 elements" }
                return ChangeSequence(
                    PriceChange(list[0]),
                    PriceChange(list[1]),
                    PriceChange(list[2]),
                    PriceChange(list[3])
                )
            }
        }

    }

    private fun simulateSecretPrices(initialSecret: Long, steps: Int = 2000): Map<ChangeSequence, Int> {
        var secret = initialSecret
        val prices = mutableMapOf<ChangeSequence, Int>()
        var lastPrice = initialSecret.mod(10)
        val changes = mutableListOf<Int>()

        repeat(steps) {
            secret = mangleSecret(secret)
            val price = secret.mod(10)
            val change = price - lastPrice
            changes.add(change)
            if (changes.size == 4) {
                val changeSequence = ChangeSequence.fromList(changes)
                changes.removeFirst()
                prices.computeIfAbsent(changeSequence) { price }
            }
            lastPrice = price
        }
        return prices
    }

    private fun mangleSecret(secret: Long): Long {
        var s = secret
        s = (s * 64).xor(s).mod(16777216L)
        s = (s / 32).xor(s).mod(16777216L)
        s = (s * 2048).xor(s).mod(16777216L)
        return s
    }


    private fun findBestSequence(buyersSecrets: List<Long>): Long {
        val bananasForChange = mutableMapOf<ChangeSequence, Long>()
        buyersSecrets.forEach { secret ->
            simulateSecretPrices(secret).forEach { (k, v) ->
                bananasForChange[k] = bananasForChange.getOrDefault(k, 0L) + v
            }
        }
        return bananasForChange.maxOf { it.value }
    }


}
