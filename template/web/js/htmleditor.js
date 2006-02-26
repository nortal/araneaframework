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

function pasteText() {        
    var text = window.clipboardData.getData("Text");
    if (text != null) {
        text = text.replace(/(<[^>]*>)/g, '');
        text = text.replace(/&([a-z]+)(;?)/g, '');
        text = text.replace(/nbsp;/g, '');
        idContent.document.execCommand("Paste", "false", text);   
    }
}

function insertHR(editor) {        
    rtb_ID = editor;
    rtb_ID.focus();
    var hr = rtb_ID.document.createElement('HR');

    hr.width = '100%';
    hr.noshade = 'true';
    hr.size = 1;

    if (rtb_ID.document.selection.type=='Control') { 
        sel.pasteHTML(hr.outerHTML); 
    } else {
        sel = rtb_ID.document.selection.createRange();
        sel.pasteHTML(hr.outerHTML);
    }
}

function ClipBoard()
	{
		holdtext.innerText = copytext.innerText;
		Copied = holdtext.createTextRange();
		Copied.execCommand("Copy");
	}
function button_over(eButton)
	{
		eButton.style.backgroundColor = "#B5BDD6";
		eButton.style.borderColor = "darkblue darkblue darkblue darkblue";
	}
function button_out(eButton)
{
	eButton.style.backgroundColor = "";
	eButton.style.borderColor = "";
}
function button_down(eButton)
	{
		eButton.style.backgroundColor = "#8494B5";
		eButton.style.borderColor = "darkblue darkblue darkblue darkblue";
	}
function button_up(eButton)
	{
		eButton.style.backgroundColor = "#B5BDD6";
		eButton.style.borderColor = "darkblue darkblue darkblue darkblue";
		eButton = null; 
	}
var isHTMLMode=false

function document.onreadystatechange()
	{
		idContent.document.designMode="On"
	}
function cmdExec(cmd,opt) 
	{
		if (isHTMLMode){alert("Please uncheck 'Edit HTML'");return;}
		idContent.document.execCommand(cmd,"",opt);idContent.focus();
	}
function setMode(bMode)
	{
		var sTmp;
		isHTMLMode = bMode;
		if (isHTMLMode){sTmp=idContent.document.body.innerHTML;idContent.document.body.innerText=sTmp;} 
		else {sTmp=idContent.document.body.innerText;idContent.document.body.innerHTML=sTmp;}
		idContent.focus();
	}
function insertImage()
	{
		if (isHTMLMode)
			{
				alert("Please uncheck 'Edit HTML'");
				return;
			}
		var sImgSrc = prompt("Insert Image File : ", "");
		if (sImgSrc!=null) 
			{
			cmdExec("InsertImage",sImgSrc);
			}
		}
function Save() 
	{
		if (isHTMLMode){alert("Please uncheck 'Edit HTML'");return;}
		htmlEditForm.body.value=idContent.document.body.innerHTML;
		htmlEditForm.submit();
		}
function foreColor()
		{
			var arr = showModalDialog("templates/selcolor.htm","","font-family:Verdana; font-size:12; dialogWidth:30em; dialogHeight:34em" );
			if (arr != null) cmdExec("ForeColor",arr);
		}
function addModule()
		{
			var arr = showModalDialog("templates/sel_plugin.php","","font-family:Verdana; font-size:12; dialogWidth:30em; dialogHeight:17em" );
			if (arr != null)
				{
					cmdExec('Copy',arr);
					cmdExec('Paste',arr);
				}
			} 

function change_css(css_name)
{
if (idContent.document.selection.type!="None") 
{
var sel=idContent.document.selection.createRange();
var text = sel.htmlText;
text = text.replace(/(<\/span>)+/gi, "");
text = text.replace(/(<span[^>]+>)+/gi, "");
text = text.replace(/<td/gi, '<td class="'+css_name+'"');
sel.pasteHTML('<span class="'+css_name+'">'+text+'</span>');
var all_text = idContent.document.body.innerHTML;
all_text = all_text.replace(/(<\/span>)+/gi, "</span>");
all_text = all_text.replace(/(<span[^>]+>)+/gi, "$1");
idContent.document.body.innerHTML = all_text;
var sel=idContent.document.body.createTextRange();
sel.findText(text);
sel.select();
idContent.focus();
}
}

function RTB_Paste(editor,htmlmode)
{
	editor.focus();
	editor.document.execCommand('paste','',null);
}

