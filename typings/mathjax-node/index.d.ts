
// export as namespace "mathjax-node"

/*
  if (config.displayMessages != null)    {displayMessages = config.displayMessages}
  if (config.displayErrors != null)      {displayErrors   = config.displayErrors}
  if (config.undefinedCharError != null) {undefinedChar   = config.undefinedCharError}
  if (config.extensions != null)         {extensions      = config.extensions}
  if (config.paths != null)              {paths           = config.paths}
  if (config.fontURL != null)            {fontURL         = config.fontURL}
  if (config.MathJax) {
    // strip MathJax config blocks to avoid errors
    if (config.MathJax.config) delete config.MathJax.config
    MathJaxConfig = config.MathJax
  }

 */

declare module "mathjax-node" {
    import * as mj from "mathjax"

    interface Config {
        displayMessages: any,
        displayErrors: any,
        undefinedCharError: any,
        extensions: any,
        paths: any,
        fontURL: any,
        MathJax: mj.MathJax,
    }

    // export = {} as any
    export const config: Partial<Config>, start: () => void, typeset: any
}


