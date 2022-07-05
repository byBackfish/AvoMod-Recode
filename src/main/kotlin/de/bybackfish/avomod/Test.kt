package de.bybackfish.avomod

val a = 0

class Test {
    val a = 0
}

fun main(args: Array<String>) {

    val field = Test::class.java.getDeclaredField("a")

    field.isAccessible = true
    println(field.type.isAssignableFrom(Int::class.java))


}
