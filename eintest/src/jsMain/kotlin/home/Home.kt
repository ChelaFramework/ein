package home

import ein.js.view.viewmodel.eDomVM

class Home:eDomVM(){
    var x by kv("x", "")
    var title by kv("title", "Main Title")
    val contents by kv("contents", object:eDomVM(){
        override var html by html("contents Text")
        override var color by color("#ff0000")
    })
    val tmpl by kv("tmpl", object:eDomVM() {
        override var template by template{
            data(
                object:eDomVM() {
                    override var html by html("title1")
                },
                object:eDomVM() {
                    override var html by html("title2")
                }
            )
            templates("tmpl1")
        }
    })
}