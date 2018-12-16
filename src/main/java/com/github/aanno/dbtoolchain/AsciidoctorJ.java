package com.github.aanno.dbtoolchain;

import static org.asciidoctor.Asciidoctor.Factory.create;
import static org.asciidoctor.OptionsBuilder.options;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.SafeMode;

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
options = options().safe(SafeMode.SAFE).asMap();
options.put("backend", "docbook5");
    }

    public void convert(Path in, Path out) throws IOException {
        try (Reader reader = Files.newBufferedReader(in);
        Writer writer = Files.newBufferedWriter(out)) {
            asciidoctor.convert(reader, writer, options);
        }
    }

    public static void main(String[] args) throws Exception {
        Path test = Paths.get("asciidoctor.org/README.adoc");
        Path out = Paths.get("out.fo.xml");

        AsciidoctorJ adj = new AsciidoctorJ();
        adj.convert(test, out);
        System.out.println("asciidoctorj from " + test + " to " + out);
    }

}
