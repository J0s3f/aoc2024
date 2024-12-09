package aockt.y2024

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

private const val EXAMPLE = """....#.....
.........#
..........
..#.......
.......#..
..........
.#..^.....
........#.
#.........
......#..."""

@AdventDay(2024, 6, "Guard Gallivant")
class Y2024D06Test : AdventSpec<Y2024D06>({
    partOne {
        EXAMPLE shouldOutput 41
    }
    partTwo {
        EXAMPLE shouldOutput 6
    }


})
