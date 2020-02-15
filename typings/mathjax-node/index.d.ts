
//export as namespace "mathjax-node"

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

    import "mathjax"

    interface Config {
        displayMessages: boolean,
        displayErrors: boolean,
        undefinedCharError: boolean,
        extensions: any,
        paths: any,
        fontURL: any,
        MathJax: MathJax.Config,
    }

    //export = {} as any
    export const config: (conf: Partial<Config>) => Config;
        const start: () => void;
        const typeset: any;
        const MathJax: any;
const }


