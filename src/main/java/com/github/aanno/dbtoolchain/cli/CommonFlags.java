package com.github.aanno.dbtoolchain.cli;

import picocli.CommandLine;
import static picocli.CommandLine.*;

@Command(name = "com.github.aanno.dbtoolchain.App", description = "common flags",
        subcommands = {TransformFlags.class, ListFlags.class})
public class CommonFlags {

    @Option(names = {"-v", "--verbose"}, description = "be verbose", defaultValue = "false",
    versionHelp = true, usageHelp = true)
    public boolean verbose;
}
