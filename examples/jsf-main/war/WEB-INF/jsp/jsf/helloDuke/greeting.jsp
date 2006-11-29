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
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:h="http://java.sun.com/jsf/html"
    version="2.0">
    
    <jsp:directive.page contentType="text/html;charset=UTF-8"/>

    <h2>My name is Duke.  What is yours?</h2>
    <jsp:useBean id="UserNameBean" class="org.araneaframework.example.jsf.web.jsf.helloDuke.UserNameBean" scope="session" />
    <f:view>
<!--    <h:form id="helloForm" >-->
        <h:graphicImage id="waveImg" url="gfx/helloDuke/wave.med.gif" />
  	<h:inputText id="username"
			value="#{UserNameBean.userName}"/>
  	<h:commandButton id="submit" action="success" value="Submit" 
		          type="submit" />
<!--    </h:form>-->
    </f:view>
</jsp:root>