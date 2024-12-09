package aockt.y2024

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec
import io.github.jadarma.aockt.test.ExecMode

@AdventDay(2024, 9, "Disk Fragmenter")
class Y2024D09Test : AdventSpec<Y2024D09>({
    val example = "2333133121414131402"

    partOne {
        example shouldOutput 1928
    }
    partTwo {
        example shouldOutput 2858
    }

})
