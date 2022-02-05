package com.github.aanno.dbtoolchain.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;

public class LoggingURIResolver implements URIResolver {

    private static final Logger LOG = LoggerFactory.getLogger(LoggingURIResolver.class);

    private final URIResolver wrapped;

    public LoggingURIResolver(URIResolver wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public Source resolve(String rel, String base) throws TransformerException {
        Source result = wrapped.resolve(rel, base);
        LOG.warn("resolve(" + rel + ", " + base + ") is '" + result.getSystemId() + "'");
        return result;
    }
}
