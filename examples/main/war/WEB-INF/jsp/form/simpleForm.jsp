<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
  xmlns:c="http://java.sun.com/jsp/jstl/core"
  xmlns:fmt="http://java.sun.com/jsp/jstl/fmt"
  xmlns:ui="http://araneaframework.org/tag-library/standard"
  xmlns:tui="http://araneaframework.org/tag-library/template"
  version="2.1">

  <!-- SimpleFormWidget context starts here -->
  <ui:widgetContext>

    <!-- Variable to store how disabled form elements are rendered: as disabled or as read-only. -->
    <c:set var="disabledRenderMode" value="${viewData.readonly ? 'readonly' : 'disabled'}" />

    <!-- Set the form widget context, in order to access and render form elements -->
    <ui:form id="simpleForm">

      <!--
        Now, these are the first custom tags in template application. They do nothing particularly interesting, just
        set up the HTML DIV element containing the component header
      -->
      <tui:componentHeader>
        <tui:componentName><fmt:message key="form.title" /></tui:componentName>
      </tui:componentHeader>

      <!-- Another custom template tag, purely design-focused (look ComponentTag for source)-->
      <tui:component>

        <!--
          Another custom tag, but more interesting that previous tags. It derives from LayoutHtmlTag tag and allows
          putting row tags inside of it. Attribute rowClasses defines the styleClass attribute for rows inserted under
          componentForm here, cellClasses does the same for cells. These classes are repeating e.g first cell in a row
          is with styleClass "name", second with "inpt", and third cell again has styleClass "name". In HTML, this tag
          creates a TABLE.
        -->
        <tui:componentForm rowClasses="cols2" cellClasses="name, inpt">

          <!-- The row and cell tags correspond to HTML tags TR and TD. -->
          <ui:row>
            <ui:cell>
              <!--
                label is a form-element tag. As we are inside the form already, we provide it with form element ID and
                corresponding form element label will be shown
              -->
              <ui:label id="textbox1" />
            </ui:cell>

            <ui:cell>
              <!--
                This is another approach to providing information about form element that tag should apply to - instead
                of providing form element ID to every single tag, tags could be enclosed inside single formElement tag.
                Also note that the form element control must be suitable for the tag to render it. Each control has its
                specific tag.
              -->
              <ui:formElement id="textbox1">
                <ui:textInput styleClass="norm" disabledRenderMode="${disabledRenderMode}" />
              </ui:formElement>
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="textbox3" />
            </ui:cell>
            <ui:cell>
              <ui:textInput id="textbox3" styleClass="norm" disabledRenderMode="${disabledRenderMode}" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="textbox2" />
            </ui:cell>
            <ui:cell>
              <ui:textInput id="textbox2" styleClass="norm right" disabledRenderMode="${disabledRenderMode}" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="textarea" />
            </ui:cell>
            <ui:cell>
              <ui:textarea id="textarea" rows="4" cols="80" disabledRenderMode="${disabledRenderMode}" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="richTextarea" />
            </ui:cell>
            <ui:cell>
              <ui:richTextarea id="richTextarea" rows="4" cols="80" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="dateTime" />
            </ui:cell>
            <ui:cell>
              <ui:dateTimeInput id="dateTime" disabledRenderMode="${disabledRenderMode}" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="dateTimeRangeEnd" />
            </ui:cell>
            <ui:cell>
              <ui:dateTimeInput id="dateTimeRangeStart" disabledRenderMode="${disabledRenderMode}" />
              <ui:dateTimeInput id="dateTimeRangeEnd" disabledRenderMode="${disabledRenderMode}" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="date" />
            </ui:cell>
            <ui:cell>
              <ui:dateInput id="date" disabledRenderMode="${disabledRenderMode}" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="dateRangeEnd" />
            </ui:cell>
            <ui:cell>
              <ui:dateInput id="dateRangeStart" /> - <ui:dateInput id="dateRangeEnd" disabledRenderMode="${disabledRenderMode}" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="time" />
            </ui:cell>
            <ui:cell>
              <ui:timeInput id="time" disabledRenderMode="${disabledRenderMode}" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="timeRangeEnd" />
            </ui:cell>
            <ui:cell>
              <ui:timeInput id="timeRangeStart" disabledRenderMode="${disabledRenderMode}" />
              <ui:timeInput id="timeRangeEnd" disabledRenderMode="${disabledRenderMode}" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="intNumber" />
            </ui:cell>
            <ui:cell>
              <ui:numberInput id="intNumber" styleClass="right" disabledRenderMode="${disabledRenderMode}" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="intRangeEnd" />
            </ui:cell>
            <ui:cell>
              <ui:numberInput id="intRangeStart" styleClass="right" disabledRenderMode="${disabledRenderMode}" />
              -
              <ui:numberInput id="intRangeEnd" styleClass="right" disabledRenderMode="${disabledRenderMode}" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="floatNumber" />
            </ui:cell>
            <ui:cell>
              <ui:floatInput id="floatNumber" styleClass="right" disabledRenderMode="${disabledRenderMode}" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="floatRangeEnd" />
            </ui:cell>
            <ui:cell>
              <ui:floatInput id="floatRangeStart" styleClass="right" disabledRenderMode="${disabledRenderMode}" />
              -
              <ui:floatInput id="floatRangeEnd" styleClass="right" disabledRenderMode="${disabledRenderMode}" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="checkbox" />
            </ui:cell>
            <ui:cell>
              <!-- This input does not support read-only mode. -->
              <ui:checkbox id="checkbox" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="select" />
            </ui:cell>
            <ui:cell>
              <!-- This input does not support read-only mode. -->
              <ui:select id="select" localizeDisplayItems="false" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="selectRadio" />
            </ui:cell>
            <ui:cell>
              <!-- This input does not support read-only mode. -->
              <ui:radioSelect id="selectRadio" type="vertical" localizeDisplayItems="false" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="multiselect" />
            </ui:cell>
            <ui:cell>
              <!-- This input does not support read-only mode. -->
              <ui:multiSelect id="multiselect" size="10" localizeDisplayItems="false" />
            </ui:cell>
          </ui:row>

          <ui:row>
            <ui:cell>
              <ui:label id="multiselectChecks" />
            </ui:cell>
            <ui:cell>
              <!-- This input does not support read-only mode. -->
              <ui:checkboxMultiSelect id="multiselectChecks" type="vertical" localizeDisplayItems="false" />
            </ui:cell>
          </ui:row>

        </tui:componentForm>

        <!-- pure design tag -->
        <tui:componentActions>
          <ui:button id="fillBtn" />
          <ui:button id="validateBtn" />
        </tui:componentActions>
      </tui:component>

    </ui:form>

  </ui:widgetContext>
</jsp:root>
