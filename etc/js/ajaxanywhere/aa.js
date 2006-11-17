/*
	 The file has been changed extensively.
	 Copyright 2006  Webmedia Ltd. http://www.webmedia.ee
	 
	 Copyright 2005  Vitaliy Shevchuk (shevit@users.sourceforge.net)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

AjaxAnywhere.defaultInstanceName = "default";
// constructor;
function AjaxAnywhere() {

    this.id = AjaxAnywhere.defaultInstanceName;
    this.formName = null;
    this.notSupported = false;
    this.updateRegions = [];

    if (window.XMLHttpRequest) {
        this.req = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        try {
            this.req = new ActiveXObject("Msxml2.XMLHTTP");
        } catch(e) {
            try {
                this.req = new ActiveXObject("Microsoft.XMLHTTP");
            } catch(e1) {
                this.notSupported = true; /* XMLHTTPRequest not supported */
            }
        }
    }

    if (this.req == null || typeof this.req == "undefined")
        this.notSupported = true;
}

/**
* Returns a Form object that corresponds to formName property of this AjaxAnywhere class instance.
*/
AjaxAnywhere.prototype.findForm = function () {
    var form;
    if (this.formName != null)
        form = document.forms[this.formName];
    else if (document.forms.length > 0)
        form = document.forms[0];

    if (typeof form != "object") {
        //alert("AjaxAnywhere error: Form with name [" + this.formName + "] not found");
    }
    return form;
}

/**
* Binds this instance to window object using "AjaxAnywhere."+this.id as a key.
*/
AjaxAnywhere.prototype.bindById = function () {
    var key = "AjaxAnywhere." + this.id;
    window[key] = this;
	 return key;
}

/**
* Finds an instance by id.
*/
AjaxAnywhere.findInstance = function(id) {
    var key = "AjaxAnywhere." + id;
    return window[key];
}

/**
* This function is used to submit all form fields by AJAX request to the server.
* If the form is submited with &lt;input type=submit|image&gt;, submitButton should be a reference to the DHTML object. Otherwise - undefined.
*/
AjaxAnywhere.prototype.submitAJAX = function(ajaxRequestId) {

    this.bindById();

    var form = this.findForm();
	 
	 if (form == null)
		 return false;

    var url = form.action;
    if (url == "")
        url = location.href;

    this.dropPreviousRequest();
    this.ajaxRequestId = ajaxRequestId;

    this.req.open("POST", url, true);
    this.req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
    
    araneaPage().getLogger().info("Sending AJAX request '" + ajaxRequestId + "'");
    
    var postData = "&" + this.preparePostData() + "&updateRegions=" + this.updateRegions + "&ajaxRequestId=" + ajaxRequestId;
    this.sendPreparedRequest(postData);
	return true;
}
/**
* sends a GET request to the server.
*/
AjaxAnywhere.prototype.getAJAX = function(url) {

    this.bindById();

    this.dropPreviousRequest();

    url += (url.indexOf("?") != -1) ? "&" : "?";

    url += "aa_rand=" + Math.random();

    this.req.open("GET", url, true);

    this.sendPreparedRequest("");
}

/**
* @private
*/
AjaxAnywhere.prototype.sendPreparedRequest = function (postData) {
	 var callbackResultKey = this.id + "_callbackReturned";
	 var test = window[callbackResultKey];
	 if (!(typeof test == 'undefined' || test == true))
		 return 1;

    var callbackKey = this.id + "_callbackFunction";
    if (typeof window[callbackKey] == "undefined")
        window[callbackKey] = new Function("AjaxAnywhere.findInstance(\"" + this.id + "\").callback(); ");
    this.req.onreadystatechange = window[callbackKey];

    this.showLoadingMessage();
	 window[callbackResultKey] == false;
    this.req.send(postData);
}
/**
* Used internally by AjaxAnywhere. Aborts previous request if not completed.
*/
AjaxAnywhere.prototype.dropPreviousRequest = function() {
    if (this.req != null && this.req.readyState != 0 && this.req.readyState != 4) {
    	araneaPage().getLogger().warn("Dropping AA request.");
        // abort previous request if not completed
        this.req.onreadystatechange = null;
        this.req.abort();
        this.handlePreviousRequestAborted();
    }
}

