<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jstl/core"
  xmlns:fmt="http://java.sun.com/jstl/fmt"
  xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template"
  version="1.2">

  <ui:widgetContext>
    <ui:form id="testform">

      <tui:componentHeader>
        <tui:componentName>Autocomplete (suggestion) demo</tui:componentName>
      </tui:componentHeader>

      <tui:component>
      	<p>
      		It is well known fact that users do not know what they really want. With input suggestions, you can help
      		them to overcome their initial cluelessness and maybe learn correct spelling of long words :)
	    </p>
	        
	    <p>
	       When you type at least one character in input field below, browser will ask the server for suggested completions
	       (which in this case are country names). User is by no means tied to suggested inputs though, 'Mägi-Karabahh'
	       is as feasible input to below text-field as any other.
	    </p>

	    <br/>
      
        <tui:componentForm>

          <ui:row>

            <ui:formElement id="acinput">
              <ui:cell styleClass="name">
                <ui:label />
              </ui:cell>

              <ui:cell styleClass="inpt">
                <ui:autoCompleteTextInput />
              </ui:cell>
            </ui:formElement>

          </ui:row>

        </tui:componentForm>

        <tui:componentActions>
          <ui:eventButton eventId="test" labelId="#Test"/>
        </tui:componentActions>
      </tui:component>
    </ui:form>

  </ui:widgetContext>
</jsp:root>
