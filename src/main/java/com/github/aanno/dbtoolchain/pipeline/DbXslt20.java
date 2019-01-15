package com.github.aanno.dbtoolchain.pipeline;

import com.github.aanno.dbtoolchain.cli.EFileType;
import com.github.aanno.dbtoolchain.cli.TransformCommand;
import com.github.aanno.dbtoolchain.xml.S9ApiUtils;
import net.sf.saxon.s9api.SaxonApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class DbXslt20 implements IPipeline {

    private static final Logger LOG = LoggerFactory.getLogger("DbXslt20");

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final String[] VARIANTS = new String[]{
            "css", "fo"
    };

    public static String[] getVariants() {
        return VARIANTS;
    }

    private final String variant;

    private Fo fo;

    public DbXslt20(String variant) {
        this.variant = variant;
    }

    @Override
    public String getName() {
        return "DbXslt20";
    }

    @Override
    public String getDescription() {
        return "docbook xslt20 based transform";
    }

    @Override
    public IStage process(TransformCommand command, IStage current, IStage finish) throws IOException {
        try {
            while (current.getType() != finish.getType()) {
                IStage old = current;
                if (EFileType.DB == current.getType()) {
                    current = processDbXml(command, current);
                }
                if (EFileType.FO == current.getType()) {
                    current = processFo(command, current, finish);
                }
                if (old.getType() == current.getType()) {
                    throw new IllegalArgumentException("get stocked on " + old + " and " + current);
                }
            }
            return current;
        } catch (SaxonApiException e) {
            throw new IOException(e);
        }
    }

    private IStage processDbXml(TransformCommand command, IStage current) throws IOException, SaxonApiException {
        if (EFileType.DB != current.getType()) {
            throw new IllegalArgumentException();
        }
        IStage result;
        List<String> args = new ArrayList<>();

        if ("css".equals(variant)) {
            String css = S9ApiUtils.getDefaultCss().toAbsolutePath().toString();
            result = Stage.from(command, EFileType.XHTML);

            args.add("-f");
            args.add("cssprint");
            args.add("-o");
            args.add(result.getPath().toString());
            args.add("--css");
            args.add(css);
            args.add(current.getPath().toString());
        } else if ("fo".equals(variant)) {
            result = Stage.from(command, EFileType.FO);

            args.add("-f");
            args.add("fo");
            args.add("-o");
            args.add(result.getPath().toString());
            args.add(current.getPath().toString());
        } else {
            throw new IllegalStateException("unknown variant: " + variant);
        }
        LOG.warn("xslt20 args: " + args);
        org.docbook.Main.main(args.toArray(EMPTY_STRING_ARRAY));

        return result;
    }

    private IStage processFo(TransformCommand command, IStage current, IStage finish) {
        return getFo().process(command, current, finish);
    }

    private Fo getFo() {
        if (fo == null) {
            fo = new Fo();
        }
        return fo;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", DbXslt20.class.getSimpleName() + "[", "]")
                .add("variant='" + variant + "'")
                .add("fo=" + fo)
                .toString();
    }
}
