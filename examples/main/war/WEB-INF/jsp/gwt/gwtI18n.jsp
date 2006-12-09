<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core" 
	xmlns:fmt="http://java.sun.com/jstl/fmt" 
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>

		<ui:componentHeader>
			<ui:componentName>GWT Internationalization sample</ui:componentName>
        </ui:componentHeader>

		<ui:component>

			<p>
				<ui:eventButton eventId="test" labelId="#Empty submit"/>
			</p>
			<p>
				<ui:eventButton eventId="start" labelId="#Start new flow"/>
			</p>

			<script type="text/javascript">
				// Create dictionaries using the following style:
				var userInfo = {
					name: "Amelie Crutcher",
					timeZone: "EST",
					userID: "123",
					lastLogOn: "2/2/2006"
				};
			</script>
			<ui:gwtWidgetInclude id="i18n" styleClass="gwtI18n">
				<h1>
					&#206;&#241;&#355;&#233;&#114;&#241;&#229;&#355;&#238;&#246;&#241;&#229;&#316;&#238;&#382;&#229;&#355;&#238;&#246;&#241;
					Sample
				</h1>
				<p>
					Sample that shows the use of i18n text processing classes.
				</p>
				<p>
					In a real internationalized application, all strings (including this one),
					would be internationalized.
				</p>
				<p>
					Add <a href="I18N.html?locale=fr"> locale=fr </a> to the query string to
					change the language display.
					For example, 
					<code>http://localhost:8888/com.google.gwt.sample.i18n.I18N/I18N.html?locale=fr</code>
					changes the locale to French.
				</p>
				<div id="root"/>
			</ui:gwtWidgetInclude>

		</ui:component>

	</ui:widgetContext>

</jsp:root>
