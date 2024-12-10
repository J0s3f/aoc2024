package aockt.y2024

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2024, 10, "Hoof It")
class Y2024D10Test : AdventSpec<Y2024D10>({
    val example = "89010123\n" +
            "78121874\n" +
            "87430965\n" +
            "96549874\n" +
            "45678903\n" +
            "32019012\n" +
            "01329801\n" +
            "10456732"

    partOne {
        example shouldOutput 36
    }
    partTwo {
        example shouldOutput 81
    }


})
