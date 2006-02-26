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
                        <!-- div id="cont1">                        
                            <ui:systemForm method="POST">
															<div id="header">
																<div class="box1">
																	<a href="#" id="logo"><img src="gfx/logo_aranea_print.gif" alt="" /></a>
																	<div id="menu1">
																	</div>
																</div>
															</div>
															<div class="stripe1"><ui:nbsp/></div>
															<div id="wholder">
																<div id="leftcol">
																</div>
															</div>	
																
															
															<div id="content">
																<ui:messages/>
																<ui:widgetInclude id="flow"/>
															</div>                                                           
                            </ui:systemForm>
                        </div>
                        <div class="clear1"><ui:nbsp/></div>         
												<div id="footer">
													<div class="box1"> © Webmedia 2006 <i>|</i>
														<a href="mailto:info@araneaframework.org">info@araneaframework.org</a>
													</div>
												</div-->
<div id="cont1">
	<div id="header">
		<div class="box1">
			<a href="#" id="logo"><img src="gfx/logo_aranea_print.gif" alt="" /></a>
			<div id="menu1">
				<div class="item"><a href="#">First item</a></div>
				<div class="item"><a href="#" class="active">Second item</a></div>
				<div class="item"><a href="#">Third item</a></div>
				<div class="item"><a href="#">Fourth item</a></div>
				<div class="item"><a href="#">Fifth item</a></div>
			</div>
		</div>
	</div>
	<div class="stripe1"><ui:nbsp/></div>
	<div id="wholder">
		<div id="leftcol">
			<ul id="menu2">
				<li>
					<a href="#" class="active">Lorem ipsum dolor</a>
					<ul>
						<li><a href="#">Adipiscing elit</a></li>
						<li><a href="#" class="active">Proin sapien purus, blandit</a></li>
						<li><a href="#">Ut, placerat nec</a></li>
						<li><a href="#">Cursus vel, elit</a></li>
						<li><a href="#">Aliquam sagittis Sed eu mi</a></li>
						<li><a href="#">Scelerisque odio</a></li>
					</ul>
				</li>
				<li><a href="#">Sit amet, consectetuer</a></li>
				<li><a href="#">Adipiscing elit</a></li>
				<li><a href="#">Proin sapien purus, blandit</a></li>
				<li><a href="#">Ut, placerat nec</a></li>
				<li><a href="#">Cursus vel, elit</a></li>
				<li><a href="#">Aliquam sagittis Sed eu mi</a></li>
				<li><a href="#">Scelerisque odio</a></li>
				<li><a href="#">Interdum posuere.</a></li>
				<li><a href="#">Nullam arcu.</a></li>
			</ul>
		</div>
		<div id="content">
			<!-- start content -->
			<h1>Page title</h1>
			<div class="msg-neutral">Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Donec hendrerit felis venenatis enim. Cras purus lorem, sollicitudin sed, posuere ut, hendrerit sed, neque. Lorem ipsum dolor sit amet, consectetuer adipiscing elit.</div>
			<div class="msg-ok">Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Donec hendrerit felis venenatis enim. Cras purus lorem, sollicitudin sed, posuere ut, hendrerit sed, neque. Lorem ipsum dolor sit amet, consectetuer adipiscing elit.</div>
			<div class="msg-error">Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Donec hendrerit felis venenatis enim. Cras purus lorem, sollicitudin sed, posuere ut, hendrerit sed, neque. Lorem ipsum dolor sit amet, consectetuer adipiscing elit.</div>
			<div class="component-header">
				<div class="name">
					Component name
				</div>
			</div>
			<div class="component">
				<div class="w100p">
					<!-- start component content -->
					<table class="data">
						<tr>
							<th class="nowrap"><a href="#">ID<img src="gfx/ico_sortup.gif" alt="" /></a></th>
							<th><a href="#">Date 1</a></th>
							<th><a href="#">Heading</a></th>
							<th><a href="#">Date 2</a></th>
							<th class="right"><a href="#">Price</a></th>
							<th><a href="#">Currency</a></th>
							<th><a href="#">Heading</a></th>
							<th class="actions"><ui:nbsp/></th>
						</tr>
						<tr class="filter">
							<td class="inpt"><input type="text" class="min" /></td>
							<td class="inpt">
								<input type="text" class="date" id="startDate" /><a href="javascript:;"><img src="gfx/ico_calendar.gif" id="startDate_button_id" title="Ava kalender" alt="Ava kalender" class="ico" /></a>
								<script type="text/javascript">
								Calendar.setup({
									inputField : "startDate", // id of the input field
									ifFormat : "%d.%m.%Y", // format of the input field
									showsTime : false, // will display a time selector
									button : "startDate_button_id", // trigger for the calendar (button ID)
									singleClick : true, // double-click mode
									step : 1, // show all years in drop-down boxes (instead of every other year as default)
									firstDay : 1
								});
								</script>
								<input type="text" class="date" id="endDate" /><a href="javascript:;"><img src="gfx/ico_calendar.gif" id="endDate_button_id" title="Ava kalender" alt="Ava kalender" class="ico" /></a>
								<script type="text/javascript">
								Calendar.setup({
									inputField : "endDate", // id of the input field
									ifFormat : "%d.%m.%Y", // format of the input field
									showsTime : false, // will display a time selector
									button : "endDate_button_id", // trigger for the calendar (button ID)
									singleClick : true, // double-click mode
									step : 1, // show all years in drop-down boxes (instead of every other year as default)
									firstDay : 1
								});
								</script>
							</td>
							<td class="inpt"><input type="text" class="w200" /></td>
							<td class="inpt">
								<input type="text" class="date" id="start1Date" /><a href="javascript:;"><img src="gfx/ico_calendar.gif" id="start1Date_button_id" title="Ava kalender" alt="Ava kalender" class="ico" /></a>
								<script type="text/javascript">
								Calendar.setup({
									inputField : "start1Date", // id of the input field
									ifFormat : "%d.%m.%Y", // format of the input field
									showsTime : false, // will display a time selector
									button : "start1Date_button_id", // trigger for the calendar (button ID)
									singleClick : true, // double-click mode
									step : 1, // show all years in drop-down boxes (instead of every other year as default)
									firstDay : 1
								});
								</script>
								<input type="text" class="date" id="end2Date" /><a href="javascript:;"><img src="gfx/ico_calendar.gif" id="end2Date_button_id" title="Ava kalender" alt="Ava kalender" class="ico" /></a>
								<script type="text/javascript">
								Calendar.setup({
									inputField : "end2Date", // id of the input field
									ifFormat : "%d.%m.%Y", // format of the input field
									showsTime : false, // will display a time selector
									button : "end2Date_button_id", // trigger for the calendar (button ID)
									singleClick : true, // double-click mode
									step : 1, // show all years in drop-down boxes (instead of every other year as default)
									firstDay : 1
								});
								</script>
							</td>
							<td class="inpt"><input type="text" class="w70" /></td>
							<td class="inpt">
								<select>
									<option></option>
									<option>EEK</option>
									<option>USD</option>
									<option>EUR</option>
								</select>
							</td>
							<td class="inpt">
								<select>
									<option></option>
									<option>Yes</option>
									<option>No</option>
								</select>
							</td>
							<td class="actions"><input type="button" value="Filter" /></td>
						</tr>
						<tr>
							<td><a href="#">312602</a></td>
							<td>18.12.2003</td>
							<td>Text text text text text text text</td>
							<td>18.12.2003</td>
							<td class="right">22 931.00</td>
							<td>EEK</td>
							<td>No</td>
							<td class="actions"><input type="button" value="Action" /></td>
						</tr>
						<tr class="even">
							<td><a href="#">312602</a></td>
							<td>18.12.2003</td>
							<td>Text text text text text text text</td>
							<td>18.12.2003</td>
							<td class="right">22 931.00</td>
							<td>EEK</td>
							<td>No</td>
							<td class="actions"><input type="button" value="Action" /></td>
						</tr>
						<tr>
							<td><a href="#">312602</a></td>
							<td>18.12.2003</td>
							<td>Text text text text text text text</td>
							<td>18.12.2003</td>
							<td class="right">22 931.00</td>
							<td>EEK</td>
							<td>No</td>

							<td class="actions"><input type="button" value="Action" /></td>
						</tr>
						<tr class="even">
							<td><a href="#">312602</a></td>
							<td>18.12.2003</td>
							<td>Text text text text text text text</td>
							<td>18.12.2003</td>
							<td class="right">22 931.00</td>
							<td>EEK</td>
							<td>No</td>

							<td class="actions"><input type="button" value="Action" /></td>
						</tr>
						<tr>
							<td><a href="#">312602</a></td>
							<td>18.12.2003</td>
							<td>Text text text text text text text</td>
							<td>18.12.2003</td>
							<td class="right">22 931.00</td>
							<td>EEK</td>
							<td>No</td>

							<td class="actions"><input type="button" value="Action" /></td>
						</tr>
						<tr class="even">
							<td><a href="#">312602</a></td>
							<td>18.12.2003</td>
							<td>Text text text text text text text</td>
							<td>18.12.2003</td>
							<td class="right">22 931.00</td>
							<td>EEK</td>
							<td>No</td>

							<td class="actions"><input type="button" value="Action" /></td>
						</tr>
						<tr>
							<td><a href="#">312602</a></td>
							<td>18.12.2003</td>
							<td>Text text text text text text text</td>
							<td>18.12.2003</td>
							<td class="right">22 931.00</td>
							<td>EEK</td>
							<td>No</td>

							<td class="actions"><input type="button" value="Action" /></td>
						</tr>
						<tr class="even">
							<td><a href="#">312602</a></td>
							<td>18.12.2003</td>
							<td>Text text text text text text text</td>
							<td>18.12.2003</td>
							<td class="right">22 931.00</td>
							<td>EEK</td>
							<td>No</td>

							<td class="actions"><input type="button" value="Action" /></td>
						</tr>
						<tr>
							<td><a href="#">312602</a></td>
							<td>18.12.2003</td>
							<td>Text text text text text text text</td>
							<td>18.12.2003</td>
							<td class="right">22 931.00</td>
							<td>EEK</td>
							<td>No</td>

							<td class="actions"><input type="button" value="Action" /></td>
						</tr>
						<tr class="even">
							<td><a href="#">312602</a></td>
							<td>18.12.2003</td>
							<td>Text text text text text text text</td>
							<td>18.12.2003</td>
							<td class="right">22 931.00</td>
							<td>EEK</td>
							<td>No</td>
							<td class="actions"><input type="button" value="Action" /></td>
						</tr>
					</table>
					<div class="pages">
						<div class="nr">
							<a href="#" class="ico first"><ui:nbsp/></a>
							<a href="#" class="ico prev"><ui:nbsp/></a>
							<a href="#">1</a>
							<a href="#">2</a>
							<a href="#">3</a>
							<a href="#">4</a>
							<a href="#" class="active">5</a>
							<a href="#">6</a>
							<a href="#">7</a>
							<a href="#">8</a>
							<a href="#">9</a>
							<a href="#">10</a>
							<a href="#">...</a>
							<a href="#" class="ico next"><ui:nbsp/></a>
							<a href="#" class="ico last"><ui:nbsp/></a>
						</div>
						<div class="info">
							Showing 50-60. Total 200. <a href="#">Show all</a>
						</div>
					</div>
					<div class="actions">
						<input type="button" value="Action1" />
						<input type="button" value="Action2" />
					</div>
					<!-- end component content -->
				</div>
			</div>
			<table class="form">
				<tr class="cols4">
					<td class="name"><label for="input1">Maximum input:</label></td>
					<td class="inpt"><input type="text" class="max" name="input1" id="input1" /></td>
					<td class="name"><label for="input2">Normal input:</label></td>
					<td class="inpt"><input type="text" class="norm" name="input2" id="input2" /></td>
				</tr>
				<tr class="cols4">
					<td class="name"><label for="input3">Short input:</label></td>
					<td class="inpt"><input type="text" class="small" name="input3" id="input3" /></td>
					<td class="name"><label for="input4">Shortest input:</label></td>
					<td class="inpt"><input type="text" class="min" name="input4" id="input4" /></td>
				</tr>
				<tr class="cols4">
					<td class="name"><label for="input5">Required input <span class="req">*</span>:</label></td>
					<td class="inpt"><input type="text" class="norm" name="input5" id="input5" /></td>
					<td class="name"><label for="input6">Limited length <span class="limit">(20)</span>:</label></td>
					<td class="inpt"><input type="text" class="norm" name="input6" id="input6" maxlength="20" /></td>
				</tr>
				<tr class="cols4">
					<td class="name"><label for="input7">Disabled input:</label></td>
					<td class="inpt"><input type="text" class="norm disabled" name="input7" id="input7" disabled="disabled" /></td>
					<td class="name"><label for="start2Date">Date:</label></td>
					<td class="inpt"><input type="text" name="start2Date" id="start2Date" class="date" maxlength="10" />
						<a href="javascript:;"><img src="gfx/ico_calendar.gif" id="start2Date_button_id" title="Ava kalender" alt="Ava kalender" class="ico" /></a> (DD.MM.YYYY)
						<script type="text/javascript">
						Calendar.setup({
							inputField : "start2Date", // id of the input field
							ifFormat : "%d.%m.%Y", // format of the input field
							showsTime : false, // will display a time selector
							button : "start2Date_button_id", // trigger for the calendar (button ID)
							singleClick : true, // double-click mode
							step : 1, // show all years in drop-down boxes (instead of every other year as default)
							firstDay : 1
						});
						</script>
					</td>
				</tr>
				<tr class="cols4">
					<td class="name"><label for="input9">Time:</label></td>
					<td class="inpt"><input type="text" class="time" name="input9" id="input9" /> (HH.MM.SS)</td>
					<td class="name"><label for="input11">Select:</label></td>
					<td class="inpt">
						<select name="input11" id="input11">
							<option></option>
							<option>Pikk tus</option>
						</select>
					</td>
				</tr>
				<tr class="subheading">
					<td colspan="4">Subheading</td>
				</tr>
				<tr class="cols4">
					<td class="name"><label for="input10">Textarea:</label></td>
					<td class="inpt"><textarea class="max" name="input10" id="input10" rows="5" cols="30"></textarea></td>
					<td class="name">Text:</td>
					<td class="data">tekst tekst tekst tekst <a href="#">link in text</a> tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst tekst</td>
				</tr>
				<tr class="cols4">
					<td class="name">Radiobuttons:</td>
					<td class="inpt">
						<label for="radio1"><input type="radio" name="radio1" id="radio1" />option 1</label><br />
						<label for="radio2"><input type="radio" name="radio1" id="radio2" />option 2</label><br />
						<label for="radio3"><input type="radio" name="radio1" id="radio3" />option 3</label><br />
					</td>
					<td class="name">Table in form:</td>
					<td class="tbl">
						<table class="data">
							<tr>
								<th><ui:nbsp/></th>
								<th>Label 1</th>
								<th>Label 2</th>
								<th>Label 3</th>
							</tr>
							<tr>
								<td class="inpt"><input type="radio" name="radio2" /></td>
								<td>Text text text</td>
								<td>Text text</td>
								<td>Text text</td>
							</tr>
							<tr class="even">
								<td class="inpt"><input type="radio" name="radio2" /></td>
								<td>Text text text</td>
								<td>Text text</td>
								<td>Text text</td>
							</tr>
							<tr>
								<td class="inpt"><input type="radio" name="radio2" /></td>
								<td>Text text text</td>
								<td>Text text</td>
								<td>Text text</td>
							</tr>
							<tr class="even">
								<td class="inpt"><input type="radio" name="radio2" /></td>
								<td>Text text text</td>
								<td>Text text</td>
								<td>Text text</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr class="error">
					<td class="name"><label for="input1-1">Error:</label></td>
					<td class="inpt" colspan="3"><input type="text" class="max" name="input1" id="input1-1" value="Error!" /></td>
				</tr>
				<tr class="error">
					<td class="name"><label for="input1-2">Error with description:</label></td>
					<td class="inpt" colspan="3"><input type="text" class="max" name="input1" id="input1-2" value="Error!" />
						<div class="desc">
							Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Lorem ipsum dolor sit amet, consectetuer adipiscing elit.
						</div>
					</td>
				</tr>
				<tr>
					<td class="name"><label for="input16">Input with description:</label></td>
					<td class="inpt" colspan="3"><input type="text" class="max" name="input16" id="input16" />
						<div class="desc">
							Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Lorem ipsum dolor sit amet, consectetuer adipiscing elit.
						</div>
					</td>
				</tr>
			</table>
			<div class="actions">
				<input type="button" value="Action1" />
				<input type="button" value="Action2" />
			</div>

			<!-- end content -->
		</div>
	</div>
	<div class="clear1"><ui:nbsp/></div>
</div>
<div id="footer">
	<div class="box1">
		 ARANEA 2006<i>|</i><a href="#">info@aranea.com</a><i>|</i><a href="#">Link 1</a><i>|</i><a href="#">Link 2</a><i>|</i><a href="#">Link 3</a>
	</div>
</div>												
                    </body>
                </html>
            </ui:widgetContext>

        </ui:viewPort>
    </ui:root>

</jsp:root>