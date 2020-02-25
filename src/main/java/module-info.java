module com.github.aanno.dbtoolchain {

    // needed for awt
    requires java.desktop;
    requires java.xml;
    requires jdk.xml.dom;

    // requires jdk.xml.dom;
    // requires xercesImpl;
    requires xerces.stripped;

    // requires xmlresolver;
    requires xml.resolver;
    requires Saxon.HE;
    requires jingtrang;
    requires com.helger.schematron;
    requires xmlcalabash;
    requires prince;

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
    requires jnr.posix;
    // requires jnrchannels;
    // requires jnr.enxio;
    // requires jnr.unixsocket;
    requires jnr.constants;

    // requires slf4j.api;
    requires info.picocli;
    requires org.slf4j;

    // exports

    exports com.github.aanno.dbtoolchain.cli;
    exports com.xmlcalabash.util.print;
}
