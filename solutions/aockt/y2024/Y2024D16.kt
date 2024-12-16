package aockt.y2024

import aockt.utils.CharGrid
import aockt.utils.CharGrid.Companion.toGrid
import aockt.utils.Direction
import aockt.utils.Point
import io.github.jadarma.aockt.core.Solution
import java.util.*

object Y2024D16 : Solution {

    override fun partOne(input: String): Int = input.toGrid().findPath()

    override fun partTwo(input: String): Int = input.toGrid().findPath(returnTilesVisited = true)

    private fun CharGrid.findPath(returnTilesVisited: Boolean = false): Int {
        val start = findChar('S') ?: error("Start point not found")
        val end = findChar('E') ?: error("End point not found")

        data class State(
            val position: Point, val direction: Direction, val score: Int, val path: Set<Point>
        )

        val visited = mutableMapOf<Pair<Point, Direction>, Int>()
        val queue = PriorityQueue(compareBy<State> { it.score })
        queue.add(State(start, Direction.RIGHT, 0, if (returnTilesVisited) setOf(start) else emptySet()))

        var bestScore = Int.MAX_VALUE
        val bestPathTiles = mutableSetOf<Point>()

        while (queue.isNotEmpty()) {
            val (pos, dir, score, path) = queue.poll()

            when {
                pos == end -> {
                    if (score <= bestScore) {
                        if (score < bestScore && returnTilesVisited) {
                            bestPathTiles.clear()
                        }
                        bestScore = score
                        if (returnTilesVisited) {
                            bestPathTiles.addAll(path)
                        }
                    }
                    if (!returnTilesVisited) break else continue
                }

                score > bestScore -> continue

                visited[Pair(pos, dir)]?.let { it < score } == true -> continue

                else -> {
                    visited[Pair(pos, dir)] = score
                    queue.addAll(
                        listOf(
                            State(pos, dir.rotateRight(), score + 1000, path),
                            State(pos, dir.rotateLeft(), score + 1000, path)
                        )
                    )

                    val forward = pos + dir
                    if (isValidPosition(forward) && this[forward] != '#') {
                        queue.add(
                            State(
                                position = forward,
                                direction = dir,
                                score = score + 1,
                                path = if (returnTilesVisited) path + forward else emptySet()
                            )
                        )
                    }
                }
            }
        }

        return if (!returnTilesVisited) bestScore else bestPathTiles.size
    }
}
