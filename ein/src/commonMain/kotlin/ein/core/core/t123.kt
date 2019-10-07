package ein.core.core

infix fun <A, B, C> Pair<A, B>.t3(that:C) = Triple(this.first, this.second, that)
infix fun <A, B, C> Pair<A, B>.t2(that:C) = Triple(this.first, that, this.second)
infix fun <A, B, C> Pair<A, B>.t1(that:C) = Triple(that, this.first, this.second)