package aockt.y2024

import io.github.jadarma.aockt.core.Solution

object Y2024D05 : Solution {
    override fun partOne(input: String): Int {
        val (rules, updates) = parseInput(input)

        val validUpdates = updates.filter { update ->
            isValidUpdate(update, rules)
        }

        return validUpdates.sumOf { update ->
            val middleIndex = update.size / 2
            update[middleIndex]
        }
    }

    override fun partTwo(input: String): Int {
        val (rules, updates) = parseInput(input)

        val invalidUpdates = updates.filter { update ->
            !isValidUpdate(update, rules)
        }

        val fixedUpdates = invalidUpdates.map { fixUpdate(it, rules) }

        return fixedUpdates.sumOf { update ->
            val middleIndex = update.size / 2
            update[middleIndex]
        }
    }

    private fun parseInput(input: String): Pair<List<Pair<Int, Int>>, List<List<Int>>> {
        val parts = input.trim().split("\n\n")
        val rules = parts[0].lines().map {
            val (before, after) = it.trim().split("|").map { page -> page.trim().toInt() }
            before to after
        }
        val updates = parts[1].lines().map { it.trim().split(",").map { page -> page.trim().toInt() } }
        return rules to updates
    }

    private fun isValidUpdate(update: List<Int>, rules: List<Pair<Int, Int>>): Boolean {
        var rulesLeft = rules.toMutableList()
        val cannotHave = mutableSetOf<Int>()
        for (page in update) {
            if (cannotHave.contains(page)) return false
            rulesLeft = rulesLeft.filter { (before, after) ->
                if (after == page) {
                    cannotHave.add(before)
                    false
                } else true
            }.toMutableList()
        }

        return true
    }

    private fun fixUpdate(update: List<Int>, rules: List<Pair<Int, Int>>): List<Int> {
        var fixed = update.toMutableList()
        val rulesLeft = rules.toMutableList()
        fixed = reorderPages(fixed, rulesLeft)
        return fixed
    }

    private fun reorderPages(
        fixed: MutableList<Int>,
        rulesLeft: MutableList<Pair<Int, Int>>
    ): MutableList<Int> {
        for ((index, page) in fixed.withIndex()) {
            var rule = rulesLeft.find { it.second == page }
            while (rule != null) {
                rulesLeft.remove(rule)
                val pos = fixed.indexOf(rule.first)
                if (pos > index) {
                    fixed.removeAt(pos)
                    fixed.add(index, rule.first)
                    return reorderPages(fixed, rulesLeft)
                }
                rule = rulesLeft.find { it.second == page }
            }
        }
        return fixed
    }


}
