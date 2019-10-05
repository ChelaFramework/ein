package ein.core.core

fun <T> elazy(isNONE:Boolean = false, initializer: () -> T): Lazy<T> = lazy(
    if(isNONE) LazyThreadSafetyMode.NONE else LazyThreadSafetyMode.PUBLICATION,
    initializer
)
