<?xml version="1.0" encoding="UTF-8"?>
<jsp:root 
    xmlns:jsp="http://java.sun.com/JSP/Page" 
    xmlns:c="http://java.sun.com/jstl/core" 
    xmlns:ui="http://araneaframework.org/tag-library/template" version="1.2">
    <jsp:directive.page contentType="text/html; charset=UTF-8"/>
    
    <!-- This is a master page of Aranea Widget QuickStart Example (examples/widgetQuickStart/war/WEB-INF/jsp/root.jsp) -->
    <ui:root>
        <ui:viewPort>

            <ui:widgetContext>
                <html>
                    <head>
                        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

                        <!-- Scripts and Styles-->
												<ui:importStyles file="styles/_styles_global.css" media="all"/>
												<ui:importStyles file="styles/_styles_new.css" media="screen" />
												<ui:importStyles file="styles/_styles_screen.css" media="screen"/>
												<ui:importStyles file="styles/_styles_print.css" media="print" />
                        <ui:importScripts includeTemplate="true"/>
                        
                        <title>Aranea Widget QuickStart Example</title>
                    </head>

                    <body>
                        <div id="cont1">                                                    
													<div id="header">
														<div class="box1">
															<a href="#" id="logo"><img src="gfx/logo_aranea_print.gif" alt="" /></a>
															<div id="menu1">
																<div class="item"><!-- ASD --></div>
															</div>
														</div>
													</div>
													<div class="stripe1"><ui:nbsp/></div>
													<div id="wholder">
														<div id="leftcol"><!-- ASD --></div>															
																													
														<div id="content">
															<ui:messages/>
															<ui:systemForm method="POST">
																<div class="w100p">
																<ui:widgetInclude id="flow"/>
																</div>
															</ui:systemForm>
														</div> 
														</div>                                                                                                              
                        <div class="clear1"><ui:nbsp/></div>         
												<div id="footer">
													<div class="box1"> © Webmedia 2006 <i>|</i>
														<a href="mailto:info@araneaframework.org">info@araneaframework.org</a>
													</div>
												</div>
												</div>
                    </body>
                </html>
            </ui:widgetContext>

        </ui:viewPort>
    </ui:root>

</jsp:root>