package com.github.aanno.dbtoolchain.pipeline;

import com.github.aanno.dbtoolchain.cli.EFileType;

import java.nio.file.Path;

public interface IStage {

    Path getPath();

    EFileType getType();
}
