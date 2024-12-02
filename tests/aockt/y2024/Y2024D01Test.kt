package aockt.y2024

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec

@AdventDay(2024, 1, "Historian Hysteria")
class Y2024D01Test : AdventSpec<Y2024D01>({
    partOne {
        "3   4\n4   3\n2   5\n1   3\n3   9\n3   3" shouldOutput 11
    }
    partTwo {
        "3   4\n4   3\n2   5\n1   3\n3   9\n3   3" shouldOutput 31
    }
})
