<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<!--
 The contents of this file are subject to the terms
 of the Common Development and Distribution License
 (the License). You may not use this file except in
 compliance with the License.
 
 You can obtain a copy of the License at
 https://javaserverfaces.dev.java.net/CDDL.html or
 legal/CDDLv1.0.txt. 
 See the License for the specific language governing
 permission and limitations under the License.
 
 When distributing Covered Code, include this CDDL
 Header Notice in each file and include the License file
 at legal/CDDLv1.0.txt.    
 If applicable, add the following below the CDDL Header,
 with the fields enclosed by brackets [] replaced by
 your own identifying information:
 "Portions Copyrighted [year] [name of copyright owner]"
 
 [Name of File] [ver.__] [Date]
 
 Copyright 2005 Sun Microsystems Inc. All Rights Reserved
-->

<jsp:root
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jstl/core"
    xmlns:ui="http://araneaframework.org/tag-library/standard_rt"
    xmlns:t="http://araneaframework.org/tag-library/template"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:h="http://java.sun.com/jsf/html"
    version="2.0">
    
    <jsp:directive.page contentType="text/html;charset=UTF-8"/>
    
    <ui:widgetContext>
    
    <f:view>
	   <h:form id="companyForm">
	   	<h2>Add company here</h2>
		
		<h:outputLabel value="Name:" id="Name" for="companyName"/>
		<h:inputText id="companyName" label="Name" value="#{widget.name}"/>
		<br/>
		<h:outputLabel value="Address:" id="Address"/>
		<h:inputText id="companyAddress" label="Address" value="#{widget.address}"/>
	   </h:form>
	   <br/>
	   
 	<t:componentActions>
	   <ui:eventButton eventId="endFlow" labelId="#Commit"/>
		</t:componentActions>
    </f:view>
    
    </ui:widgetContext>
</jsp:root>
