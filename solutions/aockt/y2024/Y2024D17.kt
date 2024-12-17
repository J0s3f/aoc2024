import io.github.jadarma.aockt.core.Solution
import java.lang.StrictMath.toIntExact

object Y2024D17 : Solution {

    override fun partOne(input: String): String {
        val (registers, program) = parseInput(input)
        return runProgram(registers, program)
    }

    override fun partTwo(input: String): Long {
        val (registers, program) = parseInput(input)
        val possible = mutableSetOf<Long>()

        fun recursiveSearch(a: Long, compareIndex: Int) {
            for (n in 0..7) {
                val a2 = (a shl 3) or n.toLong()
                val reg = Registers(a2, registers.b, registers.c)
                val output = runProgram(reg, program)

                val targetOutput = program.takeLast(compareIndex).joinToString(",")
                if (output == targetOutput) {
                    if (output == program.joinToString(",")) {
                        possible.add(a2)
                    } else {
                        recursiveSearch(a2, compareIndex + 1)
                    }
                }
            }
        }

        recursiveSearch(0, 1)
        return possible.minOrNull() ?: 0

    }

    private data class Registers(var a: Long, var b: Long, var c: Long)

    private fun parseInput(input: String): Pair<Registers, List<Int>> {
        val lines = input.lines().map { it.trim() }.filter { it.isNotEmpty() }
        val a = lines.first { it.startsWith("Register A:") }.split(":")[1].trim().toLong()
        val b = lines.first { it.startsWith("Register B:") }.split(":")[1].trim().toLong()
        val c = lines.first { it.startsWith("Register C:") }.split(":")[1].trim().toLong()
        val program = lines.first { it.startsWith("Program:") }.split(":")[1]
            .split(",")
            .map { it.trim().toInt() }
        return Registers(a, b, c) to program
    }

    private fun runProgram(registers: Registers, program: List<Int>): String {
        val output = mutableListOf<Int>()
        var instructionPointer = 0
        val maxSteps = 10_000_000
        var steps = 0

        while (instructionPointer < program.size) {
            if (steps++ > maxSteps) {
                error("Infinite loop at $instructionPointer | Registers: $registers")
            }

            val opcode = program[instructionPointer]
            val operand = if (instructionPointer + 1 < program.size) program[instructionPointer + 1] else 0

            when (opcode) {
                0 -> { // adv
                    val denominator = 1 shl toIntExact(resolveComboOperand(operand, registers))
                    registers.a /= denominator
                }
                1 -> { // bxl
                    registers.b = (toIntExact(registers.b) xor operand).toLong()
                }
                2 -> { // bst
                    registers.b = (resolveComboOperand(operand, registers) % 8).toLong()
                }
                3 -> { // jnz
                    if (registers.a != 0L) {
                        instructionPointer = operand // Jump directly to the operand
                        continue
                    }
                }
                4 -> { // bxc
                    registers.b = registers.b xor registers.c
                }
                5 -> { // out
                    output.add(toIntExact(resolveComboOperand(operand, registers) % 8))
                }
                6 -> { // bdv
                    val denominator = 1 shl toIntExact(resolveComboOperand(operand, registers))
                    registers.b = registers.a / denominator
                }
                7 -> { // cdv
                    val denominator = 1 shl toIntExact(resolveComboOperand(operand, registers))
                    registers.c = registers.a / denominator
                }
                else -> error("Invalid opcode: $opcode at instruction $instructionPointer")
            }

            instructionPointer += 2 // Move to the next instruction
        }

        return output.joinToString(",")
    }

    private fun resolveComboOperand(operand: Int, registers: Registers): Long = when (operand) {
        in 0..3 -> operand.toLong()
        4 -> registers.a
        5 -> registers.b
        6 -> registers.c
        else -> error("Invalid combo operand $operand")
    }
}
