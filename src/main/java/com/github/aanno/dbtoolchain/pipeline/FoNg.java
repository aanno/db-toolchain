package com.github.aanno.dbtoolchain.pipeline;

import com.github.aanno.dbtoolchain.cli.EFileType;
import com.github.aanno.dbtoolchain.cli.TransformCommand;
import com.github.aanno.dbtoolchain.xml.TraxSingleton;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FopFactoryBuilder;
import org.apache.fop.apps.MimeConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sourceforge.jeuclid.fop.plugin.JEuclidFopFactoryConfigurator;

import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FoNg implements IPipeline {

    private static final Logger LOG = LoggerFactory.getLogger("FoNg");

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    public FoNg() {
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
    public IStage process(TransformCommand command, IStage current, IStage finish) throws IOException {
        if (EFileType.FO != current.getType()) {
            throw new IllegalArgumentException();
        }
        if (EFileType.PDF == finish.getType()) {
            current = processToPdf(command, current);
        } else {
            throw new IllegalArgumentException("process command " + command + " failed:\n" +
                    "\tcurrent=" + current + "\n\tfinish=" + finish);
        }
        return current;
    }

    private IStage processToPdfOld(TransformCommand command, IStage stage) {
        IStage result = Stage.from(command, EFileType.PDF);
        List<String> args = new ArrayList<>();

        args.add(stage.getPath().toString());
        args.add("-pdf");
        args.add(result.getPath().toString());
        LOG.warn("fop args: " + args);
        org.apache.fop.cli.Main.main(args.toArray(EMPTY_STRING_ARRAY));

        return result;
    }

    private IStage processToPdf(TransformCommand command, IStage current) throws IOException {
        // see https://xmlgraphics.apache.org/fop/2.4/embedding.html
        // see https://xmlgraphics.apache.org/fop/2.4/servlets.html
        // https://xmlgraphics.apache.org/fop/2.4/configuration.html
        // see https://xmlgraphics.apache.org/fop/2.4/events.html
        IStage result = Stage.from(command, EFileType.PDF);
        FopFactoryBuilder builder = new FopFactoryBuilder(command.workDir.toUri());
        // FopFactoryBuilder builder = new FopFactoryBuilder(baseURI, myResourceResolver);
        builder.setStrictFOValidation(false);
        builder.setBreakIndentInheritanceOnReferenceAreaBoundary(true);
        // builder.setSourceResolution(96); // =96dpi (dots/pixels per Inch)
        FopFactory fopFactory = builder.build();
        // support MathML
        JEuclidFopFactoryConfigurator.configure(fopFactory);
        // fopFactory.addElementMapping(myElementMapping); // myElementMapping is a org.apache.fop.fo.ElementMapping
        FOUserAgent userAgent = fopFactory.newFOUserAgent();
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(command.out.toFile()));
        LOG.warn("fop args: " + command);
        TraxSingleton traxSingleton = TraxSingleton.getInstance();
        StreamSource inStage = new StreamSource(current.getPath().toFile());
        try {
            // customize userAgent
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent, outputStream);
            // userAgent.setProducer("MyKillerApplication");
            // userAgent.setCreator("John Doe");
            // userAgent.setAuthor("John Doe");
            // userAgent.setCreationDate(new Date());
            // userAgent.setTitle("Invoice No 138716847");
            // userAgent.setKeywords("XML XSL-FO");
            // userAgent.setTargetResolution(300); // =300dpi (dots/pixels per Inch)
            // userAgent.setDocumentHandlerOverride(documentHandler); // documentHandler is an instance of org.apache.fop.render.intermediate.IFDocumentHandler
            // userAgent.setFOEventHandlerOverride(myFOEventHandler); // myFOEventHandler is an org.apache.fop.fo.FOEventHandler

            // StreamResult out = new StreamResult(result.getPath().toFile());
            Result out = new SAXResult(fop.getDefaultHandler());
            traxSingleton.transform(inStage, out);
        } catch (TransformerException e) {
            throw new IOException(e);
        } catch (FOPException e) {
            throw new IOException(e);
        } finally {
            traxSingleton.close(inStage);
            outputStream.close();
        }
        return result;
    }
}
