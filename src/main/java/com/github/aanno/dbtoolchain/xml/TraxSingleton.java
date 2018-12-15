package com.github.aanno.dbtoolchain.xml;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TraxSingleton {

    private static TraxSingleton INSTANCE = new TraxSingleton();

    private final SAXParserFactory saxParserFactory;

    private TraxSingleton() {
        saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware(true);
        saxParserFactory.setValidating(true);
        saxParserFactory.setXIncludeAware(true);
    }

    public static TraxSingleton getInstance() {
        return INSTANCE;
    }

    public SAXParser getSAXParser() throws SAXException {
        SAXParser result = null;
        try {
            result = saxParserFactory.newSAXParser();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
        return result;
    }

    public Source getSource(Path path) throws IOException {
        try {
            SAXParser parser = getSAXParser();
            return new SAXSource(parser.getXMLReader(), new InputSource(Files.newInputStream(path)));
        } catch (SAXException e) {
            throw new IOException(e);
        }
    }
}
