<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" xmlns:c="http://java.sun.com/jstl/core" xmlns:ui="http://araneaframework.org/tag-library/template" version="1.2">		
	<ui:widgetContext>
	
		<h2>Viewing company</h2>
		
		<p>
			Here you can see the data of selected company.
		</p>
		
		<p>
			<jsp:text>Name: </jsp:text>
			<c:out value="${contextWidget.data.company.name}"/>
		</p>
		
		<p>
			<jsp:text>Address: </jsp:text>
			<c:out value="${contextWidget.data.company.address}"/>
		</p>
	</ui:widgetContext>		
</jsp:root>