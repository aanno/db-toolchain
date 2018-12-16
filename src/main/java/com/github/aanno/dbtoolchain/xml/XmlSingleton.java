package com.github.aanno.dbtoolchain.xml;

import com.helger.schematron.ISchematronResource;
import com.helger.schematron.pure.SchematronResourcePure;
import org.xml.sax.SAXException;
import org.xmlresolver.Catalog;
import org.xmlresolver.CatalogSource;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Vector;

public class XmlSingleton {

    private static final Path catalogPath = Paths.get("schema/5.1/schemas/catalog.xml");

    private static XmlSingleton INSTANCE = new XmlSingleton();

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
        return SchematronResourcePure.fromFile(path.toString());
    }

}
