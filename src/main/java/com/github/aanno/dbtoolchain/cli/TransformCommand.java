package com.github.aanno.dbtoolchain.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.nio.file.Path;
import java.util.StringJoiner;
import java.util.concurrent.Callable;

import static picocli.CommandLine.*;


@Command(name = "transform", aliases = {"build", "convert"}, description = "transform input file to output file")
public class TransformCommand implements Callable<Object> {

    private static final Logger LOG = LoggerFactory.getLogger("TransformCommand");

    @Option(names = {"-i", "--in"}, required = true)
    public Path in;

    @Option(names = {"-o", "--out"})
    public Path out;

    @Option(names = {"-if", "--informat"})
    public EFileType inFormat;

    @Option(names = {"-of", "--outformat"})
    public EFileType outFormat;

    @Option(names = {"-p", "--pipeline"})
    public String pipeline;

    @Option(names = {"-c", "--check", "--validate"})
    public boolean validate = true;

    @Override
    public Object call() throws Exception {
        in = in.toAbsolutePath();
        String inName = in.getFileName().toString();
        LOG.warn("call");
        if (inFormat == null) {
            inFormat = EFileType.getType(inName);
        }
        if (inFormat == null) {
            throw new IllegalArgumentException("informat unknown");
        }
        String inBasename = EFileType.getBasename(inName);
        if (out == null) {
            if (outFormat == null) {
                // default out format
                outFormat = EFileType.PDF;
            }
            out = in.getParent().resolve(inBasename + "." + outFormat.getDefaultExtension());
        }
        if (outFormat == null) {
            outFormat = EFileType.getType(out.getFileName().toString());
        }
        if (outFormat == null) {
            throw new IllegalArgumentException("outformat unknown");
        }
        out = out.toAbsolutePath();
        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TransformCommand.class.getSimpleName() + "[", "]")
                .add("in=" + in)
                .add("out=" + out)
                .add("inFormat=" + inFormat)
                .add("outFormat=" + outFormat)
                .add("pipeline='" + pipeline + "'")
                .add("validate=" + validate)
                .toString();
    }
}
