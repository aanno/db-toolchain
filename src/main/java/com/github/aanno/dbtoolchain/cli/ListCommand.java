package com.github.aanno.dbtoolchain.cli;


import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import picocli.CommandLine;

import java.util.concurrent.Callable;

import static picocli.CommandLine.Command;

@Command(name = "list", aliases = {"show"}, description = "list/show all pipelines")
public class ListCommand implements Callable<Object> {

    private static final Logger LOG = LogManager.getLogger("ListCommand");

    @CommandLine.Option(names = {"-h", "--help"}, usageHelp = true)
    public boolean help;

    @Override
    public Object call() throws Exception {
        LOG.warn("call");
        return this;
    }

}
