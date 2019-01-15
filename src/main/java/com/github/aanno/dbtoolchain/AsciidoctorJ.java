package com.github.aanno.dbtoolchain;

import static org.asciidoctor.Asciidoctor.Factory.create;
import static org.asciidoctor.api.OptionsBuilder.options;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.api.Options;
import org.asciidoctor.api.SafeMode;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class AsciidoctorJ {

    private Asciidoctor asciidoctor = create();

    private Map<String, Object> options;

    public AsciidoctorJ() {
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
        String result = asciidoctor.convertFile(in.toFile(), options);
        System.out.println(result);
    }

    public static void main(String[] args) throws Exception {
        Path test = Paths.get("submodules/asciidoctor.org/README.adoc");
        Path out = Paths.get("out.fo.xml");

        AsciidoctorJ adj = new AsciidoctorJ();
        adj.convert2(test, out);
        System.out.println("asciidoctorj from " + test + " to " + out);
    }

}
