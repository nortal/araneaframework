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

package org.araneaframework.servlet.filter.importer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.araneaframework.InputData;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class JsFileImporter extends DefaultFileImporter {
	private static final Logger log = Logger.getLogger(JsFileImporter.class);
	
	public static final String TYPE = "jsFileImporter";
	
	public static final String INCLUDE_JS_FILE_KEY = "loadJSFile";
	public static final String INCLUDE_JS_KEY = "loadJS";
	public static final String INCLUDE_TMPLT_JS_KEY = "loadTemplateJS";
	
	private static final List jsFiles = new ArrayList();
	static {
		jsFiles.add("js/ui.js");
		jsFiles.add("js/ui-aranea.js");
		jsFiles.add("js/ui-handlers.js");
		jsFiles.add("js/ui-popups.js");
		jsFiles.add("js/aa.js");
		
		jsFiles.add("js/validation/localization.js");
		jsFiles.add("js/validation/clientside-form-validation.js");
		jsFiles.add("js/validation/clientside-datetime-form-validation.js");		
		jsFiles.add("js/validation/jsp-ui-events.js");
		jsFiles.add("js/validation/jsp-ui-form-validation.js");
		jsFiles.add("js/validation/jsp-ui-validation.js");
		jsFiles.add("js/validation/jsp-ui.js");
		
		jsFiles.add("js/calendar/calendar.js");
		jsFiles.add("js/calendar/calendar-en.js");
		jsFiles.add("js/calendar/calendar-setup.js");
		
		// tiny mce stuff
		jsFiles.add("js/tiny_mce/plugins/searchreplace/jscripts/search.js");
		jsFiles.add("js/tiny_mce/plugins/searchreplace/jscripts/replace.js");
		jsFiles.add("js/tiny_mce/plugins/searchreplace/langs/en.js");
		jsFiles.add("js/tiny_mce/plugins/searchreplace/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/searchreplace/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/zoom/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/zoom/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/contextmenu/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/contextmenu/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/directionality/langs/en.js");
		jsFiles.add("js/tiny_mce/plugins/directionality/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/directionality/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/insertdatetime/langs/en.js");
		jsFiles.add("js/tiny_mce/plugins/insertdatetime/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/insertdatetime/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/cleanup/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/cleanup/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/advhr/jscripts/rule.js");
		jsFiles.add("js/tiny_mce/plugins/advhr/langs/en.js");
		jsFiles.add("js/tiny_mce/plugins/advhr/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/advhr/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/print/langs/en.js");
		jsFiles.add("js/tiny_mce/plugins/print/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/print/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/emotions/jscripts/functions.js");
		jsFiles.add("js/tiny_mce/plugins/emotions/langs/en.js");
		jsFiles.add("js/tiny_mce/plugins/emotions/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/emotions/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/table/jscripts/table.js");
		jsFiles.add("js/tiny_mce/plugins/table/jscripts/row.js");
		jsFiles.add("js/tiny_mce/plugins/table/jscripts/cell.js");
		jsFiles.add("js/tiny_mce/plugins/table/jscripts/merge_cells.js");
		jsFiles.add("js/tiny_mce/plugins/table/langs/en.js");
		jsFiles.add("js/tiny_mce/plugins/table/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/table/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/advimage/jscripts/functions.js");
		jsFiles.add("js/tiny_mce/plugins/advimage/langs/en.js");
		jsFiles.add("js/tiny_mce/plugins/advimage/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/advimage/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/layer/langs/en.js");
		jsFiles.add("js/tiny_mce/plugins/layer/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/layer/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/preview/langs/en.js");
		jsFiles.add("js/tiny_mce/plugins/preview/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/preview/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/noneditable/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/noneditable/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/iespell/langs/en.js");
		jsFiles.add("js/tiny_mce/plugins/iespell/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/iespell/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/style/jscripts/props.js");
		jsFiles.add("js/tiny_mce/plugins/style/langs/en.js");
		jsFiles.add("js/tiny_mce/plugins/style/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/style/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/fullpage/jscripts/fullpage.js");
		jsFiles.add("js/tiny_mce/plugins/fullpage/langs/en.js");
		jsFiles.add("js/tiny_mce/plugins/fullpage/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/fullpage/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/flash/jscripts/flash.js");
		jsFiles.add("js/tiny_mce/plugins/flash/langs/en.js");
		jsFiles.add("js/tiny_mce/plugins/flash/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/flash/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/_template/langs/en.js");
		jsFiles.add("js/tiny_mce/plugins/_template/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/_template/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/paste/jscripts/pastetext.js");
		jsFiles.add("js/tiny_mce/plugins/paste/jscripts/pasteword.js");
		jsFiles.add("js/tiny_mce/plugins/paste/langs/en.js");
		jsFiles.add("js/tiny_mce/plugins/paste/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/paste/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/autosave/langs/en.js");
		jsFiles.add("js/tiny_mce/plugins/autosave/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/autosave/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/fullscreen/langs/en.js");
		jsFiles.add("js/tiny_mce/plugins/fullscreen/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/fullscreen/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/inlinepopups/jscripts/mcwindows.js");
		jsFiles.add("js/tiny_mce/plugins/inlinepopups/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/inlinepopups/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/advlink/jscripts/functions.js");
		jsFiles.add("js/tiny_mce/plugins/advlink/langs/en.js");
		jsFiles.add("js/tiny_mce/plugins/advlink/editor_plugin.js");
		jsFiles.add("js/tiny_mce/plugins/advlink/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/save/langs/en.js");
		jsFiles.add("js/tiny_mce/plugins/save/editor_plugin_src.js");
		jsFiles.add("js/tiny_mce/plugins/save/editor_plugin.js");
		jsFiles.add("js/tiny_mce/themes/simple/editor_template_src.js");
		jsFiles.add("js/tiny_mce/themes/simple/editor_template.js");
		jsFiles.add("js/tiny_mce/themes/advanced/jscripts/source_editor.js");
		jsFiles.add("js/tiny_mce/themes/advanced/jscripts/image.js");
		jsFiles.add("js/tiny_mce/themes/advanced/jscripts/color_picker.js");
		jsFiles.add("js/tiny_mce/themes/advanced/jscripts/anchor.js");
		jsFiles.add("js/tiny_mce/themes/advanced/jscripts/about.js");
		jsFiles.add("js/tiny_mce/themes/advanced/jscripts/link.js");
		jsFiles.add("js/tiny_mce/themes/advanced/jscripts/charmap.js");
		jsFiles.add("js/tiny_mce/themes/advanced/langs/en.js");
		jsFiles.add("js/tiny_mce/themes/advanced/editor_template_src.js");
		jsFiles.add("js/tiny_mce/themes/advanced/editor_template.js");
		jsFiles.add("js/tiny_mce/utils/editable_selects.js");
		jsFiles.add("js/tiny_mce/utils/mctabs.js");
		jsFiles.add("js/tiny_mce/utils/mclayer.js");
		jsFiles.add("js/tiny_mce/utils/form_utils.js");
		jsFiles.add("js/tiny_mce/utils/validate.js");
		jsFiles.add("js/tiny_mce/langs/en.js");
		jsFiles.add("js/tiny_mce/tiny_mce_popup.js");
		jsFiles.add("js/tiny_mce/tiny_mce.js");
		jsFiles.add("js/tiny_mce/tiny_mce_src.js");
		
		// tinyMCE css files
		jsFiles.add("js/tiny_mce/plugins/contextmenu/css");
		jsFiles.add("js/tiny_mce/plugins/contextmenu/css/contextmenu.css");
		jsFiles.add("js/tiny_mce/plugins/advhr/css");
		jsFiles.add("js/tiny_mce/plugins/advhr/css/advhr.css");
		jsFiles.add("js/tiny_mce/plugins/table/css");
		jsFiles.add("js/tiny_mce/plugins/table/css/table.css");
		jsFiles.add("js/tiny_mce/plugins/table/css/row.css");
		jsFiles.add("js/tiny_mce/plugins/table/css/cell.css");
		jsFiles.add("js/tiny_mce/plugins/advimage/css");
		jsFiles.add("js/tiny_mce/plugins/advimage/css/advimage.css");
		jsFiles.add("js/tiny_mce/plugins/noneditable/css");
		jsFiles.add("js/tiny_mce/plugins/noneditable/css/noneditable.css");
		jsFiles.add("js/tiny_mce/plugins/style/css");
		jsFiles.add("js/tiny_mce/plugins/style/css/props.css");
		jsFiles.add("js/tiny_mce/plugins/fullpage/css");
		jsFiles.add("js/tiny_mce/plugins/fullpage/css/fullpage.css");
		jsFiles.add("js/tiny_mce/plugins/flash/css");
		jsFiles.add("js/tiny_mce/plugins/flash/css/content.css");
		jsFiles.add("js/tiny_mce/plugins/flash/css/flash.css");
		jsFiles.add("js/tiny_mce/plugins/paste/css");
		jsFiles.add("js/tiny_mce/plugins/paste/css/pasteword.css");
		jsFiles.add("js/tiny_mce/plugins/paste/css/blank.css");
		jsFiles.add("js/tiny_mce/plugins/inlinepopups/css");
		jsFiles.add("js/tiny_mce/plugins/inlinepopups/css/inlinepopup.css");
		jsFiles.add("js/tiny_mce/plugins/advlink/css");
		jsFiles.add("js/tiny_mce/plugins/advlink/css/advlink.css");
		jsFiles.add("js/tiny_mce/themes/simple/css");
		jsFiles.add("js/tiny_mce/themes/simple/css/editor_content.css");
		jsFiles.add("js/tiny_mce/themes/simple/css/editor_ui.css");
		jsFiles.add("js/tiny_mce/themes/simple/css/editor_popup.css");
		jsFiles.add("js/tiny_mce/themes/advanced/docs/en/style.css");
		jsFiles.add("js/tiny_mce/themes/advanced/css");
		jsFiles.add("js/tiny_mce/themes/advanced/css/editor_content.css");
		jsFiles.add("js/tiny_mce/themes/advanced/css/editor_ui.css");
		jsFiles.add("js/tiny_mce/themes/advanced/css/editor_popup.css");
		// tiny mce gif images
		jsFiles.add("js/tiny_mce/plugins/searchreplace/images/replace.gif");
		jsFiles.add("js/tiny_mce/plugins/searchreplace/images/search.gif");
		jsFiles.add("js/tiny_mce/plugins/contextmenu/images/spacer.gif");
		jsFiles.add("js/tiny_mce/plugins/directionality/images/rtl.gif");
		jsFiles.add("js/tiny_mce/plugins/directionality/images/ltr.gif");
		jsFiles.add("js/tiny_mce/plugins/insertdatetime/images/inserttime.gif");
		jsFiles.add("js/tiny_mce/plugins/insertdatetime/images/insertdate.gif");
		jsFiles.add("js/tiny_mce/plugins/advhr/images/advhr.gif");
		jsFiles.add("js/tiny_mce/plugins/print/images/print.gif");
		jsFiles.add("js/tiny_mce/plugins/emotions/images/smiley-undecided.gif");
		jsFiles.add("js/tiny_mce/plugins/emotions/images/smiley-cool.gif");
		jsFiles.add("js/tiny_mce/plugins/emotions/images/smiley-foot-in-mouth.gif");
		jsFiles.add("js/tiny_mce/plugins/emotions/images/smiley-tongue-out.gif");
		jsFiles.add("js/tiny_mce/plugins/emotions/images/smiley-frown.gif");
		jsFiles.add("js/tiny_mce/plugins/emotions/images/smiley-cry.gif");
		jsFiles.add("js/tiny_mce/plugins/emotions/images/smiley-wink.gif");
		jsFiles.add("js/tiny_mce/plugins/emotions/images/smiley-money-mouth.gif");
		jsFiles.add("js/tiny_mce/plugins/emotions/images/emotions.gif");
		jsFiles.add("js/tiny_mce/plugins/emotions/images/smiley-embarassed.gif");
		jsFiles.add("js/tiny_mce/plugins/emotions/images/smiley-yell.gif");
		jsFiles.add("js/tiny_mce/plugins/emotions/images/smiley-sealed.gif");
		jsFiles.add("js/tiny_mce/plugins/emotions/images/smiley-innocent.gif");
		jsFiles.add("js/tiny_mce/plugins/emotions/images/smiley-kiss.gif");
		jsFiles.add("js/tiny_mce/plugins/emotions/images/smiley-surprised.gif");
		jsFiles.add("js/tiny_mce/plugins/emotions/images/smiley-smile.gif");
		jsFiles.add("js/tiny_mce/plugins/emotions/images/smiley-laughing.gif");
		jsFiles.add("js/tiny_mce/plugins/table/images/table_merge_cells.gif");
		jsFiles.add("js/tiny_mce/plugins/table/images/table_delete.gif");
		jsFiles.add("js/tiny_mce/plugins/table/images/table.gif");
		jsFiles.add("js/tiny_mce/plugins/table/images/table_insert_row_after.gif");
		jsFiles.add("js/tiny_mce/plugins/table/images/table_delete_col.gif");
		jsFiles.add("js/tiny_mce/plugins/table/images/table_cell_props.gif");
		jsFiles.add("js/tiny_mce/plugins/table/images/table_row_props.gif");
		jsFiles.add("js/tiny_mce/plugins/table/images/table_insert_col_before.gif");
		jsFiles.add("js/tiny_mce/plugins/table/images/table_insert_col_after.gif");
		jsFiles.add("js/tiny_mce/plugins/table/images/table_split_cells.gif");
		jsFiles.add("js/tiny_mce/plugins/table/images/table_insert_row_before.gif");
		jsFiles.add("js/tiny_mce/plugins/table/images/table_delete_row.gif");
		jsFiles.add("js/tiny_mce/plugins/table/images/buttons.gif");
		jsFiles.add("js/tiny_mce/plugins/advimage/images/sample.gif");
		jsFiles.add("js/tiny_mce/plugins/layer/images/insert_layer.gif");
		jsFiles.add("js/tiny_mce/plugins/layer/images/absolute.gif");
		jsFiles.add("js/tiny_mce/plugins/layer/images/backward.gif");
		jsFiles.add("js/tiny_mce/plugins/layer/images/forward.gif");
		jsFiles.add("js/tiny_mce/plugins/preview/images/preview.gif");
		jsFiles.add("js/tiny_mce/plugins/iespell/images/iespell.gif");
		jsFiles.add("js/tiny_mce/plugins/style/images/apply_button_bg.gif");
		jsFiles.add("js/tiny_mce/plugins/style/images/style_info.gif");
		jsFiles.add("js/tiny_mce/plugins/fullpage/images/fullpage.gif");
		jsFiles.add("js/tiny_mce/plugins/fullpage/images/move_down.gif");
		jsFiles.add("js/tiny_mce/plugins/fullpage/images/move_up.gif");
		jsFiles.add("js/tiny_mce/plugins/fullpage/images/add.gif");
		jsFiles.add("js/tiny_mce/plugins/fullpage/images/remove.gif");
		jsFiles.add("js/tiny_mce/plugins/flash/images/flash.gif");
		jsFiles.add("js/tiny_mce/plugins/_template/images/template.gif");
		jsFiles.add("js/tiny_mce/plugins/paste/images/pasteword.gif");
		jsFiles.add("js/tiny_mce/plugins/paste/images/pastetext.gif");
		jsFiles.add("js/tiny_mce/plugins/paste/images/selectall.gif");
		jsFiles.add("js/tiny_mce/plugins/fullscreen/images/fullscreen.gif");
		jsFiles.add("js/tiny_mce/plugins/inlinepopups/images/window_close.gif");
		jsFiles.add("js/tiny_mce/plugins/inlinepopups/images/window_maximize.gif");
		jsFiles.add("js/tiny_mce/plugins/inlinepopups/images/window_minimize.gif");
		jsFiles.add("js/tiny_mce/plugins/inlinepopups/images/spacer.gif");
		jsFiles.add("js/tiny_mce/plugins/inlinepopups/images/window_resize.gif");
		jsFiles.add("js/tiny_mce/plugins/save/images/save.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/underline_ru.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/bold.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/numlist.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/cleanup.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/bold_ru.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/undo.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/italic_tw.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/bullist.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/bold_fr.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/redo.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/bold_tw.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/underline_tw.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/italic.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/underline.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/strikethrough.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/italic_de_se.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/underline_fr.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/spacer.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/separator.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/bold_de_se.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/italic_ru.gif");
		jsFiles.add("js/tiny_mce/themes/simple/images/buttons.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/xp/tab_sel_end.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/xp/tab_end.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/xp/tab_bg.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/xp/tab_sel_bg.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/xp/tabs_bg.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/underline_ru.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/unlink.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/justifyright.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/forecolor.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/bold.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/custom_1.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/copy.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/numlist.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/cleanup.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/bold_ru.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/undo.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/removeformat.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/italic_tw.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/color.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/close.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/bullist.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/backcolor.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/bold_fr.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/bold_es.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/cancel_button_bg.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/newdocument.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/image.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/menu_check.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/redo.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/help.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/sup.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/underline_tw.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/bold_tw.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/underline_es.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/italic.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/anchor.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/strikethrough.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/underline.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/italic_es.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/link.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/insert_button_bg.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/italic_de_se.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/button_menu.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/visualaid.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/sub.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/outdent.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/justifyfull.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/cut.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/code.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/underline_fr.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/browse.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/anchor_symbol.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/spacer.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/separator.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/justifycenter.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/hr.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/charmap.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/justifyleft.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/paste.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/bold_de_se.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/indent.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/statusbar_resize.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/italic_ru.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/images/buttons.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/docs/en/images/insert_anchor_window.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/docs/en/images/insert_image_window.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/docs/en/images/insert_link_window.gif");
		jsFiles.add("js/tiny_mce/themes/advanced/docs/en/images/insert_table_window.gif");
		
		jsFiles.add("js/tiny_mce/themes/advanced/source_editor.htm");
	}
	
	private static final List tmpltJsFiles = new ArrayList();
	static {
		tmpltJsFiles.add("js/validation/template-jsp-ui.js");
	}
	
	private static final List roJsFiles = Collections.unmodifiableList(jsFiles);
	private static final List roTmpltJsFiles = Collections.unmodifiableList(tmpltJsFiles);
	private static final List allJsFiles = new ArrayList();
	{
		allJsFiles.addAll(roJsFiles);
		allJsFiles.addAll(roTmpltJsFiles);
	}
	
	public List importFiles(InputData input) {
		List rtrn = new ArrayList();
		
		String fileName = (String)input.getGlobalData().get(INCLUDE_JS_FILE_KEY);
		String loadDefault = (String)input.getGlobalData().get(INCLUDE_JS_KEY);
		String loadTmplt = (String)input.getGlobalData().get(INCLUDE_TMPLT_JS_KEY);
		
		
		if (loadDefault != null) {
			return roJsFiles;
		}
		else if (loadTmplt != null) {
			return allJsFiles;
		}
		else if (fileName != null) {
			// have to load only ONE file, lets check if it is
			// in one of the lists
			int index = roJsFiles.indexOf(fileName);
			if (index != -1) {
				rtrn.add(fileName);
				return rtrn;
			}
			
			index = roTmpltJsFiles.indexOf(fileName);
			if (index != -1) {
				rtrn.add(fileName);
			}
			
			if (rtrn.size() == 0) {
				log.warn("Not allowed to load file "+fileName+". Add it to the allowed list.");
			}
		}
		return rtrn;
	}
}
