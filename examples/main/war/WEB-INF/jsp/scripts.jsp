<?xml version="1.0" encoding="UTF-8"?>
<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"	
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	version="1.2">
	
	<!--
		Use a short-cut to include Aranea CSS styles:
			<ui:importStyles group="all" media="screen"/>
		or include separately:
	-->
	<ui:importStyles group="aranea" media="screen"/>
	<ui:importStyles group="calendar" media="screen"/>
	<ui:importStyles group="contextmenu" media="screen"/>
	<ui:importStyles group="modalbox" media="screen"/>
	<ui:importStyles group="prototip" media="screen"/>

	<!-- CSS for the demo app:  -->
	<ui:importStyles file="styles/_styles_global.css" media="all"/>
	<ui:importStyles group="templateScreenStyleGroup.css" media="screen"/>
	<ui:importStyles file="styles/_styles_print.css" media="print" />

	<!-- Imports all Aranea scripts. -->
	<ui:importScripts/>

	<!--
		Or include separately.
		Note that the difference between groups "core-all" and "core" is that
		the first one includes Aranea core scripts + JavaScript for modalbox
		and back-button (rsh) support.

		<ui:importScripts group="core-all"/>
		<ui:importScripts group="core"/>
		<ui:importScripts group="calendar"/>
		<ui:importScripts group="calendar_et"/>
		<ui:importScripts group="modalbox"/>
		<ui:importScripts group="rsh"/>
		<ui:importScripts group="prototip"/>
		<ui:importScripts group="tinyMCE"/>
	-->

	<!-- Enables firebug js console logging, if firebug present. In general, you may not want to include it. -->
	<script type="text/javascript">araneaPage().setFirebugLogger();</script>

	<!-- Enables stand-alone javascript logging -->
	<ui:importScripts group="logger"/>

	<script type="text/javascript">
		if (window['log4javascript/log4javascript.js']) {
			araneaPage().setDefaultLogger();
		}
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
