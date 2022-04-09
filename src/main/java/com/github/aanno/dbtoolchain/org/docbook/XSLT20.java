package com.github.aanno.dbtoolchain.org.docbook;

import com.github.aanno.dbtoolchain.xml.S9ApiUtils;
import com.github.aanno.dbtoolchain.xml.TraxSingleton;
import com.xmlcalabash.core.XProcConfiguration;
import com.xmlcalabash.core.XProcRuntime;
import com.xmlcalabash.io.WritableDocument;
import com.xmlcalabash.model.RuntimeValue;
import com.xmlcalabash.model.Serialization;
import com.xmlcalabash.runtime.XPipeline;
import com.xmlcalabash.util.Input;
import com.xmlcalabash.util.XProcURIResolver;
import net.sf.saxon.s9api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.ls.LSInput;
import org.xml.sax.InputSource;

import javax.xml.catalog.CatalogException;
import javax.xml.catalog.CatalogResolver;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import java.io.*;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSource;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

public class XSLT20 {
    private static final QName _output = new QName("", "output");
    private static final QName _format = new QName("", "format");

    private static final  Logger logger = LoggerFactory.getLogger(XSLT20.class);

    private String proctype = "he";
    private boolean schemaAware = false;
    // location of docbook-xsl.jar
    private String jarLoc = null;
    private Hashtable<String,String> nsbindings = new Hashtable<String, String> ();
    private Hashtable<QName,RuntimeValue> params = new Hashtable<QName, RuntimeValue> ();
    private Vector<String> paramFiles = new Vector<String>();
    private String sourcefn = null;
    private Hashtable<QName,RuntimeValue> options = new Hashtable<QName,RuntimeValue>();
    private String version = null;
    private String resourcesVersion = null;
    private String catalogFile = null;
    private Properties configProperties = null;

    public XSLT20() {
        jarLoc = S9ApiUtils.jarOrBasePathForResource("org/docbook/XSLT20.class");
    }

    public String getJarLoc() {
        return jarLoc;
    }

    public void setParam(String param, String value) {
        if (param.contains(":")) {
            int pos = param.indexOf(":");
            String pfx = param.substring(0, pos);
            String local = param.substring(pos+1);
            if (nsbindings.containsKey(pfx)) {
                QName name = new QName(pfx, nsbindings.get(pfx), local);
                params.put(name, new RuntimeValue(value));
                logger.debug("Parameter " + name.getClarkName() + "=" + value);
            } else {
                throw new RuntimeException("No namespace binding for prefix: " + pfx);
            }

        } else {
            params.put(new QName("", param), new RuntimeValue(value));
            logger.debug("Parameter " + param + "=" + value);
        }
    }

    public void setOption(String opt, String value) {
        if (opt.contains(":")) {
            int pos = opt.indexOf(":");
            String pfx = opt.substring(0, pos);
            String local = opt.substring(pos+1);
            if (nsbindings.containsKey(pfx)) {
                QName name = new QName(pfx, nsbindings.get(pfx), local);
                options.put(name, new RuntimeValue(value));
                logger.debug("Option " + name.getClarkName() + "=" + value);
            } else {
                throw new RuntimeException("No namespace binding for prefix: " + pfx);
            }

        } else {
            options.put(new QName("", opt), new RuntimeValue(value));
            logger.debug("Option " + opt + "=" + value);
        }
    }

    public void setNamespace(String prefix, String uri) {
        nsbindings.put(prefix, uri);
    }

    public void addParameterFile(String fn) {
        paramFiles.add(fn);
        logger.debug("Param file=" + fn);
    }

    public String createCatalog() {
        return createCatalog(null);
    }

