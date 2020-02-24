package com.github.aanno.dbtoolchain.cli;

import picocli.CommandLine;

import static picocli.CommandLine.IFactory;
import static picocli.CommandLine.ITypeConverter;

public class Parser {

    public Parser() {
    }

    public CommandLine build() {
        CommandLine result = new CommandLine(new CommonFlags(), new Factory());
        // result.addSubcommand("transform", new TransformCommand());
        return result;
    }

    private static class Factory implements IFactory {

        @Override
        public <K> K create(Class<K> cls) throws Exception {
            /*
            if (EFileType.class.equals(cls)) {

            }
             */
            return cls.getDeclaredConstructor().newInstance();
        }
    }

    public static class EFileTypeConverter implements ITypeConverter<EFileType> {

        @Override
        public EFileType convert(String filename) throws Exception {
            if (filename == null) {
                return null;
            }
            EFileType result = EFileType.getType(filename);
            if (result == null) {
                throw new IllegalArgumentException("Not of a known file type: " + filename);
            }
            return result;
        }
    }
}
