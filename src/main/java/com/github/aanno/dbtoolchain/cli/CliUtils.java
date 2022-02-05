package com.github.aanno.dbtoolchain.cli;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class CliUtils {

    private static final Logger LOG = LoggerFactory.getLogger("CliUtils");

    private static final List<String> COMMANDS = Arrays.asList(new String[] {
       "list", "transform"
    });

    private CliUtils() {
        // Never invoked
    }

    public static String[] stripVmArgs(String[] args, Class<?> callClass) {
        String classname = callClass.getName();
        int i = 0;
        int len = args.length;
        while (i < len) {
            String s = args[i];
            ++i;

            if (s.endsWith(classname)) {
                break;
            }
            if (COMMANDS.contains(s)) {
                --i;
                break;
            }
        }
        if (i <= len) {
            String[] result = new String[len - i];
            System.arraycopy(args, i, result, 0, len - i);
            return result;
        }
        return args;
    }
}
