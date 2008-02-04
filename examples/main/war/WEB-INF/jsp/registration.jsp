<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jstl/core" 
    xmlns:ui="http://araneaframework.org/tag-library/standard" 
    xmlns:tui="http://araneaframework.org/tag-library/template"
    version="1.2">
    <jsp:directive.page contentType="text/html; charset=UTF-8"/>

        	<!-- This tag should generally be the root of every widget JSP. It makes the widget view model accessible as an EL variable.
        	It can also be used to render a descendant widget in the same JSP with the current widget. 
        	In the latter case you should set the id attribute to the identifier path of the descendent 
        	widget in question. Note that all widget-related tags inside of this tag will assume that the widget in question
        	is their parent or ancestor (that is all the identifier pathes will start from it). -->
            <ui:widgetContext>
                <html>
                    <head>
                        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                        <!-- Include script and style definitions from another file -->
                        <jsp:include page="scripts.jsp"/>
                        <title>RSS Feed Reader Account Registration</title>
                    </head>

                    <ui:body>
                    
                    	<!-- This tag will render an HTML form tag along with some Aranea-specific hidden fields. 
                    	It is strongly suggested to have only one system form in the template and have it submitted using 
                    	POST. This will ensure that no matter what user does no data is ever lost. -->
	                    <ui:systemForm method="POST">
							<ui:onLoadEvent event="Form.focusFirstElement(araneaPage().getSystemForm());"/>

									                    
		                   <div id="outer">
		                        <div id="middle">
		                            <div id="inner">
		                                <div class="login">
		                                
		                                
		                                <div class="msg-info">
								<div>
									<div>
										<ui:messages type="info"/>
									</div>
								</div>
							</div>
							<ui:messages type="error" styleClass="msg-error"/>
		                                
		                    
		                    <tui:componentHeader>
		                    	<tui:componentName>Enter your registration data</tui:componentName>
		                    </tui:componentHeader>
		                    
		                    <tui:componentForm rowClasses="cols4" cellClasses="name,inpt">
		
                               <ui:form id="registrationForm">
                               
                               <ui:row>
                                   <ui:formElement id="username">
                                   	<ui:cell colspan="2">
                                       <ui:label/>
                                    </ui:cell>
                                    <ui:cell colspan="2">
                                       <ui:textInput styleClass="usr"/>
                                    </ui:cell>
                                   </ui:formElement>
                               </ui:row>
                                   
                                   <ui:row>
                                   <ui:formElement id="password">
                                   <ui:cell colspan="2">
                                       <ui:label/>
                                       </ui:cell>
                                       <ui:cell colspan="2">
                                       <ui:passwordInput styleClass="pwd"/>
                                       </ui:cell>
                                   </ui:formElement>
                                   </ui:row>
                                   
                                   <ui:row>
                                   <ui:formElement id="password2">
                                   <ui:cell colspan="2">
                                       <ui:label/>
                                      </ui:cell>
                                      <ui:cell colspan="2">
                                       <ui:passwordInput styleClass="pwd"/>
                                       </ui:cell>
                                   </ui:formElement>
                                   </ui:row>

                                   <div class="clear1"><ui:nbsp/></div>


                               </ui:form>
		                    </tui:componentForm>

                                 <div class="actions">
                                 	<ui:eventButton eventId="return" labelId="#Return to login screen"/>
                                     <ui:eventButton id="bypassButton" eventId="confirm" labelId="#Confirm registration"/>
                                   	<ui:formEnterKeyboardHandler fullElementId="bypassButton"/>
                                 </div>

		                    </div>
		                    </div>
		                    </div>
		                    </div>
		                    
		                    
		                    
	                    </ui:systemForm>
                    
                    </ui:body>

                </html>
            </ui:widgetContext>
</jsp:root>