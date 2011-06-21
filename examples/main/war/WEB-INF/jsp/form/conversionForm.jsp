<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
  xmlns:ui="http://araneaframework.org/tag-library/standard"
  xmlns:tui="http://araneaframework.org/tag-library/template"
  version="2.1">

  <!-- ConversionFormWidget context starts here -->
  <ui:widgetContext>

    <!-- Set the form widget context, in order to access and render form elements -->
    <ui:form id="conversionForm">

      <tui:componentHeader>
        <tui:componentName><fmt:message key="form.roman.title" /></tui:componentName>
      </tui:componentHeader>

      <tui:component>
        <tui:componentForm rowClasses="cols2" cellClasses="name, inpt" >

          <!-- The row and cell tags correspond to HTML tags TR and TD. -->
          <ui:row>
            <!-- Form element controls can be accessed in the context of form element created using the ui:formElement tag -->
            <ui:formElement id="toRoman">

              <ui:cell>
                <ui:label />
              </ui:cell>

              <ui:cell>
                <ui:numberInput />
                <ui:nbsp />
                <ui:eventButton labelId="form.convert" eventId="toRoman" styleClass="submit" />
              </ui:cell>

            </ui:formElement>
          </ui:row>

          <ui:row>
            <!-- The second input is rendered here -->
            <ui:formElement id="fromRoman">

              <ui:cell>
                <ui:label />
              </ui:cell>

              <ui:cell>
                <ui:textInput />
                <ui:nbsp />
                <ui:eventButton labelId="form.convert" eventId="fromRoman" styleClass="submit" />
              </ui:cell>

            </ui:formElement>
          </ui:row>

        </tui:componentForm>
      </tui:component>

    </ui:form>

  </ui:widgetContext>
</jsp:root>
