package com.github.aanno.dbtoolchain.xml;

import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;

import java.io.IOException;

/**
 * * https://www.saxonica.com/html/documentation/using-xsl/embedding/
 * * https://www.saxonica.com/html/documentation/using-xsl/embedding/s9api-transformation.html
 * * https://www.saxonica.com/html/documentation/sourcedocs/jaxpsources.html
 */
public class S9ApiSingleton {

    private static S9ApiSingleton INSTANCE = new S9ApiSingleton();

    private final TraxSingleton traxSingleton;

    private final Processor processor;

    private final XsltCompiler compiler;

    private final XsltExecutable db51ToFo;

    private S9ApiSingleton() {
        try {
            traxSingleton = TraxSingleton.getInstance();
            processor = new Processor(S9ApiUtils.getConfiguration());
            compiler = processor.newXsltCompiler();
            db51ToFo = compiler.compile(traxSingleton.getSource(S9ApiUtils.getDocbookPath(), true));
        } catch (IOException | SaxonApiException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static S9ApiSingleton getInstance() {
        return INSTANCE;
    }
}
