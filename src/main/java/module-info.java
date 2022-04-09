open module com.github.aanno.dbtoolchain {

    // needed for awt
    requires java.desktop;
    requires java.xml;
    requires jdk.xml.dom;

    // requires jdk.xml.dom;
    // requires xercesImpl;
    requires xerces.stripped;

    requires Saxon.HE;
    requires jingtrang;
    requires com.helger.schematron;
    requires com.helger.schematron.pure;
    requires com.helger.commons;
    requires xmlcalabash;
    requires prince.java.wrapper;

    requires asciidoctorj.api;
    requires asciidoctorj;
    // requires asciidoctorj.pdf;
    requires docbook.xslt2;

    // fop stuff
    requires fop;
    // requires fop.pdf.images;
    requires commons.io;

    // requires batik.all;
    requires batik.all;
    requires xml.apis.ext;

    // requires avalon.framework.impl;
    // requires avalon.framework.api;
    // fop -> batik
    // requires batik.all.stripped;
    // requires xml.apis.ext;
    // requires xml.apis.stripped;

    // jruby stuff
    // requires org.jruby;
    requires org.jruby.jcodings;
    requires org.jruby.joni;

    // sun.misc.Unsafe (from jruby)
    requires jdk.unsupported;
    // more jruby
    requires com.headius.invokebinder;
    // requires jnr.posix;
    // requires jnrchannels;
    // requires jnr.enxio;
    // requires jnr.unixsocket;
    // requires jnr.constants;

    // requires slf4j.api;
    requires info.picocli;
    // only api
    requires org.slf4j;
    requires org.apache.logging.log4j;

    // exports

    exports com.github.aanno.dbtoolchain.cli;
    exports com.github.aanno.dbtoolchain.pipeline;
    exports com.github.aanno.dbtoolchain;

    exports com.xmlcalabash.util.print;
    exports com.xmlcalabash.extensions.math_to_svg;
    exports com.xmlcalabash.extensions.xslthl;

    // temporary (for original version of org.docbook.Main)
    requires commons.cli;

    // ?test?
    // requires org.testng;
}
