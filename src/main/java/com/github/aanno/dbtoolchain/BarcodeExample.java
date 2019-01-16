package com.github.aanno.dbtoolchain;

import com.github.aanno.dbtoolchain.xml.S9ApiUtils;
import com.github.aanno.dbtoolchain.xml.XmlSingleton;

public class BarcodeExample {

    // Don't delete. Needed to start xml processing early (tp)
    private static final XmlSingleton XML_SINGLETON = XmlSingleton.getInstance();

    public static void main(String[] args) throws Exception {
        org.apache.fop.cli.Main.main("out.fo.xml -pdf out.pdf".split("[ \t]"));
    }
}
