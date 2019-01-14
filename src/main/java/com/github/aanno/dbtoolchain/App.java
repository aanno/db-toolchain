package com.github.aanno.dbtoolchain;

import com.github.aanno.dbtoolchain.cli.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;

import java.util.List;

import static picocli.CommandLine.*;

public class App {

    String name;
    private static final Logger LOG = LoggerFactory.getLogger("App");

    public App() {
    }

    public void process(List<Object> list) throws Exception {
LOG.info(list.toString());
    }

    public static void main(String[] args) throws Exception {
        App dut = new App();
        CommandLine cl = new Parser().build();
        dut.process(cl.parseWithHandler(new RunAll(), args));
    }
}
