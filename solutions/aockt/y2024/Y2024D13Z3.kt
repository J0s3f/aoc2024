package aockt.y2024

import aockt.y2024.Y2024D13.Machine
import io.github.jadarma.aockt.core.Solution
import io.ksmt.KContext
import io.ksmt.solver.KSolverStatus
import io.ksmt.solver.z3.KZ3Solver
import io.ksmt.utils.getValue
import io.ksmt.utils.toBigInteger
import java.math.BigInteger
import java.util.regex.Pattern
import kotlin.time.Duration.Companion.seconds


object Y2024D13Z3 : Solution {
    override fun partOne(input: String): BigInteger {
        val machines = parseMachines(input)

        val results = machines.map { machine -> solve(machine) }

        return results.filterNotNull().sumOf { it }
    }

    override fun partTwo(input: String): BigInteger {
        val partTwoOffset = 10000000000000.toBigInteger()
        val machines = parseMachines(input).map { machine ->
            Machine(
                machine.aX,
                machine.aY,
                machine.bX,
                machine.bY,
                machine.prizeX + partTwoOffset,
                machine.prizeY + partTwoOffset
            )
        }

        val results = machines.map { machine -> solve(machine, false) }

        return results.filterNotNull().sumOf { it }
    }

    private fun solve(machine: Machine, limit: Boolean = true): BigInteger? {
        val ctx = KContext()
        with(ctx) {
            val a by intSort
            val b by intSort

            val xs = a * machine.aX.expr + b * machine.bX.expr eq machine.prizeX.expr
            val ys = a * machine.aY.expr + b * machine.bY.expr eq machine.prizeY.expr

            val limA = a le 100.expr
            val limB = b le 100.expr


            KZ3Solver(this).use { solver -> // create a Z3 SMT solver instance
                solver.assert(xs)
                solver.assert(ys)
                if (limit) {
                    solver.assert(limA)
                    solver.assert(limB)
                }

                val satisfiability = solver.check(timeout = 1.seconds)
                if (satisfiability == KSolverStatus.UNSAT) return null

                val model = solver.model()



                return model.eval(a).toString().toBigInteger() * 3.toBigInteger() + model.eval(b).toString()
                    .toBigInteger()
            }
        }
    }

    private fun parseMachines(input: String): List<Machine> {
        val machineRegex = Pattern.compile(
            "Button A: X\\+(?<aX>-?\\d+), Y\\+(?<aY>-?\\d+)\\s+" +
                    "Button B: X\\+(?<bX>-?\\d+), Y\\+(?<bY>-?\\d+)\\s+" +
                    "Prize: X=(?<prizeX>-?\\d+), Y=(?<prizeY>-?\\d+)"
        )

        return machineRegex.matcher(input).results().map {
            val aX = it.group("aX").toInt()
            val aY = it.group("aY").toInt()
            val bX = it.group("bX").toInt()
            val bY = it.group("bY").toInt()
            val prizeX = it.group("prizeX").toBigInteger()
            val prizeY = it.group("prizeY").toBigInteger()

            Machine(aX, aY, bX, bY, prizeX, prizeY)
        }.toList()
    }

    data class Machine(
        val aX: Int,
        val aY: Int,
        val bX: Int,
        val bY: Int,
        val prizeX: BigInteger,
        val prizeY: BigInteger
    )
}

