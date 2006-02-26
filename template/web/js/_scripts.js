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

document.write('<div id="tooltip"><div id="tooltipInner" class="tooltip-normal">&nbsp;</div></div>');

// false, kui tegemist pole InternetExplorer brauseriga.
var	ie=document.all;
// false, kui tegemist pole DOM standardit toetava brauseriga brauseriga.
var	dom=document.getElementById;
// false, kui tegemist pole Netscape4.X brauseriga.
var ns4=document.layers;
window.onload = function(){
	focusFirstForm();
}
function focusFirstForm(){
	if(document.getElementById('firstform')){
		o=document.getElementById('firstform');
		focusFirstFormItem(o);
	} else {
		if (document.getElementById('headersearch')){
			document.getElementById('headersearch').focus();
		} else {
			return;
		}
	}
}
function focusFirstFormItem(o){
	for (var i=0; i<o.childNodes.length; i++)
	{
		if (o.childNodes.item(i).style && o.childNodes.item(i).style.display == "none")
		{
			continue;
		}
		var temp = o.childNodes.item(i).nodeName.toUpperCase();
		var text = false;
		if (temp == "INPUT")
		{
			var type = o.childNodes.item(i).getAttribute("type");
			if (type == "text" || type == "radio" || type == "checkbox")
			{
				text = true;
			}
		}
		if (text || temp == "SELECT" || temp == "SELECT")
		{
			o.childNodes.item(i).focus();
			return true;
		}
		else
		{
			var result = focusFirstFormItem(o.childNodes.item(i));
			if (result)
			{
				return true;
			}
		}
	}
	return false;
}
function toggleId(id){
	if (document.getElementById(id).style.display == 'none'){
		if (document.getElementById(id).tagName == 'TR' && !document.all){
			document.getElementById(id).style.display = "table-row";
		} else if ((navigator.userAgent.indexOf("Opera")!=-1 || navigator.userAgent.indexOf("Opera/")!=-1) && document.getElementById(id).tagName == 'TR') {
			document.getElementById(id).style.display = "table-row";
		} else {
			document.getElementById(id).style.display = 'block';
		}
	} else {
		document.getElementById(id).style.display = 'none';
	}
}
function toggleItems(){
	var count=toggleItems.arguments.length;
	for (var i=0; i<count; i++)
	{
		toggleId(toggleItems.arguments[i]);
	}
	return false;
}
function toggleCheckboxes(){
	var count=toggleCheckboxes.arguments.length;
	var check = 0;
	for (var i=0; i<count; i++)
	{
		if (i==0){
			if(toggleCheckboxes.arguments[i].checked){
				var check=1;
			} else {
				var check=0;
			}
		} else {
			if(check==1){
				document.getElementById(toggleCheckboxes.arguments[i]).checked=true;
			} else {
				document.getElementById(toggleCheckboxes.arguments[i]).checked=false;
			}
		}
		//toggleId(toggleItems.arguments[i]);
	}
	return false;
}
function hideElement( elmID, overDiv )
{
  if( ie )
  {
	for( i = 0;	i <	document.all.tags( elmID ).length; i++ )
	{
	  obj =	document.all.tags( elmID )[i];
	  if( !obj || !obj.offsetParent	)
	  {
		continue;
	  }
	  // Find the element's	offsetTop and offsetLeft relative to the BODY tag.
	  objLeft	= obj.offsetLeft;
	  objTop	= obj.offsetTop;
	  objParent	= obj.offsetParent;
	  while( objParent.tagName.toUpperCase() !=	"BODY" )
	  {
		objLeft	 +=	objParent.offsetLeft;
		objTop	 +=	objParent.offsetTop;
		objParent =	objParent.offsetParent;
		if (objParent.tagName == "HTML") break;
	  }
	  objHeight	= obj.offsetHeight;
	  objWidth = obj.offsetWidth;
	  if(( overDiv.offsetLeft +	overDiv.offsetWidth	) <= objLeft );
	  else if((	overDiv.offsetTop +	overDiv.offsetHeight ) <= objTop );
	  else if( overDiv.offsetTop >=	( objTop + objHeight ));
	  else if( overDiv.offsetLeft >= ( objLeft + objWidth ));
	  else
	  {
		obj.style.visibility = "hidden";
	  }
	}
  }
}
function showElement( elmID	)
{
  if( ie )
  {
	for( i = 0;	i <	document.all.tags( elmID ).length; i++ )
	{
	  obj =	document.all.tags( elmID )[i];
	  if( !obj || !obj.offsetParent	)
	  {
		continue;
	  }
	  obj.style.visibility = "";
	}
  }
}
function openPopup(ref,w,h){
	var winl = (screen.width-w)/2;
	var wint = (screen.height-h)/2;
	if (winl < 0) winl = 0;
	if (wint < 0) wint = 0;
	var page = ref;
	windowprops = "height="+h+",width="+w+",top="+ wint +",left="+ winl +",location=no,"
	+ "scrollbars=yes,menubars=no,toolbars=no,resizable=no,status=yes";
	window.open(page, "Popup", windowprops);
	return false;
}
function showTooltip(ref,txt,type){
	var leftpos = 0;
	var toppos = 0;
	aTag = ref;
	do
	{
		aTag = aTag.offsetParent;
		if (aTag.tagName == "HTML") break;
		leftpos	+= aTag.offsetLeft;
		toppos += aTag.offsetTop;
	}
	while(aTag.tagName!="BODY");
	leftpos = leftpos + ref.offsetLeft + ref.offsetWidth + 5;
	toppos = toppos + ref.offsetTop;

	if(type=='error'){
		document.getElementById("tooltipInner").className = 'tooltip-error';
	} else {
		document.getElementById("tooltipInner").className = 'tooltip-normal';
	}
	document.getElementById("tooltip").style.left = leftpos+'px';
	document.getElementById("tooltip").style.top = toppos+'px';
	document.getElementById("tooltipInner").innerHTML = txt;
	document.getElementById("tooltip").style.display = 'block';

	hideElement( 'SELECT', document.getElementById("tooltip") );
	hideElement( 'APPLET', document.getElementById("tooltip") );

}
function hideTooltip(){
	document.getElementById("tooltip").style.display = 'none';
	showElement( 'SELECT', document.getElementById("tooltip") );
	showElement( 'APPLET', document.getElementById("tooltip") );
}
