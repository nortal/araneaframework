// Siin on kalendri komponendiga seotud tegevused.
// Kasutatakse CSS klasse prefiksiga "calendar".

// Algväärtustamine lehe laadimisel.
window.onload = init;

// Kui vajutatakse ESC klahvi, peidame kalendri \x{FFFD}ra.
document.onkeypress = function hidecal1 (e) {
   var keyCode;
   if(document.all) { //it's IE
     keyCode = window.event.keyCode;
   }
   else{
     keyCode = e.which;
   } 
   if (keyCode==27)
   {
     hideCalendar();
   }
}

// false, kui tegemist pole InternetExplorer brauseriga.
var	ie=document.all;
// false, kui tegemist pole DOM standardit toetava brauseriga brauseriga.
var	dom=document.getElementById;
// false, kui tegemist pole Netscape4.X brauseriga.
var ns4=document.layers;

// Horisontaalne positsioon dokumendil, kuhu kalendri komponent ilmub. Et ilmuks nupu alla kasutada -1.
var	fixedX = -1;
// Vertikaalne positsioon dokumendil, kuhu kalendri komponent ilmub. Et ilmuks nupu alla kasutada -1.
var	fixedY = -1;
// Kui 0, siis nädal algab Pühapäevast. Kui 1, siis nädal algab Esmaspäevast.
var	startAt = 1;
// Kui 0, siis nädala järjekorranumbrit aastas ei näidata; kui 1 siis näidatakse.
var	showWeekNumber = 0;
// Kui 0, siis tänast kuupäeva eriliselt ei näidata; kui 1, siis näidatakse.
var	showToday = 1;
// Kus asuvad kalendri komponendis kasutatavad pildid.
var	imgDir = "gfx/";
// Nädala tulba pealkiri.
var weekString = "Wk";
// Kuupäeva valiku teade. "[date]" asendatakse konkreetse kuupäevaga.
var selectDateMessage = "Select [date] as date.";

// Tänane kuupäev.
var today = new Date();
// Päev.
var dateNow = today.getDate();
// Kuu.
var monthNow = today.getMonth();
// Aasta.
var yearNow = today.getYear();

// true, kui leht on laetud ja võib puutuda kalendri komponenti.
var bPageLoaded=false

// true, kui kalendri komponent on nähtav; false, kui peidus.
var bShow =	false;

// Viide objektile, kuhu läheb kalendri HTML kood. <div id="calendar"></div>
var	crossobj;
// Viide objektile, kuhu läheb kuu valiku HTML kood. <div id="selectMonth"></div>
var crossMonthObj;
// Viide objektile, kuhu läheb aasta valiku HTML kood. <div id="selectYear"></div>
var crossYearObj;
// Viide vormi sisendile, kuhu lõpus pannakse kuupäev.
var date;
// Valitud kuu.
var monthSelected;
// Valitud aasta.
var yearSelected;
// Valitud päev.
var dateSelected;
// Valitud kuu objekt.
var omonthSelected;
// Valitud päeva objekt.
var oyearSelected;
// Valitud aasta objekt.
var odateSelected;
// true, kui kuude valik on valmis; false, kui pole valmis.
var monthConstructed;
// true, kui aastate valik on valmis; false, kui pole valmis.
var yearConstructed;
// Kuude valiku kerimise ajaline intervall.
var intervalID1;
// Aastate valiku kerimise ajaline intervall.
var intervalID2;
// Kuude valiku kerimise timeout.
var timeoutID1;
// Aastate valiku kerimise timeout.
var timeoutID2;
// Objekt, kuhu pannake valitud kuupäev.
var ctlToPlaceValue;
// Viide viimati vajutatud kalendri kutsumise nupule.
var ctlNow;
// Siin hoitakse kuupäeva formaati.
var dateFormat;
// Esimene aasta, mida aastate valikus kuvatakse.
var nStartingYear;

// CSS stiil kuupäeva jaoks.
var	styleAnchor="text-decoration:none;color:black;"
// CSS lisastiil valitud kuupäeva jaoks.
var	styleLightBorder="border-style:solid;border-width:1px;border-color:#a0a0a0;"

