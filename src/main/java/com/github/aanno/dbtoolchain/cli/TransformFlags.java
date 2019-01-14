package com.github.aanno.dbtoolchain.cli;

import picocli.CommandLine;

import java.nio.file.Path;

import static picocli.CommandLine.*;


@Command(name = "transform", aliases = {"build", "convert"}, description = "transform input file to output file")
public class TransformFlags {

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
}
