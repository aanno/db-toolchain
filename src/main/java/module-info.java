module com.github.aanno.dbtoolchain {

    requires java.xml;
    requires com.helger.schematron;
    requires prince;

    requires asciidoctorj.api;
    requires asciidoctorj;

    // fop stuff
    requires fop;
    requires fop.pdf.images;
    requires commons.io;
    // requires avalon.framework.impl;
    requires avalon.framework.api;

    // jruby stuff
    requires org.jruby;
    requires org.jruby.jcodings;
    requires org.jruby.joni;
    // sun.misc.Unsafe (from jruby)
    requires jdk.unsupported;
    // more jruby
    requires com.headius.invokebinder;
}
