<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
  xmlns:ui="http://araneaframework.org/tag-library/standard"
  xmlns:tui="http://araneaframework.org/tag-library/template"
  version="2.1">

  <!-- ConversionFormWidget context starts here -->
  <ui:widgetContext>

    <!-- Set the form widget context, in order to access and render form elements -->
    <ui:form id="validationForm">

      <tui:componentHeader>
        <tui:componentName><fmt:message key="form.valid.title" /></tui:componentName>
      </tui:componentHeader>

      <tui:component>
        <tui:componentForm rowClasses="cols2" cellClasses="name, inpt" >

          <!-- The row and cell tags correspond to HTML tags TR and TD. -->
          <ui:row>
            <ui:cell>
              <ui:label id="first" />
            </ui:cell>
            <ui:cell>
              <ui:numberInput id="first" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="second" />
            </ui:cell>
            <ui:cell>
              <ui:numberInput id="second" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="third" />
            </ui:cell>
            <ui:cell>
              <ui:numberInput id="third" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="fourth" />
            </ui:cell>
            <ui:cell>
              <ui:numberInput id="fourth" />
            </ui:cell>
          </ui:row>

        </tui:componentForm>

        <tui:componentActions>
          <ui:eventButton eventId="validate" labelId="form.validate" />
        </tui:componentActions>

      </tui:component>

    </ui:form>

  </ui:widgetContext>
</jsp:root>
