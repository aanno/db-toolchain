package com.github.aanno.dbtoolchain.xml;


import net.sf.saxon.Configuration;

public class S9ApiUtils {

    private S9ApiUtils() {
        // Never invoked
    }

    public static Configuration getConfiguration() {
        Configuration result = new Configuration();
        return result;
    }
}
