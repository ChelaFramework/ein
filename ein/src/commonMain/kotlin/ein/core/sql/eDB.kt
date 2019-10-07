package ein.core.sql

import ein.core.value.eJsonArray
import ein.core.log.log

abstract class eDB(protected val db:String, protected val ver:Int, protected val create:String){
    companion object{
        private val blocks = mutableMapOf<String, (String)->eDB>()
        private val dbs = mutableMapOf<String, eDB>()
        operator fun get(k:String) = dbs[k] ?: blocks[k]?.invoke(k)?.apply{dbs[k] = this}
        operator fun set(k:String, block:(String)->eDB){blocks[k] = block}
        operator fun minusAssign(k:String){
            get(k)?.let{
                it.remove {
                    blocks.remove(k)
                    dbs.remove(k)
                }
            }
        }
        fun remove(k:String) = minusAssign(k)
        private val EMPTY:(Array<Array<Any?>>?, String?)->Unit = {_,_->}
    }
    protected var isBegin = false
            private set

    fun begin(){
        isBegin = true
    }
    fun commit(){
        isBegin = false
    }
    fun rollback(){
        isBegin = false
    }
    abstract fun transactionBegin()
    abstract fun transactionCommit()
    abstract fun transactionRollback()
    internal abstract fun remove(block:()->Unit)
    abstract fun query(k:String, vararg arg:Pair<String, Any>, block:(Array<Array<Any?>>?, String?)->Unit = EMPTY)
    fun i(k:String, vararg arg:Pair<String, Any>):Int = getValue(k, arg)
    fun f(k:String, vararg arg:Pair<String, Any>):Float = getValue(k, arg)
    fun s(k:String, vararg arg:Pair<String, Any>):String = getValue(k, arg)
    fun b(k:String, vararg arg:Pair<String, Any>):ByteArray = getValue(k, arg)
    fun i(k:String, v:Int, vararg arg:Pair<String, Any>):Int = try {
        getValue(k, arg)
    } catch(e:Throwable) {
        v
    }
    fun f(k:String, v:Float, vararg arg:Pair<String, Any>):Float = try{
        getValue(k, arg)
    }catch(e:Throwable){
        v
    }
    fun s(k:String, v:String, vararg arg:Pair<String, Any>):String = try{
        getValue(k, arg)
    }catch(e:Throwable){
        v
    }
    fun b(k:String, v:ByteArray, vararg arg:Pair<String, Any>):ByteArray = try{
        getValue(k, arg)
    }catch(e:Throwable){
        v
    }
    private fun <T>getValue(k:String, arg:Array<out Pair<String, Any>>):T{
        var r:T? = null
        query(k, *arg){rs,m->
            @Suppress("UNCHECKED_CAST")
            if(rs != null && rs.isNotEmpty()) r = rs[0][0] as? T ?: throw Throwable("invalid type - ${rs[0][0]}")
            else throw Throwable("nodata - $k, $arg")
        }
        return r ?: throw Throwable("invalid query - $k, $arg")
    }

}