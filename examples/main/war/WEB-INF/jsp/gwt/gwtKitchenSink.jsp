<!--
	<meta name="gwt:module" content="org.araneaframework.example.gwt.hello.Hello">
-->
<script type="text/javascript">
	//TODO a more sophisticated formula to find HEAD element
	document.body.previousSibling.appendChild(
		Builder.node('meta', { name: 'gwt:module', content: 'gwt/org.araneaframework.example.gwt.kitchensink.KitchenSink=org.araneaframework.example.gwt.kitchensink.KitchenSink' })
	);
</script>
<script language="javascript" src="gwt/org.araneaframework.example.gwt.kitchensink.KitchenSink/gwt.js"> </script>
<iframe id='__gwt_historyFrame' style='width:0;height:0;border:0'></iframe>
<div id="gwtKitchenSink"> </div>
