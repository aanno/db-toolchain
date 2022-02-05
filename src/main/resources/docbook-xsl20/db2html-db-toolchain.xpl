<p:declare-step xmlns:p="http://www.w3.org/ns/xproc"
                xmlns:db="http://docbook.org/ns/docbook"
                xmlns:dbp="http://docbook.github.com/ns/pipeline"
                xmlns:pxp="http://exproc.org/proposed/steps"
                xmlns:cx="http://xmlcalabash.com/ns/extensions"
                xmlns:mml="http://www.w3.org/1998/Math/MathML"
                name="main" version="1.0">
  <p:input port="source" sequence="true" primary="true"/>
  <p:input port="parameters" kind="parameter"/>
  <p:output port="result" sequence="true" primary="true"/>
  <p:serialization port="result" method="html" encoding="utf-8" indent="false"
                   version="5"/>

  <p:option name="style" select="'docbook'"/>
  <p:option name="preprocess" select="''"/>
  <p:option name="postprocess" select="''"/>

  <!-- p:import href="docbook.xpl"/ -->
  <!-- TODO tp -->
  <p:import href="https://cdn.docbook.org/release/xsl20/current/xslt/base/pipelines/docbook.xpl"/>

  <dbp:docbook format="html" return-secondary="false">
    <p:with-option name="style" select="$style"/>
    <p:with-option name="preprocess" select="$preprocess"/>
    <p:with-option name="postprocess" select="$postprocess"/>
  </dbp:docbook>

  <!-- p:import href="https://cdn.docbook.org/release/xsl20/current/xslt/base/pipelines/docbook.xpl"/ -->
  <!-- see https://xmlcalabash.com/docs/reference/cx-steps.html -->
  <p:declare-step type="cx:mathml-to-svg">
    <p:input port="source" sequence="false"/>
    <p:input port="parameters" kind="parameter"/>
    <p:output port="result" sequence="false"/>
  </p:declare-step>

  <p:viewport match="mml:*">
    <cx:mathml-to-svg>
      <p:input port="parameters">
        <p:empty/>
      </p:input>
      <p:with-param name="mathsize" select="'25f'"/>
    </cx:mathml-to-svg>
  </p:viewport>

</p:declare-step>
