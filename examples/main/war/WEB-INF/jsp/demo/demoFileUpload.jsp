<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" 
       xmlns:ui="http://araneaframework.org/tag-library/template" 
       xmlns:c="http://java.sun.com/jstl/core"
       xmlns:fmt="http://java.sun.com/jstl/fmt"
      version="1.2">

       <ui:widgetContext>
                       <!-- Label -->
                       <ui:componentHeader>
                               <ui:componentName>File upload demo</ui:componentName>
                       </ui:componentHeader>
                       <ui:component>
                       		<ui:form id="uploadForm">

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
                                               <ui:formElement id="upload">
	                                               <ui:cell styleClass="data">
	                                                       <ui:button/>
	                                               </ui:cell>
	                                           </ui:formElement>
                                       </ui:row>
                               </ui:componentForm>
                           </ui:form>
                             
                       <c:if test="${not empty contextWidget.children['uploadList']}">
	                       	<ui:list id="uploadList">
	                       		<ui:componentList>
	                       			<ui:componentListHeader/>
	                       			
	                       			<ui:listRows>
	                       				<ui:row>
	                       					<ui:cell>
												<ui:listRowLinkButton eventId="selectFile">
													<c:out value="${row.originalFilename}"/>
												</ui:listRowLinkButton>
	                       					</ui:cell>
	                        					<ui:cell>
	                        						<c:out value="${row.size}"/>
	                       					</ui:cell>
	                       					<ui:cell>
	                       						<c:out value="${row.contentType}"/>
	                       					</ui:cell>
	                       					<ui:cell>
												
	                       					</ui:cell>
	                       				</ui:row>
	                       			</ui:listRows>
	
	                       		</ui:componentList>
	                       	 </ui:list>	
                       </c:if>

                       </ui:component>
               
       </ui:widgetContext>
</jsp:root>