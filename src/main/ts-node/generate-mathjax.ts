// a simple TeX-input example

/* -- /// <reference path="typings/myModules.d.ts" /> */
import * as mjAPI from "mathjax-node"

mjAPI.config({
    MathJax: {
        // traditional MathJax configuration
    }
})
mjAPI.start()

var yourMath = 'E = mc^2'

mjAPI.typeset({
    math: yourMath,
    format: "TeX", // or "inline-TeX", "MathML"
    mml:true,      // mml:true or svg:true, or html:true
}, (data: any) => {
    if (!data.errors) {
        console.log(data.mml)
    }
})
