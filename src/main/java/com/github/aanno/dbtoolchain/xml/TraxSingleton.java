package com.github.aanno.dbtoolchain.xml;

import com.github.aanno.dbtoolchain.org.docbook.XSLT20;
import com.thaiopensource.relaxng.jaxp.XMLSyntaxSchemaFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.catalog.Catalog;
import javax.xml.catalog.CatalogFeatures;
import javax.xml.catalog.CatalogManager;
import javax.xml.catalog.CatalogResolver;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TraxSingleton {

    // see https://xerces.apache.org/xerces2-j/features.html
    // from XIncludeAwareParserConfiguration
    public static final String XINCLUDE_FEATURE = "http://apache.org/xml/features/xinclude";

    private static final Logger LOG = LoggerFactory.getLogger(TraxSingleton.class);

    static {
        // full XInclude support, see 'DocBook V5.0 Transition Guide FAQ 6.6.1.4'
        /*
        System.setProperty("org.apache.xerces.xni.parser.XMLParserConfiguration",
                XIncludeParserConfiguration.class.getName());
                */

        // TODO tp:
        System.setProperty(SchemaFactory.class.getName() + ":" + XMLConstants.RELAXNG_NS_URI,
                XMLSyntaxSchemaFactory.class.getName());

        // see https://xerces.apache.org/xml-commons/components/resolver/resolver-article.html
        // but does not work (setup is now in constructor)
        System.setProperty("xml.catalog.files",
                "schema/5.1/schemas/catalog.xml" +
                        ";schema/5.0/docbook-5.0/catalog.xml"
        );
        System.setProperty("xml.catalog.cacheUnderHome", "true");
        System.setProperty("xml.catalog.prefer", "true");

        System.setProperty("com.xmlcalabash.css-processor", "com.xmlcalabash.util.print.CssPrince");
        System.setProperty("com.xmlcalabash.css.prince.exepath", "/usr/bin/prince");
    }

    private static TraxSingleton INSTANCE = new TraxSingleton();

    private final SAXParserFactory saxParserFactory;

    private final SAXParserFactory simpleSaxParserFactory;

    private final TransformerFactory transformerFactory;

    private final Catalog catalog;

    private final CatalogResolver resolver;

    private final EntityResolver entityResolver;

    // We need an instance of XSLT20 (for getting the catalog), but it could _NOT_ be shared
    private final XSLT20 xslt20;

    private TraxSingleton() {
        xslt20 = new XSLT20();
        try {
            File current = new File(System.getProperty("user.dir")).getCanonicalFile();
            String currentURI = current.toURI().toASCIIString();

            File xslt20Catalog = new File(current, "schema/docbook-xslt20/catalog.xml");
            xslt20Catalog.getParentFile().mkdirs();
            String xslt20CatalogUri = xslt20Catalog.toURI().toASCIIString();
            xslt20.createCatalog(xslt20Catalog.toString());

            CatalogFeatures catalogFeatures = CatalogFeatures.builder()
                    // .with(CatalogFeatures.Feature.FILES, current.toURI().toASCIIString())
                    .build();
            catalog = CatalogManager.catalog(catalogFeatures,
                    new URI(currentURI + "/schema/5.1/schemas/catalog.xml"),
                    new URI(currentURI + "/schema/5.0/docbook-5.0/catalog.xml"),
                    new URI(currentURI + "/lib/docbook-xsl/catalog.xml"),
                    new URI(currentURI + "/submodules/asciidoctor-fopub/src/dist/catalog.xml"),
                    new URI(xslt20CatalogUri)
                    );
        } catch (URISyntaxException e) {
            throw new ExceptionInInitializerError(e);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }

        resolver = CatalogManager.catalogResolver(catalog);

        saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware(true);
        saxParserFactory.setValidating(true);
        saxParserFactory.setXIncludeAware(true);

        simpleSaxParserFactory = SAXParserFactory.newInstance();
        simpleSaxParserFactory.setNamespaceAware(true);
        simpleSaxParserFactory.setValidating(false);
        simpleSaxParserFactory.setXIncludeAware(true);

        try {
            saxParserFactory.setFeature(XINCLUDE_FEATURE, true);
            simpleSaxParserFactory.setFeature(XINCLUDE_FEATURE, true);
        } catch (Exception e) {
            LOG.error("xinclude failed", e);
        }

        entityResolver = resolver;

        /*
        try {
            transformerFactory = ((TransformerFactory)
                    Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl"
                    ).newInstance()
            );
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        } catch (IllegalAccessException e) {
            throw new ExceptionInInitializerError(e);
        } catch (InstantiationException e) {
            throw new ExceptionInInitializerError(e);
        }
         */
        transformerFactory = TransformerFactory.newInstance();
        // transformerFactory = SAXTransformerFactory.newInstance();
        transformerFactory.setErrorListener(new ErrorListener() {
            @Override
            public void warning(TransformerException exception) throws TransformerException {
                LOG.warn("transformer warn: " + exception.toString());
            }

            @Override
            public void error(TransformerException exception) throws TransformerException {
                LOG.error("transformer error: " + exception.toString(), exception);
            }

            @Override
            public void fatalError(TransformerException exception) throws TransformerException {
                LOG.error("transformer fatal error: " + exception.toString(), exception);
            }
        });
    }

    public static TraxSingleton getInstance() {
        return INSTANCE;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public CatalogResolver getCatalogResolver() {
        return resolver;
    }

    public EntityResolver getEntityResolver() {
        return entityResolver;
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
        result.getXMLReader().setEntityResolver(entityResolver);
        return result;
    }

    public Source sourceFromUri(String uri, boolean validating) throws IOException {
        return getSource(lookup(uri), validating);
    }

    public Path pathFromUri(String uri) throws IOException {
        // String result = catalog.lookupURI(uri).uri();
        String result = lookup(uri);
        if (!result.startsWith("file:/")) {
            throw new IllegalStateException(result);
        }
        result = result.substring(5);
        return Paths.get(result);
    }

    private String lookup(String identifier) throws IOException {
        String result = catalog.matchURI(identifier);
        if (result == null) {
            result = catalog.catalogs()
                    .map(c -> c.matchURI(identifier))
                    .filter(s -> s != null)
                    .findFirst().orElse(null);
        }
        if (result == null) {
            result = catalog.matchSystem(identifier);
        }
        if (result == null) {
            result = catalog.catalogs()
                    .map(c -> c.matchSystem(identifier))
                    .filter(s -> s != null)
                    .findFirst().orElse(null);
        }
        if (result == null) {
            result = catalog.matchPublic(identifier);
        }
        if (result == null) {
            result = catalog.catalogs()
                    .map(c -> c.matchPublic(identifier))
                    .filter(s -> s != null)
                    .findFirst().orElse(null);
        }
        return result;
    }

    public String uriFromUri(String uri) throws IOException {
        // TODO tp: Better use lookup(..)?
        String result = catalog.matchURI(uri);
        if (result == null) {
            result = catalog.catalogs()
                    .map(c -> c.matchSystem(uri))
                    .filter(s -> s != null)
                    .findFirst().orElse(null);
        }
        return  result;
    }

    public Source getSource(String path, boolean validating) throws IOException {
        return getSource(Paths.get(path), validating);
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

    public void transform(Source template, Source input, Result output) throws TransformerException {
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer(template);
        } catch (TransformerConfigurationException e) {
            LOG.error("creating transformer failed: " + e.toString(), e);
        }
        if (transformer != null) {
            transformer.setURIResolver(new URIResolver() {
                @Override
                public Source resolve(String href, String base) throws TransformerException {
                    return null;
                }
            });
            transformer.transform(input, output);
        }
    }

    public void transform(Source input, Result output) throws TransformerException {
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
        } catch (TransformerConfigurationException e) {
            LOG.error("creating transformer failed: " + e.toString(), e);
        }
        if (transformer != null) {
            transformer.setURIResolver(new URIResolver() {
                @Override
                public Source resolve(String href, String base) throws TransformerException {
                    return null;
                }
            });
            transformer.transform(input, output);
        }
    }

    public void close(StreamSource source) {
        if (source != null) {
            try {
                if (source.getInputStream() != null) {
                    source.getInputStream().close();
                }
                if (source.getReader() != null) {
                    source.getReader().close();
                }
            } catch (IOException e) {
                LOG.warn("close failed: " + e);
            }
        }
    }

    public void close(StreamResult result) {
        if (result != null) {
            try {
                if (result.getOutputStream() != null) {
                    result.getOutputStream().close();
                }
                if (result.getWriter() != null) {
                    result.getWriter().close();
                }
            } catch (IOException e) {
                LOG.warn("close failed: " + e);
            }
        }
    }

}
