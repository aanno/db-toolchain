package com.github.aanno.dbtoolchain;

import com.github.aanno.dbtoolchain.cli.*;
import com.github.aanno.dbtoolchain.pipeline.*;
import com.github.aanno.dbtoolchain.xml.XmlSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.css.DOMImplementationCSS;
import picocli.CommandLine;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static picocli.CommandLine.RunAll;

public class App {

    private static final Logger LOG = LoggerFactory.getLogger("App");

    // site effect for set system properties (tp)
    private XmlSingleton xmlSingleton = XmlSingleton.getInstance();

    private CommonFlags commonFlags;

    public App() {
    }

    public void process(List<Object> list) throws Exception {
        if (list != null) {
            LOG.warn("parsed args: " + list.toString());
            Iterator<Object> it = list.iterator();
            if (it.hasNext()) {
                commonFlags = (CommonFlags) it.next();
                if (!it.hasNext()) {
                    handleNoSubcommand();
                }
                while (it.hasNext()) {
                    Object command = it.next();
                    if (command != null) {
                        Class<?> clazz = command.getClass();
                        if (TransformCommand.class.equals(clazz)) {
                            transform((TransformCommand) command);
                        } else if (ListCommand.class.equals(clazz)) {
                            list((ListCommand) command);
                        }
                    } else {
                        handleNoSubcommand();
                    }
                }
            } else {
                handleNoSubcommand();
            }
        }
    }

    private void handleNoSubcommand() {
        LOG.error("No subcommand given. Examples: list, transform");
    }

    private void transform(TransformCommand transform) throws Exception {
        String pipeline = transform.pipeline;
        IStage result;
        if (pipeline.startsWith("xsl10-")) {
            result = null;
            int index = pipeline.indexOf("-");
            String variant = pipeline.substring(index + 1);
            DbXslt10a p;
            if (variant != null) {
                p = new DbXslt10a(variant);
            } else {
                throw new IllegalArgumentException("xsl10 is only for html");
            }
            // result = p.process(transform);
            result = convertAdToDbIfNeeded(transform, p);
        } else if (pipeline.startsWith("xsl20")) {
            DbXslt20Ng p;
            if (pipeline.contains("css")) {
                p = new DbXslt20Ng("css");
            } else if (pipeline.contains("fo")){
                p = new DbXslt20Ng("fo");
            } else {
                throw new IllegalArgumentException();
            }
            result = convertAdToDbIfNeeded(transform, p);
        } else if (pipeline.startsWith("ascii") || pipeline.startsWith("ad")) {
            AsciidoctorJ ad = new AsciidoctorJ();
            LOG.warn("pipeline: " + ad);
            result = ad.process(transform);
        } else if (pipeline.startsWith("fo")) {
            FoNg p = new FoNg();
            LOG.warn("pipeline: " + p);
            result = p.process(transform);
        } else {
            throw new IllegalArgumentException("unknown pipeline: " + pipeline);
        }
        LOG.warn("result stage: " + result);
    }

    private IStage convertAdToDbIfNeeded(TransformCommand transform, IPipeline p) throws IOException, ReflectiveOperationException {
        IStage result;
        LOG.warn("pipeline: " + p);
        if (EFileType.AD == transform.inFormat) {
            // If input is asciidoc(tor), first convert it to docbook ...
            AsciidoctorJ aPipeline = new AsciidoctorJ();
            result = aPipeline.process(transform, Stage.fromIn(transform),
                    Stage.from(transform, EFileType.DB));
            // ... and then proceed as normal
            result = p.process(transform, result, Stage.fromOut(transform));
        } else {
            result = p.process(transform);
        }
        return result;
    }

    private void list(ListCommand list) throws Exception {
        String[] pipelines = new String[]{"xsl-css", "xsl-fo", "ad", "fo"};
        LOG.warn("implemented pipelines:");
        for (String p : pipelines) {
            LOG.warn("\t* " + p);
        }
    }

    public static void main(String[] args) throws Exception {
        DOMImplementationCSS dummy = null;
        LOG.warn("raw args: " + Arrays.asList(args));
        App dut = new App();
        CommandLine cl = new Parser().build();
        args = CliUtils.stripVmArgs(args, App.class);
        LOG.warn("stripped args: " + Arrays.asList(args));
        dut.process(cl.parseWithHandler(new RunAll(), args));
    }
}
