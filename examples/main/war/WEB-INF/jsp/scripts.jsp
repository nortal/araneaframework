<?xml version="1.0" encoding="UTF-8"?>
<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"	
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	version="1.2">
	
		<ui:importStyles file="styles/_styles_global.css" media="all"/>
		<ui:importStyles file="styles/_styles_new.css" media="screen" />
		<ui:importStyles file="styles/_styles_screen.css" media="screen"/>
		<ui:importStyles file="styles/_styles_print.css" media="print" />
		<ui:importStyles file="css/calendar/calendar.css" media="screen"/>

		<ui:importScripts/>
		<ui:importScripts group="debugScripts"/>
		<script type="text/javascript">
			getActiveAraneaPage().setDefaultLogger();
		</script>

		<ui:richTextAreaInit>
			<ui:attribute name="theme" value="advanced"/>
			<ui:attribute name="theme_advanced_buttons1" value="bold,italic,underline,separator,strikethrough,justifyleft,justifycenter,justifyright, justifyfull,bullist,numlist,undo,redo,link,unlink,code"/>
			<ui:attribute name="theme_advanced_buttons3" value=""/>
			<ui:attribute name="theme_advanced_toolbar_location" value="top"/>
			<ui:attribute name="theme_advanced_toolbar_align" value="left"/>
			<ui:attribute name="theme_advanced_path_location" value="bottom"/>
			<ui:attribute name="extended_valid_elements" value="a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]"/>
		</ui:richTextAreaInit>
</jsp:root>