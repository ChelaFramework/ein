package ein.js.js

private val inF = js("function(k, t){return k in t;}")
infix fun String.jsIn(target:dynamic) = inF(this, target) as Boolean