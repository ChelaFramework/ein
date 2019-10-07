package ein.android.app

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import ein.core.value.eJsonArray
import ein.core.value.eJsonObject
import ein.core.log.log
import ein.core.regex.eReg
import ein.core.regex.eRegValue
import ein.core.resource.eLoader

/*
    "drawable": {
      "inputFocus": {
        "corner": "5dp",
        "strokeWidth": "1.5dp",
        "strokeColor": "#18ba9b",
        "solid": "#ffffff"
      },
      "inputBlur": {
        "corner": "5dp",
        "strokeWidth": "1dp",
        "strokeColor": "#e4e4e4",
        "solid": "#ffffff"
      },
      "inputVali": {
        "corner": "5dp",
        "strokeWidth": "1dp",
        "strokeColor": "#fa635c",
        "solid": "#ffffff"
      },
    }
 */
object eDrawable:eLoader{
    init {eLoader += eDrawable}
    override fun load(res:eJsonObject){
        (res["drawable"] as? eJsonObject)?.forEach{(k, v)->set(k, v as eJsonObject)}
    }
    private sealed class Param{
        class StrokeWidth(val width:Int):Param()
        class StrokeColor(val color:String):Param()
        class Solid(val color:String):Param()
        class Gradient(val colors:IntArray):Param()
        class Corner(val radius:Float):Param()
        object Line:Param()
        object Rect:Param()
        object Oval:Param()
        object Ring:Param()
        object None:Param()
        object Linear:Param()
        object Radial:Param()
        object Sweep:Param()
        object BL_TR:Param()
        object B_T:Param()
        object BR_TL:Param()
        object L_R:Param()
        object TL_BR:Param()
        object T_B:Param()
        object TR_BL:Param()
    }
    private val drawables = mutableMapOf<String, Drawable>()
    operator fun get(k:String) = drawables[k]
    operator fun minusAssign(k:String) = remove(k)
    fun remove(k:String){drawables.remove(k)}
    operator fun set(k:String, obj:eJsonObject){
        val arg = mutableListOf<Param>()
        obj.forEach{ (key, value) ->
            val v = value.v
            when(key.toLowerCase()){
                "strokewidth" -> {
                    if (v is Number) arg += Param.StrokeWidth(v.toInt())
                    else if(v is String) eRegValue.num(v)?.let{arg += Param.StrokeWidth(it.toInt())}
                }
                "corner" -> {
                    if (v is Number) arg += Param.Corner(v.toFloat())
                    else if (v is String) eRegValue.num(v)?.let {arg += Param.Corner(it.toFloat())}
                }
                "strokecolor" -> arg += Param.StrokeColor("$v")
                "solid" -> arg += Param.Solid("$v")
                "shape" -> arg += when("$v"){
                    "line" ->Param.Line
                    "oval" ->Param.Oval
                    "ring" ->Param.Ring
                    "rect" ->Param.Rect
                    else ->Param.Rect
                }
                "line" -> arg += Param.Line
                "rect" -> arg += Param.Rect
                "oval" -> arg += Param.Oval
                "ring" -> arg += Param.Ring
                "none" -> arg += Param.None
                "linear" -> arg += Param.Linear
                "radial" -> arg += Param.Radial
                "sweep" -> arg += Param.Sweep
                "bl_tr" -> arg += Param.BL_TR
                "b_t" -> arg += Param.B_T
                "br_tl" -> arg += Param.BR_TL
                "l_r" -> arg += Param.L_R
                "tl_br" -> arg += Param.TL_BR
                "t_b" -> arg += Param.T_B
                "tr_bl" -> arg += Param.TR_BL
                "gradient"-> arg += Param.Gradient((value as eJsonArray).map {Color.parseColor("$it.v")}.toIntArray())
                else->throw Throwable("invalid key: $key")
            }
        }
        var width = 0
        var stroke = 0
        var solid = 0
        var isSolid = false
        var gradient = intArrayOf()
        var isCorner = false
        var corner = 0F
        var shape:Param = Param.None
        var type = GradientDrawable.LINEAR_GRADIENT
        var orient = GradientDrawable.Orientation.LEFT_RIGHT
        arg.forEach {
            when(it){
                is Param.StrokeWidth-> width = it.width
                is Param.StrokeColor-> stroke = Color.parseColor(it.color)
                is Param.Solid->{
                    isSolid = true
                    solid = Color.parseColor(it.color)
                }
                is Param.Gradient-> gradient = it.colors
                is Param.Corner->{
                    isCorner = true
                    corner = it.radius
                }
                is Param.Ring,is Param.Oval,is Param.Rect,is Param.Line->shape = it
                is Param.Linear->type = GradientDrawable.LINEAR_GRADIENT
                is Param.Radial->type = GradientDrawable.RADIAL_GRADIENT
                is Param.Sweep->type = GradientDrawable.SWEEP_GRADIENT
                is Param.BL_TR->orient = GradientDrawable.Orientation.BL_TR
                is Param.B_T->orient = GradientDrawable.Orientation.BOTTOM_TOP
                is Param.BR_TL->orient = GradientDrawable.Orientation.BR_TL
                is Param.L_R->orient = GradientDrawable.Orientation.LEFT_RIGHT
                is Param.TL_BR->orient = GradientDrawable.Orientation.TL_BR
                is Param.T_B->orient = GradientDrawable.Orientation.TOP_BOTTOM
                is Param.TR_BL->orient = GradientDrawable.Orientation.TR_BL
            }
        }
        val key = if(k != "") k
            else "shape-$width:$stroke:$solid:$isSolid:$gradient:$isCorner:$corner:$shape:$type:$orient"
        if (isSolid) gradient = intArrayOf(solid, solid)
        val gd = GradientDrawable(orient, gradient)
        gd.gradientType = type
        if (shape != Param.None) gd.shape = when (shape) {
            is Param.Line-> GradientDrawable.LINE
            is Param.Oval-> GradientDrawable.OVAL
            is Param.Ring-> GradientDrawable.RING
            is Param.Rect-> GradientDrawable.RECTANGLE
            else -> GradientDrawable.RECTANGLE
        }
        if (isCorner) gd.cornerRadius = corner
        if (width > 0) gd.setStroke(width, stroke)
        drawables[key] = gd
    }
}