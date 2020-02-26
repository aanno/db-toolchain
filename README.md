# db-toolchain

Asciidoc and DocBook to PDF (or FO, HTML, ...) conversion made easy! 

This project has similiar aims as [asciidoctor-fopub](https://github.com/asciidoctor/asciidoctor-fopub):
Make using Asciidoc(tor) (or DocBook) convertion tools as easy as it can be.

In detail:

* Provide several processing pipelines for conversion.
* Provide the most up-to-date pipelines possible.
* Provide an unified CLI access to the different pipelines.
* Only depend on an installed Java JDK 11 (and some standard Linux tools).

## State

This project is in its early stages, but the most basic things work now: Using pipelines to convert Asciidoc(tor)
or DocBook to PDF.

## Pipelines

So far the following pipelines are implemented:

* **xsl-fo**: <br/>
  PDF (or intermediate) conversion of Asciidoc(tor) and DocBook (5.1) using the DocBook 
  [Xslt 2.0 Stylesheets](https://github.com/docbook/xslt20-stylesheets) (2.4.3) and Apache FOP (2.4)
* **xsl-css**: <br/>
  PDF (or intermediate) conversion of Asciidoc(tor) and DocBook (5.1) using the DocBook 
  [Xslt 2.0 Stylesheets](https://github.com/docbook/xslt20-stylesheets) (2.4.3) and 
  [Prince 13](https://www.princexml.com/)
* **ad**: <br/>
  PDF (or intermediate) conversion of Asciidoc(tor) using the 
  [acsiidoctorj](https://github.com/asciidoctor/asciidoctorj) port of 
  [asciidoctor](https://github.com/asciidoctor/asciidoctor)
* **fo**: <br/>
  PDF conversion of XSLT-FO (i.e. FO) using [Apache FOP](https://xmlgraphics.apache.org/fop/) (2.4)

## Prerequisite

* Java 11 JDK installed and set as JAVA_HOME environment.
* [Apache Maven](https://maven.apache.org/) (`mvn`) in PATH.
* [Apache Ant](https://ant.apache.org/) (`ant`) in PATH.
* Some Linux tools (e.g. `zip`, `unzip`, `bash`, `wget`) in PATH.
* A github account, with an associated SSH certificate (so that you _clone with SSH_) 

## Quick start

1. Check out the project from github.
2. `cd db-toolchain`
3. `./script/bootstrap.sh`
4. `./gradlew build` and `./scripts/merge-split-jars.sh` (ignoring any errors that may occur)
5. `./gradlew runApp1` to run an example convertion.
6. You have now a distribution Zip at `build/distributions/db-toolchain.zip` that you can unzip and use 
   independent of the build process.

## Docbook

### Resources

* [References](https://tdg.docbook.org/)
* [Big Example](https://docbook.org/docs/howto/howto.html) (from 2009)
  (with [xml](https://docbook.org/docs/howto/howto.xml) and 
  [pdf](https://docbook.org/docs/howto/howto.pdf))
* [DocBook XML Schema](https://docbook.org/xml/5.1/)
* [Docbook Wiki](https://github.com/docbook/wiki/wiki)

### Instructions

* [Introduction to DocBook](https://opensource.com/article/17/9/docbook)
* [Giurca's DocBook Tutorial](https://www.informatik.tu-cottbus.de/~giurca/tutorials/DocBook/index.htm)
* [Introduction to DocBook](https://opensource.com/article/17/9/docbook)
* [DocBook Demystification](http://en.tldp.org/HOWTO/DocBook-Demystification-HOWTO/x128.html)
* [DocBook Wiki](https://github.com/docbook/wiki/wiki/LearningDocBook)
* [Einführung in XML/XSLT Docbook](http://www.usegroup.de/software/xmltutorial/docbook.html)
* [Doc as Code Blog (in german)](https://jaxenter.de/tag/hhgdc)
* [How to generate PDF](https://stackoverflow.com/questions/2615002/how-to-generate-pdf-from-docbook-5-0) (in german)
* [Using DocBook toolchain](http://xpt.sourceforge.net/techdocs/nix/tool/asciidoc-usg/ascu04-UsingDocBooktoolchain/)
* [Recommended toolchain](https://stackoverflow.com/questions/122752/what-is-the-recommended-toolchain-for-formatting-xml-docbook)
* [Eclipse help from Docbook](https://wiki.eclipse.org/Authoring_Eclipse_Help_Using_DocBook)

### References

* [DocBook: The Definitive Guide](https://tdg.docbook.org/tdg/5.2/)
  + [Definitive Guide sources](https://github.com/docbook/defguide/tree/master/en)
* [DocBook XSL: The Complete Guide](http://www.sagehill.net/docbookxsl/) (from 2007)
* [DoCookBook](http://doccookbook.sourceforge.net/html/en/DoCookBook.html)
  + [DoCookBook source](https://github.com/tomschr/dbcookbook)

### Big Examples

* [Firebird DocBook XML introduction](https://www.firebirdsql.org/pdfmanual/html/docwritehowto-docbook-intro.html)
  + [DocBook Source](https://github.com/FirebirdSQL/firebird-documentation/blob/1ec7b44f5e527198ca79a7ef40ff63fce73c56ff/src/docs/firebirddocs/docwriting-howto.xml)
* [elastic search definitive guide](https://www.elastic.co/guide/en/elasticsearch/guide/current/index.html)
  + [elastic search definitive guide sources](https://github.com/elastic/elasticsearch-definitive-guide)

#### DB Tools

* [docbkx-tools (maven plugin)](https://github.com/mimil/docbkx-tools)
* [fopub (java based)](https://github.com/asciidoctor/asciidoctor-fopub)
* [AsciiBook](https://github.com/arnaldorusso/AsciiBook): toolchain from AsciiDoc to PDF (in JS)
* [asciidoctor-fopub](https://github.com/asciidoctor/asciidoctor-fopub): toolchain from DB (4.5) to PDF
* [publican (old)](https://jfearn.fedorapeople.org/en-US/Publican/4.3/html/Users_Guide/index.html)
  + [article](http://dtsden.eu/article/XMLDocBookPublishingUsingPublican)
  + [asciidoc for publican](https://github.com/asciidoctor/asciidoctor/wiki/Convert-Asciidoc-to-Docbook-for-use-with-Publican)

##### asciidoctor

* [asciidoctor](https://asciidoctor.org/)
* [list of asciidoctor extensions](https://asciidoctor.org/docs/extensions/)
* [asciidoctorj integrator guide](https://github.com/asciidoctor/asciidoctorj/blob/v2.1.0/docs/integrator-guide.adoc)
* [using extensions with asciidoctorj](https://asciidoctor.org/docs/asciidoctorj/#extension-api)
* [asciidoctor-gradle-plugin](https://asciidoctor.org/docs/asciidoctor-gradle-plugin/)
* [new asciidoctor-gradle-plugin](https://asciidoctor.github.io/asciidoctor-gradle-plugin/development-3.x/user-guide/)
* [How can I enable asciimath support](https://github.com/asciidoctor/asciidoctor-fopub/issues/49)

###### Convert _to_ asciidoctor

* [Overview article](https://blogs.gnome.org/pmkovar/2015/10/27/converting-docbook-into-asciidoc/)
* [pandoc](https://pandoc.org/): `pandoc --wrap=none -f docbook -t asciidoc DocbookFile.xml > AsciiDocFile.adoc`
* [docbookrx](https://github.com/asciidoctor/docbookrx)

#### Tools with DB *output*

* [docToolchain](https://github.com/docToolchain/docToolchain)
  + [docToolchain Docs](https://doctoolchain.github.io/docToolchain/#_generatedeck)
  + [docToolchain Blog](https://rdmueller.github.io/)
* [from asciidoctor](https://asciidoctor.org/docs/convert-documents/#converting-a-document-to-docbook)

### Docbook Stylesheets

* [To Context XSL](https://github.com/doctribute/docbook-to-context-xsl-stylesheets)
* [Chunking](https://github.com/doctribute/docbook-xinclude-based-chunking-stylesheets)

#### XSLT 2.0

* [XSLT 2.0](https://github.com/docbook/xslt20-stylesheets)
* [Saxon 9.9 documentation](https://saxonica.com/documentation/index.html)
* [XSLT 2.0 java implementations](https://stackoverflow.com/questions/529309/open-source-java-xslt-2-0-implementation)

##### Technology used with DB XSLT 2.0

* [XProc](https://de.wikipedia.org/wiki/XProc)
  + [XProc Spec](https://www.w3.org/TR/xproc/)
  + [Calabash: XProc Implementation](http://xmlcalabash.com/)
  + [Calabash Docs](https://github.com/ndw/xmlcalabash1-docs)
    - [Calabash Gradle Plugin](https://github.com/ndw/xmlcalabash1-gradle)
    - [JEuclid extension step](https://xmlcalabash.com/docs/reference/cx-mathml-to-svg.html)
    - [XSLT Highlighter extension step](https://github.com/ndw/xmlcalabash1-xslthl)
    - [Pygments extension step](https://github.com/ndw/xmlcalabash1-pygments)
    - [MathML to SVG converter extension step](https://github.com/ndw/xmlcalabash1-mathml-to-svg) -
      uses JEuclid
    - [AsciiDoctor extension step](https://github.com/ndw/xmlcalabash1-asciidoctor)
    - [PlantUML extension step](https://github.com/ndw/xmlcalabash1-plantuml)
    - [IDPF epubcheck extension step](https://github.com/transpect/epubcheck-extension)
    - [MathType OLE extension step](https://github.com/transpect/mathtype-extension)
    - [DiTAA diagrams Step](https://github.com/ndw/xmlcalabash1-ditaa) -
      uses [DiTAA diagrams](http://ditaa.sourceforge.net/)
    - [image metadata extraction extension step](https://github.com/ndw/xmlcalabash1-metadata-extractor)
      uses [metadata-extractor](https://drewnoakes.com/code/exif/)
    - [Delta XML extension step](https://github.com/ndw/xmlcalabash1-deltaxml)
    - [2.x XSpec step](https://github.com/ndw/xmlcalabash2-xspec)
    - [RDF extension steps](https://github.com/ndw/xmlcalabash1-rdf)
    - [XMLUnit extension step](https://github.com/ndw/xmlcalabash1-xmlunit)
    - [pegdown Step](https://github.com/ndw/xmlcalabash1-pegdown) - but pegdown is _deprecated_
      (use [flexmark-java](https://github.com/vsch/flexmark-java)

* [EXProc](http://exproc.org/): XProc extensions

#### XSLT 1.0

* [xslt10](https://github.com/docbook/xslt10-stylesheets)
* [design of xslt10](https://nwalsh.com/docs/articles/dbdesign/)

### Html, xHtml, Html5

* [convert html to xhtml](https://stackoverflow.com/questions/29087077/is-it-possible-to-convert-html-into-xhtml-with-jsoup-1-8-1)

#### Parser

* [jsoup](https://jsoup.org/) (java)
* [htmlcleaner](http://htmlcleaner.sourceforge.net/)

### Validating DB

#### Technology used for DB Validation

* [XML catalog](http://xmlcatalogs.org/)
  + [xmlresolver implementation](https://xmlresolver.org/)
    - [xmlresolver github](https://github.com/ndw/xmlresolver/)
  + [java implementation (since java 9)](https://docs.oracle.com/javase/9/core/xml-catalog-api1.htm)
* [NVDL](https://www.kosek.cz/xml/2008w3c-nvdl/foil18.html)
  + [jNDL Implementation](http://jnvdl.sourceforge.net/)
  + [NVDL within jing-trang (experimental)](https://github.com/relaxng/jing-trang/blob/bf4bafa3fef13aa2bed1ea03aea0c79a257680b3/mod/nvdl/src/main/com/thaiopensource/validate/nvdl/SchemaImpl.java)
* [Relax NG](https://relaxng.org/jclark/)
  + [Relax NG home page](https://relaxng.org/)
  + [java validation with relax ng](https://stackoverflow.com/questions/47185975/validate-an-xml-document-with-relax-ng-and-namespaces)
  + [jing-trang](https://github.com/relaxng/jing-trang)
    - [jing home page](https://relaxng.org/jclark/jing.html)
* [Schematron](http://schematron.com/)
  + [ph-schematron Implementation](https://github.com/phax/ph-schematron/)
    - [ph-schematron home page](https://phax.github.io/ph-schematron/)
    - [example of use](https://github.com/phax/ph-schematron/blob/master/ph-schematron/src/test/java/com/helger/schematron/docs/DocumentationExamples.java)
      basically there are 2 different ways to use, read the home page!
  + [jing-trang also contains a schematron implementation (experimental)](https://relaxng.org/jclark/jing.html)
  + [Skeleton XSLT Schematron Implementation](https://github.com/Schematron/schematron)

### Math with DB

* [mathml1](https://www.data2type.de/xml-xslt-xslfo/docbook/block-elemente/gleichungen/docbook-und-mathml/) (in german)
* [mathml2](https://www.data2type.de/xml-xslt-xslfo/docbook/anpassen-von-docbook/docbook-5-erweitern/erweitern-von-docbook-mit-math/) (in german)
* [asciidoctor stem](https://asciidoctor.org/docs/user-manual/#activating-stem-support)
* [asciidoctor alternative math extension](https://github.com/asciidoctor/asciidoctor-mathematical)
  renderer: gnome/pango/cairo/gdk
* [extraordinary list of math renderer](https://github.com/gjtorikian/mathematical#history)

#### MathML Implementations

* [modern JEuclid Fork](https://github.com/rototor/jeuclid)
  + [JEuclid home page](http://jeuclid.sourceforge.net/)
  + [Calabash extension step](https://xmlcalabash.com/docs/reference/cx-mathml-to-svg.html)

#### Other Technology for Math

* [AsciiMath](https://en.m.wikipedia.org/wiki/AsciiMath)
  + [AsciiMath home page](http://asciimath.org/)
  + [MathJax Implementation](https://www.mathjax.org/)
    + [mathjax-node](https://github.com/mathjax/MathJax-node)
* [mml2tex](https://github.com/transpect/mml2tex) MathML to Latex
    
### Diagrams

* [Diagrams](https://jaxenter.de/hitchhikers-guide-docs-code-diagramme-66357)
  
### DB to ePub

* The [xslt10](https://github.com/docbook/xslt10-stylesheets) stylesheets have a epub pipeline (see README)
* [epubtools-frontend](https://github.com/transpect/epubtools-frontend) xhtml to epub
  + [epubtools](https://transpect.github.io/modules-epubtools.html)

#### Other Technology for ePub

* [XQuery to ePub converter](https://en.m.wikibooks.org/wiki/XQuery/DocBook_to_ePub)
  + [XQuery DB Implementation](http://exist-db.org/exist/apps/homepage/index.html)
* [epublib](http://www.siegmann.nl/epublib) (java)

### (Source) Code Syntax Highlighting in DB

* [xslthl](http://xslthl.sourceforge.net/) (old but maintained)
  + [xslthl github](https://github.com/innovimax/xslthl)
  + [XSLT Highlighter extension step](https://github.com/ndw/xmlcalabash1-xslthl)

## FOP

* [Apache FOP](https://xmlgraphics.apache.org/fop/)
* [Apache FOP Images Plugin](https://xmlgraphics.apache.org/fop/fop-pdf-images.html)
* [XSL-FO Input](https://xmlgraphics.apache.org/fop/fo.html)
* [XSL-FO Reference](https://www.data2type.de/xml-xslt-xslfo/xsl-fo/xslfo-referenz/) (in german)
* [State of XSL-FO 2019](https://www.rockweb.co.uk/blog/2019/04/xsl-fo-is-alive-and-kicking/)

### FOP and SVG

* [Apache FOP image support](https://xmlgraphics.apache.org/fop/0.95/graphics.html)
* [Examples of SVG inside Apache FOP](https://xmlgraphics.apache.org/fop/dev/svg.html)
  + [instream-foreign-object fo example](https://xmlgraphics.apache.org/fop/dev/fo/embedding.fo.pdf)

## dblatex

* [dblatex documentation](http://dblatex.sourceforge.net/)

## Asciidoctor

* [Installing the Toolchain](https://asciidoctor.org/docs/install-toolchain/)
* [AsciidoctorJ](https://asciidoctor.org/docs/asciidoctorj/)
* [Syntax Highlighting](https://asciidoctor.org/docs/user-manual/#source-code-blocks)
* [Antora](https://antora.org/): Site generation for asciidoc
  + [Example site](https://asciidoctor-docs.netlify.com/asciidoctor/1.5/converters/)
* [Additional Backends](https://github.com/asciidoctor/asciidoctor-backends)

## Print-CSS 

* [CSS for print tutorial](http://edutechwiki.unige.ch/en/CSS_for_print_tutorial)
* [print-css.rocks](https://print-css.rocks/)
* [PagedMedia](https://www.pagedmedia.org/)
* [State of print stylesheets in 2018](https://www.smashingmagazine.com/2018/05/print-stylesheets-in-2018/)
* [Printer friendly pages](https://www.sitepoint.com/css-printer-friendly-pages/)
* [Prepostprint Blog](https://prepostprint.org/doku.php/en/showcase)

### CSS Print Stylesheets

* [SelfHTML PrintCSS](https://wiki.selfhtml.org/wiki/CSS/Tutorials/Print-CSS)
* [PrintCSS friendly pages](https://www.sitepoint.com/css-printer-friendly-pages/)

### Print-CSS Renderer

* [Satz mit PrintCSS](https://print-css.de/) (in German)
* [PrintCSS in Verlagen](https://www.pagina.gmbh/slides/2015-11-20_PrintCSS_Markupforum_Tobias-Fischer.html)

#### Open Source Implementations

* [vivliostyle](https://vivliostyle.org/) (JS, renderer: chromium)
  + [vivliostyle github](https://github.com/vivliostyle/vivliostyle)
  + [vivliostyle MathJAX](https://github.com/vivliostyle/vivliostyle/issues/523)
  + [vivliostyle CLI](https://github.com/vivliostyle/vivliostyle-cli)
* [weasyprint](https://weasyprint.org/) (python, renderer: gnome/pango/cairo/gdk)
  + [weasyprint github](https://github.com/Kozea/WeasyPrint)
  + [weasyprint docs](https://weasyprint.readthedocs.io/en/stable/tutorial.html)
* [openhtmltopdf](https://github.com/danfickle/openhtmltopdf) (java) based on
  + [Flying Saucer](https://github.com/flyingsaucerproject/flyingsaucer) renderer

### CSS drafts

* [Multi-column Layout](https://www.chromium.org/developers/design-documents/multi-column-layout)

## Alternative Technology

* [DITA](https://www.dita-ot.org/)
  + [Converter Manual](http://www.xmlmind.com/ditac/_distrib/doc/manual/index.html)
* [CommonMark](https://commonmark.org/): Unify Markdown
* [Lyx Editor](https://www.lyx.org/)
* [Context](https://wiki.contextgarden.net/Main_Page)
* [parsX](https://www.parsx.de/)
* [pretextbook markup](https://pretextbook.org/) (formerly 'MathBook XML')
  + [pretextbook github](https://github.com/rbeezer/mathbook)
* [markua](https://leanpub.com/markua/read)
  + [spec](http://markua.com/)
  
### Markdown to Book (mostly pandoc)

* [Thorsten Ball](https://thorstenball.com/blog/2018/09/04/the-tools-i-use-to-write-books/)
* [Ryan Frazier](https://pianomanfrazier.com/post/write-a-book-with-markdown/)
* [pp](http://cdsoft.fr/pp/) (pandoc preprocessor)
* [gaiden](http://kobo.github.io/gaiden/getting-started.html)
  + [gaiden github](https://github.com/kobo/gaiden)
  
## IPC

### Java to Java

* [mappedbus](https://github.com/caplogic/mappedbus)

### Java to JS

* [j2v8](https://github.com/eclipsesource/j2v8)
  + [article: running js on jvm](https://eclipsesource.com/blogs/2016/07/20/running-node-js-on-the-jvm/)
* [trireme](https://github.com/apigee/trireme)
* [GraalVM](https://www.graalvm.org)
  + [plolyglot JS](https://www.graalvm.org/docs/examples/polyglot-javascript-java-r/)
