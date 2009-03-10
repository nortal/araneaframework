<?xml version="1.0" encoding="UTF-8"?>
<jsp:root
	xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	version="1.2">

	<!--
		We show here the short-cut way to include the JavaScripts and styles.
		A longer and more customizable way is shown below.
	-->

	<!-- CSS for visual elements of Aranea:  -->
	<ui:importStyles media="screen"/>

	<!-- CSS for the demo app:  -->
	<ui:importStyles file="styles/_styles_global.css" media="all"/>
	<ui:importStyles group="templateScreenStyleGroup" media="screen"/>
	<ui:importStyles file="styles/_styles_print.css" media="print" />

	<!-- Includes stand-alone Log4JavaScript logging (it is not included by default). -->
	<!-- ui:importScripts group="logger"/ -->

	<!-- Imports all Aranea scripts (default group="all"). -->
	<ui:importScripts group="all"/>

	<!-- We also need tinyMCE, a JavaScript-based rich-text editor -->
	<ui:importScripts file="js/tiny_mce/tiny_mce.js"/>

	<!--
		Now a longer and more customizable way to import the same things.
		In addition, of course, you can also import files one-by-one.
	-->

	<!--
		<ui:importStyles group="aranea" media="screen"/>
		<ui:importStyles group="calendar" media="screen"/>
		<ui:importStyles group="contextmenu" media="screen"/>
		<ui:importStyles group="modalbox" media="screen"/>
		<ui:importStyles group="prototip" media="screen"/>
	-->

	<!--
		Now the JavaScript dependencies.
		Notice that the difference between groups "core-all" and "core" is that
		the first one includes Aranea core scripts + JavaScript for modalbox
		and back-button (rsh) support.
	-->

	<!--
		<ui:importScripts group="core-all"/> - all aranea*.js files + dependencies
		- or -
		<ui:importScripts group="core"/> - only the core of needed aranea*.js files
		<ui:importScripts group="calendar"/> - calendar scripts provided by Aranea 
		<ui:importScripts group="calendar_et"/> - calendar scripts in Estonian provided by Aranea
		<ui:importScripts group="scriptaculous"/> - required scriptaculous files if you use modalbox (overlay) with the "core" group
		<ui:importScripts group="modalbox"/> - modalbox (overlay) provided by Aranea
		<ui:importScripts group="popup"/> - to enable Aranea popups
		<ui:importScripts group="tree"/> - to enable Aranea Trees
		<ui:importScripts group="rsh"/> - for browser back-forward navigation (also enable StandardStateVersioningFilterWidget in aranea-conf.xml)
		<ui:importScripts group="prototip"/> - for tooltips 
		<ui:importScripts group="logger"/> - imports log4javascript (enable it with "araneaPage().setDefaultLogger()")
	-->

	<!-- Enables (Firebug) console logging, if browser supports it. In general, you may not want to include it. -->
	<script type="text/javascript">araneaPage().setFirebugLogger();</script>

	<!-- You can also use log4javacript logger:
		<script type="text/javascript">araneaPage().setDefaultLogger();</script>
	 -->

	<!-- Let's specify Tiny MCE preferences: -->
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
