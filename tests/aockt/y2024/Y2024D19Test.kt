package aockt.y2024

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2024, 19, "Linen Layout")
class Y2024D19Test : AdventSpec<Y2024D19>({

    val ex1 = """
        r, wr, b, g, bwu, rb, gb, br
        
        brwrr
        bggr
        gbbr
        rrbgbr
        ubwu
        bwurrg
        brgr
        bbrgwb
        """.trimIndent()

    partOne {
        ex1 shouldOutput 6
    }

    partTwo {
        ex1 shouldOutput 16
    }

})
