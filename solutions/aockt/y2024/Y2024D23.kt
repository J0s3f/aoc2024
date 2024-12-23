package aockt.y2024

import io.github.jadarma.aockt.core.Solution

object Y2024D23 : Solution {

    override fun partOne(input: String): Int {
        val connections = input.trim().lines()
        val networks = connectComputers(connections)
        val triangles = findTriangles(networks)
        return triangles.count { it.any { it.startsWith("t") } }
    }

    override fun partTwo(input: String): String {
        val connections = input.trim().lines()
        val networks = connectComputers(connections)
        val allConnected = findLargestNetwork(networks)
        return allConnected.sorted().joinToString(",")
    }

    private fun findLargestNetwork(networks: Map<String, Set<String>>): Set<String> {
        val graph = networks.mapValues { it.value.toMutableSet() }
        var maxClique = emptySet<String>()

        fun bronKerbosch(r: Set<String>, pin: Set<String>, xin: Set<String>) {
            var p = pin.toMutableSet()
            var x = xin.toMutableSet()
            if (p.isEmpty() && x.isEmpty()) {
                if (r.size > maxClique.size) maxClique = r
                return
            }

            val pivot = (p + x).maxByOrNull { graph[it]?.size ?: 0 } ?: return
            for (v in p - graph[pivot]!!) {
                bronKerbosch(
                    r + v,
                    p.intersect(graph[v] ?: emptySet()),
                    x.intersect(graph[v] ?: emptySet())
                )
                p -= v
                x += v
            }
        }

        bronKerbosch(emptySet(), graph.keys, emptySet())
        return maxClique
    }

    private fun connectComputers(connections: List<String>): Map<String, Set<String>> =
        connections
            .map { it.split("-") }
            .flatMap { listOf(it[0] to it[1], it[1] to it[0]) }
            .groupBy({ it.first }, { it.second })
            .mapValues { it.value.toSet() }

    private fun findTriangles(networks: Map<String, Set<String>>): Set<Set<String>> =
        networks.flatMap { (a, neighbors) ->
            neighbors.flatMap { b ->
                neighbors.filter { it != b && networks[b]?.contains(it) == true }
                    .map { setOf(a, b, it) }
            }
        }.toSet()
}
