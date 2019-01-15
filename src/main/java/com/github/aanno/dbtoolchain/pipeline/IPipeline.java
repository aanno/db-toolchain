package com.github.aanno.dbtoolchain.pipeline;

import com.github.aanno.dbtoolchain.cli.TransformCommand;

import java.io.IOException;

public interface IPipeline {

    String getName();

    String getDescription();

    static String[] getVariants() {
        return new String[0];
    }

    default IStage process(TransformCommand command) throws IOException {
        return process(command, Stage.fromIn(command), Stage.fromOut(command));
    }

    IStage process(TransformCommand command, IStage current, IStage finish) throws IOException;

}
