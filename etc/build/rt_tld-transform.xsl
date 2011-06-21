<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">

	<xsl:output method="xml" encoding="UTF-8" indent="yes" />

	<xsl:variable name="no_rtexprvalue" select="false()"/>

	<xsl:template match="*">

		<xsl:choose>
			<xsl:when test="name()='rtexprvalue'">

				<xsl:element name="rtexprvalue" namespace="">true</xsl:element>
				<xsl:variable name="no_rtexprvalue" select="false()"/>

			</xsl:when>
			<xsl:when test="name()='uri'">

				<xsl:element name="uri" namespace=""><xsl:copy-of select="text()"/>_rt</xsl:element>

			</xsl:when>
			<xsl:otherwise>

				<xsl:if test="name()='attribute'">
					<xsl:variable name="no_rtexprvalue" select="true()"/>
				</xsl:if>

				<xsl:element name="{name()}" namespace="">
					<xsl:copy-of select="@*" />
					<xsl:if test="name()='taglib'">
						<xsl:comment>NOTE: This is a generated file!</xsl:comment>
					</xsl:if>
					<xsl:apply-templates />
				</xsl:element>

			</xsl:otherwise>
		</xsl:choose>

		<xsl:if test="name()='required' and $no_rtexprvalue">
			<xsl:text disable-output-escaping="yes">
			&lt;rtexprvalue&gt;true&lt;/rtexprvalue&gt;
			</xsl:text>
		</xsl:if>

	</xsl:template>

	<xsl:template match="text()">
		<xsl:copy/>
	</xsl:template>

	<xsl:template match="comment()">
		<xsl:copy/>
	</xsl:template>

</xsl:stylesheet>
