package com.github.aanno.dbtoolchain;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.asciidoctor.Asciidoctor.Factory.create;
import static org.asciidoctor.OptionsBuilder.options;

public class AsciidoctorjExample {

    private static final Logger LOG = LogManager.getLogger("AsciidoctorjExample");

    private Asciidoctor asciidoctor = create();

    private Map<String, Object> options;

    public AsciidoctorjExample() {
        options = options().safe(SafeMode.SAFE).toFile(new File("out.fo.xml")).asMap();
        options.put(Options.BACKEND, "docbook5");
    }

    /**
     * TODO tp: no doc type...
     */
    public void convert(Path in, Path out) throws IOException {
        try (Reader reader = Files.newBufferedReader(in);
             Writer writer = Files.newBufferedWriter(out)) {
            asciidoctor.convert(reader, writer, options);
        }
    }

    public void convert2(Path in, Path out) throws IOException {
        Map<String, Object> opts = new HashMap<>(options);
        // opts.put(Options.TO_FILE, "false");
        // opts.put(Options.TO_DIR, ".");
        // opts.put(Options.BASEDIR, ".");
        String result = asciidoctor.convertFile(in.toFile(), opts);
        LOG.warn("ad options: ", opts);
        LOG.warn("ad convertFile result: ", result);
    }

    public static void main(String[] args) throws Exception {
        Path test = Paths.get("submodules/asciidoctor.org/README.adoc");
        Path out = Paths.get("out.fo.xml");

        AsciidoctorjExample adj = new AsciidoctorjExample();
        adj.convert2(test, out);
        LOG.warn("asciidoctorj from " + test + " to " + out);
    }

}
