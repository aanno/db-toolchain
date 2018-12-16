package com.github.aanno.dbtoolchain.xml;

import com.thaiopensource.relaxng.jaxp.XMLSyntaxSchemaFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xmlresolver.Catalog;
import org.xmlresolver.Resolver;

import javax.xml.XMLConstants;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TraxSingleton {

    static {
        // TODO tp:
        System.setProperty(SchemaFactory.class.getName() + ":" + XMLConstants.RELAXNG_NS_URI,
                XMLSyntaxSchemaFactory.class.getName());
        System.setProperty("xml.catalog.files",
                "/home/tpasch/scm/db-toolchain/schema/5.1/schemas/catalog.xml"
        );
        System.setProperty("xml.catalog.cacheUnderHome", "true");
        System.setProperty("xml.catalog.prefer", "true");
    }

    private static TraxSingleton INSTANCE = new TraxSingleton();

    private final SAXParserFactory saxParserFactory;

    private final SAXParserFactory simpleSaxParserFactory;

    private final Catalog catalog;

    private final Resolver resolver;

    private TraxSingleton() {
        catalog = new Catalog();
        resolver = new Resolver(catalog);

        saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware(true);
        saxParserFactory.setValidating(true);
        saxParserFactory.setXIncludeAware(true);

        simpleSaxParserFactory = SAXParserFactory.newInstance();
        simpleSaxParserFactory.setNamespaceAware(true);
        simpleSaxParserFactory.setValidating(false);
        simpleSaxParserFactory.setXIncludeAware(true);
    }

    public static TraxSingleton getInstance() {
        return INSTANCE;
    }

    public Resolver getResolver() {
        return resolver;
    }

    public SAXParser getSAXParser(boolean validating) throws SAXException {
        SAXParser result = null;
        try {
            if (validating) {
                result = saxParserFactory.newSAXParser();
            } else {
                result = simpleSaxParserFactory.newSAXParser();
            }
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
        result.getXMLReader().setEntityResolver(resolver);
        return result;
    }

    public Source sourceFromUri(String uri, boolean validating) throws IOException {
        return getSource(catalog.lookupURI(uri).body(), validating);
    }

    public Path pathFromUri(String uri) throws IOException {
        String result = catalog.lookupURI(uri).uri();
        if (!result.startsWith("file:/")) {
            throw new IllegalStateException(result);
        }
        result = result.substring(6);
        return Paths.get(result);
    }

    public String uriFromUri(String uri) throws IOException {
        return catalog.lookupURI(uri).uri();
    }

    public Source getSource(Path path, boolean validating) throws IOException {
        try {
            SAXParser parser = getSAXParser(validating);
            return new SAXSource(parser.getXMLReader(), new InputSource(Files.newInputStream(path)));
        } catch (SAXException e) {
            throw new IOException(e);
        }
    }

    public Source getSource(InputStream inputStream, boolean validating) throws IOException {
        try {
            SAXParser parser = getSAXParser(validating);
            return new SAXSource(parser.getXMLReader(), new InputSource(inputStream));
        } catch (SAXException e) {
            throw new IOException(e);
        }
    }

    public StreamSource getStreamSource(Path path) throws IOException {
        StreamSource result = new StreamSource(Files.newInputStream(path));
        result.setSystemId(path.toFile());
        return result;
    }

    public InputSource getSAXInputSource(Path path) throws IOException {
        InputSource result = new InputSource(Files.newInputStream(path));
        return result;
    }
}
