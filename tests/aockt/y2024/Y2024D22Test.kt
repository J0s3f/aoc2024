package aockt.y2024

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2024, 22, "Monkey Market")
class Y2024D22Test : AdventSpec<Y2024D22>({

    val ex1 = """
        1
        10
        100
        2024
        """.trimIndent()
    val ex2 = """
        1
        2
        3
        2024
        """.trimIndent()

    partOne {
        ex1 shouldOutput 37327623
    }
    partTwo {
        ex2 shouldOutput 23
    }
})