// Massiiv kuude nimetustega.
var monthName = new Array("Jaanuar","Veebruar","M&auml;rts","Aprill","Mai","Juuni","Juuli","August","September","Oktoober","November","Detsember");

// #closeCalendar() callback function: invoked when date is picked; supplied to constructor
var callbackFunction;

// Massiiv nädala päevade nimetustega.
if (startAt==0)
{
	dayName = new Array ("P","E","T","K","N","R","L")
}
else
{
	dayName = new Array ("E","T","K","N","R","L","P")
}

// Teeme valmis HTML objektid, kuhu kalendri osi kirjutada.
if (dom)
{
	document.write ("<div onclick='bShow=true' id='calendar' style='z-index:+999;position:absolute;visibility:hidden;'>")
	document.write ('<table	border="0" cellspacing="1" cellpadding="1" bgcolor="#5D8DC7"><tr bgcolor="#FFFFFF"><td>')

	document.write ('<span id="caption"></span>')
	document.write ('<span id="content"></span>')
	document.write ('<span id="lblToday"></span>')

	document.write ('</td></tr></table><br><br>')
	document.write ('</div>')
	document.write ('<div id="selectMonth" style="z-index:+999;position:absolute;visibility:hidden;"></div>')
	document.write ('<div id="selectYear" style="z-index:+999;position:absolute;visibility:hidden;"></div>')
}



///// FUNKTSIOONID



/* hides <select> and <applet> objects (for	IE only) */
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

/*
* unhides <select> and <applet>	objects	(for IE	only)
*/
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

function HolidayRec	(d,	m, y, desc)
{
	this.d = d
	this.m = m
	this.y = y
	this.desc =	desc
}

var	HolidaysCounter	= 0
var	Holidays = new Array()

function addHoliday	(d,	m, y, desc)
{
	Holidays[HolidaysCounter++]	= new HolidayRec ( d, m, y,	desc )
}

function swapImage(srcImg, destImg){
	if (ie)	{ document.getElementById(srcImg).setAttribute("src",imgDir	+ destImg) }
}

/**
 * Algväärtustamine.
 */
function init()	{
	if (!ns4)
	{
		if (!ie) { yearNow += 1900	}

		crossobj=(dom)?document.getElementById("calendar").style : ie? document.all.calendar : document.calendar
		hideCalendar()

		crossMonthObj=(dom)?document.getElementById("selectMonth").style : ie? document.all.selectMonth	: document.selectMonth

		crossYearObj=(dom)?document.getElementById("selectYear").style : ie? document.all.selectYear : document.selectYear

		monthConstructed=false;
		yearConstructed=false;

		if (showToday==1)
		{
			document.getElementById("lblToday").innerHTML =	"<table	width='100%' cellpadding='4' cellspacing='0' bgcolor='#FFFFFF';	class='calendar-footer'><tr><td	align='center'>T&auml;na	on:	<a href='javascript:monthSelected=monthNow;	yearSelected=yearNow;constructCalendar();'>"+dayName[(today.getDay()-startAt==-1)?6:(today.getDay()-startAt)]+", " + dateNow + " " + monthName[monthNow]	+ "	" +	yearNow	+ "</a></td></tr></table>";
		}

		sHTML1 ="<table cellpadding=0 cellspacing=0 border=0><tr><td>";
		sHTML1+="<table	cellpadding=0 cellspacing=0	border=0><tr><td><span id='spanLeft' onclick='javascript:decMonth()' onmouseout='clearInterval(intervalID1)' onmousedown='clearTimeout(timeoutID1);	timeoutID1=setTimeout(\"StartDecMonth()\",500)'	onmouseup='clearTimeout(timeoutID1); clearInterval(intervalID1)'><IMG id='changeLeft' style='cursor: hand' SRC='gfx/calendar_ico_left.gif' BORDER=0></span></td>";
		sHTML1+='<td class="calendar-month"	bgcolor="#93C1D8" valign="middle" align="center" style="width: 80px; cursor: hand" id="spanMonth"  onclick="popUpMonth()"></td>';
		sHTML1+="<td><span id='spanRight' onclick='incMonth()' onmouseout='clearInterval(intervalID1)' onmousedown='clearTimeout(timeoutID1); timeoutID1=setTimeout(\"StartIncMonth()\",500)'	onmouseup='clearTimeout(timeoutID1); clearInterval(intervalID1)'><IMG id='changeRight' style='cursor: hand'	SRC='gfx/calendar_ico_right.gif' BORDER=0></span></td>";
		sHTML1+='<td class="calendar-month"	bgcolor="#93C1D8" valign="middle" align="center" style="width: 50px; cursor: hand" id="spanYear"  onclick="popUpYear()"></td>';
		sHTML1+='<td><img src="gfx/calendar_close.gif" style="cursor: hand"	onClick="javascript:hideCalendar()"></td>';
		sHTML1+='</tr></table>';
		
		document.getElementById("caption").innerHTML  =	sHTML1;
		bPageLoaded=true;
	}
}

