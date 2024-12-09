package aockt.y2024

import io.github.jadarma.aockt.core.Solution
import java.util.*

object Y2024D09 : Solution {
    override fun partOne(input: String): Long {
        val (disk, fileCount) = generateDiskLayout(input)

        var freeSpacePos = disk.leftmostFreeSpace()
        var filePos = disk.rightmostFilePos()
        while (freeSpacePos < filePos) {
            disk.moveBlock(filePos, freeSpacePos)
            freeSpacePos = disk.leftmostFreeSpace()
            filePos = disk.rightmostFilePos()
        }
        return disk.checksum()
    }

    override fun partTwo(input: String): Long {
        val (disk, fileCount) = generateDiskLayout(input)
        var fileToMove = fileCount

        while (fileToMove >= 0) {
            val filePos = disk.rightmostFileWithId(fileToMove)
            val freeSpacePos = disk.leftmostFreeSpace(filePos.second)
            if (freeSpacePos.isPresent && freeSpacePos.get() < filePos.first) {
                disk.moveBlock(filePos.first, freeSpacePos.get(), filePos.second)
            }
            fileToMove--
        }
        return disk.checksum()
    }

    private fun generateDiskLayout(input: String): Pair<Array<Block>, Int> {
        val output = mutableListOf<Block>()
        var fileId = 0
        var isFile = true
        input.trim().toCharArray().map { it.digitToInt() }.forEach {
            if (isFile) {
                val file = Block.File(fileId++)
                for (i in 0 until it) {
                    output.add(file)
                }
            } else {
                for (i in 0 until it) {
                    output.add(Block.FreeSpace)
                }
            }
            isFile = !isFile
        }
        return Pair(output.toTypedArray(), fileId - 1)
    }

    private fun Array<Block>.moveBlock(from: Int, to: Int) {
        require(this[to] == Block.FreeSpace) { "Cannot move block to a non-empty space" }
        this[to] = this[from]
        this[from] = Block.FreeSpace
    }

    private fun Array<Block>.moveBlock(from: Int, to: Int, size: Int) {
        for (pos in to until to + size) {
            require(this[pos] == Block.FreeSpace) { "Cannot move block to a non-empty space" }
        }
        for (pos in 0 until size) {
            this[to + pos] = this[from + pos]
            this[from + pos] = Block.FreeSpace
        }
    }

    private fun Array<Block>.rightmostFileWithId(id: Int): Pair<Int, Int> {
        val lastIdx = this.indices.reversed().first {
            val block = this[it]
            block != Block.FreeSpace && (block as Block.File).id == id
        }
        var firstIdx = lastIdx - 1
        while (firstIdx > 0 && this[firstIdx] is Block.File && (this[firstIdx] as Block.File).id == id) {
            firstIdx--
        }
        return Pair(firstIdx + 1, lastIdx - firstIdx)
    }

    private fun Array<Block>.rightmostFilePos() =
        this.indices.reversed().first { this[it] != Block.FreeSpace }


    private fun Array<Block>.leftmostFreeSpace() =
        this.indices.first { this[it] == Block.FreeSpace }

    private fun Array<Block>.leftmostFreeSpace(size: Int): Optional<Int> {
        try {
            return Optional.of(this.indices.first {
                for (pos in it until it + size) {
                    if (pos >= this.size || this[pos] != Block.FreeSpace) {
                        return@first false
                    }
                }
                true
            }
            )
        } catch (e: NoSuchElementException) {
            return Optional.empty()
        }
    }

    private fun Array<Block>.checksum() =
        this.indices.sumOf {
            if (this[it] == Block.FreeSpace) {
                0L
            } else {
                (this[it] as Block.File).id.toLong() * it.toLong()
            }
        }

    sealed class Block {
        data class File(val id: Int) : Block()
        data object FreeSpace : Block()

    }
}
