package com.xmlcalabash.extensions.xslthl;

import com.xmlcalabash.core.XMLCalabash;
import com.xmlcalabash.core.XProcException;
import com.xmlcalabash.core.XProcRuntime;
import com.xmlcalabash.io.ReadablePipe;
import com.xmlcalabash.io.WritablePipe;
import com.xmlcalabash.library.DefaultStep;
import com.xmlcalabash.model.RuntimeValue;
import com.xmlcalabash.runtime.XAtomicStep;
import com.xmlcalabash.util.ProcessMatch;
import com.xmlcalabash.util.ProcessMatchingNodes;
import com.xmlcalabash.util.XProcURIResolver;
import net.sf.saxon.om.AttributeMap;
import net.sf.saxon.s9api.QName;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XdmNode;
import net.sf.xslthl.Block;
import net.sf.xslthl.Config;
import net.sf.xslthl.MainHighlighter;
import net.sf.xslthl.StyledBlock;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.xml.sax.InputSource;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.URIResolver;
import javax.xml.transform.sax.SAXSource;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

@XMLCalabash(
        name = "cx:xslthl",
        type = "{http://xmlcalabash.com/ns/extensions}xslthl")

public class XsltHL extends DefaultStep implements ProcessMatchingNodes {
    private static final QName _highlighter = new QName("highlighter");
    private static final QName _config = new QName("config");

    private ProcessMatch matcher = null;
    private ReadablePipe source = null;
    private WritablePipe result = null;

    private static final String library_xpl = "http://xmlcalabash.com/extension/steps/xslthl.xpl";
    private static final String library_url = "/com/xmlcalabash/extensions/xslthl/library.xpl";
    private String prefix = "xslthl";
    private String namespace = "http://xslthl.sf.net";

    private Config hlConfig = null;
    private MainHighlighter highlighter = null;

    public XsltHL(XProcRuntime runtime, XAtomicStep step) {
        super(runtime, step);
    }

    public void setInput(String port, ReadablePipe pipe) {
        source = pipe;
    }

    public void setOutput(String port, WritablePipe pipe) {
        result = pipe;
    }

    public void reset() {
        source.resetReader();
        result.resetWriter();
    }

    public void run() throws SaxonApiException {
        super.run();

        String hid = getOption(_highlighter).getString();

        if (hlConfig == null) {
            RuntimeValue cfgValue = getOption(_config);
            if (cfgValue != null) {
                URI config = cfgValue.getBaseURI().resolve(cfgValue.getString());
                logger.debug("Loaded xslthl config: " + config.toASCIIString());
                hlConfig = Config.getInstance(config.toASCIIString());
            } else {
                URL rsrc = getClass().getResource("/com/xmlcalabash/xslthl-config/xslthl-config.xml");
                if (rsrc == null) {
                    throw new XProcException("Cannot find xslthl configuration");
                }
                logger.debug("Loaded xslthl config: " + rsrc.toString());
                hlConfig = Config.getInstance(rsrc.toString());
            }
        }

        highlighter = hlConfig.getMainHighlighter(hid);

        XdmNode doc = source.read();
        RuntimeValue matchExpr = new RuntimeValue("text()", doc);

        matcher = new ProcessMatch(runtime, this);
        matcher.match(doc, matchExpr);

        result.write(matcher.getResult());
    }

    public static void configureStep(XProcRuntime runtime) {
        XProcURIResolver resolver = runtime.getResolver();
        URIResolver uriResolver = resolver.getUnderlyingURIResolver();
        URIResolver myResolver = new StepResolver(uriResolver);
        resolver.setUnderlyingURIResolver(myResolver);
    }

    @Override
    public boolean processStartDocument(XdmNode node) {
        throw new XProcException("Highlighter error; attempted to process start document");
    }

    @Override
    public void processEndDocument(XdmNode node) {
        throw new XProcException("Highlighter error; attempted to process end document");
    }

    @Override
    public boolean processStartElement(XdmNode node, AttributeMap attributes) {
        throw new XProcException("Highlighter error; attempted to process start element");
    }

    @Override
    public AttributeMap processAttributes(XdmNode node, AttributeMap matchingAttributes, AttributeMap nonMatchingAttributes) {
        throw new XProcException("Highlighter error; attempted to process attribute");
    }

    @Override
    public void processEndElement(XdmNode node) {
        throw new XProcException("Highlighter error; attempted to process end element");
    }

    @Override
    public void processText(XdmNode node) {
        List<Block> blocks = highlighter.highlight(node.getStringValue());

        for (Block block: blocks) {
            if (block.isStyled()) {
                StyledBlock styled = (StyledBlock) block;
                matcher.addStartElement(new QName(prefix, namespace, styled.getStyle()));
                // ??? not in xmlcalabash 1.2.5 (tp)
                // matcher.startContent();
                matcher.addText(styled.getText());
                matcher.addEndElement();
            } else {
                matcher.addText(block.getText());
            }
        }
    }

    @Override
    public void processComment(XdmNode node) {
        throw new XProcException("Highlighter error; attempted to process comment");
    }

    @Override
    public void processPI(XdmNode node) {
        throw new XProcException("Highlighter error; attempted to process processing instruction");
    }

    private static class StepResolver implements URIResolver {
        Logger logger = LogManager.getLogger(XsltHL.class);
        URIResolver nextResolver = null;

        public StepResolver(URIResolver next) {
            nextResolver = next;
        }

        @Override
        public Source resolve(String href, String base) throws TransformerException {
            try {
                URI baseURI = new URI(base);
                URI xpl = baseURI.resolve(href);
                if (library_xpl.equals(xpl.toASCIIString())) {
                    URL url = XsltHL.class.getResource(library_url);
                    logger.debug("Reading library.xpl for cx:xslthl from " + url);
                    InputStream s = XsltHL.class.getResourceAsStream(library_url);
                    if (s != null) {
                        SAXSource source = new SAXSource(new InputSource(s));
                        return source;
                    } else {
                        logger.info("Failed to read " + library_url + " for cx:xslthl");
                    }
                }
            } catch (URISyntaxException e) {
                // nevermind
            }

            if (nextResolver != null) {
                return nextResolver.resolve(href, base);
            } else {
                return null;
            }
        }
    }
}
