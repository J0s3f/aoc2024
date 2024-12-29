package aockt.utils

    fun <T> List<T>.pairs(): Sequence<Pair<T, T>> {
        val list = this
        return sequence {
            for (i in 0 until list.size - 1)
                for (j in i + 1 until list.size)
                    yield(list[i] to list[j])
        }
    }
fun <T, S> Collection<T>.cartesianProduct(other: Iterable<S>): List<Pair<T, S>> {
    return cartesianProduct(other) { first, second -> first to second }
}

fun <T, S, V> Collection<T>.cartesianProduct(other: Iterable<S>, transformer: (first: T, second: S) -> V): List<V> {
    return this.flatMap { first -> other.map { second -> transformer.invoke(first, second) } }
}
