package com.github.aanno.dbtoolchain.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.StringJoiner;
import java.util.concurrent.Callable;

import static picocli.CommandLine.*;

@Command(description = "common flags",
        subcommands = {TransformCommand.class, ListCommand.class})
public class CommonFlags implements Callable<Object> {

    private static final Logger LOG = LoggerFactory.getLogger("CommonFlags");

    @Option(names = {"-v", "--verbose"}, description = "be verbose", defaultValue = "false", required = false)
    public boolean verbose = false;

    @Override
    public Object call() throws Exception {
        LOG.warn("call");
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", CommonFlags.class.getSimpleName() + "[", "]")
                .add("verbose=" + verbose)
                .toString();
    }
}