/**
 * Kalendri komponent peidetakse ära.
 */
function hideCalendar()	{
	crossobj.visibility="hidden"
	// usual hidden div still counts for the document width and height - thus sometimes
	// creating unwanted scrollbars. Zero the position to fix it.
	
    crossobj.left=0;
    crossobj.top=0;
	if (crossMonthObj != null){crossMonthObj.visibility="hidden"}
	if (crossYearObj !=	null){crossYearObj.visibility="hidden"}
 
	showElement( 'SELECT' );
	showElement( 'APPLET' );
}

/**
 * Kümnest väiksemale arvule pannakse 0 ette.
 * @param num - arv (eeldatavasti 0-99).
 * @return etteantud arv, kuhu on vajadusel ette pandud 0.
 */
function padZero(num) {
	return (num < 10) ? '0' + num : num ;
}

/**
 * Võetakse kuupäeva formaat dateFormat ja asendatakse seal kuupäeva elemendid väärtustega.
 * @param d - päev
 * @param m - kuu
 * @param y - aasta
 * @return kuupäev vajalikus formaadis.
 */
function constructDate(d,m,y)
{
	var sTmp = dateFormat;
	sTmp = sTmp.replace	("dd","<e>");
	sTmp = sTmp.replace	("d","<d>");
	sTmp = sTmp.replace	("<e>",padZero(d));
	sTmp = sTmp.replace	("<d>",d);
	sTmp = sTmp.replace	("mmm","<o>");
	sTmp = sTmp.replace	("mm","<n>");
	sTmp = sTmp.replace	("m","<m>");
	sTmp = sTmp.replace	("<m>",m+1);
	sTmp = sTmp.replace	("<n>",padZero(m+1));
	sTmp = sTmp.replace	("<o>",monthName[m]);
	return sTmp.replace	("yyyy",y);
}

/**
 * Peidetakse kalender ära ja vormi väljale pannakse kuupäev.
 */
function closeCalendar() {
	hideCalendar();
	date.value = padZero(dateSelected) + '.' + padZero(monthSelected+1) + '.' + yearSelected;
        callbackFunction();
}

/**
 * Alustame kuude valiku kerimist üles.
 */
function StartDecMonth()
{
	intervalID1=setInterval("decMonth()",80);
}

/**
 * Alustame kuude valiku kerimist alla.
 */
function StartIncMonth()
{
	intervalID1=setInterval("incMonth()",80);
}

/**
 * Valitud kuu võhendatakse ühe võrra.
 * Kui tegemist oli jaanuariga, siis aasta vähendatakse ühe võrra ja kuuks pannakse detsember.
 * @see constructCalendar();
 */
function decMonth () {
	monthSelected--;
	if (monthSelected<0)
	{
		monthSelected=11;
		yearSelected--;
	}
	constructCalendar();
}

/**
 * Valitud kuu suurendatakse ühe võrra.
 * Kui tegemist oli detsembriga, siis aasta suurendatakse ühe võrra ja kuuks pannakse jaanuar.
 * @see constructCalendar();
 */
function incMonth () {
	monthSelected++;
	if (monthSelected>11)
	{
		monthSelected=0;
		yearSelected++;
	}
	constructCalendar();
}

/**
 * Pannakse kokku kuude valik.
 */
