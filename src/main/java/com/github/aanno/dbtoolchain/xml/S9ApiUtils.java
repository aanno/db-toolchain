package com.github.aanno.dbtoolchain.xml;


import com.github.aanno.dbtoolchain.org.docbook.XSLT20;
import net.sf.saxon.Configuration;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.util.List;
import java.util.stream.Collectors;

public class S9ApiUtils {

    private static final Logger logger = LogManager.getLogger(S9ApiUtils.class);

    private static Path tmpDir = null;

    static {
        try {
            tmpDir = Files.createTempDirectory("db-toolchain");
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private S9ApiUtils() {
        // Never invoked
    }

    public static URL getResource(String resource) {
        URL resLoc = null;
        // Where am I?
        // TODO tp
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl == null) {
            cl = ClassLoader.getSystemClassLoader();
        }
        List<URL> list = cl.resources(resource).collect(Collectors.toList());
        int size = list.size();
        if (size > 1) {
            throw new IllegalArgumentException("Resource " + resource + " is not unique");
        }
        if (size == 1) {
            // throw new IllegalArgumentException("Resource " + resource + " not found");
            resLoc = list.get(0);
        }
        logger.info("resource '" + resource + "' resolved to " + resLoc);
        return resLoc;
    }

    public static String jarOrBasePathForResource(String resource) {
        String resLoc = getResource(resource).toExternalForm();

        String jarLoc = null;
        if (resLoc != null && resLoc.indexOf(".jar!/") >= 0) {
            jarLoc = resLoc.substring(0, resLoc.indexOf(".jar!/") + 5);
        } else {
            // This is only supposed to happen on a dev box; if you're integrating this into some bigger
            // application, well, sorry, it's all a bit of a hack if it's not in the jar.
            if (resLoc.indexOf("/build/") > 0) {
                jarLoc = resLoc + "../../../resources/main";
            } else if (resLoc.indexOf("/out/production/") > 0) { // IntelliJ
                jarLoc = resLoc + "../resources";
            } else {
                throw new RuntimeException("cannot find root from " + resLoc);
            }
        }
        logger.info("jarLoc=" + jarLoc);
        return jarLoc;
    }

    public static Configuration getConfiguration() {
        Configuration result = new Configuration();
        return result;
    }

    public static InputStream getDb2FoPath() throws IOException {
        // return Paths.get("submodules/xslt20-stylesheets/build/xslt/base/pipelines/db2fo.xpl");
        return getResource("xslt/base/pipelines/db2fo.xpl").openStream();
    }

    public static InputStream getDocbookPath() throws IOException{
        // return Paths.get("submodules/xslt20-stylesheets/build/xslt/base/pipelines/docbook.xpl");
        return getResource("xslt/base/pipelines/docbook.xpl").openStream();
    }

    public static Path getDefaultCss() throws IOException {
        // return Paths.get("submodules/xslt20-resources/build/stage/css/default.css");
        // from resources folder of distribution
        return url2Filepath(getResource("docbook-xslt2/css/default.css"));
    }

    public static Path url2Filepath(URL url) throws IOException {
        String urlStr = url.toExternalForm();
        if (urlStr.startsWith("file:/")) {
            return Paths.get(urlStr.replaceAll("file:/+(.*)", "/$1"));
        }
        throw new IllegalArgumentException("URL " + url + " is not a file URL");
    }

}
