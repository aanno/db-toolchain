module com.github.aanno.dbtoolchain {
    requires java.xml;
    requires com.helger.schematron;
    requires prince;
    requires asciidoctorj.api;
    requires asciidoctorj;

    // jruby stuff
    requires org.jruby;
    requires org.jruby.jcodings;
    requires org.jruby.joni;
    // sun.misc.Unsafe (from jruby)
    requires jdk.unsupported;
    // more jruby
    requires com.headius.invokebinder;

    /*
    requires prince;
    requires com.helger.schematron;
    requires org.xml.sax;
    requires net.sf.saxon;
    requires org.asciidoctor;
     */
}
