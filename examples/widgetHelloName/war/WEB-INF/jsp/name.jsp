<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template" version="2.0">
  	<html>
    	<head>
        <ui:importScripts/>
            
        <title>Hello Name Example</title>
      </head>			
             
      <ui:body>
<ui:systemForm method="GET">
  Insert your name: <input type="text" name="name"/><br/><br/>	        	
  <ui:eventButton labelId="#Say hello" eventId="hello"/>			
</ui:systemForm>
			</ui:body>
		</html>
</jsp:root>
