package com.github.aanno.dbtoolchain.xml;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;

/**
 * https://www.saxonica.com/html/documentation/using-xsl/embedding/
 * https://www.saxonica.com/html/documentation/using-xsl/embedding/s9api-transformation.html
 */
public class S9ApiSingleton {

    private static S9ApiSingleton INSTANCE = new S9ApiSingleton();

    private final Processor processor;

    private final XsltCompiler compiler;

    private final XsltExecutable db51ToFo;

    private S9ApiSingleton() {
        processor = new Processor(S9ApiUtils.getConfiguration());
        compiler = processor.newXsltCompiler();
        db51ToFo = compiler.compile();
    }
}