function constructMonth() {
	popDownYear();
	if (!monthConstructed) {
		sHTML =	"";
		for (i=0; i<12;	i++) {
			sName =	monthName[i];
			if (i==monthSelected){
				sName = "<B>" +	sName + "</B>";
			}
			sHTML += "<tr><td class=calendar-months id='m" + i + "' onmouseover='this.style.backgroundColor=\"#D3E5FB\"; this.style.border=\"1px solid #5D8DC7\"' onmouseout='this.style.backgroundColor=\"\"; this.style.border=\"1px solid #FFFFFF\"'	style='cursor:hand;	border:\"1px solid #FFFFFF\"' onclick='monthConstructed=false; monthSelected=" + i + ";	constructCalendar(); popDownMonth(); event.cancelBubble=true'>&nbsp;" + sName + "&nbsp;</td></tr>";
		}
		document.getElementById("selectMonth").innerHTML = "<table cellpadding=2 cellspacing=0 bgcolor=#FFFFFF style='border: 1px solid	#5D8DC7'><tr><td><table	border=0 cellspacing=0 cellpadding=2 onmouseover='clearTimeout(timeoutID1)'	onmouseout='clearTimeout(timeoutID1); timeoutID1=setTimeout(\"popDownMonth()\",100); event.cancelBubble=true'>" + sHTML + "</table></td></tr></table>";
		monthConstructed=true;
	}
}

/**
 * Kuvatakse kuude valik.
 * @see constructMonth()
 */
function popUpMonth() {
	constructMonth()
	crossMonthObj.visibility = (dom||ie)? "visible"	: "show"
	crossMonthObj.left = parseInt(crossobj.left) + 50
	crossMonthObj.top =	parseInt(crossobj.top) + 26

	hideElement( 'SELECT', document.getElementById("selectMonth") );
	hideElement( 'APPLET', document.getElementById("selectMonth") );
}

/**
 * Peidetakse kuude valik.
 */
function popDownMonth()	{
	crossMonthObj.visibility= "hidden"
	crossMonthObj.top=0;
	crossMonthObj.left=0;
}

/**
 * Aastate valiku esimene aasta suurendatakse ühe võrra. Valitud aasta kuvatakse paksemalt.
 */
function incYear() {
	for	(i=0; i<7; i++){
		newYear	= (i+nStartingYear)+1;
		if (newYear==yearSelected)
		{
			txtYear = "&nbsp;<B>" + newYear + "</B>&nbsp;";
		}
		else
		{
			txtYear = "&nbsp;" + newYear + "&nbsp;";
		}
		document.getElementById("y"+i).innerHTML = txtYear;
	}
	nStartingYear ++;
	bShow = true;
}

/**
 * Aastate valiku esimene aasta vähendatakse ühe võrra. Valitud aasta kuvatakse paksemalt.
 */
function decYear() {
	for	(i=0; i<7; i++){
		newYear	= (i+nStartingYear)-1
		if (newYear==yearSelected)
		{ txtYear =	"&nbsp;<B>"	+ newYear +	"</B>&nbsp;" }
		else
		{ txtYear =	"&nbsp;" + newYear + "&nbsp;" }
		document.getElementById("y"+i).innerHTML = txtYear
	}
	nStartingYear --;
	bShow=true
}

/**
 * Valitakse etteantud aastaarv. Aastate valik pannakse kinni. Kalender uuendatakse.
 * @see constructCalendar()
 * @param nYear - aastaarv.
 */
function selectYear(nYear) {
	yearSelected=parseInt(nYear+nStartingYear);
	yearConstructed=false;
	constructCalendar();
	popDownYear();
}

/**
 * Pannakse kokku aastate valik.
 */
