<?xml version="1.0" encoding="UTF-8"?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:c="http://java.sun.com/jstl/core"
	xmlns:fmt="http://java.sun.com/jstl/fmt"
	xmlns:ui="http://araneaframework.org/tag-library/template"
	version="1.2">

	<ui:widgetContext>

		<ui:form id="form">

			<script type="text/javascript">
				var formId = '<c:out value="${formFullId}" />';
				var callback = function(request, response) {
					if (request.status != 200) {
						alert(request.responseText);	// Very ugly
						return;
					}
					var fields = request.responseText.split('|');
					$(formId + '.quantity').value = fields[0];
					$(formId + '.vat').value = fields[1];
					$(formId + '.total').value = fields[2];
					$(formId + '.vatTotal').value = fields[3];
					$(formId + '.bigTotal').value = fields[4];
				};
				var widgetId = '<c:out value="${widgetId}" />';
			</script>
				
			<ui:componentHeader>
				<ui:componentName>Form with actions</ui:componentName>
			</ui:componentHeader>

			<ui:component>

				<ui:componentForm>

					<ui:row>
						<ui:cell>
							Sauron's Chocolate Bar
						</ui:cell>

						<ui:cell style="text-align: right">
							<ui:formElement id="price">
								<ui:label />
								<ui:floatInput style="text-align: right" />
							</ui:formElement>
						</ui:cell>

						<ui:cell style="text-align: right">
							<ui:formElement id="quantity">
								<ui:label />
								<ui:numberInput style="text-align: right" />
								<script type="text/javascript">
									Event.observe(formId + '.quantity', 'change', function(event) {
										araneaPage().action(Event.element(event), 'quantityChange', widgetId, Event.element(event).value, false, callback);
									});
								</script>
							</ui:formElement>
						</ui:cell>

						<ui:cell style="text-align: right">
							<ui:formElement id="total">
								<ui:label />
								<ui:floatInput style="text-align: right" />
							</ui:formElement>
						</ui:cell>
					</ui:row>


					<ui:row>
						<ui:cell>
							<ui:label id="vat" />
						</ui:cell>

						<ui:cell>
						</ui:cell>

						<ui:cell style="text-align: right">
							<ui:formElement id="vat">
								<ui:floatInput style="text-align: right" />
								<script type="text/javascript">
									Event.observe(formId + '.vat', 'change', function(event) {
										araneaPage().action_6(document.system_form_0, 'vatChange', widgetId, $F(formId + '.vat'), false, callback);
									});
								</script>
							</ui:formElement>
						</ui:cell>

						<ui:cell style="text-align: right">
							<ui:floatInput id="vatTotal" style="text-align: right" />
						</ui:cell>
					</ui:row>

					<ui:row>
						<ui:cell>
							<ui:label id="bigTotal" />
						</ui:cell>

						<ui:cell>
						</ui:cell>

						<ui:cell>
						</ui:cell>

						<ui:cell style="text-align: right">
							<ui:floatInput id="bigTotal" style="text-align: right" />
						</ui:cell>
					</ui:row>

				</ui:componentForm>

			</ui:component>

		</ui:form>

	</ui:widgetContext>
</jsp:root>
