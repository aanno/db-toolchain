package com.github.aanno.dbtoolchain;

import com.github.aanno.dbtoolchain.cli.*;
import com.github.aanno.dbtoolchain.pipeline.DbXslt20;
import com.github.aanno.dbtoolchain.pipeline.Fo;
import com.github.aanno.dbtoolchain.pipeline.IStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static picocli.CommandLine.*;

public class App {

    private static final Logger LOG = LoggerFactory.getLogger("App");

    private CommonFlags commonFlags;

    public App() {
    }

    public void process(List<Object> list) throws Exception {
        if (list != null) {
            LOG.warn("parsed args: " + list.toString());
            Iterator<Object> it = list.iterator();
            if (it.hasNext()) {
                commonFlags = (CommonFlags) it.next();
                while (it.hasNext()) {
                    Object command = it.next();
                    if (command != null) {
                        Class<?> clazz = command.getClass();
                        if (TransformCommand.class.equals(clazz)) {
                            transform((TransformCommand) command);
                        } else if (ListCommand.class.equals(clazz)) {
                            list((ListCommand) command);
                        }
                    }
                }
            }
        }
    }

    private void transform(TransformCommand transform) throws Exception {
        String pipeline = transform.pipeline;
        IStage result;
        if (pipeline.startsWith("xsl")) {
            DbXslt20 p;
            if (pipeline.contains("css")) {
                p = new DbXslt20("css");
            } else {
                p = new DbXslt20("fo");
            }
            LOG.warn("pipeline: " + p);
            result = p.process(transform);
        } else if (pipeline.startsWith("ascii") || pipeline.startsWith("ad")) {
            throw new UnsupportedOperationException();
        } else if (pipeline.startsWith("fo")) {
            Fo p = new Fo();
            LOG.warn("pipeline: " + p);
            result = p.process(transform);
        } else {
            throw new IllegalArgumentException("unknown pipeline: " + pipeline);
        }
        LOG.warn("result stage: " + result);
    }

    private void list(ListCommand list) throws Exception {

    }

    public static void main(String[] args) throws Exception {
        LOG.warn("raw args: " + Arrays.asList(args));
        App dut = new App();
        CommandLine cl = new Parser().build();
        args = CliUtils.stripVmArgs(args, App.class);
        LOG.warn("stripped args: " + Arrays.asList(args));
        dut.process(cl.parseWithHandler(new RunAll(), args));
    }
}