function constructYear() {
	popDownMonth();
	sHTML =	"";
	if (!yearConstructed) {

		sHTML =	"<tr><td class=calendar-months align='center' onmouseover='this.style.backgroundColor=\"#D3E5FB\"; this.style.border=\"1px solid #5D8DC7\"'	onmouseout='clearInterval(intervalID1);this.style.backgroundColor=\"#FFFFFF\"; this.style.border=\"1px solid #E0E0E0\"'	style='cursor:hand;	border:	1px	solid #E0E0E0; background: #FFFFFF'	onmousedown='clearInterval(intervalID1); intervalID1=setInterval(\"decYear()\",30)'	onmouseup='clearInterval(intervalID1)'><img	src='gfx/calendar_scrollup.gif'></td></tr>";

		j =	0;
		nStartingYear =	yearSelected-3;
		for	(i=(yearSelected-3); i<=(yearSelected+3); i++) {
			sName =	i;
			if (i==yearSelected){
				sName =	"<B>" +	sName +	"</B>";
			}
			sHTML += "<tr><td class=calendar-months	align=center id='y"	+ j	+ "' onmouseover='this.style.backgroundColor=\"#D3E5FB\"; this.style.border=\"1px solid	#5D8DC7\"' onmouseout='this.style.backgroundColor=\"\";	this.style.border=\"1px	solid #FFFFFF\"' style='cursor:hand; border:\"1px solid	#FFFFFF\"' onclick='selectYear("+j+");event.cancelBubble=true'>&nbsp;" + sName + "&nbsp;</td></tr>";
			j ++;
		}
		sHTML += "<tr><td class=calendar-months	align='center' onmouseover='this.style.backgroundColor=\"#D3E5FB\";	this.style.border=\"1px	solid #5D8DC7\"' onmouseout='clearInterval(intervalID2);;this.style.backgroundColor=\"#FFFFFF\"; this.style.border=\"1px solid #E0E0E0\"' style='cursor:hand; border: 1px solid	#E0E0E0; background: #FFFFFF' onmousedown='clearInterval(intervalID2); intervalID2=setInterval(\"incYear()\",30)'	onmouseup='clearInterval(intervalID2)'><img	src='gfx/calendar_scrolldown.gif'></td></tr>";
		document.getElementById("selectYear").innerHTML	= "<table cellpadding=2	cellspacing=0 bgcolor=#FFFFFF style='border: 1px solid #5D8DC7'><tr><td><table border=0	cellspacing=0 cellpadding=2	onmouseover='clearTimeout(timeoutID2)' onmouseout='clearTimeout(timeoutID2);timeoutID2=setTimeout(\"popDownYear()\",100)' cellspacing=0	width=80>"	+ sHTML	+ "</table></td></tr></table>";
		yearConstructed	= true;
	}
}

/**
 * Kuvatakse aastate valik.
 * @see constructYear()
 */
function popUpYear() {
	var	leftOffset;

	constructYear();
	crossYearObj.visibility	= (dom||ie)? "visible" : "show";
	leftOffset = parseInt(crossobj.left) + document.getElementById("spanYear").offsetLeft;
	if (ie)
	{
		leftOffset += 6;
	}

	// fixup for too-far-on-the-left bug by Martin Paljak
	ecy=document.getElementById("selectYear");
	ecyr=leftOffset+ecy.offsetWidth;
	if (ecyr>document.body.clientWidth) {
		leftOffset = document.body.clientWidth-ecy.offsetWidth;
	}
	crossYearObj.left =	leftOffset;
	crossYearObj.top = parseInt(crossobj.top) +	26;
}

/**
 * Peidetakse aastate valik.
 */
function popDownYear() {
	clearInterval(intervalID1);
	clearTimeout(timeoutID1);
	clearInterval(intervalID2);
	clearTimeout(timeoutID2);
	crossYearObj.visibility= "hidden";
	crossYearObj.top=0;
	crossYearObj.left=0;
}

/**
 * Arvutatakse välja nädala järjekorra number.
 * 
 * @param n - Date tyypi objekt
 * @return etteantud kuupäevale vastava nädala järjekorranumber aastas.
 */
