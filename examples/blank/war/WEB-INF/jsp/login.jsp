<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jstl/core" 
    xmlns:ui="http://araneaframework.org/tag-library/standard" 
    xmlns:tui="http://araneaframework.org/tag-library/template"
    version="1.2">
    <jsp:directive.page contentType="text/html; charset=UTF-8"/>

    <ui:root>
            <ui:widgetContext>
                <html>
                    <head>
                        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                        <ui:importScripts/>
                        <title>Aranea Blank Application Login Screen</title>
                    </head>

                    <ui:body>
	                    <ui:systemForm method="POST">
							<ui:onLoadEvent event="Form.focusFirstElement(araneaPage().getSystemForm());"/>

		                                        <ui:messages styleClass="msg-error"/>

		                                        <ui:form id="loginForm">
		                                        
		                                        	<ui:layout>

													<ui:row>
		                                            <ui:formElement id="username">
		                                            	<ui:cell>
		                                                <ui:label/>
		                                                </ui:cell>
		                                                <ui:cell>
		                                                <ui:textInput styleClass="usr"/>
		                                                </ui:cell>
		                                            </ui:formElement>
		                                            </ui:row>
		                                            
		                                            <ui:row>
		                                            <ui:formElement id="password">
		                                            	<ui:cell>
		                                                <ui:label/>
		                                                </ui:cell>
		                                                <ui:cell>
		                                                <ui:passwordInput styleClass="pwd"/>
		                                                </ui:cell>
		                                            </ui:formElement>
		                                            </ui:row>
		                                            
		                                            <ui:row>
		                                            
													<ui:cell colspan="2">
	                                                <ui:eventButton eventId="login" labelId="#Login"/>
	                                                <ui:eventButton id="bypassButton" eventId="justLetMeIn" labelId="#Just Let Me In"/>
    	                                          	<ui:formEnterKeyboardHandler fullElementId="bypassButton"/>
    	                                          	</ui:cell>
    	                                          	</ui:row>
    	                                          	
    	                                          	</ui:layout>

		                                        </ui:form>

		                    
	                    </ui:systemForm>
                    
                    </ui:body>

                </html>
            </ui:widgetContext>
    </ui:root>
</jsp:root>