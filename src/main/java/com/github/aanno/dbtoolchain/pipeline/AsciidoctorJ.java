package com.github.aanno.dbtoolchain.pipeline;

import com.github.aanno.dbtoolchain.cli.EFileType;
import com.github.aanno.dbtoolchain.cli.TransformCommand;
import org.asciidoctor.Asciidoctor;
import org.asciidoctor.api.Options;
import org.asciidoctor.api.SafeMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static org.asciidoctor.Asciidoctor.Factory.create;
import static org.asciidoctor.api.OptionsBuilder.options;

public class AsciidoctorJ implements IPipeline {

    private static final Logger LOG = LoggerFactory.getLogger("AsciidoctorJ");

    private final Asciidoctor asciidoctor;
    // private Asciidoctor asciidoctor = create("submodules/asciidoctorj/asciidoctorj-core/build/gems");

    public AsciidoctorJ() throws MalformedURLException, ClassNotFoundException, ReflectiveOperationException {
        asciidoctor = create();

        /*
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        ClassLoader ncl = new URLClassLoader(new URL[]{
                new URL("file:///home/tpasch/scm/db-toolchain/jnr-enxio-0.19.jar"),
        }, cl);
        // test ncl
        Class<?> clazz = ncl.loadClass("jnr.enxio.channels.NativeSelectableChannel");
        LOG.warn("loaded class: " + clazz);

        // asciidoctor = create(ncl);

        Class<?> ad = ncl.loadClass("org.asciidoctor.Asciidoctor$Factory");
        asciidoctor = (Asciidoctor) ad.getDeclaredMethod("create").invoke(null);
         */
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
                current = processWithBackend(EAsciidoctorBackend.DOCBOOK5, command, current, finish);
            }
            if (EFileType.XHTML == finish.getType()) {
                current = processWithBackend(EAsciidoctorBackend.HTML, command, current, finish);
            }
            if (EFileType.PDF == finish.getType()) {
                current = processWithBackend(EAsciidoctorBackend.PDF, command, current, finish);
            }
            if (old.getType() == current.getType()) {
                throw new IllegalArgumentException("get stuck on " + old + " and " + current);
            }
        }
        return current;
    }

    private IStage processWithBackend(EAsciidoctorBackend be, TransformCommand command, IStage current, IStage finish) throws IOException {
        Map<String, Object> opts = new HashMap<>();
        opts.put(Options.BACKEND, be.name().toLowerCase());
        convert2(current.getPath(), finish.getPath(), opts);
        return finish;
    }

    private void convert2(Path in, Path out, Map<String, Object> options) throws IOException {
        Map<String, Object> opts = baseOptions(out);
        opts.putAll(options);
        // opts.put(Options.TO_FILE, "false");
        // opts.put(Options.TO_DIR, ".");
        // opts.put(Options.BASEDIR, ".");
        String result = asciidoctor.convertFile(in.toFile(), opts);
        LOG.warn("ad output to file " + out);
        LOG.warn("ad options: ", opts);
        LOG.warn("ad convertFile result: " + result);
    }

    private Map<String, Object> baseOptions(Path output) {
        return options().safe(SafeMode.SAFE).toFile(output.toFile()).asMap();
    }

}
