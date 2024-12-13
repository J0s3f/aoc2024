package aockt.y2024

import io.github.jadarma.aockt.test.AdventDay
import io.github.jadarma.aockt.test.AdventSpec
import io.github.jadarma.aockt.test.ExecMode

@AdventDay(2024, 13, "Claw Contraption")
class Y2024D13Test : AdventSpec<Y2024D13Z3>({
    val ex1 = """
        Button A: X+94, Y+34
        Button B: X+22, Y+67
        Prize: X=8400, Y=5400
        
        Button A: X+26, Y+66
        Button B: X+67, Y+21
        Prize: X=12748, Y=12176
        
        Button A: X+17, Y+86
        Button B: X+84, Y+37
        Prize: X=7870, Y=6450
        
        Button A: X+69, Y+23
        Button B: X+27, Y+71
        Prize: X=18641, Y=10279
        """.trimIndent()
    val ex2 = """
        Button A: X+30, Y+12
        Button B: X+13, Y+47
        Prize: X=3851, Y=4907
        """.trimIndent()

    partOne {
        ex1 shouldOutput 480
        ex2 shouldOutput 0
    }
    partTwo {
        ex1 shouldOutput 875318608908
        ex2 shouldOutput 956937799404
    }


})