/**
* Internally used to prepare Post data.
* If the form is submited with &lt;input type=submit|image&gt;, submitButton is a reference to the DHTML object. Otherwise - undefined.
*/
AjaxAnywhere.prototype.preparePostData = function(submitButton) {
    var form = this.findForm();
    var result = "";
    for (var i = 0; i < form.elements.length; i++) {
        var el = form.elements[i];
        if (el.tagName.toLowerCase() == "select") {
            for (var j = 0; j < el.options.length; j++) {
                var op = el.options[j];
                if (op.selected)
                    result += "&" + escape(el.name) + "=" + encodeURIComponent(op.value);
            }
        } else if (el.tagName.toLowerCase() == "textarea") {
            result += "&" + escape(el.name) + "=" + encodeURIComponent(el.value);
        } else if (el.tagName.toLowerCase() == "input") {
            if (el.type.toLowerCase() == "checkbox" || el.type.toLowerCase() == "radio") {
                if (el.checked)
                    result += "&" + escape(el.name) + "=" + encodeURIComponent(el.value);
            } else if (el.type.toLowerCase() == "submit" || el.type.toLowerCase() == "image") {
                if (el == submitButton) // is "el" the submit button that fired the form submit?
                    result += "&" + escape(el.name) + "=" + encodeURIComponent(el.value);
            } else if (el.type.toLowerCase() != "button") {
                result += "&" + escape(el.name) + "=" + encodeURIComponent(el.value);
            }
        }
    }
    return result;
}

/**
* A callback. internally used
*/
AjaxAnywhere.prototype.callback = function() {
   if (this.req.readyState == 4) {
     this.onBeforeResponseProcessing();
     this.hideLoadingMessage();
    
     if (this.req.status) {
      text = this.req.responseText;
      
      if (this.req.status == 200) {
        araneaPage().getLogger().info("Processing ajax response '" + extractResponseId(text) + "'");
        updateRegions(text);
        
        var trId = extractTransactionId(text);

        if (this.systemForm.transactionId)
          this.systemForm.transactionId.value = trId;
        else {
          var el = createNamedElement("input", "transactionId");
          el.type = "hidden";
          el.value = trId;
          this.systemForm.appendChild(el);
        }
      } 
     else if (this.req.status == 302) {
        window.location.href = window.location.href;
      }
      else {           
        document.body.innerHTML = text;
      }

      this.onAfterResponseProcessing();
    }
  }
}

/**
*  Default sample loading message show function. Overrride it if you like.
*/
AjaxAnywhere.prototype.showLoadingMessage = function() {
		var defaultKey = "AA_default_main_loading_div";
		
		if (document.getElementById(defaultKey) == null) {
		
	    var div = document.getElementById("AA_" + this.id + "_loading_div");
	    
	    if (div == null) {
	        div = document.createElement("DIV");
	        
	        document.body.appendChild(div);
	        div.id = "AA_" + this.id + "_loading_div";
					var msg = this.loadingMessage?this.loadingMessage:"Loading...";
					
	        div.innerHTML = "&nbsp;"+msg;
	        div.style.position = "absolute";
	        div.style.border = "1 solid black";
	        div.style.color = "white";
	        div.style.backgroundColor = "#e01601";
	        div.style.width = "100px";
	        div.style.heigth = "50px";
	        div.style.fontFamily = "Arial, Helvetica, sans-serif";
	        div.style.fontWeight = "bold";
	        div.style.fontSize = "11px";
	    }
	    
	    div.style.top = document.body.scrollTop + "px";
	    div.style.left = (document.body.offsetWidth - 100 - (document.all?20:0)) + "px";
	    div.style.display = "";
	 }
	 else {
	 		var div = document.getElementById("AA_default_main_loading_div");
	 		div.style.display="block";
	 }
}

/**
*  Default sample loading message hide function. Overrride it if you like.
*/
AjaxAnywhere.prototype.hideLoadingMessage = function() {
		var defaultKey = "AA_default_main_loading_div";
		
		if (document.getElementById(defaultKey)==null) {
	    var div = document.getElementById("AA_" + this.id + "_loading_div");
	    if (div != null) 
	        div.style.display = "none";
	  }
		else {
			document.getElementById(defaultKey).style.display = "none";
		}
}

AjaxAnywhere.prototype.setLoadingMessage = function(loadingMessage) {
	this.loadingMessage = loadingMessage;
}

/**
* This function is used to facilitatte AjaxAnywhere integration with existing projects/frameworks.
* It substitutes default Form.sumbit().
* The new implementation calls AjaxAnywhere.isFormSubmitByAjax() function to find out if the form
* should be submitted in traditional way or by AjaxAnywhere.
*/
AjaxAnywhere.prototype.substituteFormSubmitFunction = function() {

    this.bindById();

    var form = this.findForm();

    form.submit_old = form.submit;
    var code = "var ajax = AjaxAnywhere.findInstance(\"" + this.id + "\"); " +
               "if (typeof ajax !='object' || ! ajax.isFormSubmitByAjax() ) " +
               "ajax.findForm().submit_old();" +
               " else " +
               "ajax.submitAJAX();"
    //form.submit = new Function(code);
}


/**
* Override it if you need.
*/
AjaxAnywhere.prototype.handlePreviousRequestAborted = function() {
   //alert("AjaxAnywhere default error handler. INFO: previous AJAX request dropped")
}

