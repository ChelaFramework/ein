package ein.core.view.viewmodel

abstract class eProperty<T> {
    private val props = mutableMapOf<String, (T, Any)->Unit>()
    operator fun get(k:String) = props[k]
    operator fun set(k:String, v:(T, Any)->Unit){props[k] = v}
}
