package com.github.aanno.dbtoolchain.pipeline;

import com.github.aanno.dbtoolchain.cli.EFileType;
import com.github.aanno.dbtoolchain.cli.TransformCommand;
import com.github.aanno.dbtoolchain.xml.TraxSingleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class DbXslt10a implements IPipeline {

    private static final Logger LOG = LoggerFactory.getLogger("DbXslt10a");

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final String[] VARIANTS = new String[]{
            "html"
    };

    private static final String[] PATHS = new String[]{
            // "lib/docbook-xsl-1.79.2/html/docbook.xsl"
            "lib/docbook-xsl-1.79.2/xhtml/docbook.xsl"
    };

    private static final Map<String, Path> variant2Path = new HashMap<>();

    static {
        for (int i = 0; i < VARIANTS.length; ++i) {
            String v = VARIANTS[i];
            String p = PATHS[i];
            Path path = Path.of(p);
            if (!path.toFile().isFile()) {
                throw new ExceptionInInitializerError();
            }
            variant2Path.put(v, path);
        }
    }

    public static String[] getVariants() {
        return VARIANTS;
    }

    private TraxSingleton traxSingleton = TraxSingleton.getInstance();

    private final String variant;

    private Fo fo;

    public DbXslt10a(String variant) {
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
                if (EFileType.DB == current.getType() && EFileType.XHTML == finish.getType()) {
                    Path p = variant2Path.get(variant);
                    StreamSource stylesheet = new StreamSource(p.toFile());
                    StreamSource in = new StreamSource(current.getPath().toFile());
                    StreamResult out = new StreamResult(finish.getPath().toFile());
                    traxSingleton.transform(stylesheet, in, out);
                    current = Stage.from(command, finish.getType());
                } else {
                    throw new IllegalArgumentException();
                }
                if (old.getType() == current.getType()) {
                    throw new IllegalArgumentException("get stuck on " + old + " and " + current);
                }
            }
            return current;
        } catch (TransformerException e) {
            throw new IOException(e);
        }
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
        return new StringJoiner(", ", DbXslt10a.class.getSimpleName() + "[", "]")
                .add("variant='" + variant + "'")
                .add("fo=" + fo)
                .toString();
    }
}
