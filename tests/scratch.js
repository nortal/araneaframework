
  /**
  * Function that is called when a context of a certain widget is reached.
  * Assumes that it is in a context of a containing systemform.
  */
 function uiWidgetContext(widgetName){
   if (!uiSystemFormProperties) window.status = 'Javascript error: current system form context not found!';
   else if (!uiSystemFormProperties.widgets[widgetName]) {
     uiSystemFormProperties.widgets[widgetName] = true;
     document.write("<input name='" + widgetName + ".__present' type='hidden' value='true'/>");
   }
 } 
 
 function uiWidgetContext2(widgetName){
   if (!uiSystemFormProperties) window.status = 'Javascript error: current system form context not found!';
   else if (!uiSystemFormProperties.widgets[widgetName]) {
     uiSystemFormProperties.widgets[widgetName] = true;
		var node = document.createElement('input');
		node.type = 'hidden';
		node.value = true;
		node.name = widgetName + ".__present"; 
		var elems = document.getElementsByTagName("body");
		elems.item(0).appendChild(node);
   }
 } 
