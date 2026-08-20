package org.tavioribeiro.griot.core.domain.util

object NaturalOrderComparator : Comparator<String> {

    override fun compare(a: String, b: String): Int {
        var i = 0
        var j = 0
        while (i < a.length && j < b.length) {
            val charA = a[i]
            val charB = b[j]

            if (charA.isDigit() && charB.isDigit()) {
                val endA = scanDigits(a, i)
                val endB = scanDigits(b, j)
                val numericA = a.substring(i, endA).toLong()
                val numericB = b.substring(j, endB).toLong()
                if (numericA != numericB) {
                    return if (numericA < numericB) -1 else 1
                }
                i = endA
                j = endB
            } else {
                val lowerA = charA.lowercaseChar()
                val lowerB = charB.lowercaseChar()
                if (lowerA != lowerB) {
                    return if (lowerA < lowerB) -1 else 1
                }
                if (charA != charB) {
                    return if (charA < charB) -1 else 1
                }
                i++
                j++
            }
        }
        return (a.length - i).compareTo(b.length - j)
    }

    private fun scanDigits(value: String, start: Int): Int {
        var index = start
        while (index < value.length && value[index].isDigit()) {
            index++
        }
        return index
    }
}