function WeekNbr(n) {
	year = n.getFullYear();
	month	= n.getMonth() + 1;
	if (startAt == 0)	{
	 day = n.getDate() + 1;
	}
	else {
	 day = n.getDate();
	}

	a	= Math.floor((14-month)	/ 12);
	y	= year + 4800 -	a;
	m	= month	+ 12 * a - 3;
	b	= Math.floor(y/4) -	Math.floor(y/100) +	Math.floor(y/400);
	J	= day +	Math.floor((153	* m	+ 2) / 5) +	365	* y	+ b	- 32045;
	d4 = (((J	+ 31741	- (J % 7)) % 146097) % 36524) %	1461;
	L	= Math.floor(d4	/ 1460);
	d1 = ((d4	- L) % 365)	+ L;
	week = Math.floor(d1/7) +	1;

	return week;
}
function selectDate(date) {
	dateSelected=date;
	closeCalendar();
}

/**
 * Pannakse kokku kalender.
 */
function constructCalendar () {
	var	aNumDays = Array (31,0,31,30,31,30,31,31,30,31,30,31);

	var	dateMessage;
	var	startDate =	new	Date (yearSelected,monthSelected,1);
	var	endDate;

	if (monthSelected==1)
	{
		endDate	= new Date (yearSelected,monthSelected+1,1);
		endDate	= new Date (endDate	- (24*60*60*1000));
		numDaysInMonth = endDate.getDate();
	}
	else
	{
		numDaysInMonth = aNumDays[monthSelected];
	}

	datePointer	= 0;
	dayPointer = startDate.getDay()	- startAt;
	if (dayPointer<0)
	{
		dayPointer = 6;
	}

	// Alustame.
	sHTML =	"<table	cellspacing=0 cellpadding=6	border=0 width=100%><tr><td	align=center><table	border=0 cellpadding=0 cellspacing=1 class=calendar	bgcolor=#9FA09E><tr>";
	if (showWeekNumber==1)
	{
		sHTML += "<td class=calendar-date-passive>"	+ weekString + "</td><td width=1 rowspan=7 bgcolor='#d0d0d0' style='padding:0px'><img src='"+imgDir+"divider.gif' width=1></td>";
	}

	for	(i=0; i<7; i++)	{
		sHTML += "<td class=calendar-days align=center>"+ dayName[i]+"</td>";
	}
	sHTML +="</tr><tr>";
	
	if (showWeekNumber==1)
	{
		sHTML += "<td class=calendar-date-passive>"	+ WeekNbr(startDate) + "</td>";
	}

	// Tühjad ruudud, enne kuu esimest p?eva
	for ( var i=1; i<=dayPointer;i++ )
	{
		sHTML += "<td class=calendar-date-passive>&nbsp;</td>";
	}

	// Päevad nädalate kaupa real
	for	( datePointer=1; datePointer<=numDaysInMonth; datePointer++	)
	{
		dayPointer++;
		sStyle=styleAnchor;
		if ((datePointer==odateSelected) &&	(monthSelected==omonthSelected)	&& (yearSelected==oyearSelected))
		{ sStyle+=styleLightBorder; }

		sHint =	"";
		for	(k=0;k<HolidaysCounter;k++)
		{
			if ((parseInt(Holidays[k].d)==datePointer)&&(parseInt(Holidays[k].m)==(monthSelected+1)))
			{
				if ((parseInt(Holidays[k].y)==0)||((parseInt(Holidays[k].y)==yearSelected)&&(parseInt(Holidays[k].y)!=0)))
				{
					sStyle+="background-color:#FFDDDD;";
					sHint+=sHint==""?Holidays[k].desc:"\n"+Holidays[k].desc;
				}
			}
		}

		var	regexp=	/\"/g;
		sHint=sHint.replace(regexp,"&quot;");

		dateMessage	= "onmousemove='window.status=\""+selectDateMessage.replace("[date]",constructDate(datePointer,monthSelected,yearSelected))+"\"' onmouseout='window.status=\"\"' ";

		// if this is the selected date
		if	((datePointer==odateSelected)&&(omonthSelected==monthSelected)&&(oyearSelected==yearSelected))
			{ sHTML	+= "<td	align=center onClick='selectDate("+datePointer+");' class=calendar-date-today style='cursor:hand; border:2px solid	#93C1D8; background-color: #E5F0F6;'><a href='javascript:selectDate(" + datePointer + ");'>"	+ datePointer +	"</a>";}
			
		// if the day is today		
		else if	((datePointer==dateNow)&&(monthSelected==monthNow)&&(yearSelected==yearNow))
			{ sHTML	+= "<td	align=center class=calendar-date-today onMouseOver='this.style.border=\"2px solid #93C1D8\"'  onClick='selectDate("+datePointer+");' onMouseOut='this.style.border=\"2px solid #FF0000\"' style='cursor:hand; border:2px solid	#FF0000; background-color: #E5F0F6;'><a href='javascript:selectDate(" + datePointer + ");'>" + datePointer + "</a>";}
			
		// if the day is a sunday
		else if	(dayPointer	% 7	== (startAt	* -1)+1)
			{ sHTML	+= "<td	align=center class=calendar-date-weekend  onMouseOver='this.style.border=\"2px solid #93C1D8\"'	onClick='selectDate("+datePointer+");' onMouseOut='this.style.backgroundColor=\"#F9E9E9\";	this.style.border=\"2px	solid #E5CECE\"' style='cursor:hand; border:2px	solid #E5CECE; background-colo: #F9E9E9;'><a	href='javascript:selectDate(" + datePointer + ");'>" + datePointer +	"</a>"; }			
		
		// else it's an ordinary day.
		else
			{ sHTML	+= "<td	align='center' class='calendar-date' onClick='selectDate("+datePointer+");' onMouseOver='this.style.backgroundColor=\"#E5F0F6\"; this.style.border=\"2px solid	#93C1D8\"' onMouseOut='this.style.backgroundColor=\"#FFFFFF\"; this.style.border=\"2px solid #EBEBEB\"'	style='cursor:hand;	border:2px solid #EBEBEB; background-color=#FFFFFF'><a href='javascript:selectDate(" + datePointer + ");'>" + datePointer + "</a>"; }		
		sHTML += "";
		if ((dayPointer+startAt) % 7 ==	startAt) {
			sHTML += "</tr><tr>"
			if ((showWeekNumber==1)&&(datePointer<numDaysInMonth))
			{
				sHTML += "<td class=calendar-date-passive>"	+ (WeekNbr(new Date(yearSelected,monthSelected,datePointer+1)))	+ "&nbsp;</td>";
			}
		}
	}

	// Tühjad ruudud, peale kuu viimast päeva
	if ((dayPointer+startAt	- 1) % 7 !=	0)
	{
		for	( var i=(dayPointer+startAt	- 1) % 7 ; i<=6; i++ )
		{
			sHTML += "<td class=calendar-date-passive>&nbsp;</td>";
		}
	}

	// Kuvame.
	sHTML += "</table></td></tr></table>";
	document.getElementById("content").innerHTML   = sHTML;
	document.getElementById("spanMonth").innerHTML = monthName[monthSelected] +	'<img src="gfx/1.gif" width="5"	height="1"><IMG	id="changeMonth" SRC="gfx/calendar_ico_open.gif" BORDER="0"	align="absmiddle">';
	document.getElementById("spanYear").innerHTML =	yearSelected + '<img src="gfx/1.gif" width="5" height="1"><IMG id="changeYear" SRC="gfx/calendar_ico_open.gif" BORDER="0" align="absmiddle">';
}

