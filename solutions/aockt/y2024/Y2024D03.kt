package aockt.y2024

import io.github.jadarma.aockt.core.Solution

object Y2024D03 : Solution {

    override fun partOne(input: String): Int {

        return "mul\\((?<a>\\d{1,3}),(?<b>\\d{1,3})\\)".toRegex().findAll(input).toList()
            .sumOf { it.groups["a"]!!.value.toInt() * it.groups["b"]!!.value.toInt() }
    }

    override fun partTwo(input: String): Int {
        val operations = "don't\\(\\)|do\\(\\)|mul\\((?<a>\\d{1,3}),(?<b>\\d{1,3})\\)".toRegex().findAll(input)
            .map { Operation.eval(it) }.toMutableList()

        var throwAway = false
        val iterator = operations.iterator()

        while (iterator.hasNext()) {
            val element = iterator.next()
            if (element is DoNot) {
                throwAway = true
                iterator.remove()
            } else if (element is Do) {
                throwAway = false
                iterator.remove()
            } else if (throwAway) {
                iterator.remove()
            }
        }
        return operations.sumOf { if (it is Mul) it.a * it.b else 0 }
    }

    abstract class Operation {

        companion object {
            fun eval(data: MatchResult): Operation {
                if (data.value == "don't()") return DoNot()
                if (data.value == "do()") return Do()
                if (data.value.startsWith("mul")) return Mul(
                    data.groups["a"]!!.value.toInt(),
                    data.groups["b"]!!.value.toInt()
                )
                throw IllegalArgumentException("Invalid operation ${data.value}")
            }
        }
    };
    class Mul(val a: Int, val b: Int) : Operation()
    class DoNot : Operation()
    class Do : Operation()

}



