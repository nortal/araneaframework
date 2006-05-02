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
                                       <ui:newRow>
                                               <ui:newCell styleClass="name">
                                                       <ui:label id="select"/>
                                               </ui:newCell>
                                               <ui:newCell styleClass="data">
                                                       <ui:select id="select"/>
                                               </ui:newCell>
                                       </ui:newRow>
                                       <ui:newRow>
                                               <ui:newCell styleClass="name">
                                                       <ui:fileUpload id="file"/>
                                               </ui:newCell>
                                               <ui:formElement id="upload">
	                                               <ui:newCell styleClass="data">
	                                                       <ui:button/>
	                                               </ui:newCell>
	                                           </ui:formElement>
                                       </ui:newRow>
                               </ui:componentForm>
                           </ui:form>
                             
                       <c:if test="${not empty contextWidget.children['uploadList']}">
	                       	<ui:list id="uploadList">
	                       		<ui:componentList>
	                       			<ui:componentListHeader/>
	                       			
	                       			<ui:listRows>
	                       				<ui:newRow>
	                       					<ui:newCell>
												<ui:listRowLinkButton eventId="selectFile">
													<c:out value="${row.originalFilename}"/>
												</ui:listRowLinkButton>
	                       					</ui:newCell>
	                        					<ui:newCell>
	                        						<c:out value="${row.size}"/>
	                       					</ui:newCell>
	                       					<ui:newCell>
	                       						<c:out value="${row.contentType}"/>
	                       					</ui:newCell>
	                       					<ui:newCell>
												
	                       					</ui:newCell>
	                       				</ui:newRow>
	                       			</ui:listRows>
	
	                       		</ui:componentList>
	                       	 </ui:list>	
                       </c:if>

                       </ui:component>
               
       </ui:widgetContext>
</jsp:root>