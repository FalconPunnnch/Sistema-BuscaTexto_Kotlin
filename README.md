<sub>beta 1.0 - Posibles actualizaciones futuras.</sub>
# Sistema de Búsqueda de Textos en Kotlin
## Sistema con menú de acceso y búsqueda de textos mediante 3 posibles algoritmos

### Ingreso al Sistema
Se accede al programa mediante una serie de usuarios y contraseñas previamente registrados. Si se tiene éxito, se muestra el menú de opciones:
  1. Registrar texto
  2. Buscar palabra/oración en texto registrado
  3. Ver historial de búsquedas
  4. Salir

### Registro de texto
Los textos son registrados mediante la ruta de ubicación de los mismos.
***Es importante que el archivo esté en la misma unidad de memoria para evitar errores.***

### Búsqueda de palabra/oración
Se dan 3 opciones de algoritmos de búsqueda de cadenas al usuario:
- Fuerza Bruta
- Boyer-Moore
- KMP (Knutt-Morris-Pratt)
(Los algoritmos ya están implementados para su uso directo con este sistema).

Una vez elegido, el usuario debe ingresar la cadena a buscar. Se mostrarán las posiciones del texto en que se encontraron coincidencias, además de otro menú con las siguientes opciones:
  1. Mostrar cantidad de apariciones
  2. Ver apariciones
  3. Ir a la ocurrencia siguiente
  4. Ir a la ocurrencia anterior
  5. Cancelar operación
(Opción 5 devuelve a menú de selección de algoritmos).

### Historial de búsquedas
Se muestra:
- Número de orden de búsqueda
- Texto elegido
- Palabra/oración de búsqueda
- Tiempo de duración de búsqueda (del sistema, no interacción con usuario).
- Cantidad de apariciones

### Librerías Utilizadas
1. java.util.Scanner: para lectura desde el teclado.
2. java.io.File: para cargar y procesar archivos de texto.
3. kotlin.system.measureTimeMillis: para medir el tiempo de ejecución del código en milisegundos.

*ㅤ*
## Fuentes y referencias:
1. Introduction to Algorithms, Third Edition (Thomas H. Cormen, Charles E. Leiserson etc.). (BM y KMP)
2. Estructuras de datos en Java, 4ª Edición (Mark Allen Weiss).
3. Algorithms Unlocked (Thomas H. Cormen). (FB)
4. Kotlin Cookbook - A Problem-Focused Approach (Ken Kousen). (Para problemas en sintáxis).
5. www.geeksforgeeks.org
  - /file-class-in-java/?ref=gcse (para carga de archivos).
6. https://chat.openai.com/ (ChatGPT para problemas menores al compilar).
