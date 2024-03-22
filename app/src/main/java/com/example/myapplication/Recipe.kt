package com.example.myapplication

class Recipe(var id: Int, var name: String, var time: UInt, var kcal: UInt, var serv: UInt, var ingridients: List<String>, var instruction: List<String>, var image: ByteArray, val category: String) {
    var countIngr: Int = ingridients.size

    // Второй конструктор без id
    constructor(name: String, time: UInt, kcal: UInt, serv: UInt, ingredients: List<String>, instruction: List<String>, image: ByteArray, category: String) :
            this(-1, name, time, kcal, serv, ingredients, instruction, image, category)

    override fun equals(other: Any?): Boolean {
        return if (other is Recipe){
            id == other.id &&
            name == other.name &&
            time == other.time &&
            kcal == other.kcal &&
            serv == other.serv &&
            ingridients == other.ingridients &&
            instruction == other.instruction &&
            image == other.image
        }
        else{
            return false
        }
    }
}
