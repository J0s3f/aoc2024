package aockt.y2024

import kotlinx.benchmark.*

@State(Scope.Benchmark)
class Y2024D12Benchmark {
    val input = """
        RRRRIICCFF
        RRRRIICCCF
        VVRRRCCFFF
        VVRCCCJFFF
        VVVVCJJCFE
        VVIVCCJJEE
        VVIIICJJEE
        MIIIIIJJEE
        MIIISIJEEE
        MMMISSJEEE
        """.trimIndent()

    @Benchmark
    fun benchPartOne(): Long {
        return Y2024D12.partOne(input)
    }

    @Benchmark
    fun benchPartTwo(): Long {
        return Y2024D12.partTwo(input)
    }

}
