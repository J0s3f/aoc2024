package aockt.y2024

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2024, 21, "Keypad Conundrum")
class Y2024D21Test : AdventSpec<Y2024D21>({

    val ex1 = """
        029A
        980A
        179A
        456A
        379A
        """.trimIndent()

    partOne {
        ex1 shouldOutput 126384
    }
    partTwo  {  }
})