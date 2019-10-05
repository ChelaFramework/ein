package ein.core.core

inline fun<A,B> eNN(a:A?, b:B?, block:(A, B)->Unit){
    if(a == null || b == null) return
    block(a, b)
}
inline fun<A,B,C> eNN(a:A?, b:B?, c:C?, block:(A, B, C)->Unit){
    if(a == null || b == null || c == null) return
    block(a, b, c)
}
inline fun<A,B,C,D> eNN(a:A?, b:B?, c:C?, d:D?, block:(A, B, C, D)->Unit){
    if(a == null || b == null || c == null || d == null) return
    block(a, b, c, d)
}
inline fun<A,B,C,D,E> eNN(a:A?, b:B?, c:C?, d:D?, e:E?, block:(A, B, C, D, E)->Unit){
    if(a == null || b == null || c == null || d == null || e == null) return
    block(a, b, c, d, e)
}