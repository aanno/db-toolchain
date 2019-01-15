package com.github.aanno.dbtoolchain.cli;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum EFileType {

    AD(new String[] {"adoc", "ad", "asciidoc", "asiciidoctor"}),
    DB(new String[] {"db.xml", "db", "docbook", "xml"}),
    FO(new String[] {"fo.xml", "fo", "xsl-fo"}),
    XHTML(new String[] {"xhtml", "html.xml"}),
    PDF(new String[] {"pdf"});

    private final List<String> extensions;

    private EFileType(List<String> extensions) {
        this.extensions = extensions;
    }

    private EFileType(String[] extensions) {
        this(Arrays.asList(extensions));
    }

    public String getDefaultExtension() {
        return extensions.get(0);
    }

    public static EFileType getType(String filename) {
        if (filename == null) {
            return null;
        }
        for (EFileType t: values()) {
            for (String ext: t.extensions) {
                if (filename.endsWith("." + ext)) {
                    return t;
                }
            }
        }
        return null;
    }

    private static final Pattern BASE_RE = Pattern.compile("(.*)\\.(.+(\\.xml)?)");

    public static String getBasename(String filename) {
        Matcher m = BASE_RE.matcher(filename);
        if (m.matches()) {
            return m.group(1);
        }
        return filename;
    }
}
