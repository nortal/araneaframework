<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>
	
		<ui:richTextAreaInit>
			<ui:attribute name="theme" value="advanced"/>
			<ui:attribute name="theme_advanced_buttons1" value="bold,italic,underline,separator,strikethrough,justifyleft,justifycenter,justifyright, justifyfull,bullist,numlist,undo,redo,link,unlink,code"/>
			<ui:attribute name="theme_advanced_buttons3" value=""/>
			<ui:attribute name="theme_advanced_toolbar_location" value="top"/>
			<ui:attribute name="theme_advanced_toolbar_align" value="left"/>
			<ui:attribute name="theme_advanced_path_location" value="bottom"/>
			<ui:attribute name="extended_valid_elements" value="a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]"/>
		</ui:richTextAreaInit>
		
		<ui:form id="form">
			<tui:component>
				<ui:formElement id="richArea">
                    <ui:richTextarea cols="100" rows="20"/>
				</ui:formElement>

				<ui:formElement id="button">
					<ui:button/>
				</ui:formElement>
			</tui:component>
		</ui:form>

		<div style="border-style: solid; border-width: 1px; padding: 7px">
			<c:out value="${viewData.preview}" escapeXml="false" />
		</div>

	</ui:widgetContext>
</jsp:root>
