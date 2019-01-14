package com.github.aanno.dbtoolchain.cli;

import picocli.CommandLine;

import static picocli.CommandLine.*;

public class Parser {

    public Parser() {
    }

    public CommandLine build() {
        CommandLine result = new CommandLine(new CommonFlags(), new Factory());
        // result.addSubcommand("transform", new TransformFlags());
        return result;
    }

    private static class Factory implements IFactory {

        @Override
        public <K> K create(Class<K> cls) throws Exception {
            return cls.getDeclaredConstructor(null).newInstance();
        }
    }
}
