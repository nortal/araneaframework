<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:fmt="http://java.sun.com/jstl/fmt" 
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>

		<ui:componentHeader>
			<ui:componentName>GWT JSON Interoperability Example</ui:componentName>
        </ui:componentHeader>

		<ui:component>

			<p>
				<ui:eventButton eventId="test" labelId="#Empty submit"/>
			</p>
			<p>
				<ui:eventButton eventId="start" labelId="#Start new flow"/>
			</p>


			<ui:gwtWidgetInclude id="json" styleClass="gwtJson">
				<h1>JSON Interop Using JSNI</h1>
				<div class="intro">
					<p>
						This example application demonstrates a simple approach to 
						interoperating with services that return their output in JSON format.
						It uses GWT's JavaScript Native Interface (JSNI) to analyze a JSON 
						response and create Java-accessible objects.  The JSON classes in this sample 
						are general-purpose and can be reused in other projects if you find 
						them useful.
					</p>
					<p>
						When you click the "Search" button below, you can browse the cached 
						results of a Yahoo JSON image search for "potato."  The response is 
						parsed into Java objects which are used to populate a tree view below.
						<ul>
							<li>For more details on how the JSON response is parsed into a set 
							    of Java objects, see the JSONParser class.
							</li>
							<li>The search URL used in this example was 
								<a href="http://api.search.yahoo.com/ImageSearchService/V1/imageSearch?appid=YahooDemo&amp;query=potato&amp;results=2&amp;output=json">http://api.search.yahoo.com/ImageSearchService/V1/imageSearch?appid=YahooDemo&amp;query=potato&amp;results=2&amp;output=json</a>.
							</li>
						</ul>
					</p>
					<p id="search"></p>
				</div>
				<table align="center" width="80%" style="margin-top: 1em">
					<tr><th style="text-align: center; margin: 1em">JSON Response Tree View</th></tr>
					<tr><td id="tree"></td></tr>
				</table>
			</ui:gwtWidgetInclude>

		</ui:component>

	</ui:widgetContext>

</jsp:root>
