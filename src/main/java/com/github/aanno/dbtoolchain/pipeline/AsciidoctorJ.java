package com.github.aanno.dbtoolchain.pipeline;

import com.github.aanno.dbtoolchain.cli.EFileType;
import com.github.aanno.dbtoolchain.cli.TransformCommand;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.api.Options;
import org.asciidoctor.api.SafeMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.asciidoctor.Asciidoctor.Factory.create;
import static org.asciidoctor.api.OptionsBuilder.options;

public class AsciidoctorJ implements IPipeline {

    private static final Logger LOG = LoggerFactory.getLogger("AsciidoctorJ");

    private Asciidoctor asciidoctor = create();

    public AsciidoctorJ() {
    }

    @Override
    public String getName() {
        return "AsciidoctorJ";
    }

    @Override
    public String getDescription() {
        return "asciidoctorj based pipeline";
    }

    @Override
    public IStage process(TransformCommand command, IStage current, IStage finish) throws IOException {
        if (EFileType.AD != current.getType()) {
            throw new IllegalArgumentException("asciidoctorj pipeline can only process asciidoc");
        }
        while (current.getType() != finish.getType()) {
            IStage old = current;
            if (EFileType.DB == finish.getType()) {
                current = processToDb(command, current, finish);
            }
            if (old.getType() == current.getType()) {
                throw new IllegalArgumentException("get stuck on " + old + " and " + current);
            }
        }
        return current;
    }

    private IStage processToDb(TransformCommand command, IStage current, IStage finish) throws IOException {
        Map<String, Object> opts = new HashMap<>();
        opts.put(Options.BACKEND, "docbook5");
        convert2(current.getPath(), finish.getPath(), opts);
        return finish;
    }

    private void convert2(Path in, Path out, Map<String, Object> options) throws IOException {
        Map<String, Object> opts = baseOptions(out);
        opts.putAll(options);
        // opts.put(Options.TO_FILE, "false");
        // opts.put(Options.TO_DIR, ".");
        // opts.put(Options.BASEDIR, ".");
        String result = asciidoctor.convertFile(in.toFile(), options);
        LOG.warn("ad output to file " + out);
        LOG.warn("ad convertFile result: " + result);
    }

    private Map<String, Object> baseOptions(Path output) {
        return options().safe(SafeMode.SAFE).toFile(output.toFile()).asMap();
    }

}
