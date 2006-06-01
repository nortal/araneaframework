/**
 * Copyright 2006 Webmedia Group Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
**/

//
// Aranea functions
// 

/**
 * Submit event in a given system form
 *
 * @author Jevgeni Kabanov
 */
function araneaStandardSubmitEvent(systemForm, widgetId, eventId, eventParam, precondition) {
	return uiStandardSubmitEvent(
		systemForm, 
		widgetId, 
		eventId, 
		eventParam, 
		function() {araneaSubmitEvent(systemForm, widgetId, eventId, eventParam)}, 
		precondition);
}

/**
 * Submit event in a given system form
 *
 * @author Jevgeni Kabanov
 */
function araneaSubmitEvent(systemForm, widgetId, eventId, eventParam, updateRegions) {
	systemForm.widgetEventPath.value = widgetId;
	systemForm.widgetEventHandler.value = eventId;	
	systemForm.widgetEventParameter.value = eventParam;
	uiSystemFormSubmit(systemForm, updateRegions);
}

/**
 * More compact aranea submit for using with uiStandardSubmit*Event
 */
function araneaSubmitEvent_C(standardParams, updateRegions) {
	standardParams.systemForm.widgetEventPath.value = standardParams.widgetId;
	standardParams.systemForm.widgetEventHandler.value = standardParams.eventId;
	standardParams.systemForm.widgetEventParameter.value = standardParams.eventParam;
	uiSystemFormSubmit(standardParams.systemForm, updateRegions);
}