    public String createCatalog(String catalogFilename) {
        if (catalogFile != null) {
            if (catalogFilename == null || catalogFile.equals(catalogFilename)) {
                return catalogFile;
            }
        }

        try {
            Processor processor = new Processor(false);
            DocumentBuilder builder = processor.newDocumentBuilder();
            builder.setDTDValidation(false);
            builder.setLineNumbering(true);

            URL uris_url = new URL(jarLoc + "/etc/uris.xml");
            URL xsl_url   = new URL(jarLoc + "/etc/make-catalog.xsl");

            InputSource uris_src = new InputSource(getStream(uris_url));
            InputSource xsl_src = new InputSource(getStream(xsl_url));

            uris_src.setSystemId(uris_url.toURI().toASCIIString());
            xsl_src.setSystemId(xsl_url.toURI().toASCIIString());

            XdmNode uris = builder.build(new SAXSource(uris_src));
            XdmNode xsl  = builder.build(new SAXSource(xsl_src));

            XsltCompiler compiler = processor.newXsltCompiler();
            compiler.setSchemaAware(false);
            XsltExecutable exec = compiler.compile(xsl.asSource());
            XsltTransformer transformer = exec.load();

            transformer.setParameter(new QName("", "jarloc"), new XdmAtomicValue(jarLoc));
            transformer.setParameter(new QName("", "version"), new XdmAtomicValue(version()));
            transformer.setParameter(new QName( "", "resourcesVersion"), new XdmAtomicValue(resourcesVersion()));
            transformer.setInitialContextNode(uris);

            XdmDestination xresult = new XdmDestination();
            transformer.setDestination(xresult);

            transformer.setSchemaValidationMode(ValidationMode.DEFAULT);
            transformer.transform();
            XdmNode xformed = xresult.getXdmNode();

            File tempcat = null;
            if (catalogFilename == null) {
                tempcat = File.createTempFile("dbcat", ".xml");
                tempcat.deleteOnExit();
            } else {
                tempcat = new File(catalogFilename);
            }

            logger.debug("Transient catalog file: " + tempcat.getAbsolutePath());

            PrintStream catstream = new PrintStream(tempcat);
            catstream.print(xformed.toString());
            catstream.close();

            catalogFile = tempcat.getAbsolutePath();
            return catalogFile;
        } catch (SaxonApiException | IOException | URISyntaxException sae) {
            logger.info("org.docbook.XSLT20 failed to create catalog: " + sae.getMessage());
            throw new RuntimeException(sae);
        }
    }

    private InputStream getStream(URL url) {
        URLConnection conn = null;
        try {
            try {
                conn = (JarURLConnection) url.openConnection();
            } catch (ClassCastException cce) {
                conn = url.openConnection();
            }

            return conn.getInputStream();
        } catch (IOException ioe) {
            throw new RuntimeException("Cannot read: " + url.toString());
        }
    }

    public String version() {
        if (version != null) {
            return version;
        }

        loadProperties();
        version = configProperties.getProperty("version");
        if (version == null) {
            throw new UnsupportedOperationException("No version property in version.properties!?");
        }
        return version;
    }

    public String resourcesVersion() {
        if (resourcesVersion != null) {
            return resourcesVersion;
        }

        loadProperties();
        resourcesVersion = configProperties.getProperty("resourcesVersion");
        if (resourcesVersion == null) {
            throw new UnsupportedOperationException("No resourcesVersion property in version.properties!?");
        }
        return resourcesVersion;
    }

    private void loadProperties() {
        if (configProperties != null) {
            return;
        }

        configProperties = new Properties();
        InputStream stream = null;
        try {
            URL version_url = new URL(jarLoc + "/etc/version.properties");
            stream = getStream(version_url);
            configProperties.load(stream);
        } catch (IOException ioe) {
            throw new UnsupportedOperationException("Failed to load version.properties file from JAR!?");
        }
    }

    public XProcRuntime newDefaultXProcRuntime() {
        XProcConfiguration config = new XProcConfiguration(proctype, schemaAware);
        XProcRuntime runtime = new XProcRuntime(config);
        return runtime;
    }

