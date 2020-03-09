package com.github.aanno.dbtoolchain.xml;

import com.helger.schematron.ISchematronResource;
import com.helger.schematron.pure.SchematronResourcePure;
import com.helger.schematron.xpath.IXPathConfig;
import com.helger.schematron.xpath.XPathConfigBuilder;
import com.helger.commons.io.resource.FileSystemResource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.xpath.XPathFactoryConfigurationException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class XmlSingleton {

    private static final Path catalogPath = Paths.get("schema/5.1/schemas/catalog.xml");

    private static XmlSingleton INSTANCE = new XmlSingleton();

    private static IXPathConfig xPathConfig = null;

    static {
        try {
            xPathConfig = new XPathConfigBuilder()
                    // .setXPathFactoryClass(com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl.class)
                    .setGlobalXPathFactory("com.sun.org.apache.xpath.internal.jaxp.XPathFactoryImpl")
                    .build();
        } catch (XPathFactoryConfigurationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private final TraxSingleton traxSingleton;

    private XmlSingleton() {
        traxSingleton = TraxSingleton.getInstance();

        /*
        resolverProperties = new Properties();
        resolverProperties.setProperty("relative-catalogs", "yes");
        catalogConfig = new org.xmlresolver.Configuration(resolverProperties, null);
        Vector<CatalogSource> list = new Vector<>();
        try {
            list.add(new CatalogSource.InputSourceCatalogSource(traxSingleton.getSAXInputSource(catalogPath)));
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
        catalog = new Catalog(catalogConfig, list);
         */
    }

    public static XmlSingleton getInstance() {
        return INSTANCE;
    }

    public Schema getRelaxNgSchema(Path path) throws IOException {
        try {
            SchemaFactory relaxFactory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);
            relaxFactory.setProperty("http://relaxng.org/properties/datatype-library-factory",
                    new org.relaxng.datatype.helpers.DatatypeLibraryLoader());
            Schema result = relaxFactory.newSchema(traxSingleton.getSource(path, false));
            return result;
        } catch (SAXException e) {
            throw new IOException(e);
        }
    }

    public ISchematronResource getSchematronResource(Path path) {
        // ISchematronResource result = SchematronResourcePure.fromFile(path.toString());
        SchematronResourcePure result = new SchematronResourcePure(new FileSystemResource(path.toString()), true);
        result.setXPathConfig(xPathConfig);
        if (!result.isValidSchematron()) {
            throw new IllegalArgumentException("Invalid Schematron!");
        }
        return result;
    }

}
