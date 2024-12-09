package aockt.y2024

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2024, 8, "Resonant Collinearity")
class Y2024D08Test : AdventSpec<Y2024D08>({
    val example = """............
........0...
.....0......
.......0....
....0.......
......A.....
............
............
........A...
.........A..
............
............"""

    partOne {
        example shouldOutput 14
    }
    partTwo {
        example shouldOutput 34
    }


})
