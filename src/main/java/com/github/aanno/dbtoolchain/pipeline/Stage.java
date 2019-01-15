package com.github.aanno.dbtoolchain.pipeline;

import com.github.aanno.dbtoolchain.cli.EFileType;
import com.github.aanno.dbtoolchain.cli.TransformCommand;

import java.nio.file.Path;
import java.util.Objects;
import java.util.StringJoiner;

public class Stage implements IStage {

    public static Stage fromIn(TransformCommand command) {
        return new Stage(command.in, command.inFormat);
    }

    public static Stage fromOut(TransformCommand command) {
        return new Stage(command.out, command.outFormat);
    }

    public static Stage from(TransformCommand command, EFileType type) {
        if (type == command.outFormat) {
            return fromOut(command);
        } else {
            return new Stage(command.workDir.resolve(command.inBasename + "." + type.getDefaultExtension()), type);
        }
    }


    private final Path path;

    private final EFileType type;

    public Stage(Path path, EFileType type) {
        this.path = path;
        this.type = type;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public EFileType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stage stage = (Stage) o;
        return path.equals(stage.path) &&
                type == stage.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, type);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Stage.class.getSimpleName() + "[", "]")
                .add("path=" + path)
                .add("type=" + type)
                .toString();
    }
}