function RTB_DeleteRow(editor,htmlmode)
{
	if (isHTMLMode){alert("Please uncheck 'Edit HTML'");return;}
	rtb_ID = editor;
	objReference=RTB_GetRangeReference(rtb_ID);
	objReference=RTB_CheckTag(objReference,'/^(TABLE)|^(TR)|^(TD)|^(TBODY)/');
	switch(objReference.tagName)
	{
		case 'TR' :var rowIndex = objReference.rowIndex;//Get rowIndex
		var parentTable=objReference.parentElement.parentElement;
		parentTable.deleteRow(rowIndex);
		break;
		case 'TD' :var cellIndex=objReference.cellIndex;
		var parentRow=objReference.parentElement;//Get Parent Row
		var rowIndex = parentRow.rowIndex;//Get rowIndex
		var parentTable=objReference.parentElement.parentElement.parentElement;
		parentTable.deleteRow(rowIndex);
		if (rowIndex>=parentTable.rows.length)
		{
			rowIndex=parentTable.rows.length-1;
		}
		if (rowIndex>=0)
		{
			var r = rtb_ID.document.body.createTextRange();
			r.moveToElementText(parentTable.rows[rowIndex].cells[cellIndex]);
			r.moveStart('character',r.text.length);
			r.select();
		}
		else
		{
			parentTable.removeNode(true);
		}
		break;
		default :return;
	}
}

function RTB_Copy(editor,htmlmode)
{
	editor.focus();
	editor.document.execCommand('copy','',null);
}

function RTB_InsertDefaultTable(editor,htmlmode)
{
	if (isHTMLMode){alert("Please uncheck 'Edit HTML'");return;}
	RTB_InsertTable(editor,3,3);
}

function RTB_InsertTable(rtb_ID,rows,columns)
{
	if (isHTMLMode){alert("Please uncheck 'Edit HTML'");return;}
	rtb_ID.focus();
	var newTable = rtb_ID.document.createElement('TABLE');
	for(y=0; y<rows; y++)
	{
		var newRow = newTable.insertRow();
                if (y == 0) {
                    newRow.setAttribute("bgcolor", "#EFF4F9");
                }
                
		for(x=0; x<columns; x++)
		{
			var newCell = newRow.insertCell();
			if ((y==0)&&(x==0)) newCell.id='ura'; 
                        if (y == 0) {
                            newCell.className = 'tabel-p2is-2';
                            newCell.setAttribute("bgcolor", "#EFF4F9");
                            newCell.setAttribute("background", "gfx/bg06.gif");
                        } else {
                            newCell.className = 'tabel-andmed';
                        }
		}
	}
	newTable.border = 0;
	newTable.cellspacing = 1;
	newTable.cellpadding = 4;
	newTable.width = '100%';
	newTable.bgcolor = '#C9D4DE';

	if (rtb_ID.document.selection.type=='Control') { sel.pasteHTML(newTable.outerHTML); }
	else
	{
		sel = rtb_ID.document.selection.createRange();
		sel.pasteHTML(newTable.outerHTML);
	}

	var r = rtb_ID.document.body.createTextRange();
	var item=rtb_ID.document.getElementById('ura');
	r.moveToElementText(item);
	r.moveStart('character',r.text.length);
	r.select();
}


function RTB_DeleteColumn(editor,htmlmode)
{
	if (isHTMLMode){alert("Please uncheck 'Edit HTML'");return;}
	rtb_ID = editor;
	objReference=RTB_GetRangeReference(rtb_ID);
	objReference=RTB_CheckTag(objReference,'/^(TABLE)|^(TR)|^(TD)|^(TBODY)/');
	switch(objReference.tagName)
	{
		case 'TD' :var rowIndex=objReference.parentElement.rowIndex;
			var cellIndex = objReference.cellIndex;//Get cellIndex
			var parentTable=objReference.parentElement.parentElement.parentElement;
			var newTable=parentTable.cloneNode(true);
			if (newTable.rows[0].cells.length==1)
			{
				parentTable.removeNode(true);
				return;
			}
			for(x=0; x<newTable.rows.length; x++)
			{
				if (newTable.rows[x].cells[cellIndex]=='[object]')
				{
					newTable.rows[x].deleteCell(cellIndex);
				}
			}
			if (cellIndex>=newTable.rows[0].cells.length)
			{
				cellIndex=newTable.rows[0].cells.length-1;
			}
			if (cellIndex>=0)  newTable.rows[rowIndex].cells[cellIndex].id='ura';
			parentTable.outerHTML=newTable.outerHTML;
			if (cellIndex>=0)
			{
				var r = rtb_ID.document.body.createTextRange();
				var item=rtb_ID.document.getElementById('ura');
				item.id='';
				r.moveToElementText(item);
				r.moveStart('character',r.text.length);
				r.select();
			}
		break;
		default :return;
	}
}


