package com.github.aanno.dbtoolchain.cli;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.util.StringJoiner;
import java.util.concurrent.Callable;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;

@Command(description = "common flags",
        subcommands = {TransformCommand.class, ListCommand.class})
public class CommonFlags implements Callable<Object> {

    private static final Logger LOG = LogManager.getLogger("CommonFlags");

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
