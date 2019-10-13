package ein.js.js

import ein.core.core.elazy
import kotlin.browser.window

val isMobile by elazy(true){
    """android|webos|iphone|ipad|ipod|blackberry|windows phone""".toRegex().containsMatchIn(window.navigator.userAgent.toLowerCase())
}