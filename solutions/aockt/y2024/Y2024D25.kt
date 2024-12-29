package aockt.y2024

import aockt.utils.CharGrid
import aockt.utils.cartesianProduct
import io.github.jadarma.aockt.core.Solution

object Y2024D25 : Solution {

    override fun partOne(input: String): Int {
        val (lockHeights, keyHeights) = parseInput(input)

        return lockHeights.cartesianProduct(keyHeights).count { (lock, key) -> checkFit(lock, key) }
    }

    private fun CharGrid.toColumnHeights(): List<Int> {
        val heights = mutableListOf<Int>()
        for (x in 0..this.colindices.last) {
            var height = 0
            for (y in 0..this.rowindices.last) {
                if (this[y, x] == '#') {
                    height++
                }
            }
            heights.add(height - 1)
        }
        return heights
    }

    private fun checkFit(lockHeights: List<Int>, keyHeights: List<Int>): Boolean {
        if (lockHeights.size != keyHeights.size) return false
        return lockHeights.zip(keyHeights).all { (lock, key) -> lock + key <= 5 }
    }

    private fun parseInput(input: String): Pair<Set<List<Int>>, Set<List<Int>>> {
        val inputs = input.replace("\r\n", "\n").split("\n\n")

        val lockHeights = mutableSetOf<List<Int>>()
        val keyHeights = mutableSetOf<List<Int>>()
        for (input in inputs) {
            val grid = CharGrid.fromString(input)
            val isLock = grid[0, 0] == '#'
            if (isLock) {
                lockHeights.add(grid.toColumnHeights())
            } else {
                keyHeights.add(grid.toColumnHeights())
            }
        }
        return Pair(lockHeights, keyHeights)
    }
}
