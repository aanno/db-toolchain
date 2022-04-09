/*

CssPrince is copy from https://github.com/ndw/xmlcalabash1-print
because of java11 module system problems:

* package com.xmlcalabash.util is also xmlcalabash
* java.lang.module.ResolutionException: Modules xmlcalabash1.print and xmlcalabash
  export package com.xmlcalabash.util to module joda.time

 */
package com.xmlcalabash.util.print;

import java.io.*;
import java.util.Properties;
import java.util.Vector;

import com.princexml.wrapper.Prince;
import com.princexml.wrapper.enums.InputType;
import com.princexml.wrapper.enums.KeyBits;
import com.princexml.wrapper.enums.PdfProfile;
import com.princexml.wrapper.events.MessageType;
import com.princexml.wrapper.events.PrinceEvents;
import com.xmlcalabash.util.Base64;
import com.xmlcalabash.util.S9apiUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.xmlcalabash.config.CssProcessor;
import com.xmlcalabash.core.XProcConstants;
import com.xmlcalabash.core.XProcException;
import com.xmlcalabash.core.XProcRuntime;
import com.xmlcalabash.runtime.XStep;

import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.XdmNode;

/**
 * Created by IntelliJ IDEA.
 * User: ndw
 * Date: 9/1/11
 * Time: 4:24 PM
 * To change this template use File | Settings | File Templates.
 *
 * Now uses api("com.princexml", "prince-java-wrapper", "1.2.0")
 * TODO: Settings are now normalized with no (i.e. setNoEmbedFonts)
 * TODO: Check if there are more settings in Prince
 * TODO: Recheck unsupported stuff
 */
public class CssPrince implements CssProcessor {
    private Logger logger = LogManager.getLogger(CssPrince.class);
    private static final QName _content_type = new QName("content-type");
    private static final QName _encoding = new QName("", "encoding");

    XProcRuntime runtime = null;
    Properties options = null;
    String primarySS = null;
    Vector<String> userSS = new Vector<String> ();

    XStep step = null;
    Prince prince = null;

    public void initialize(XProcRuntime runtime, XStep step, Properties options) {
        this.runtime = runtime;
        this.step = step;
        this.options = options;

        String exePath = getStringProp("exePath");
        if (exePath == null) {
            exePath = System.getProperty("com.xmlcalabash.css.prince.exepath");
        }
        if (exePath == null || "".equals(exePath)) {
            throw new XProcException("Attempt to use Prince as CSS formater but no Prince exePath specified");
        }

        prince = new Prince(exePath, new PrinceMessages());

        String s = getStringProp("baseURL");
        if (s != null) {
            prince.setBaseUrl(s);
        }

        Boolean b = getBooleanProp("compress");
        if (b != null) {
            prince.setNoCompress(!b);
        }

        b = getBooleanProp("debug");
        if (b != null) {
            prince.setDebug(b);
        }

        b = getBooleanProp("embedFonts");
        if (b != null) {
            prince.setNoEmbedFonts(!b);
        }

        b = getBooleanProp("encrypt");
        if (b != null) {
            prince.setEncrypt(b);
        }

        Integer keyBits = getIntProp("keyBits");
        if (keyBits != null) {
            String up = getStringProp("userPassword");
            if (up != null) {
                prince.setUserPassword(up);
            }
            String op = getStringProp("ownerPassword");
            if (op != null) {
                prince.setOwnerPassword(op);
            }
            b = getBooleanProp("disallowPrint");
            boolean dp = b == null ? false : b;
            prince.setDisallowPrint(dp);

            b = getBooleanProp("disallowModify");
            boolean dm = b == null ? false : b;
            prince.setDisallowModify(dm);

            b = getBooleanProp("disallowCopy");
            boolean dc = b == null ? false : b;
            prince.setDisallowCopy(dc);

            b = getBooleanProp("disallowAnnotate");
            boolean da = b == null ? false : b;
            prince.setDisallowAnnotate(da);

            int kb = keyBits.intValue();
            if (kb == 40) {
                prince.setKeyBits(KeyBits.BITS40);
            } else {
                prince.setKeyBits(KeyBits.BITS128);
            }

            // prince.setEncryptInfo(keyBits, up, op, dp, dm, dc, da);
        }

        s = getStringProp("fileRoot");
        if (s != null) {
            // prince.setFileRoot(s);
            // ???
            throw new XProcException("Unsupported prop 'fileRoot': " + s);
        }

        b = getBooleanProp("html");
        if (b != null) {
            // prince.setHTML(b);
            // ???
            prince.setInputType(InputType.HTML);
        }

        s = getStringProp("httpPassword");
        if (s != null) {
            prince.setAuthPassword(s);
        }

        s = getStringProp("httpUsername");
        if (s != null) {
            prince.setAuthUser(s);
        }

        s = getStringProp("httpProxy");
        if (s != null) {
            prince.setHttpProxy(s);
        }

        s = getStringProp("inputType");
        if (s != null) {
            InputType type = null;
            if (s.equalsIgnoreCase("html")) {
                type = InputType.HTML;
            } else if (s.equalsIgnoreCase("xml")) {
                type = InputType.XML;
            } else if (s.equalsIgnoreCase("auto")) {
                type = InputType.AUTO;
            } else {
                throw new XProcException("Unsupported input type: " + s);
            }
            prince.setInputType(type);
        }

        b = getBooleanProp("javascript");
        if (b != null) {
            prince.setJavaScript(b);
        }

        s = getStringProp("log");
        if (s != null) {
            prince.setLog(s);
        }

        b = getBooleanProp("network");
        if (b != null) {
            prince.setNoNetwork(!b);
        }

        b = getBooleanProp("subsetFonts");
        if (b != null) {
            prince.setNoSubsetFonts(!b);
        }

        b = getBooleanProp("verbose");
        if (b != null) {
            prince.setVerbose(b);
        }

        b = getBooleanProp("XInclude");
        if (b != null) {
            prince.setXInclude(b);
        }

        s = getStringProp("scripts");
        if (s != null) {
            for (String js : s.split("\\s+")) {
                prince.addScript(js);
            }
        }
    }

