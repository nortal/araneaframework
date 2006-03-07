<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" 
       xmlns:ui="http://araneaframework.org/tag-library/template" 
       xmlns:c="http://java.sun.com/jstl/core"
       xmlns:fmt="http://java.sun.com/jstl/fmt"
       version="1.2">

       <ui:widgetContext>
               <ui:form id="editForm">
                       <!-- Label -->
                       <ui:componentHeader>
                               <ui:componentName>File upload demo</ui:componentName>
                       </ui:componentHeader>
                       <ui:component>



                               <!-- Body -->
                               <ui:componentForm>
                                       <ui:row>
                                               <ui:cell styleClass="name">
                                                       <ui:label id="select"/>
                                               </ui:cell>
                                               <ui:cell styleClass="data">
                                                       <ui:select id="select"/>
                                               </ui:cell>
                                       </ui:row>
                                       <ui:row>
                                               <ui:cell styleClass="name">
                                                       <ui:fileUpload id="file"/>
                                               </ui:cell>
                                               <ui:cell styleClass="data">
                                                       <ui:button id="upload"/>
                                               </ui:cell>
                                       </ui:row>
                                       <ui:row>
                                               <ui:cell colSpan="2">
                                                       <c:forEach var="file" items="${contextWidget.data.files}">
                                                               -<ui:nbsp/><c:out value="${file}"/>
                                                               <ui:newLine/>
                                                       </c:forEach>
                                               </ui:cell>
                                       </ui:row>
                               </ui:componentForm>

                       </ui:component>
               </ui:form>
       </ui:widgetContext>
</jsp:root>