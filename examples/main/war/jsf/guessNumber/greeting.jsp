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
    
    <jsp:useBean id="UserNumberBean" class="org.araneaframework.example.main.web.jsf.guessNumber.UserNumberBean" scope="session" />
    
    <f:view>
    <h:form id="helloFormZ" >
      <h2>Hi. My name is Duke.  I'm thinking of a number from
      <h:outputText lang="en_US" value="#{UserNumberBean.minimum}"/> to
      <h:outputText value="#{UserNumberBean.maximum	}"/>.  Can you guess
      it?</h2>

        <h:graphicImage id="waveImgZ" url="/jsf/guessNumber/wave.med.gif" />
  	<h:inputText id="userNoZ" label="User Number" value="#{UserNumberBean.userNumber}"
                      validator="#{UserNumberBean.validate}"/>          
	 <h:commandButton id="submitZ" action="success" value="Submit" />
         <p />
	 <h:message showSummary="true" showDetail="false" style="color: red; font-family: 'New Century Schoolbook', serif; font-style: oblique; text-decoration: overline" id="errors1Z" for="userNoZ"/>

    </h:form>
    </f:view>
</jsp:root>
