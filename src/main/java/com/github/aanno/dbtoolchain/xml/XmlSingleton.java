package com.github.aanno.dbtoolchain.xml;

import org.xmlresolver.Catalog;
import org.xmlresolver.CatalogSource;

import javax.xml.transform.Source;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Vector;

public class XmlSingleton {

    private static final Path catalogPath = Paths.get("schema/5.1/schemas/catalog.xml");

    private static XmlSingleton INSTANCE = new XmlSingleton();

    private final TraxSingleton traxSingleton;

    private final Properties resolverProperties;

    private final org.xmlresolver.Configuration catalogConfig;

    private final Catalog catalog;

    private XmlSingleton() {
        traxSingleton = TraxSingleton.getInstance();

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
    }

    public static XmlSingleton getInstance() {
        return INSTANCE;
    }

    public Source lookupURI(String uri, boolean validating) throws IOException {
        return traxSingleton.getSource(catalog.lookupURI(uri).body(), validating);
    }

}
