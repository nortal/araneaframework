<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:ui="http://araneaframework.org/tag-library/template" version="1.2">
  <ui:root>
  	<ui:viewPort>	
  	<html>
    	<head>
        <ui:importScripts/>
            
        <title>Hello Name Example</title>
      </head>			
             
      <body>
       	<ui:systemForm method="GET">
						Insert your name: <input type="text" name="name"/><br/><br/>	        	
						<ui:eventButton labelId="#Say hello" eventId="hello"/>			
				</ui:systemForm>
			</body>
		</html>
		</ui:viewPort>
	</ui:root>	
</jsp:root>