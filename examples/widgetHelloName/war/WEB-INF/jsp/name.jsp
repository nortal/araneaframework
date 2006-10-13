<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:ui="http://araneaframework.org/tag-library/standard" version="1.2">
	<ui:widgetContext>
	  Insert your name: 
	  <ui:element name="input">
	    <ui:attribute name="type" value="text"/>
	    <ui:attribute name="name" value="${widgetId}.name"/>
	  </ui:element>
	  <br/> 	
	  <ui:eventButton labelId="#Say hello" eventId="hello"/>			
  </ui:widgetContext>
</jsp:root>