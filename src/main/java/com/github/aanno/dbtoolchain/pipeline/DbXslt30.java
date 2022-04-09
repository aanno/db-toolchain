package com.github.aanno.dbtoolchain.pipeline;

import com.github.aanno.dbtoolchain.cli.EFileType;
import com.github.aanno.dbtoolchain.cli.TransformCommand;
import com.github.aanno.dbtoolchain.xml.S9ApiUtils;
import net.sf.saxon.s9api.SaxonApiException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import org.docbook.xsltng.Main;

public class DbXslt30 implements IPipeline {

    private static final Logger LOG = LogManager.getLogger("DbXslt30");

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final String[] VARIANTS = new String[]{
            "css"
    };

    public static String[] getVariants() {
        return VARIANTS;
    }

    private final String variant;

    public DbXslt30(String variant) {
        this.variant = variant;
    }

    @Override
    public String getName() {
        return "DbXslt30";
    }

    @Override
    public String getDescription() {
        return "docbook xslt30 based transform (https://xsltng.docbook.org/)";
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
                } else if (EFileType.XHTML == current.getType()) {
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
        List<String> args = new ArrayList<>();

        if ("css".equals(variant) || EFileType.HTML5 == finish.getType()) {
            /* NOT working
            result = Stage.from(command, EFileType.PDF);

            args.add("-f");
            args.add("cssprint");
            args.add("-o");
            args.add(result.getPath().toString());
            args.add("--css");
            args.add(css);
            args.add(current.getPath().toString());
             */
            result = Stage.from(command, EFileType.HTML5);

            args.add(current.getPath().toString());
            args.add("-o:" + result.getPath().toString());
            // The --resources flag only _copies_ resources - hence it is not what we need.
            // args.add("--resources:.");
            // args.add("--resources:/mnt/home/tpasch/scm/aanno/db-toolchain");

            // Stylesheet params are simply appended (see Main for details)
            // TODO tp: make this sane
            args.add("resource-base-uri=/mnt/home/tpasch/scm/aanno/db-toolchain/lib/docbook-xslTNG/resources/");
            // output parameters starts with '!', see https://www.saxonica.com/documentation9.5/using-xsl/commandline.html
            args.add("!indent=yes");
            // debug stylesheet, https://www.saxonica.com/documentation9.5/using-xsl/commandline.html
            // args.add("-explain");
        } else {
            throw new IllegalStateException("unknown variant: " + variant);
        }
        LOG.warn("xslt30 args: " + args);
        Main.main(args.toArray(EMPTY_STRING_ARRAY));

        return result;
    }

    private IStage processDbXmlByApi(TransformCommand command, IStage current, IStage finish)
            throws IOException, SaxonApiException, URISyntaxException {
        if (EFileType.DB != current.getType()) {
            throw new IllegalArgumentException();
        }
        IStage result;
        List<String> args = new ArrayList<>();

        if ("css".equals(variant)) {
            result = Stage.from(command, EFileType.PDF);
            String css = Path.of(S9ApiUtils.getDefaultCss().toURI()).toAbsolutePath().toString();

            args.add("-f");
            args.add("cssprint");
            args.add("-o");
            args.add(result.getPath().toString());
            args.add("--css");
            args.add(css);
            args.add(current.getPath().toString());
        } else if ("fo".equals(variant)) {
            throw new IllegalStateException("processDbXmlByApi not available for  variant: " + variant);
        } else {
            throw new IllegalStateException("unknown variant: " + variant);
        }
        LOG.warn("xslt30 args: " + args);
        Main.main(args.toArray(EMPTY_STRING_ARRAY));

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

    @Override
    public String toString() {
        return new StringJoiner(", ", DbXslt30.class.getSimpleName() + "[", "]")
                .add("variant='" + variant + "'")
                .toString();
    }
}
