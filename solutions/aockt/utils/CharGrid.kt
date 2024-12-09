package aockt.utils

class CharGrid(private val data: Array<CharArray>) {
    val rowindices: IntRange
        get() = IntRange(0, data.lastIndex)
    val colindices: IntRange
        get() = IntRange(0, data[0].lastIndex)

    operator fun get(rowindex: Int): CharArray {
        return data[rowindex]
    }

    operator fun get(rowindex: Int, colindex: Int): Char {
        return data[rowindex][colindex]
    }

    operator fun set(rowindex: Int, colindex: Int, value: Char) {
        data[rowindex][colindex] = value
    }

    fun isValidPosition(row: Int, col: Int): Boolean {
        return row in rowindices && col in colindices
    }

    fun isValidPosition(pos: Pair<Int, Int>): Boolean {
        return pos.first in rowindices && pos.second in colindices
    }


    init {
        require(data.isNotEmpty()) { "Grid must have at least one row" }
        require(data.all { it.size == data[0].size }) { "All rows must have the same length" }
    }

}
