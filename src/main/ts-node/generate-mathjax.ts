//a simple TeX-input example

/*-- /// <reference path="typings/myModules.d.ts" /> */
import * as mjAPI from "mathjax-node"
import {outputFile} from "fs-extra"

//--- <reference path="mathjax/index.d.ts" />
//import {} from "mathjax"
//import "mathjax.d"

mjAPI.config({
    //determines whether Message.Set() calls are logged
    displayMessages: false,

    //determines whether error messages are shown on the console
    displayErrors:   true,

    //determines whether "unknown characters" (i.e., no glyph in the configured fonts) are saved in the error array
    undefinedCharError: false,

    //a convenience option to add MathJax extensions
    extensions: "",

    //for webfont urls in the CSS for HTML output
    //fontURL: 'https://cdnjs.cloudflare.com/ajax/libs/mathjax/2.7.5/fonts/HTML-CSS',

    //configures custom path variables (e.g., for third party extensions, cf. test/config-third-party-extensions.js)
    paths: {},

    //standard MathJax configuration options, see https://docs.mathjax.org for more detail.
    MathJax: {
        //traditional MathJax configuration
    }
})
mjAPI.start()

const yourMath = "E = mc^2"

//http://docs.mathjax.org/en/latest/output.html
mjAPI.typeset({
    math: yourMath,
    format: "TeX", //or "inline-TeX", "MathML"
    mml: true,      //mml:true or svg:true, or html:true
    svg: true,
    html: true
}).then((data: any) => {
    if (!data.errors) {
        console.log("mml\n" + data.mml + "\nsvg:\n" + data.svg + "\nhtml:\n" + data.html)
        if (data.svg) {
outputFile("examples/svg/mathjax.svg", data.svg)
        }
    } else {
        console.log("error: " + data)
    }
})
