<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/standard"
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>

		<!-- Label -->
		<tui:componentHeader>
			<tui:componentName>The tree demos</tui:componentName>
        </tui:componentHeader>

		<tui:component>

			<p>
				Aranea tree component allows for easy navigation of complex hierarchical structures. Backing datastore
				resides on server and only parts that are currently shown to user are fetched to client. Demos below
				demonstrate two approaches of communication<ui:entity code="mdash"/>first makes use of usual Aranea
				event API (produces HTTP requests here because no update regions are used), the other one uses Aranea
				Action API. Switching between the two models is as easy as calling <code>Treewidget.setUseActions(true)</code>.
			</p>
			
			<p>
				Although current example does not demonstrate this, it is also easy to customize rendering for each tree node.
				One can have each node rendered differently, if he so desires.
			</p>

			<p>Tree with events:</p>
			<ui:tree id="tree1"/>

			<p>Tree with actions:</p>
			<ui:tree id="tree2"/>

		</tui:component>

	</ui:widgetContext>

</jsp:root>
