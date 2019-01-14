package com.github.aanno.dbtoolchain.cli;

import java.util.Arrays;
import java.util.List;

public enum EFileType {

    AD(new String[] {"adoc", "ad", "asciidoc", "asiciidoctor"}),
    DB(new String[] {"db.xml", "db", "docbook"}),
    FO(new String[] {"fo.xml", "fo", "xsl-fo"}),
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
}