    public void run(String sourcefn) throws IOException, SaxonApiException {
        File cwdFile = new File(System.getProperty("user.dir"));
        String baseURI = cwdFile.toURI().toASCIIString();
        if (!baseURI.endsWith("/")) {
            baseURI = baseURI + "/";
        }

        String format = "html";
        if (options.containsKey(_format)) {
            format = options.get(_format).getString();
        }

        String xpl = "xslt/base/pipelines/db2html.xpl";
        if (format.equals("foprint") || format.equals("cssprint")) {
            xpl = "xslt/base/pipelines/db2pdf.xpl";
        } else if (format.equals("xhtml")) {
            xpl = "xslt/base/pipelines/db2xhtml.xpl";
        } else if (format.equals("fo")) {
            xpl = "xslt/base/pipelines/db2fo.xpl";
        }

        xpl = jarLoc + "/xslt/base/pipelines/" + xpl;

        runXpl(sourcefn, xpl, baseURI);
    }

    public void runXpl(String sourcefn, String xpl, String baseURI) throws IOException, SaxonApiException {
        if (!baseURI.endsWith("/")) {
            baseURI = baseURI + "/";
        }
        XProcRuntime runtime = newDefaultXProcRuntime();
        XdmNode source = runtime.parse(sourcefn, baseURI);

        // String catalogFn = createCatalog();
        // Catalog catalog = new Catalog(catalogFn);

        XProcURIResolver resolver = runtime.getResolver();
        URIResolver uriResolver = resolver.getUnderlyingURIResolver();
        URIResolver myResolver = new DocBookResolver(uriResolver, TraxSingleton.getInstance().getCatalogResolver());
        resolver.setUnderlyingURIResolver(myResolver);

        logger.debug("Pipline=" + xpl);

        XPipeline pipeline = runtime.load(new Input(xpl));
        pipeline.writeTo("source", source);

        for (String param : paramFiles) {
            XdmNode pfile = runtime.parse(param, baseURI);
            pipeline.writeTo("parameters", pfile);
        }

        for (QName param : params.keySet()) {
            pipeline.setParameter(param, params.get(param));
        }

        for (QName opt : options.keySet()) {
            pipeline.passOption(opt, options.get(opt));
        }

        pipeline.run();

        XdmNode result = pipeline.readFrom("result").read();
        if (result != null) {
            Serialization serial = pipeline.getSerialization("result");

            if (serial == null) {
                serial = new Serialization(runtime, pipeline.getNode()); // The node's a hack
                serial.setMethod(new QName("", "xhtml"));
            }
            // https://www.saxonica.com/html/documentation11/xsl-elements/output.html
            // indent
            serial.setIndent(true);

            WritableDocument wd = null;
            if (options.containsKey(_output)) {
                String filename = options.get(_output).getStringValue().getPrimitiveStringValue().toString();
                FileOutputStream outfile = new FileOutputStream(filename);
                wd = new WritableDocument(runtime, filename, serial, outfile);
                logger.info("Writing output to " + filename);
            } else {
                wd = new WritableDocument(runtime, null, serial);
            }

            wd.write(result);
        }
    }

    private class DocBookResolver implements CatalogResolver {
        URIResolver nextResolver = null;
        CatalogResolver resolver = null;

        DocBookResolver(URIResolver resolver, CatalogResolver catalogResolver) {
            nextResolver = resolver;
            this.resolver = catalogResolver;
        }

        @Override
        public Source resolve(String href, String base) {
            // We go first
            Source src = null;
            try {
                src = resolver.resolve(href, base);
            } catch (CatalogException e) {
                // do nothing
            }
            if (src == null) {
                try {
                    return nextResolver.resolve(href, base);
                } catch (TransformerException e) {
                    throw new IllegalArgumentException(e);
                }
            } else {
                return src;
            }
        }

        @Override
        public InputSource resolveEntity(String publicId, String systemId) {
            return resolver.resolveEntity(publicId, systemId);
        }

        @Override
        public InputStream resolveEntity(String publicId, String systemId, String baseUri, String namespace) {
            return resolver.resolveEntity(publicId, systemId, baseUri, namespace);
        }

        @Override
        public LSInput resolveResource(String type, String namespaceUri, String publicId, String systemId, String baseUri) {
            return resolver.resolveResource(type, namespaceUri, publicId, systemId, baseUri);
        }
    }
}
