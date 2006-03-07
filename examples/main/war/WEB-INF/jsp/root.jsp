<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core"  %>
<%@ taglib prefix="ui" uri="http://araneaframework.org/tag-library/template" %>

<jsp:directive.page contentType="text/html; charset=UTF-8"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<!-- This is a master page of Aranea framework template application (examples/main/war/WEB-INF/jsp/root.jsp) -->
<ui:root>
    <ui:viewPort>

        <ui:widgetContext>
            <html>
                <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

                    <!-- Scripts -->
                    <jsp:include page="scripts.jsp"/>
                    <title>Aranea&mdash;Java Web Framework Construction and Integration Kit</title>
                </head>

                <body>

                    <div id="cont1">
                        <ui:systemForm method="POST" enctype="multipart/form-data">
						<!-- Renders the menu on top of the screen -->
                         <jsp:include page="/WEB-INF/jsp/mainlayout/menu.jsp"/>
                            <div class="stripe1"><ui:nbsp/></div>

                            <div id="wholder">
                             <!-- Renders the menu on left side of screen -->
                             <jsp:include page="/WEB-INF/jsp/mainlayout/sidemenu.jsp"/>
 
                             <div id="content">
                                 <ui:messages/>
                                 <!-- Renders the menu widget itself. As MenuWidget is subclass
                                 	of StandardFlowContainerWidget, this means that actual
                                 	widget rendered here is whatever widget is on top of call
                                 	stack at the moment of rendering. -->
                                 <ui:widgetInclude id="menu"/>
                             </div>
 
                             <div class="clear1"><ui:nbsp/></div>
														 </div>
				
                        </ui:systemForm>
                    </div>
                    
                    <jsp:include page="/WEB-INF/jsp/mainlayout/footer.jsp"/>

                </body>
            </html>
        </ui:widgetContext>

    </ui:viewPort>
</ui:root>