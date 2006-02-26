<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:jedit="java:code2html.Main"
                exclude-result-prefixes="jedit"
				xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version='1.0'>

<xsl:param name="linenumbering.extension" select="0"/>
<xsl:param name="codehighlighting.extension" select="1"/>
<xsl:param name="use.extensions" select="1"/>
				
<xsl:template match="programlisting/text()">
		<xsl:if test="function-available('jedit:foSyntax') and $codehighlighting.extension = '1' and $use.extensions != '0'">
			<xsl:copy-of select="jedit:foSyntax(.)"/>
		</xsl:if>
		<xsl:if test="function-available('jedit:foSyntax')=false or $codehighlighting.extension != '1' or $use.extensions = '0'">
			<xsl:apply-templates />
		</xsl:if>
</xsl:template>

</xsl:stylesheet>
