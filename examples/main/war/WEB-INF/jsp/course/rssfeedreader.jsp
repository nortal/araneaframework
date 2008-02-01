<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" 
       xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template" 
       xmlns:c="http://java.sun.com/jstl/core"
       xmlns:fmt="http://java.sun.com/jstl/fmt"
      version="1.2">

       <ui:widgetContext>
              <!-- Label -->
              <tui:componentHeader>
                      <tui:componentName>RSS Feed Reader</tui:componentName>
              </tui:componentHeader>
              <tui:component>
					
	               	<ui:list id="rssFeedList">
	               		<tui:componentList>
	               			<tui:componentListHeader/>
	               			
	               			<ui:listRows>
	               				<ui:row>
	               					<ui:cell>
										<c:out value="${row.feedUrl}"/>
	               					</ui:cell>
	                					<ui:cell>
	                					<c:out value="${row.feedDescription}"/>
	               					</ui:cell>
	               					<ui:cell/>
	               				</ui:row>
	               			</ui:listRows>
	
	               		</tui:componentList>
	               		
	               		
	               		<tui:componentForm>
	               			<ui:row>
		               		<ui:form id="feedAddForm">
		               			<ui:formElement id="newFeedUrl">
			               			<ui:cell>
			               				<ui:label/>
			               			</ui:cell>
			               			
			               			<ui:cell>
			               				<ui:textInput/>
			               			</ui:cell>
		               			</ui:formElement>
		               		</ui:form>
		               		</ui:row>
		               	</tui:componentForm>
		               	
	               	 </ui:list>
	               	 

              
                <!-- pure design tag -->
                <tui:componentActions>
                    
                </tui:componentActions>

              </tui:component>
               
       </ui:widgetContext>
</jsp:root>