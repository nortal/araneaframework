<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page" 
       xmlns:ui="http://araneaframework.org/tag-library/standard" xmlns:tui="http://araneaframework.org/tag-library/template" 
       xmlns:c="http://java.sun.com/jstl/core"
       xmlns:fmt="http://java.sun.com/jstl/fmt"
      version="1.2">

       <ui:widgetContext>
       		<c:if test="${not empty widget.children['feedAddForm']}">

              <tui:componentHeader>
                   <tui:componentName>Add Feed</tui:componentName>
              </tui:componentHeader>
              
              <tui:component>
              	<tui:componentForm rowClasses="cols4" cellClasses="name, inpt">
         		<ui:form id="feedAddForm">
          			<ui:row>
            		
            			<ui:formElement id="newFeedUrl">
             			<ui:cell>
             				<ui:label/>
             			</ui:cell>
             			
             			<ui:cell colspan="2">
             				<ui:textInput size="70"/>
             			</ui:cell>
            			</ui:formElement>
            			
            			<ui:cell>
            				<tui:componentActions>
            					<ui:button id="addFeedButton"/>
            				</tui:componentActions>
            			</ui:cell> 
            					               		
           			</ui:row>
           			
           			<ui:row>
           				<ui:cell colspan="4" style="align:center;">
           					Read-only subscription view:
           					<ui:link href="${viewData.publicFeedViewURL}"><c:out value="${viewData.publicFeedViewURL}"/></ui:link>
           				</ui:cell>
           			</ui:row>
           			
           		  </ui:form>
           		  </tui:componentForm>
           		</tui:component>
           		

  			
           		
           </c:if>
		               		
           	 <tui:componentHeader>
                   <tui:componentName>List of registered feeds <c:if test="${not empty viewData.userName}"> for user <c:out value="${viewData.userName}"/> </c:if> 
                   </tui:componentName>
                  
              </tui:componentHeader>

              <tui:component>

	               	<ui:list id="rssFeedList">
	               		<tui:componentList>
	               			<tui:componentListHeader/>
	               			
	               			<ui:listRows>
	               				
	               				<ui:row id="$row">
	               					<ui:cell>
	               					<ui:listRowLinkButton eventId="viewFeedDetails">
										<c:out value="${row.feedUrl}"/>
									</ui:listRowLinkButton>
	               					</ui:cell>
	                					<ui:cell>
	                					<ui:listRowLinkButton eventId="viewFeedDetails">
	                						<c:out value="${row.feedDescription}"/>
	                					</ui:listRowLinkButton>
	               					</ui:cell>
	               					<ui:cell/>
	               					
	               				</ui:row>
	               			</ui:listRows>
	
	               		</tui:componentList>
	               	 </ui:list>

                <!-- pure design tag -->
                <tui:componentActions>
                    
                </tui:componentActions>

              </tui:component>
              
         <c:if test="${not empty widget.children['feedItemList']}">
         
         <tui:componentHeader>
         	<tui:componentName><c:out value="${viewData.detailedFeedTitle}"/></tui:componentName>
         </tui:componentHeader>
         
         <tui:component>
                   	<ui:list id="feedItemList">
	               		<tui:componentList>
	               			<tui:componentListHeader/>
	               			
	               			<ui:listRows>
	               				
	               				<ui:row>
	               					<ui:cell>
										<c:out value="${row.doublinCoreElements['dc:creator']}"/>
	               					</ui:cell>
	               				
	               					<ui:cell>
										<c:out value="${row.title}"/>
	               					</ui:cell>
	               					
	               				
	               					<ui:cell>
	               						<!-- escapeXML false is dangerous but nice - expect to see p0rn soon :) -->
										<c:out value="${row.description}" escapeXml="false"/>
	               					</ui:cell>
	               					
	               					<ui:cell>
	               						<ui:listRowLinkButton eventId="gotoArticle">Go to article</ui:listRowLinkButton>
	               					</ui:cell>

	               				</ui:row>
	               			</ui:listRows>

	               		</tui:componentList>
	               	 </ui:list>
         
         </tui:component>
         
         </c:if>
               
       </ui:widgetContext>
</jsp:root>