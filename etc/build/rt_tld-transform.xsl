<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:output 
    method="xml" 
    encoding="UTF-8" 
    indent="yes" 
    doctype-public="-//Sun Microsystems, Inc.//DTD JSP Tag Library 1.2//EN"
    doctype-system="http://java.sun.com/dtd/web-jsptaglibrary_1_2.dtd"/>

    <xsl:template match="*">
      <xsl:element name="{name()}" namespace="">
        <xsl:copy-of select="@*"/>
        <xsl:apply-templates/>
        
         <xsl:if test="name()='attribute'">
            <xsl:text disable-output-escaping="yes">
              &lt;rtexprvalue&gt;true&lt;/rtexprvalue&gt;
            </xsl:text>
         </xsl:if>

      </xsl:element>
    </xsl:template>

	<xsl:template match="text()">
		<xsl:copy/>
	</xsl:template>
	
	<xsl:template match="comment()">
		<xsl:copy/>
	</xsl:template>
</xsl:stylesheet>
