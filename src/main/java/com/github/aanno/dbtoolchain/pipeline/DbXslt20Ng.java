package com.github.aanno.dbtoolchain.pipeline;

import com.github.aanno.dbtoolchain.cli.EFileType;
import com.github.aanno.dbtoolchain.cli.TransformCommand;
import com.github.aanno.dbtoolchain.org.docbook.Main;
import com.github.aanno.dbtoolchain.org.docbook.XSLT20;
import com.github.aanno.dbtoolchain.xml.S9ApiUtils;
import com.github.aanno.dbtoolchain.xml.TraxSingleton;
import net.sf.saxon.s9api.SaxonApiException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class DbXslt20Ng implements IPipeline {

    private static final Logger LOG = LogManager.getLogger("DbXslt20Ng");

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final String[] VARIANTS = new String[]{
            "css", "fo"
    };

    public static String[] getVariants() {
        return VARIANTS;
    }

    private final String variant;

    private FoNg fo;

    public DbXslt20Ng(String variant) {
        this.variant = variant;
    }

    @Override
    public String getName() {
        return "DbXslt20Ng";
    }

    @Override
    public String getDescription() {
        return "xslt20 based transform on arbitrary XPL";
    }

    @Override
    public IStage process(TransformCommand command, IStage current, IStage finish) throws IOException {
        try {
            while (current.getType() != finish.getType()) {
                IStage old = current;
                if (EFileType.DB == current.getType()) {
                    if (command.princeApi) {
                        current = processDbXmlByApi(command, current, finish);
                    } else {
                        current = processDbXmlByXhmtl(command, current, finish);
                    }
                } else if (EFileType.FO == current.getType()) {
                    current = processFo(command, current, finish);
                } else if (EFileType.HTML5 == current.getType() || EFileType.XHTML == current.getType()) {
                    current = processXhtmlByPrinceProcess(command, current, finish);
                }
                if (old.getType() == current.getType()) {
                    throw new IllegalArgumentException("get stuck on " + old + " and " + current);
                }
            }
            return current;
        } catch (SaxonApiException e) {
            throw new IOException(e);
        } catch (InterruptedException e) {
            throw new IOException(e);
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    private IStage processDbXmlByXhmtl(TransformCommand command, IStage current, IStage finish) throws IOException, SaxonApiException {
        if (EFileType.DB != current.getType()) {
            throw new IllegalArgumentException();
        }
        IStage result;
        String format, output, xpl;

        if ("css".equals(variant) || EFileType.HTML5 == finish.getType() || EFileType.XHTML == finish.getType()) {
            result = Stage.from(command, finish.getType());

            format = getHtmlFormat(finish.getType());
            output = result.getPath().toString();
            // ???
            // xpl = "db2xhtml.xpl";
            // own pipeline of db-toolchain*.jar
            xpl = "docbook-xsl20/db2html-db-toolchain.xpl";
            // xpl = "/mnt/home/tpasch/scm/aanno/db-toolchain/src/main/resources/docbook-xsl20/db2html-db-toolchain.xpl";
        } else if ("fo".equals(variant)) {
            result = Stage.from(command, EFileType.FO);

            format = "fo";
            output = result.getPath().toString();
            xpl = "xslt/base/pipelines/db2fo.xpl";
        } else {
            throw new IllegalStateException("unknown variant: " + variant);
        }
        XSLT20 xslt20 = new XSLT20();
        if (!Path.of(xpl).isAbsolute()) {
            xpl = S9ApiUtils.getResource(xpl).toExternalForm();
        }
        LOG.warn("xslt20 format: " + format + " output: " + output + " xpl: " + xpl);
        xslt20.setOption("format", format);
        xslt20.setOption("output", output);
        xslt20.runXpl(current.getPath().toString(), xpl, command.workDir.toString());

        return result;
    }

    private IStage processDbXmlByApi(TransformCommand command, IStage current, IStage finish)
            throws IOException, SaxonApiException, URISyntaxException {
        if (EFileType.DB != current.getType()) {
            throw new IllegalArgumentException();
        }
        IStage result;
        String format, output, xpl, css;

        if ("css".equals(variant)) {
            result = Stage.from(command, EFileType.PDF);

            css = Path.of(S9ApiUtils.getDefaultCss().toURI()).toAbsolutePath().toString();
            format = "cssprint";
            output = result.getPath().toString();
            xpl = "db2pdf.xpl";
        } else if ("fo".equals(variant)) {
            throw new IllegalStateException("processDbXmlByApi not available for  variant: " + variant);
        } else {
            throw new IllegalStateException("unknown variant: " + variant);
        }
        XSLT20 xslt20 = new XSLT20();
        if (!Path.of(xpl).isAbsolute()) {
            xpl = S9ApiUtils.getResource(xpl).toExternalForm();
        }
        LOG.warn("xslt20 format: " + format + " output: " + output + " xpl: " + xpl + " css: " + css);
        xslt20.setOption("format", format);
        xslt20.setOption("output", output);
        xslt20.setOption("css", css);
        xslt20.runXpl(current.getPath().toString(), xpl, command.workDir.toString());

        return result;
    }

    private String getHtmlFormat(EFileType type) {
        final String result;
        if (type == EFileType.XHTML) {
            result = "xhtml";
        } else if (type == EFileType.HTML5) {
            result = "html";
        } else {
            throw new IllegalArgumentException(type.toString());
        }
        return result;
    }

    private IStage processXhtmlByPrinceProcess(TransformCommand command, IStage current, IStage finish)
            throws IOException, InterruptedException, URISyntaxException {
        IStage result = Stage.from(command, EFileType.PDF);
        Path log = command.workDir.resolve("prince.log");
        String css = Path.of(S9ApiUtils.getDefaultCss().toURI()).toAbsolutePath().toString();
        List<String> args = new ArrayList<>();

        args.add("prince");
        args.add("-s");
        args.add(css);
        args.add("--page-size");
        args.add("A4");
        args.add("-o");
        args.add(result.getPath().toString());
        args.add(current.getPath().toString());

        ProcessBuilder pb = new ProcessBuilder(args);
        pb.redirectErrorStream(true);
        pb.redirectOutput(log.toFile());
        pb.directory(command.workDir.toFile());
        LOG.warn("start process: " + args);

        int exitCode = pb.start().waitFor();
        if (exitCode != 0) {
            LOG.warn("cssprint with prince terminated with error " + exitCode);
        }
        return result;
    }

    private IStage processFo(TransformCommand command, IStage current, IStage finish) throws IOException {
        return getFo().process(command, current, finish);
    }

    private FoNg getFo() {
        if (fo == null) {
            fo = new FoNg();
        }
        return fo;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DbXslt20Ng.class.getSimpleName() + "[", "]")
                .add("variant='" + variant + "'")
                .add("fo=" + fo)
                .toString();
    }
}
