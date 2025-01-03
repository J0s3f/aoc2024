package aockt.y2024

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2024, 12, "Garden Groups")
class Y2024D12Test : AdventSpec<Y2024D12>({
    val ex1 = """
        AAAA
        BBCD
        BBCC
        EEEC
        """.trimIndent()
    val ex2 = """
        OOOOO
        OXOXO
        OOOOO
        OXOXO
        OOOOO
        """.trimIndent()
    val ex3 = """
        RRRRIICCFF
        RRRRIICCCF
        VVRRRCCFFF
        VVRCCCJFFF
        VVVVCJJCFE
        VVIVCCJJEE
        VVIIICJJEE
        MIIIIIJJEE
        MIIISIJEEE
        MMMISSJEEE
        """.trimIndent()

    partOne {
        ex1 shouldOutput 140
        ex2 shouldOutput 772
        ex3 shouldOutput 1930
    }
    partTwo {
        ex1 shouldOutput 80
        ex2 shouldOutput 436
        ex3 shouldOutput 1206
        """
            EEEEE
            EXXXX
            EEEEE
            EXXXX
            EEEEE
            """.trimIndent() shouldOutput 236
        """
            AAAAAA
            AAABBA
            AAABBA
            ABBAAA
            ABBAAA
            AAAAAA
            """.trimIndent() shouldOutput 368
    }


})
