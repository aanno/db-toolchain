package com.github.aanno.dbtoolchain.xml;


import net.sf.saxon.Configuration;

import java.nio.file.Path;
import java.nio.file.Paths;

public class S9ApiUtils {

    private S9ApiUtils() {
        // Never invoked
    }

    public static Configuration getConfiguration() {
        Configuration result = new Configuration();
        return result;
    }

    public static Path getDb2FoPath() {
        return Paths.get("xslt20-stylesheets/build/xslt/base/pipelines/db2fo.xpl");
    }

    public static Path getDocbookPath() {
        return Paths.get("xslt20-stylesheets/build/xslt/base/pipelines/docbook.xpl");
    }

}
