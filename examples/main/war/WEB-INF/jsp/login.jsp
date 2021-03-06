<jsp:root 
	xmlns:jsp="http://java.sun.com/JSP/Page" 
	xmlns:c="http://java.sun.com/jsp/jstl/core" 
	xmlns:ui="http://araneaframework.org/tag-library/standard" 
	xmlns:tui="http://araneaframework.org/tag-library/template"
	version="2.1">

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
				<title>Aranea Template Application Login Screen</title>
			</head>

			<ui:body>
				<!-- This tag will render an HTML form tag along with some Aranea-specific hidden fields. 
						 It is strongly suggested to have only one system form in the template and have it submitted using 
						 POST. This will ensure that no matter what user does no data is ever lost. -->
				<ui:systemForm id="aranea-form" method="post">

					<ui:onLoadEvent event="Form.focusFirstElement(araneaPage().getSystemForm());"/>

					<div id="outer">
						<div id="middle">
							<div id="inner">
								<div class="login">

									<div id="login-logo">
										<tui:image src="gfx/logo_aranea_login.jpg"/>
									</div>

									<div class="a">
										<!-- This tag will render messages of given type if they are present in current MessageContext,
												 i.e. message about failured login (wrong username or password).
										-->
										<ui:messages styleClass="msg-error"/>

										<!-- This is the form we added to our TemplateLoginWidget.
												 Tag makes it the current form context tag and allows accessing
												 form's subelements just by their names, no path is needed. -->
										<ui:form id="loginForm">

											<ui:formElement id="username">
												<!-- Outputs the form element label -->
												<ui:label/>

												<!-- Outputs the input field for username -->
												<ui:textInput styleClass="usr"/>
											</ui:formElement>

											<ui:formElement id="password">
												<ui:label/>
												<ui:passwordInput styleClass="pwd"/>
											</ui:formElement>

											<div class="clear1"><ui:nbsp/></div>

											<div class="actions">
												<!-- Render buttons that will submit all systemForm data,
														 and tie them with events defined in TemplateLoginWidget.
												-->
												<ui:eventButton eventId="login" labelId="#Login"/>
												<ui:eventButton id="bypassButton" eventId="bypass" labelId="#Bypass login"/>
												<ui:formEnterKeyboardHandler fullElementId="bypassButton" />
											</div>

											<div class="clear1"><ui:nbsp/></div>

										</ui:form>
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