package com.github.aanno.dbtoolchain.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.concurrent.Callable;

import static picocli.CommandLine.*;


@Command(name = "transform", aliases = {"build", "convert"}, description = "transform input file to output file")
public class TransformFlags implements Callable<Object> {

    private static final Logger LOG = LoggerFactory.getLogger("TransformFlags");

    @Option(names = {"-i", "--in"}, required = true)
    public Path in;

    @Option(names = {"-o", "--out"}, required = true)
    public Path out;

    @Option(names = {"-if", "--informat"})
    public String inFormat;

    @Option(names = {"-of", "--outformat"})
    public String outFormat;

    @Option(names = {"-p", "--pipeline"})
    public String pipeline;

    @Option(names = {"-c", "--check", "--validate"})
    public boolean validate = true;

    @Override
    public Object call() throws Exception {
        LOG.warn("call");
        return this;
    }
}
