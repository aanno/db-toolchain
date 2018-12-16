package com.github.aanno.dbtoolchain;

import com.github.aanno.dbtoolchain.xml.TraxSingleton;
import com.helger.schematron.ISchematronResource;
import com.helger.schematron.pure.SchematronResourcePure;
import com.thaiopensource.relaxng.jaxp.XMLSyntaxSchemaFactory;
import org.xmlresolver.Catalog;
import org.xmlresolver.CatalogSource;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Vector;

public class Validation {

    public Validation() {

    }

    public static void main(String[] args) throws Exception {
        // TODO tp:
        System.setProperty(SchemaFactory.class.getName() + ":" + XMLConstants.RELAXNG_NS_URI,
                XMLSyntaxSchemaFactory.class.getName());

        TraxSingleton traxSingleton = TraxSingleton.getInstance();

        Path catalogPath = Paths.get("schema/5.1/schemas/catalog.xml");
        Path dbRelaxPath = Paths.get("schema/5.1/schemas/rng/docbookxi.rng");
        Path dbSchematronPath = Paths.get("schema/5.1/schemas/sch/docbookxi.sch");
        Path dbPath = Paths.get("examples/db/transition/howto.xml");

        org.xmlresolver.Configuration config = new org.xmlresolver.Configuration(null, null);
        Vector<CatalogSource> list = new Vector<>();
        list.add(new CatalogSource.InputSourceCatalogSource(traxSingleton.getSAXInputSource(catalogPath)));
        Catalog catalog = new Catalog(config, list);

        SchemaFactory relaxFactory = SchemaFactory.newInstance(XMLConstants.RELAXNG_NS_URI);
        relaxFactory.setProperty("http://relaxng.org/properties/datatype-library-factory",
                new org.relaxng.datatype.helpers.DatatypeLibraryLoader());
        Schema dbRelax = relaxFactory.newSchema(traxSingleton.getSource(dbRelaxPath, false));

        Validator validator = dbRelax.newValidator();
        validator.validate(traxSingleton.getSource(dbPath, false));

        ISchematronResource aResPure = SchematronResourcePure.fromFile(dbSchematronPath.toFile());
        if (!aResPure.isValidSchematron()) {
            throw new IllegalArgumentException("Invalid Schematron!");
        }
        if (!aResPure.getSchematronValidity(traxSingleton.getStreamSource(dbPath)).isValid ()) {
            throw new IllegalArgumentException("Validation against Schematron failed: " + dbPath);
        }
    }
}