/**
 * Kalendri komponendi kuvamine.
 * Formaadis tunnistatakse eraldajaid " ", "/", ".", "-" - 
 * Muidu kasutatakse tänast kuupäeva.
 * Aasta märkimiseks kasutage "yyyy", kuu puhul "m" või "mm", 
 * kuupäeva puhul "d" või "dd". Formaadi kirjelduses peavad olema kasutatud kõik kolm.
 * Kui midagi on puudu, kasutatakse käesolevat kuupäeva.
 * 
 * @see constructCalendar()
 * 
 * @param ct1 - välja kutsuva nupu objekt.
 * @param ct2 - vormi sisendvälja objekt, kus asub kasutaja poolt määratud kuup?ev.
 * @param ct3 - vormi sisendvälja objekt, kuhu lõpus tuleb kuupäev panna.
 * @param format - kuupäeva formaat.
 * @param callback - funktsioon, mis kutsutakse valja kui kuupaev on valitud (vt. #closeCalendar)
 */
function popUpCalendar(ctl,ctl2,ctl3,format,callback) {
	if (!bPageLoaded) return;

	var	leftpos=0
	var	toppos=0

	if ( crossobj.visibility ==	"hidden" ) {
		ctlToPlaceValue = ctl2;
		dateFormat=format;
		date = ctl3;

		// Kas on legaalne eraldaja.
		formatChar = " "
		aFormat	= dateFormat.split(formatChar)
		if (aFormat.length<3)
		{
			formatChar = "/"
			aFormat	= dateFormat.split(formatChar)
			if (aFormat.length<3)
			{
				formatChar = "."
				aFormat	= dateFormat.split(formatChar)
				if (aFormat.length<3)
				{
					formatChar = "-"
					aFormat	= dateFormat.split(formatChar)
					if (aFormat.length<3)
					{
						// invalid date	format
						formatChar="";
					}
				}
			}
		}

		// Kas kõik kolm kuupäeva komponenti on olemas.
		tokensChanged =	0;
		if ( formatChar	!= "" )
		{
			// use user's date
			aData =	ctl2.value.split(formatChar)

			for	(i=0;i<3;i++)
			{
				if ((aFormat[i]=="d") || (aFormat[i]=="dd"))
				{
					dateSelected = parseInt(aData[i], 10)
					tokensChanged ++
				}
				else if	((aFormat[i]=="m") || (aFormat[i]=="mm"))
				{
					monthSelected =	parseInt(aData[i], 10) - 1
					tokensChanged ++
				}
				else if	(aFormat[i]=="yyyy")
				{
					yearSelected = parseInt(aData[i], 10)
					tokensChanged ++
				}
				else if	(aFormat[i]=="mmm")
				{
					for	(j=0; j<12;	j++)
					{
						if (aData[i]==monthName[j])
						{
							monthSelected=j
							tokensChanged ++
						}
					}
				}
			}
		}
		
		
		// Kui midagi on puudu, kasutatakse käesolevat kuupäeva.
		if ((tokensChanged!=3)||isNaN(dateSelected)||isNaN(monthSelected)||isNaN(yearSelected))
		{	
			dateSelected = dateNow;
			monthSelected =	monthNow;
			yearSelected = yearNow;
		}
		
		odateSelected=dateSelected;
		omonthSelected=monthSelected;
		oyearSelected=yearSelected;
		
		constructCalendar ();
		
		// place the calendar
		cal	= document.getElementById("calendar");
		// Paneme kalendri nupu alla.
		// Kui kalender ei ole tervenisti nähtav, nihutame ta ülespoole.
		cal_height = cal.offsetHeight - 30;

		aTag = ctl
		do
		{
			aTag = aTag.offsetParent;
			leftpos	+= aTag.offsetLeft;
			toppos += aTag.offsetTop;
		}
		while(aTag.tagName!="BODY");

		cal_top = ctl.offsetTop + toppos + ctl.offsetHeight + 2;
		cal_bottom = cal_top + cal_height;
		scroll_bottom = document.body.scrollTop + document.body.offsetHeight;

		if (cal_bottom > scroll_bottom)
		{
			cal_top = cal_top - (cal_bottom - scroll_bottom) - ctl.offsetHeight - 45;
		}
		
		// Kuvame.
		crossobj.top = fixedY==-1 ? cal_top : fixedY;
		crossobj.left = fixedX==-1 ? ctl.offsetLeft + leftpos : fixedX;

		// fixup for too-far-on-the-left bug by Martin Paljak
		ec=document.getElementById("calendar");
		ecl=parseInt(crossobj.left);
		ecr=ecl+ec.offsetWidth;
		if (ecr>document.body.clientWidth)
			crossobj.left = document.body.clientWidth-ec.offsetWidth;

		crossobj.visibility=(dom||ie)? "visible" : "show";
		hideElement( 'SELECT', cal );
		hideElement( 'APPLET', cal );
		bShow = true;
	}
	else
	{
		hideCalendar()
		if (ctlNow!=ctl) {popUpCalendar(ctl, ctl2, ctl3, format)}
	}
	ctlNow = ctl
        callbackFunction = callback;
}
