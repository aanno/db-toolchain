package com.github.aanno.dbtoolchain.pipeline;

import com.github.aanno.dbtoolchain.cli.EFileType;
import com.github.aanno.dbtoolchain.cli.TransformCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Fo implements IPipeline {

    private static final Logger LOG = LoggerFactory.getLogger("Fo");

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    public Fo() {
    }

    @Override
    public String getName() {
        return "Fo";
    }

    @Override
    public String getDescription() {
        return "apache fop (for xsl-fo)";
    }

    @Override
    public IStage process(TransformCommand command, IStage current, IStage finish) {
        if (EFileType.FO != current.getType()) {
            throw new IllegalArgumentException();
        }
        if (EFileType.PDF == finish.getType()) {
            current = processToPdf(command, current);
        } else {
            throw new IllegalArgumentException();
        }
        return current;
    }

    private IStage processToPdf(TransformCommand command, IStage stage) {
        IStage result = Stage.from(command, EFileType.PDF);
        List<String> args = new ArrayList<>();

        args.add(stage.getPath().toString());
        args.add("-pdf");
        args.add(result.getPath().toString());
        LOG.warn("fop args: " + args);
        org.apache.fop.cli.Main.main(args.toArray(EMPTY_STRING_ARRAY));

        return result;
    }
}
