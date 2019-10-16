package ein.core.looper

interface eTaskState{
    object GO:eTaskState
    object STOP:eTaskState
}
class ERROR(val block:()->Unit):eTaskState