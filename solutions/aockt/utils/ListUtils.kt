package aockt.utils

    fun <T> List<T>.pairs(): Sequence<Pair<T, T>> {
        val list = this
        return sequence {
            for (i in 0 until list.size - 1)
                for (j in i + 1 until list.size)
                    yield(list[i] to list[j])
        }
    }
