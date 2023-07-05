import java.util.Scanner
import java.io.File
import kotlin.system.measureTimeMillis

data class ResultadoBusqueda(val texto: String, val busqueda: String, val duracion: Long, val apariciones: Int)

fun main(){
    //Ingreso de usuarios mediante diccionario con datos almacenados.
    val scanner = Scanner(System.`in`)
    val history = mutableListOf<ResultadoBusqueda>()

    val users = mapOf(
        "20194079" to "fAlejandra",
        "20170766" to "jMaria",
        "20162536" to "sDaniella",
        "20202308" to "zRobert",
        "20202333" to "zFernando"
    )

    println("Ingrese su usuario y contraseña para acceder al sistema.")
    println("")
    print("Usuario: ")
    val username = scanner.nextLine()
    print("Contraseña: ")
    val password = scanner.nextLine()

    if(users.containsKey(username) && users[username] == password){
        println(" ")
        println("Inicio de sesión exitoso. ¡Bienvenid@, $username!")
        println(" ")

        var text = ""
        //Construcción de Menú de inicio de sistema una vez logeado
        //var textoCargado = false // Variable para verificar si se ha cargado un texto
        
        var currentPosition: Int = -1
        val searchHistory = mutableListOf<Int>()

        while (true) {
            println("-------- MENU --------")
            println("1. Registrar un texto")
            println("2. Buscar palabra / oración en un texto")
            println("3. Ver historial de búsquedas")
            println("4. Salir")
            println("----------------------")
            print("Ingrese el número de la opción deseada: ")
            val option = scanner.nextInt()

            when(option){
                1 -> {
                    scanner.nextLine()
                    println(" ")
                    print("Ingrese la ruta del archivo de texto a cargar: ")
                    val filePath = scanner.nextLine()
                    text = loadTextFromFile(filePath)
                    if (text.isNotEmpty()) {
                        println("Texto cargado exitosamente.")
                        //textoCargado = true // Actualizar la variable a true cuando se carga un texto
                    } else {
                        println("Error con archivo de texto.")
                    }
                }
                2 -> {
                    if (text.isEmpty()) {
                        println("Aún no hay textos registrados.")
                    } else {
                        println(" ")
                        println("Seleccione un tipo de búsqueda...")
                        println("1. Búsqueda por Fuerza Bruta")
                        println("2. Búsqueda Boyer-Moore")
                        println("3. Búsqueda KMP")
                        print("Ingrese el número de la opción deseada: ")
                        val searchOption = scanner.nextInt()

                        scanner.nextLine()
                        print("Ingrese la palabra / oración a buscar: ")
                        val busqueda = scanner.nextLine()

                        val duracion: Long
                        var posiciones: List<Int> = emptyList()

                        when (searchOption) {
                            1 -> {
                                measureTimeMillis {
                                    val resultados = fbSearch(text, busqueda)
                                    duracion = measureTimeMillis {
                                        if (resultados.isNotEmpty()) {
                                            println("Se encontraron coincidencias en las siguientes posiciones:")
                                            resultados.forEach { posicion ->
                                                println(posicion)
                                            }
                                            searchHistory.clear()
                                            searchHistory.addAll(resultados)
                                            currentPosition = 0
                                        } else {
                                            println("No se encontraron coincidencias.")
                                        }
                                    }
                                    val resultado = ResultadoBusqueda(text, busqueda, duracion, resultados.size)
                                    history.add(resultado)
                                }.let {
                                    posiciones = fbSearch(text, busqueda).toList()
                                }
                            }
                            2 -> {
                                measureTimeMillis {
                                    val resultados = boyerMooreSearch(busqueda, text)
                                    duracion = measureTimeMillis {
                                        if (resultados.isNotEmpty()) {
                                            println("Se encontraron coincidencias en las siguientes posiciones:")
                                            resultados.forEach { posicion ->
                                                println(posicion)
                                            }
                                            searchHistory.clear()
                                            searchHistory.addAll(resultados)
                                            currentPosition = 0
                                        } else {
                                            println("No se encontraron coincidencias.")
                                        }
                                    }
                                    val resultado = ResultadoBusqueda(text, busqueda, duracion, resultados.size)
                                    history.add(resultado)
                                }.let {
                                    posiciones = boyerMooreSearch(busqueda, text).toList()
                                }
                            }
                            3 -> {
                                measureTimeMillis {
                                    val resultados = searchByKMP(text, busqueda)
                                    duracion = measureTimeMillis {
                                        if (resultados.isNotEmpty()) {
                                            println("Se encontraron coincidencias en las siguientes posiciones:")
                                            resultados.forEach { posicion ->
                                                println(posicion)
                                            }
                                            searchHistory.clear()
                                            searchHistory.addAll(resultados)
                                            currentPosition = 0
                                        } else {
                                            println("No se encontraron coincidencias.")
                                        }
                                    }
                                    val resultado = ResultadoBusqueda(text, busqueda, duracion, resultados.size)
                                    history.add(resultado)
                                }.let {
                                    posiciones = searchByKMP(text, busqueda).toList()
                                }
                            }
                            else -> {
                                println("Opción inválida. No se realizará ninguna acción adicional.")
                            }
                        }
                        if (posiciones.isNotEmpty()) {
                            while (true) {
                                println()
                                println("Seleccione una opción:")
                                println("1. Mostrar cantidad de apariciones")
                                println("2. Ver apariciones")
                                println("3. Ir a la ocurrencia siguiente")
                                println("4. Ir a la ocurrencia anterior")
                                println("5. Cancelar la operación")
                                print("Ingrese el número de la opción deseada: ")
                                val searchingOption = scanner.nextInt()
                                scanner.nextLine()

                                when (searchingOption) {
                                    1 -> {
                                        println("La palabra/oración '$busqueda' aparece ${posiciones.size} veces en el texto.")
                                    }
                                    2 -> {
                                        if (posiciones.isNotEmpty()) {
                                            println("Apariciones de la palabra/oración '$busqueda':")
                                            posiciones.forEach { posicion ->
                                                println(text.substring(posicion, posicion + busqueda.length))
                                            }
                                        } else {
                                            println("No se encontraron apariciones de la palabra/oración '$busqueda'.")
                                        }
                                    }
                                    3 -> {
                                        if (currentPosition < posiciones.size - 1) {
                                            currentPosition++
                                            val ocurrencia = posiciones[currentPosition]
                                            println("Ocurrencia siguiente encontrada en la posición: $ocurrencia")
                                        } else {
                                            println("No hay más ocurrencias siguientes.")
                                        }
                                    }
                                    4 -> {
                                        if (currentPosition > 0) {
                                            currentPosition--
                                            val ocurrencia = posiciones[currentPosition]
                                            println("Ocurrencia anterior encontrada en la posición: $ocurrencia")
                                        } else {
                                            println("No hay más ocurrencias anteriores.")
                                        }
                                    }
                                    5 -> {
                                        println("Operación cancelada.")
                                        break
                                    }
                                    else -> {
                                        println("Opción inválida. Por favor, ingrese un número válido.")
                                    }
                                }
                            }
                        }
                    }
                }
                3 -> {
                    println("Historial de búsquedas:")
                    val historialOrdenado = history.sortedByDescending { it.apariciones }
                    for ((index, resultado) in historialOrdenado.withIndex()) {
                        println("Búsqueda ${index + 1}:")
                        println("Texto elegido: ${resultado.texto}")
                        println("Palabra/oración de búsqueda: ${resultado.busqueda}")
                        println("Tiempo de duración de la búsqueda: ${resultado.duracion} ms")
                        println("Cantidad de apariciones: ${resultado.apariciones}")
                        println(" ")
                    }
                }
                4 -> {
                    println("Hasta luego!")
                    return
                }
                else -> println("Opción inválida. Por favor, ingrese un número válido.")
            }
            println()
        }
    }else{
        println("Información de usuario no válida. No puedes entrar!")
    }
}

fun loadTextFromFile(filePath: String): String{
    val file = File(filePath)
    return if(file.exists()){
        file.readText()
    }else{
        ""
    }
}



// Algoritmos de Búsqueda
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
    val table = IntArray(65536) { pattern.length } //Longitud máxima de 65536 caracteres

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