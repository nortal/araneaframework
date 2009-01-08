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
      	<xsl:choose>
      		<xsl:when test="name()='jsp-version'">
      			<xsl:text>2.0</xsl:text>
      		</xsl:when>
      		<xsl:otherwise>
		        <xsl:copy-of select="@*"/>
		        <xsl:apply-templates/>
      		</xsl:otherwise>
        </xsl:choose>
      </xsl:element>

      <xsl:if test="name()='required'">
		<xsl:text>
         </xsl:text>	
		<xsl:element name="rtexprvalue" namespace="">true</xsl:element>
      </xsl:if>
    </xsl:template>

	<xsl:template match="text()">
		<xsl:copy/>
	</xsl:template>
	
	<xsl:template match="comment()">
		<xsl:copy/>
	</xsl:template>
</xsl:stylesheet>