/**
* Override this method to implement a custom action
*/
AjaxAnywhere.prototype.onRequestSent = function () {
};
/**
* Override this method to implement a custom action
*/
AjaxAnywhere.prototype.onBeforeResponseProcessing = function () {
};
/**
* Override this method to implement a custom action
*/
AjaxAnywhere.prototype.onAfterResponseProcessing = function () {
   AraneaPage.init();
   araneaPage().onload();
};

function extractScripts(str) {
	var re = /<script[^>]*>([\s\S]*?)<\/script>/gmi;
	var tmp, result = new Array();
	while((tmp=re.exec(str))!=null)
		if (tmp[1]!=null)
			result.push(tmp[1]);
	return result;
}

function extractTransactionId(newContents) {
	var re = /<input name="transactionId" type="hidden" value="(-?[0-9]+)"\/>/
	var result = re.exec(newContents);
	return result[1];
}

function extractResponseId(response) {
  var re = /<input name="ajaxResponseId" type="hidden" value="(-?[0-9]+)"\/>/
  var result = re.exec(response);
  return result[1];
}

function extractBody(str) {
	var re = /^<body.*>(.*)<\/body>$/
	var result = re.exec(str);
	return result[1];
}

function updateRegions(str) {
  var regionBlockBegin = "<!--BEGIN:"; var commentEndMarker = "-->";
  // all update regions present in response should be updated
  var regions = new AraneaStore();
  var i = 0;
  for (var startIndex = str.indexOf(regionBlockBegin, i); startIndex != -1; startIndex = str.indexOf(regionBlockBegin, i)) {
    var endIndex = str.indexOf(commentEndMarker, startIndex);
    regions.add(str.substring(startIndex+regionBlockBegin.length, endIndex));
    i = endIndex;
  }

  regions.forEach(function(regionName) { araneaPage().debug("Updating region '" + regionName + "'"); updateRegion(regionName, str);});
}

function isUpdateRowRegion(regionId, str) {
  var b = '<!--BEGINROWS:'; var e = '-->';
  var index = str.indexOf(b+regionId+e);
  return (index != -1)
}

function extractContentsById(elemId, str) {
    var rowBlockMarker = "<!--BEGINROWS:" + elemId + "-->";
	var blockStart = "<!--BEGIN:"+elemId+"-->";
	var index = str.indexOf(blockStart);
	
	if (index == -1) {
        araneaPage().getLogger.error("Failed to find start of update region '" + elemId + "'.");
		return "";
	}

	var startIndex = index+blockStart.length;

	var rowBlockMarkerIndex = str.indexOf(rowBlockMarker);
	if (rowBlockMarkerIndex != -1)
	  startIndex = rowBlockMarkerIndex + rowBlockMarker.length;

	var blockEnd = "<!--END:"+elemId+"-->";
	index = str.indexOf(blockEnd);
	
	if (index == -1) {
		araneaPage().getLogger.error("Failed to determine end of update region '" + elemId + "'.");
		return "";
	}

	var endIndex = index;
	return str.substring(startIndex, endIndex);
}

function updateRegion(updateRegionId, str) {		
  var extracted = extractContentsById(updateRegionId, str);
  var target = document.getElementById(updateRegionId);	
  
  // if browser is IE (6?) and this updateregion updates table HTML, just setting innerHTML does not work.
  if (isUpdateRowRegion(updateRegionId, str) && document.all && target) {
    //Emptying <tbody>
    while( target.firstChild ) {
      target.removeChild( target.firstChild );
    }        		    

    //Making temp <div> and <table>
    var tempDiv = document.createElement("div");
    document.body.appendChild(tempDiv);
    tempTbodyId = updateRegionId + "TempTbody";
    tempDiv.innerHTML = "<table><tbody id=\"" + tempTbodyId + "\">" + extracted + "</tbody></table>";

    //Filling in tbody
    var tempTbody =  document.getElementById(tempTbodyId);
	
    for (var i = 0; i < tempTbody.childNodes.length; i++) {
      target.appendChild(tempTbody.childNodes[i].cloneNode(true));				
    }			
			
    for (var i = 0; i < tempTbody.rows.length; i++) {
      for (var j = 0; j < tempTbody.rows[i].cells.length; j++) {
        target.rows[i].cells[j].innerHTML = tempTbody.rows[i].cells[j].innerHTML;					
      }
    }			

    tempDiv.removeNode(true);			 				 
  }
  else if (target) {
    target.innerHTML = extracted;
  }
  // execute all the scripts
  var scripts = extractScripts(extracted);
  for(var i=0;i<scripts.length;i++) {
    var script = scripts[i];
    script = "try{"+script+"}catch(e){alert(e);}";
    eval(script);
  }	  
}

function trim(str) { 
   return str.replace(/^\s+|\s+$/, ''); 
}

// default instance.
ajaxAnywhere = new AjaxAnywhere();
ajaxKey = ajaxAnywhere.bindById();

window['ajaxanywhere/aa.js'] = true;