    public void addStylesheet(String uri) {
        if (primarySS == null) {
            primarySS = uri;
        } else {
            userSS.add(uri);
        }
    }

    public void addStylesheet(XdmNode doc) {
        doc = S9apiUtils.getDocumentElement(doc);

        String stylesheet = null;
        if ((XProcConstants.c_data.equals(doc.getNodeName())
             && "application/octet-stream".equals(doc.getAttributeValue(_content_type)))
            || "base64".equals(doc.getAttributeValue(_encoding))) {
            byte[] decoded = Base64.decode(doc.getStringValue());
            stylesheet = new String(decoded);
        } else {
            stylesheet = doc.getStringValue();
        }

        String prefix = "temp";
        String suffix = ".css";

        File temp;
        try {
            temp = File.createTempFile(prefix, suffix);
        } catch (IOException ioe) {
            throw new XProcException(step.getNode(), "Failed to create temporary file for CSS");
        }

        temp.deleteOnExit();

        try {
            PrintStream cssout = new PrintStream(temp);
            cssout.print(stylesheet);
            cssout.close();
        } catch (FileNotFoundException fnfe) {
            throw new XProcException(step.getNode(), "Failed to write to temporary CSS file");
        }

        if (primarySS == null) {
            primarySS = temp.toURI().toASCIIString();
        } else {
            userSS.add(temp.toURI().toASCIIString());
        }
    }

    public void format(XdmNode doc, OutputStream out, String contentType) {
        if (contentType != null && !"application/pdf".equals(contentType)) {
            throw new XProcException(step.getNode(), "Unsupported content-type on p:css-formatter: " + contentType);
        }

        try {
            if (primarySS != null) {
                prince.addStyleSheet(primarySS);
            }

            for (String uri : userSS) {
                prince.addStyleSheet(uri);
            }

            Serializer serializer = runtime.getProcessor().newSerializer();
            serializer.setOutputProperty(Serializer.Property.METHOD, "xml");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                serializer.setOutputStream(baos);
                S9apiUtils.serialize(runtime, doc, serializer);
            } catch (SaxonApiException sae) {
                throw new XProcException(sae);
            }

            ByteArrayInputStream bis = new ByteArrayInputStream(baos.toByteArray());

            prince.setInputType(InputType.HTML);
            // db-cssprint.xpl:161:65:output intent ICC profile required for PDF/X-4
            // prince.setPdfProfile(PdfProfile.PDFX_4);
            prince.setPageSize("A4");
            prince.setVerbose(true);

            prince.convert(bis, out);
        } catch (IOException e) {
            logger.debug(e.getMessage(), e);
            throw new XProcException(e);
        }
    }

    private String getStringProp(String name) {
        return options.getProperty(name);
    }

    private Integer getIntProp(String name) {
        String s = getStringProp(name);
        if (s != null) {
            try {
                int i = Integer.parseInt(s);
                return i;
            } catch (NumberFormatException nfe) {
                return null;
            }
        }
        return null;
    }

    private Boolean getBooleanProp(String name) {
        String s = options.getProperty(name);
        if (s != null) {
            return "true".equals(s);
        }
        return null;
    }

    private class PrinceMessages implements PrinceEvents {
        @Override
        public void onMessage(MessageType msgType, String msgLoc, String msgText) {
            if (MessageType.INF.equals(msgType)) {
                step.info(step.getNode(), msgText);
            } else if (MessageType.WRN.equals(msgType)) {
                step.warning(step.getNode(), msgText);
            } else if (MessageType.DBG.equals(msgType)) {
                step.info(step.getNode(), "Debug: " + msgText);
            } else if (MessageType.OUT.equals(msgType)) {
                step.info(step.getNode(), "Out: " + msgText);
            } else {
                step.error(step.getNode(), msgText, new QName(XProcConstants.NS_XPROC_ERROR_EX, "prince"));
            }
        }

        @Override
        public void onDataMessage(String name, String value) {
            step.info(step.getNode(), "DataMessage: " + name + ": " + value);
        }
    }
}
