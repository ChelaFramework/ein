package ein.core.looper

abstract class eScheduler{
    lateinit var looper:eLooper
    internal abstract fun start()
    internal abstract fun stop()
}
