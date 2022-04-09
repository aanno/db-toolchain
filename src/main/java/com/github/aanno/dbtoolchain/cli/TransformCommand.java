package com.github.aanno.dbtoolchain.cli;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.nio.file.Path;
import java.util.StringJoiner;
import java.util.concurrent.Callable;

import static picocli.CommandLine.Command;
import static picocli.CommandLine.Option;


@Command(name = "transform", aliases = {"build", "convert"}, description = "transform input file to output file")
public class TransformCommand implements Callable<Object> {

    private static final Logger LOG = LogManager.getLogger("TransformCommand");

    @Option(names = {"-h", "--help"}, usageHelp = true)
    public boolean help;

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

    @Option(names = {"-w", "--workdir", "-b", "--basedir"}, description = "basedir of relative paths")
    public Path workDir;

    @Option(names = {"--outdir", "--cwd", "-d"}, description = "output directory (and current working directory)")
    public Path outDir;

    @Option(names = {"--princeapi"}, description = "use the prince API for cssprint (nop for fo pipeline)")
    public boolean princeApi = false;

    ///////////////////////////
    // derived
    ///////////////////////////

    public String inBasename;

    @Override
    public Object call() throws Exception {
        in = in.toAbsolutePath();
        if (workDir == null) {
            workDir = in.getParent();
        }
        String inName = in.getFileName().toString();
        LOG.warn("call");
        if (inFormat == null) {
            inFormat = EFileType.getType(inName);
        }
        if (inFormat == null) {
            throw new IllegalArgumentException("informat unknown");
        }
        inBasename = EFileType.getBasename(inName);
        if (out == null) {
            if (outFormat == null) {
                // default out format
                outFormat = EFileType.PDF;
            }
            if (outDir != null) {
                out = outDir.resolve(inBasename + "." + outFormat.getDefaultExtension());
            } else {
                out = workDir.resolve(inBasename + "." + outFormat.getDefaultExtension());
                outDir = out.getParent();
            }
        } else {
            boolean warn = (outDir != null);
            outDir = out.getParent();
            if (warn) {
                LOG.warn("--outdir will be ignored and set to ");
            }
        }
        if (!outDir.toFile().isDirectory()) {
            LOG.error("--outdir " + outDir + " is not a directory");
        }
        if (outFormat == null) {
            outFormat = EFileType.getType(out.getFileName().toString());
        }
        if (outFormat == null) {
            throw new IllegalArgumentException("outformat unknown");
        }
        in = in.toAbsolutePath();

        // We can't rely on relative paths - as 'user.dir' could change
        workDir = workDir.toAbsolutePath();
        outDir = outDir.toAbsolutePath();
        out = out.toAbsolutePath();

        return this;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", TransformCommand.class.getSimpleName() + "[", "]")
                .add("help=" + help)
                .add("in=" + in)
                .add("out=" + out)
                .add("inFormat=" + inFormat)
                .add("outFormat=" + outFormat)
                .add("pipeline='" + pipeline + "'")
                .add("validate=" + validate)
                .add("workDir=" + workDir)
                .add("outDir=" + outDir)
                .add("inBasename='" + inBasename + "'")
                .toString();
    }
}
