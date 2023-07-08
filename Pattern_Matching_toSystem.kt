import java.util.Scanner

// Solo los Algoritmos de Búsqueda utilizados en la aplicación.

// Fuerza Bruta
// Indica posición (n) de primer caracter de patrón(palabra/oración) a buscar.
fun fbSearch(textoFB: String, patron: String): List<Int> {
    val posiciones = mutableListOf<Int>()
    var count = 0

    val n = textoFB.length
    val m = patron.length

    for (i in 0..n - m) {
        var j = 0
        while (j < m && textoFB[i + j] == patron[j]) {
            j++
        }
        if (j == m) {
            count++
            posiciones.add(i)
        }
    }

    if (count == 0) {
        println("No se encontraron coincidencias.")
    } else {
        println("Se encontraron $count ocurrencias en las siguientes posiciones:")
        for (posicion in posiciones) {
            println(posicion)
        }
    }

    return posiciones
}








// Boyer-Moore
// Función para crear la tabla de saltos de caracteres mal emparejados
private fun createBadCharacterTable(pattern: String): IntArray {
    val table = IntArray(999999) { pattern.length } //Longitud máxima de 999999 caracteres

    for (i in 0 until pattern.length - 1) {
        val c = pattern[i]
        table[c.code] = pattern.length - 1 - i
    }

    return table
}

// Función para crear la tabla de saltos de sufijos
private fun createGoodSuffixTable(pattern: String): IntArray {
    val table = IntArray(pattern.length)
    val suffixes = IntArray(pattern.length)

    // Paso 1: Calcular los sufijos más largos que coinciden con sufijos más cortos
    var lastPrefixIndex = pattern.length
    for (i in pattern.length - 1 downTo 0) {
        if (isPrefix(pattern, i + 1)) {
            lastPrefixIndex = i + 1
        }
        suffixes[i] = lastPrefixIndex
    }

    // Paso 2: Calcular los sufijos coincidentes más largos en el patrón
    for (i in 0 until pattern.length - 1) {
        val suffixLength = getSuffixLength(pattern, i)
        table[suffixLength] = pattern.length - 1 - i + suffixLength
    }

    // Paso 3: Rellenar las posiciones restantes en la tabla con desplazamientos de sufijos
    for (i in 0 until pattern.length - 1) {
        val suffixLength = getSuffixLength(pattern, i)
        if (suffixLength == i + 1) {
            for (j in 0 until pattern.length - 1 - suffixLength + 1) {
                if (table[j] == pattern.length) {
                    table[j] = pattern.length - 1 - suffixLength + 1
                }
            }
        }
    }
    return table
}

// Función auxiliar para verificar si un substring es un prefijo del patrón
private fun isPrefix(pattern: String, p: Int): Boolean {
    for (i in p until pattern.length) {
        if (pattern[i] != pattern[i - p]) {
            return false
        }
    }
    return true
}

// Función auxiliar para obtener la longitud del sufijo coincidente más largo
private fun getSuffixLength(pattern: String, p: Int): Int {
    var length = 0
    var i = p
    var j = pattern.length - 1

    while (i >= 0 && pattern[i] == pattern[j]) {
        length += 1
        i -= 1
        j -= 1
    }

    return length
}

// Función principal para realizar la búsqueda de la cadena objetivo en el patrón utilizando Boyer-Moore
fun boyerMooreSearch(pattern: String, text: String): List<Int> {
    val indices = mutableListOf<Int>()
    val badCharacterTable = createBadCharacterTable(pattern)
    var i = 0

    while (i <= text.length - pattern.length) {
        var j = pattern.length - 1

        while (j >= 0 && pattern[j] == text[i + j]) {
            j -= 1
        }

        if (j < 0) {
            indices.add(i)
            i += pattern.length
        } else {
            val badCharacterShift = badCharacterTable[text[i + j].code]
            val maxSuffixShift = j + 1

            i += maxOf(badCharacterShift, maxSuffixShift)
        }
    }

    return indices
}








// Knuth-Morris-Pratt (KMP)
// Función para construir la tabla de prefijos y sufijos más largos
private fun buildPrefixSuffixTable(pattern: String): IntArray {
    val table = IntArray(pattern.length)
    var length = 0
    var i = 1

    while (i < pattern.length) {
        if (pattern[i] == pattern[length]) {
            length++
            table[i] = length
            i++
        } else {
            if (length != 0) {
                length = table[length - 1]
            } else {
                table[i] = 0
                i++
            }
        }
    }

    return table
}

// Función principal para realizar la búsqueda del patrón en el texto utilizando KMP
fun searchByKMP(text: String, pattern: String): List<Int> {
    val positions = mutableListOf<Int>()
    val prefixSuffixTable = buildPrefixSuffixTable(pattern)
    var i = 0
    var j = 0

    while (i < text.length) {
        if (pattern[j] == text[i]) {
            i++
            j++
        }

        if (j == pattern.length) {
            positions.add(i - j)
            j = prefixSuffixTable[j - 1]
        } else if (i < text.length && pattern[j] != text[i]) {
            if (j != 0) {
                j = prefixSuffixTable[j - 1]
            } else {
                i++
            }
        }
    }

    return positions
}
