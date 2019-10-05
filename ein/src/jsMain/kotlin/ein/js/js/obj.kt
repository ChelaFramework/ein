package ein.js.js

inline fun obj(target:dynamic, block:dynamic.()->Unit):dynamic{
    block(target)
    return target
}
inline fun obj(block:dynamic.()->Unit):dynamic{
    val o = js("{}")
    block(o)
    return o
}