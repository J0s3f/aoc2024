package aockt.y2024

import aockt.y2024.Y2024D24.Gate.GateType
import io.github.jadarma.aockt.core.Solution

object Y2024D24 : Solution {
    override fun partOne(input: String): Long {
        val parsedInput = parseInput(input)
        val computed = calculateOutput(parsedInput.gates, parsedInput.initialValues)
        return parseOutput(computed)
    }

    override fun partTwo(input: String): String {
        val parsedInput = parseInput(input)

        return findFaultyGates(parsedInput).sorted().joinToString(",")

    }

    private fun findFaultyGates(parsedInput: ParsedInput): List<String> {
        val min = 0
        val max = (parsedInput.initialValues.map { it.key.drop(1).toInt() }.maxOrNull() ?: 0) + 1


        fun Gate.isFaulty(): Boolean = outputIsZ && type != GateType.XOR && outNum!! < max
                || !outputIsZ && !firstIsXY && !secondIsXY && type == GateType.XOR ||
                firstIsXY && firstNum!! > min && secondIsXY && secondNum!! > min &&
                type == GateType.XOR && !parsedInput.gates.any { it.type == GateType.XOR && (it.inputs.contains(output)) } ||
                firstIsXY && firstNum!! > min && secondIsXY && secondNum!! > min &&
                type == GateType.AND && !parsedInput.gates.any { it.type == GateType.OR && (it.inputs.contains(output)) }

        return parsedInput.gates.filter { it.isFaulty() }.map { it.output }
    }

    data class Gate(
        val inputs: List<String>,
        val output: String,
        val type: GateType
    ) {
        enum class GateType {
            AND, OR, XOR
        }

        init {
            require(inputs.size == 2) { "Gate must have 2 inputs" }
        }

        fun calculate(value1: Int, value2: Int): Int {
            return when (type) {
                GateType.AND -> value1 and value2
                GateType.OR -> value1 or value2
                GateType.XOR -> value1 xor value2
            }
        }

        val first
            get() = inputs[0]
        val second
            get() = inputs[1]
        val firstIsXY = this.first.startsWith('x') || this.first.startsWith('y')
        val secondIsXY = this.second.startsWith('x') || this.second.startsWith('y')
        val outputIsZ = this.output.startsWith('z')
        private val inOutVars = setOf<Char>('x', 'y', 'z')
        val outNum = if (inOutVars.contains(output[0])) this.output.drop(1).toInt() else null
        val firstNum = if (inOutVars.contains(first[0])) this.first.drop(1).toInt() else null
        val secondNum = if (inOutVars.contains(second[0])) this.second.drop(1).toInt() else null

    }

    data class ParsedInput(
        val initialValues: Map<String, Int>,
        val gates: List<Gate>
    )

    data class GateOutput(val data: Map<String, Int>) : HashMap<String, Int>(data)

    private fun parseInput(input: String): ParsedInput {
        val (initialStateLines, graphLines) = input.replace("\r\n", "\n").trim().split("\n\n")

        val initialState = initialStateLines.lines().associate { line ->
            val (label, value) = line.split(": ")
            label to value.toInt()
        }

        val gates = graphLines.lines().map { line ->
            val (inputs, output) = line.split(" -> ")
            val (lhs, operation, rhs) = inputs.split(" ")
            val gateType = GateType.valueOf(operation)
            Gate(listOf(lhs, rhs), output, gateType)
        }

        return ParsedInput(initialState, gates)
    }

    private fun parseOutput(wireValues: Map<String, Int>): Long {
        val binaryString = wireValues.filterKeys { it.startsWith("z") }
            .toSortedMap(compareBy { it.drop(1).toInt() })
            .values
            .joinToString("")
        return binaryString.reversed().toLong(2)
    }

    private fun calculateOutput(
        gates: List<Gate>,
        wireValues: Map<String, Int>
    ): GateOutput {
        val outputValues = wireValues.toMutableMap()
        val pendingGates = gates.toMutableList()

        while (pendingGates.isNotEmpty()) {
            pendingGates.iterator().apply {
                forEach { gate ->
                    val values = gate.inputs.mapNotNull { outputValues[it] }
                    if (values.size == gate.inputs.size) {
                        outputValues[gate.output] = gate.calculate(values[0], values[1])
                        remove()
                    }
                }
            }
        }

        return GateOutput(outputValues)
    }
}
