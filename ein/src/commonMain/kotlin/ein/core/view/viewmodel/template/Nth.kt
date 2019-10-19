package ein.core.view.viewmodel.template

interface Nth{
    companion object{
        val all = object:Nth {override fun invoke(idx:Int, size:Int) = true}
        val even = object:Nth {override fun invoke(idx:Int, size:Int) = idx % 2 == 0}
        val odd = object:Nth {override fun invoke(idx:Int, size:Int) = idx % 2 == 1}
        val first = object:Nth {override fun invoke(idx:Int, size:Int) = idx == 0}
        val last = object:Nth {override fun invoke(idx:Int, size:Int) = idx == size - 1}
        val inner = object:Nth {override fun invoke(idx:Int, size:Int) = idx != 0 && idx != size - 1}
        val outer = object:Nth {override fun invoke(idx:Int, size:Int) = idx == 0 || idx == size - 1}
        private val nths = mutableMapOf(
            "all" to all, "even" to even, "odd" to odd,
            "first" to first, "last" to last,
            "inner" to inner, "outer" to outer
        )
        operator fun get(k:String) = nths[k]
        operator fun set(k:String, v:Nth) = nths.put(k, v)
    }
    operator fun invoke(idx:Int, size:Int):Boolean
}