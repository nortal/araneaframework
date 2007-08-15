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

/**
 * @author lost in time
 */

//---------------------------- Tooltip ----------------------------//
var tipwidth='400px' //default tooltip width
var tipbgcolor='#FDFCF7'  //tooltip bgcolor
var disappeardelay=100  //tooltip disappear speed onMouseout (in miliseconds)
var vertical_offset="9px" //horizontal offset of tooltip from anchor link
var horizontal_offset="20px" //horizontal offset of tooltip from anchor link

/////No further editting needed

var ie4=document.all
var ns6=document.getElementById&&!document.all

if (ie4||ns6) {
	document.write('<div id="dHTMLToolTip" class="aranea-tooltip"><Table border=0 cellspacing=0 cellpadding=0><TR><TD></TD></TR></TABLE></div>');
}

function aranea_getposOffset(what, offsettype){
	var totaloffset=(offsettype=="left")? what.offsetLeft : what.offsetTop;
	var parentEl=what.offsetParent;
	while (parentEl!=null){
		totaloffset=(offsettype=="left")? totaloffset+parentEl.offsetLeft : totaloffset+parentEl.offsetTop;
		parentEl=parentEl.offsetParent;
	}
	return totaloffset;
}


function aranea_showhide(obj, e, visible, hidden, contentwidth){
	if (ie4||ns6)
		dropmenuobj.style.left=dropmenuobj.style.top=-500
	if (contentwidth!=""){
		dropmenuobj.widthobj=dropmenuobj.style
		if (contentwidth>66) {
		  dropmenuobj.widthobj.width=tipwidth
		}
                else {
                  dropmenuobj.widthobj.width=""
                }
	}
	if (e.type=="click" && obj.visibility==hidden || e.type=="mouseover")
		obj.visibility=visible
	else if (e.type=="click")
		obj.visibility=hidden
}

function aranea_iecompattest(){
	return (document.compatMode && document.compatMode!="BackCompat")? document.documentElement : document.body
}

function aranea_clearbrowseredge(obj, whichedge){
	var edgeoffset=(whichedge=="rightedge")? parseInt(horizontal_offset)*-1 : parseInt(vertical_offset)*-1
	if (whichedge=="rightedge"){
		var windowedge=ie4 && !window.opera? aranea_iecompattest().scrollLeft+aranea_iecompattest().clientWidth-15 : window.pageXOffset+window.innerWidth-15
		dropmenuobj.contentmeasure=dropmenuobj.offsetWidth
		if (windowedge-dropmenuobj.x < dropmenuobj.contentmeasure)
			edgeoffset=dropmenuobj.contentmeasure-obj.offsetWidth
	}
	else{
		var windowedge=ie4 && !window.opera? aranea_iecompattest().scrollTop+aranea_iecompattest().clientHeight-15 : window.pageYOffset+window.innerHeight-18
		dropmenuobj.contentmeasure=dropmenuobj.offsetHeight
		if (windowedge-dropmenuobj.y < dropmenuobj.contentmeasure)
			edgeoffset=dropmenuobj.contentmeasure+obj.offsetHeight
	}
	return edgeoffset
}


function aranea_showTooltip(menucontents, obj, e){
	if (window.event) 
		event.cancelBubble=true
	else if (e.stopPropagation) 
		e.stopPropagation()
	aranea_clearhidetip();
	dropmenuobj=document.getElementById? document.getElementById("dHTMLToolTip") : dHTMLToolTip
	dropmenuobj.innerHTML=menucontents
        
	if (ie4||ns6){
		aranea_showhide(dropmenuobj.style, e, "visible", "hidden", menucontents.length)
		dropmenuobj.x=aranea_getposOffset(obj, "left")
		dropmenuobj.y=aranea_getposOffset(obj, "top")
		dropmenuobj.style.left=dropmenuobj.x-aranea_clearbrowseredge(obj, "rightedge")+"px"
		dropmenuobj.style.top=dropmenuobj.y-aranea_clearbrowseredge(obj, "bottomedge")+obj.offsetHeight+"px"
	}
}
function aranea_hideTooltip(){
	if (ie4||ns6)
		delayhide=setTimeout("aranea_hidetip()",disappeardelay)
}

function aranea_hidetip(e){
	if (typeof dropmenuobj!="undefined"){
		if (ie4||ns6)
		dropmenuobj.style.visibility="hidden"
	}
}
function aranea_clearhidetip(){
	if (typeof delayhide!="undefined")
		clearTimeout(delayhide)
}

window['aranea-tooltip.js'] = true;