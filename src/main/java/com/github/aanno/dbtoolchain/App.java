package com.github.aanno.dbtoolchain;

import com.github.aanno.dbtoolchain.cli.CliUtils;
import com.github.aanno.dbtoolchain.cli.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.Arrays;
import java.util.List;

import static picocli.CommandLine.*;

public class App {

    private static final Logger LOG = LoggerFactory.getLogger("App");

    public App() {
    }

    public void process(List<Object> list) throws Exception {
        if (list != null) {
            LOG.warn("parsed args: " + list.toString());
        }
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
