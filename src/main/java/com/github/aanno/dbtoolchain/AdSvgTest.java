package com.github.aanno.dbtoolchain;

import static org.asciidoctor.Asciidoctor.Factory.create;
import static org.asciidoctor.OptionsBuilder.options;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.SafeMode;

public class AdSvgTest {

    private Asciidoctor asciidoctor = create();

    private Map<String, Object> options;

    public AdSvgTest() {
options = options().safe(SafeMode.SAFE).asMap();
        // .toFile(new File("out.fo.xml")).asMap();
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
        opts.put(Options.TO_FILE, out.toString());
        // opts.put(Options.TO_DIR, ".");
        // opts.put(Options.BASEDIR, ".");

        // Map<String, Object> opts = options().safe(SafeMode.SAFE).toFile(out.toFile()).asMap();

        String result = asciidoctor.convertFile(in.toFile(), opts);
        System.out.println(result);
    }

    public static void main(String[] args) throws Exception {
        // Path ad = Paths.get("src/test/resources/ad-svg/ad-svg-inline.ad");
        Path ad = Paths.get("src/test/resources/ad-svg/ad-svg-ref.ad");
        Path dbOut = Paths.get("out.db.xml");
        Path foOut = Paths.get("out.fo.xml");
        Path pdfOut = Paths.get("out.pdf");

        AdSvgTest adj = new AdSvgTest();
        adj.convert2(ad, dbOut);
        System.out.println("AdSvgTest from " + ad + " (ad-svg) to " + dbOut + " (db)");

        org.docbook.Main.main(("-f fo -o " + foOut + " " + dbOut).split("[ \t]+"));
        System.out.println("AdSvgTest from " + dbOut + " (db) to " + foOut + " (fo)");

        org.apache.fop.cli.Main.main("out.fo.xml -pdf out.pdf".split("[ \t]"));
        System.out.println("AdSvgTest from " + foOut + " (fo) to " + pdfOut + " (pdf)");
    }

}
