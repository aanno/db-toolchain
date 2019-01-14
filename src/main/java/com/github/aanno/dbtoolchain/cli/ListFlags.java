package com.github.aanno.dbtoolchain.cli;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.concurrent.Callable;

import static picocli.CommandLine.*;

@Command(name = "list", aliases = {"show"}, description = "list/show all pipelines")
public class ListFlags implements Callable<Object> {

    private static final Logger LOG = LoggerFactory.getLogger("ListFlags");

    @Override
    public Object call() throws Exception {
        LOG.warn("call");
        return this;
    }

}
