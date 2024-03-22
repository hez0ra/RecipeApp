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
            image.contentEquals(other.image)
        }
        else{
            return false
        }
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        result = 31 * result + time.hashCode()
        result = 31 * result + kcal.hashCode()
        result = 31 * result + serv.hashCode()
        result = 31 * result + ingridients.hashCode()
        result = 31 * result + instruction.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + category.hashCode()
        result = 31 * result + countIngr
        return result
    }
}