function RTB_insertColumn(editor,htmlmode)
{
	if (isHTMLMode){alert("Please uncheck 'Edit HTML'");return;}
	rtb_ID = editor;
	objReference= RTB_GetRangeReference(rtb_ID);
	objReference=RTB_CheckTag(objReference,'/^(TABLE)|^(TR)|^(TD)|^(TBODY)/');
	switch(objReference.tagName)
	{
		case 'TABLE' :// IF a table is selected, it adds a new column on the right hand side of the table.
			var newTable=objReference.cloneNode(true);
			for(x=0; x<newTable.rows.length; x++)
			{
				var newCell = newTable.rows[x].insertCell();
			}
			newCell.focus();
			objReference.outerHTML=newTable.outerHTML;
		break;
		case 'TBODY' :// IF a table is selected, it adds a new column on the right hand side of the table.
			var newTable=objReference.cloneNode(true);
			for(x=0; x<newTable.rows.length; x++)
			{
				var newCell = newTable.rows[x].insertCell();
			}
			objReference.outerHTML=newTable.outerHTML;
		break;
		case 'TR' :// IF a table is selected, it adds a new column on the right hand side of the table.
			objReference=objReference.parentElement.parentElement;
			var newTable=objReference.cloneNode(true);
			for(x=0; x<newTable.rows.length; x++)
			{
				var newCell = newTable.rows[x].insertCell();
			}
			objReference.outerHTML=newTable.outerHTML;
		break;
		case 'TD' :// IF the cursor is in a cell, or a cell is selected, it adds a new column to the right of that cell.
			var cellIndex = objReference.cellIndex;//Get cellIndex
			var rowIndex=objReference.parentElement.rowIndex;
			var parentTable=objReference.parentElement.parentElement.parentElement;
			var newTable=parentTable.cloneNode(true);
			for(x=0; x<newTable.rows.length; x++)
			{
				var newCell = newTable.rows[x].insertCell(cellIndex+1);
				if (x==rowIndex)newCell.id='ura';

                                if (x == 0) {
                                    newCell.className = 'tabel-p2is-2';
                                    newCell.setAttribute("bgcolor", "#EFF4F9");
                                    newCell.setAttribute("background", "gfx/bg06.gif");
                                } else { 
                                    newCell.className = 'tabel-andmed';
                                }


			}
			parentTable.outerHTML=newTable.outerHTML;
			var r = rtb_ID.document.body.createTextRange();
			var item=rtb_ID.document.getElementById('ura');
			item.id='';
			r.moveToElementText(item);
			r.moveStart('character',r.text.length);
			r.select();
		break;
		default :// IF the cursor is not in a table, it acts as if they clicked Insert Table.
			RTB_InsertTable(rtb_ID,3,1);
		return;
	}
}

function RTB_insertRow(editor)
{
	if (isHTMLMode){alert("Please uncheck 'Edit HTML'");return;}
	rtb_ID = editor;
	objReference=RTB_GetRangeReference(rtb_ID);
	objReference=RTB_CheckTag(objReference,'/^(TABLE)|^(TR)|^(TD)|^(TBODY)/');
	switch(objReference.tagName)
	{
		case 'TABLE' :
			var newTable=objReference.cloneNode(true);
			var newRow= newTable.insertRow();
			for(x=0; x<newTable.rows[0].cells.length; x++)
			{
				var newCell = newRow.insertCell();
			}
			objReference.outerHTML=newTable.outerHTML;
		break;
		case 'TBODY' :
			var newTable=objReference.cloneNode(true);
			var newRow = newTable.insertRow();
			for(x=0; x<newTable.rows[0].cells.length; x++)
			{
				var newCell = newRow.insertCell();
			}
			objReference.outerHTML=newTable.outerHTML;
		break;
		case 'TR' :
			var rowIndex = objReference.rowIndex;
			var parentTable=objReference.parentElement.parentElement;
			var newTable=parentTable.cloneNode(true);
			var newRow = newTable.insertRow(rowIndex+1);
			for(x=0; x< newTable.rows[0].cells.length; x++)
			{
				var newCell = newRow.insertCell();
			}
			parentTable.outerHTML=newTable.outerHTML;
		break;
		case 'TD' :
			var parentRow=objReference.parentElement;
			var rowIndex = parentRow.rowIndex;
			var cellIndex=objReference.cellIndex;
			var parentTable=objReference.parentElement.parentElement.parentElement;
			var newTable=parentTable.cloneNode(true);
			var newRow = newTable.insertRow(rowIndex+1);
			for(x=0; x< newTable.rows[0].cells.length; x++)
			{
				var newCell = newRow.insertCell();
				if (x==cellIndex)newCell.id='ura';

				newCell.className = 'tabel-andmed';

			}
			parentTable.outerHTML=newTable.outerHTML;
			var r = rtb_ID.document.body.createTextRange();
			var item=rtb_ID.document.getElementById('ura');
			item.id='';
			r.moveToElementText(item);
			r.moveStart('character',r.text.length);
			r.select();
		break;
		default :
			RTB_InsertTable(rtb_ID,1,3);
		return;
	}
}

function RTB_GetRangeReference(rtb_ID)
{
	rtb_ID.focus();
	var objReference = null;
	var RangeType = rtb_ID.document.selection.type;
	var selectedRange = rtb_ID.document.selection.createRange();
	switch(RangeType)
	{
		case 'Control' :
			if (selectedRange.length > 0 ) 
			{
				objReference = selectedRange.item(0);
			}
		break;
		case 'None' :
			objReference = selectedRange.parentElement();
		break;
		case 'Text' :
			objReference = selectedRange.parentElement();
		break;
	}
	return objReference
}

function RTB_CheckTag(item,tagName)
{
	if (item.tagName.search(tagName)!=-1)
	{
		return item;
	}
	if (item.tagName=='BODY')
	{
		return false;
	}
	item=item.parentElement;
	return RTB_CheckTag(item,tagName);
}
