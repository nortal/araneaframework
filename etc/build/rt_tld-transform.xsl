<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

	<xsl:output method="xml" encoding="UTF-8" indent="yes" />

	<xsl:template match="*">
		<xsl:element name="{name()}" namespace="">
			<xsl:copy-of select="@*" />
			<xsl:if test="name()='taglib'">
				<xsl:comment>NOTE: This is a generated file!</xsl:comment>
			</xsl:if>
			<xsl:apply-templates />
		</xsl:element>


		<xsl:if test="name()='required'">
			<xsl:text disable-output-escaping="yes">&lt;rtexprvalue&gt;true&lt;/rtexprvalue&gt;</xsl:text>
		</xsl:if>
	</xsl:template>

	<xsl:template match="text()">
		<xsl:copy/>
	</xsl:template>
	
	<xsl:template match="comment()">
		<xsl:copy/>
	</xsl:template>
</xsl:stylesheet>
