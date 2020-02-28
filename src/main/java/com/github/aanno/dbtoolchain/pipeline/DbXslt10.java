package com.github.aanno.dbtoolchain.pipeline;

import com.github.aanno.dbtoolchain.cli.EFileType;
import com.github.aanno.dbtoolchain.cli.TransformCommand;
import com.github.aanno.dbtoolchain.xml.S9ApiUtils;
import com.github.aanno.dbtoolchain.xml.TraxSingleton;
import net.sf.saxon.s9api.SaxonApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;

import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class DbXslt10 implements IPipeline {

    private static final Logger LOG = LoggerFactory.getLogger("DbXslt10");

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private static final String[] VARIANTS = new String[]{
            "html"
    };

    private static final String[] PATHS = new String[] {
            "lib/docbook-xsl-1.79.2/html/docbook.xsl"
    };

    private static final Map<String, Path> variant2Stylesheet = new HashMap<>();

    public static String[] getVariants() {
        return VARIANTS;
    }

    static {
        for (int i = 0; i < VARIANTS.length; ++i) {
            String v = VARIANTS[i];
            String p = PATHS[i];
            Path path = Path.of(p);
            if (!path.toFile().isFile()) {
                throw new ExceptionInInitializerError();
            }
            variant2Stylesheet.put(v, path);
        }
    }

    private final TraxSingleton traxSingleton = TraxSingleton.getInstance();

    private final String variant;

    private FoNg fo;

    public DbXslt10(String variant) {
        this.variant = variant;
    }

    @Override
    public String getName() {
        return "DbXslt10";
    }

    @Override
    public String getDescription() {
        return "docbook xslt10 based transform";
    }

    @Override
    public IStage process(TransformCommand command, IStage current, IStage finish) throws IOException {
        try {
            while (current.getType() != finish.getType()) {
                IStage old = current;
                if (EFileType.DB == current.getType() && "html".equals(variant)) {
                    Path stylesheet = variant2Stylesheet.get(variant);
                    StreamSource inStyle = new StreamSource(stylesheet.toFile());
                    StreamSource inStage = new StreamSource(current.getPath().toFile());
                    StreamResult out = new StreamResult(finish.getPath().toFile());
                    try {
                        traxSingleton.transform(inStyle, inStage, out);
                    } finally {
                        traxSingleton.close(out);
                        traxSingleton.close(inStyle);
                        traxSingleton.close(inStage);
                    }
                    current = Stage.from(command, EFileType.XHTML);
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
        return new StringJoiner(", ", DbXslt10.class.getSimpleName() + "[", "]")
                .add("variant='" + variant + "'")
                .add("fo=" + fo)
                .toString();
    }
}
