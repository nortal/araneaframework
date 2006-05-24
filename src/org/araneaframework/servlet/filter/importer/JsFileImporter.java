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
	}
	
	private static final List allowedFiles = new ArrayList();
	static {
		// tiny mce stuff
		allowedFiles.add("js/tiny_mce/plugins/searchreplace/jscripts/search.js");
		allowedFiles.add("js/tiny_mce/plugins/searchreplace/jscripts/replace.js");
		allowedFiles.add("js/tiny_mce/plugins/searchreplace/langs/en.js");
		allowedFiles.add("js/tiny_mce/plugins/searchreplace/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/searchreplace/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/zoom/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/zoom/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/contextmenu/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/contextmenu/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/directionality/langs/en.js");
		allowedFiles.add("js/tiny_mce/plugins/directionality/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/directionality/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/insertdatetime/langs/en.js");
		allowedFiles.add("js/tiny_mce/plugins/insertdatetime/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/insertdatetime/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/cleanup/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/cleanup/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/advhr/jscripts/rule.js");
		allowedFiles.add("js/tiny_mce/plugins/advhr/langs/en.js");
		allowedFiles.add("js/tiny_mce/plugins/advhr/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/advhr/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/print/langs/en.js");
		allowedFiles.add("js/tiny_mce/plugins/print/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/print/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/emotions/jscripts/functions.js");
		allowedFiles.add("js/tiny_mce/plugins/emotions/langs/en.js");
		allowedFiles.add("js/tiny_mce/plugins/emotions/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/emotions/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/table/jscripts/table.js");
		allowedFiles.add("js/tiny_mce/plugins/table/jscripts/row.js");
		allowedFiles.add("js/tiny_mce/plugins/table/jscripts/cell.js");
		allowedFiles.add("js/tiny_mce/plugins/table/jscripts/merge_cells.js");
		allowedFiles.add("js/tiny_mce/plugins/table/langs/en.js");
		allowedFiles.add("js/tiny_mce/plugins/table/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/table/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/advimage/jscripts/functions.js");
		allowedFiles.add("js/tiny_mce/plugins/advimage/langs/en.js");
		allowedFiles.add("js/tiny_mce/plugins/advimage/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/advimage/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/layer/langs/en.js");
		allowedFiles.add("js/tiny_mce/plugins/layer/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/layer/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/preview/langs/en.js");
		allowedFiles.add("js/tiny_mce/plugins/preview/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/preview/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/noneditable/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/noneditable/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/iespell/langs/en.js");
		allowedFiles.add("js/tiny_mce/plugins/iespell/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/iespell/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/style/jscripts/props.js");
		allowedFiles.add("js/tiny_mce/plugins/style/langs/en.js");
		allowedFiles.add("js/tiny_mce/plugins/style/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/style/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/fullpage/jscripts/fullpage.js");
		allowedFiles.add("js/tiny_mce/plugins/fullpage/langs/en.js");
		allowedFiles.add("js/tiny_mce/plugins/fullpage/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/fullpage/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/flash/jscripts/flash.js");
		allowedFiles.add("js/tiny_mce/plugins/flash/langs/en.js");
		allowedFiles.add("js/tiny_mce/plugins/flash/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/flash/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/_template/langs/en.js");
		allowedFiles.add("js/tiny_mce/plugins/_template/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/_template/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/paste/jscripts/pastetext.js");
		allowedFiles.add("js/tiny_mce/plugins/paste/jscripts/pasteword.js");
		allowedFiles.add("js/tiny_mce/plugins/paste/langs/en.js");
		allowedFiles.add("js/tiny_mce/plugins/paste/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/paste/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/autosave/langs/en.js");
		allowedFiles.add("js/tiny_mce/plugins/autosave/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/autosave/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/fullscreen/langs/en.js");
		allowedFiles.add("js/tiny_mce/plugins/fullscreen/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/fullscreen/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/inlinepopups/jscripts/mcwindows.js");
		allowedFiles.add("js/tiny_mce/plugins/inlinepopups/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/inlinepopups/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/advlink/jscripts/functions.js");
		allowedFiles.add("js/tiny_mce/plugins/advlink/langs/en.js");
		allowedFiles.add("js/tiny_mce/plugins/advlink/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/plugins/advlink/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/save/langs/en.js");
		allowedFiles.add("js/tiny_mce/plugins/save/editor_plugin_src.js");
		allowedFiles.add("js/tiny_mce/plugins/save/editor_plugin.js");
		allowedFiles.add("js/tiny_mce/themes/simple/editor_template_src.js");
		allowedFiles.add("js/tiny_mce/themes/simple/editor_template.js");
		allowedFiles.add("js/tiny_mce/themes/advanced/jscripts/source_editor.js");
		allowedFiles.add("js/tiny_mce/themes/advanced/jscripts/image.js");
		allowedFiles.add("js/tiny_mce/themes/advanced/jscripts/color_picker.js");
		allowedFiles.add("js/tiny_mce/themes/advanced/jscripts/anchor.js");
		allowedFiles.add("js/tiny_mce/themes/advanced/jscripts/about.js");
		allowedFiles.add("js/tiny_mce/themes/advanced/jscripts/link.js");
		allowedFiles.add("js/tiny_mce/themes/advanced/jscripts/charmap.js");
		allowedFiles.add("js/tiny_mce/themes/advanced/langs/en.js");
		allowedFiles.add("js/tiny_mce/themes/advanced/editor_template_src.js");
		allowedFiles.add("js/tiny_mce/themes/advanced/editor_template.js");
		allowedFiles.add("js/tiny_mce/utils/editable_selects.js");
		allowedFiles.add("js/tiny_mce/utils/mctabs.js");
		allowedFiles.add("js/tiny_mce/utils/mclayer.js");
		allowedFiles.add("js/tiny_mce/utils/form_utils.js");
		allowedFiles.add("js/tiny_mce/utils/validate.js");
		allowedFiles.add("js/tiny_mce/langs/en.js");
		allowedFiles.add("js/tiny_mce/tiny_mce_popup.js");
		allowedFiles.add("js/tiny_mce/tiny_mce_src.js");
		allowedFiles.add("js/tiny_mce/tiny_mce.js");
		
		// tinyMCE css files
		allowedFiles.add("js/tiny_mce/plugins/contextmenu/css");
		allowedFiles.add("js/tiny_mce/plugins/contextmenu/css/contextmenu.css");
		allowedFiles.add("js/tiny_mce/plugins/advhr/css");
		allowedFiles.add("js/tiny_mce/plugins/advhr/css/advhr.css");
		allowedFiles.add("js/tiny_mce/plugins/table/css");
		allowedFiles.add("js/tiny_mce/plugins/table/css/table.css");
		allowedFiles.add("js/tiny_mce/plugins/table/css/row.css");
		allowedFiles.add("js/tiny_mce/plugins/table/css/cell.css");
		allowedFiles.add("js/tiny_mce/plugins/advimage/css");
		allowedFiles.add("js/tiny_mce/plugins/advimage/css/advimage.css");
		allowedFiles.add("js/tiny_mce/plugins/noneditable/css");
		allowedFiles.add("js/tiny_mce/plugins/noneditable/css/noneditable.css");
		allowedFiles.add("js/tiny_mce/plugins/style/css");
		allowedFiles.add("js/tiny_mce/plugins/style/css/props.css");
		allowedFiles.add("js/tiny_mce/plugins/fullpage/css");
		allowedFiles.add("js/tiny_mce/plugins/fullpage/css/fullpage.css");
		allowedFiles.add("js/tiny_mce/plugins/flash/css");
		allowedFiles.add("js/tiny_mce/plugins/flash/css/content.css");
		allowedFiles.add("js/tiny_mce/plugins/flash/css/flash.css");
		allowedFiles.add("js/tiny_mce/plugins/paste/css");
		allowedFiles.add("js/tiny_mce/plugins/paste/css/pasteword.css");
		allowedFiles.add("js/tiny_mce/plugins/paste/css/blank.css");
		allowedFiles.add("js/tiny_mce/plugins/inlinepopups/css");
		allowedFiles.add("js/tiny_mce/plugins/inlinepopups/css/inlinepopup.css");
		allowedFiles.add("js/tiny_mce/plugins/advlink/css");
		allowedFiles.add("js/tiny_mce/plugins/advlink/css/advlink.css");
		allowedFiles.add("js/tiny_mce/themes/simple/css");
		allowedFiles.add("js/tiny_mce/themes/simple/css/editor_content.css");
		allowedFiles.add("js/tiny_mce/themes/simple/css/editor_ui.css");
		allowedFiles.add("js/tiny_mce/themes/simple/css/editor_popup.css");
		allowedFiles.add("js/tiny_mce/themes/advanced/docs/en/style.css");
		allowedFiles.add("js/tiny_mce/themes/advanced/css");
		allowedFiles.add("js/tiny_mce/themes/advanced/css/editor_content.css");
		allowedFiles.add("js/tiny_mce/themes/advanced/css/editor_ui.css");
		allowedFiles.add("js/tiny_mce/themes/advanced/css/editor_popup.css");
		// tiny mce gif images
		allowedFiles.add("js/tiny_mce/plugins/searchreplace/images/replace.gif");
		allowedFiles.add("js/tiny_mce/plugins/searchreplace/images/search.gif");
		allowedFiles.add("js/tiny_mce/plugins/contextmenu/images/spacer.gif");
		allowedFiles.add("js/tiny_mce/plugins/directionality/images/rtl.gif");
		allowedFiles.add("js/tiny_mce/plugins/directionality/images/ltr.gif");
		allowedFiles.add("js/tiny_mce/plugins/insertdatetime/images/inserttime.gif");
		allowedFiles.add("js/tiny_mce/plugins/insertdatetime/images/insertdate.gif");
		allowedFiles.add("js/tiny_mce/plugins/advhr/images/advhr.gif");
		allowedFiles.add("js/tiny_mce/plugins/print/images/print.gif");
		allowedFiles.add("js/tiny_mce/plugins/emotions/images/smiley-undecided.gif");
		allowedFiles.add("js/tiny_mce/plugins/emotions/images/smiley-cool.gif");
		allowedFiles.add("js/tiny_mce/plugins/emotions/images/smiley-foot-in-mouth.gif");
		allowedFiles.add("js/tiny_mce/plugins/emotions/images/smiley-tongue-out.gif");
		allowedFiles.add("js/tiny_mce/plugins/emotions/images/smiley-frown.gif");
		allowedFiles.add("js/tiny_mce/plugins/emotions/images/smiley-cry.gif");
		allowedFiles.add("js/tiny_mce/plugins/emotions/images/smiley-wink.gif");
		allowedFiles.add("js/tiny_mce/plugins/emotions/images/smiley-money-mouth.gif");
		allowedFiles.add("js/tiny_mce/plugins/emotions/images/emotions.gif");
		allowedFiles.add("js/tiny_mce/plugins/emotions/images/smiley-embarassed.gif");
		allowedFiles.add("js/tiny_mce/plugins/emotions/images/smiley-yell.gif");
		allowedFiles.add("js/tiny_mce/plugins/emotions/images/smiley-sealed.gif");
		allowedFiles.add("js/tiny_mce/plugins/emotions/images/smiley-innocent.gif");
		allowedFiles.add("js/tiny_mce/plugins/emotions/images/smiley-kiss.gif");
		allowedFiles.add("js/tiny_mce/plugins/emotions/images/smiley-surprised.gif");
		allowedFiles.add("js/tiny_mce/plugins/emotions/images/smiley-smile.gif");
		allowedFiles.add("js/tiny_mce/plugins/emotions/images/smiley-laughing.gif");
		allowedFiles.add("js/tiny_mce/plugins/table/images/table_merge_cells.gif");
		allowedFiles.add("js/tiny_mce/plugins/table/images/table_delete.gif");
		allowedFiles.add("js/tiny_mce/plugins/table/images/table.gif");
		allowedFiles.add("js/tiny_mce/plugins/table/images/table_insert_row_after.gif");
		allowedFiles.add("js/tiny_mce/plugins/table/images/table_delete_col.gif");
		allowedFiles.add("js/tiny_mce/plugins/table/images/table_cell_props.gif");
		allowedFiles.add("js/tiny_mce/plugins/table/images/table_row_props.gif");
		allowedFiles.add("js/tiny_mce/plugins/table/images/table_insert_col_before.gif");
		allowedFiles.add("js/tiny_mce/plugins/table/images/table_insert_col_after.gif");
		allowedFiles.add("js/tiny_mce/plugins/table/images/table_split_cells.gif");
		allowedFiles.add("js/tiny_mce/plugins/table/images/table_insert_row_before.gif");
		allowedFiles.add("js/tiny_mce/plugins/table/images/table_delete_row.gif");
		allowedFiles.add("js/tiny_mce/plugins/table/images/buttons.gif");
		allowedFiles.add("js/tiny_mce/plugins/advimage/images/sample.gif");
		allowedFiles.add("js/tiny_mce/plugins/layer/images/insert_layer.gif");
		allowedFiles.add("js/tiny_mce/plugins/layer/images/absolute.gif");
		allowedFiles.add("js/tiny_mce/plugins/layer/images/backward.gif");
		allowedFiles.add("js/tiny_mce/plugins/layer/images/forward.gif");
		allowedFiles.add("js/tiny_mce/plugins/preview/images/preview.gif");
		allowedFiles.add("js/tiny_mce/plugins/iespell/images/iespell.gif");
		allowedFiles.add("js/tiny_mce/plugins/style/images/apply_button_bg.gif");
		allowedFiles.add("js/tiny_mce/plugins/style/images/style_info.gif");
		allowedFiles.add("js/tiny_mce/plugins/fullpage/images/fullpage.gif");
		allowedFiles.add("js/tiny_mce/plugins/fullpage/images/move_down.gif");
		allowedFiles.add("js/tiny_mce/plugins/fullpage/images/move_up.gif");
		allowedFiles.add("js/tiny_mce/plugins/fullpage/images/add.gif");
		allowedFiles.add("js/tiny_mce/plugins/fullpage/images/remove.gif");
		allowedFiles.add("js/tiny_mce/plugins/flash/images/flash.gif");
		allowedFiles.add("js/tiny_mce/plugins/_template/images/template.gif");
		allowedFiles.add("js/tiny_mce/plugins/paste/images/pasteword.gif");
		allowedFiles.add("js/tiny_mce/plugins/paste/images/pastetext.gif");
		allowedFiles.add("js/tiny_mce/plugins/paste/images/selectall.gif");
		allowedFiles.add("js/tiny_mce/plugins/fullscreen/images/fullscreen.gif");
		allowedFiles.add("js/tiny_mce/plugins/inlinepopups/images/window_close.gif");
		allowedFiles.add("js/tiny_mce/plugins/inlinepopups/images/window_maximize.gif");
		allowedFiles.add("js/tiny_mce/plugins/inlinepopups/images/window_minimize.gif");
		allowedFiles.add("js/tiny_mce/plugins/inlinepopups/images/spacer.gif");
		allowedFiles.add("js/tiny_mce/plugins/inlinepopups/images/window_resize.gif");
		allowedFiles.add("js/tiny_mce/plugins/save/images/save.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/underline_ru.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/bold.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/numlist.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/cleanup.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/bold_ru.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/undo.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/italic_tw.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/bullist.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/bold_fr.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/redo.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/bold_tw.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/underline_tw.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/italic.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/underline.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/strikethrough.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/italic_de_se.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/underline_fr.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/spacer.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/separator.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/bold_de_se.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/italic_ru.gif");
		allowedFiles.add("js/tiny_mce/themes/simple/images/buttons.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/xp/tab_sel_end.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/xp/tab_end.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/xp/tab_bg.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/xp/tab_sel_bg.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/xp/tabs_bg.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/underline_ru.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/unlink.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/justifyright.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/forecolor.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/bold.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/custom_1.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/copy.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/numlist.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/cleanup.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/bold_ru.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/undo.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/removeformat.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/italic_tw.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/color.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/close.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/bullist.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/backcolor.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/bold_fr.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/bold_es.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/cancel_button_bg.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/newdocument.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/image.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/menu_check.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/redo.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/help.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/sup.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/underline_tw.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/bold_tw.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/underline_es.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/italic.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/anchor.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/strikethrough.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/underline.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/italic_es.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/link.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/insert_button_bg.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/italic_de_se.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/button_menu.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/visualaid.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/sub.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/outdent.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/justifyfull.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/cut.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/code.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/underline_fr.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/browse.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/anchor_symbol.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/spacer.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/separator.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/justifycenter.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/hr.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/charmap.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/justifyleft.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/paste.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/bold_de_se.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/indent.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/statusbar_resize.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/italic_ru.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/images/buttons.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/docs/en/images/insert_anchor_window.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/docs/en/images/insert_image_window.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/docs/en/images/insert_link_window.gif");
		allowedFiles.add("js/tiny_mce/themes/advanced/docs/en/images/insert_table_window.gif");
		
		allowedFiles.add("js/tiny_mce/themes/advanced/source_editor.htm");
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
			int index = allJsFiles.indexOf(fileName);
			if (index != -1) {
				rtrn.add(fileName);
				return rtrn;
			} 
			
			index = allowedFiles.indexOf(fileName);
			if (index != -1) {
				rtrn.add(fileName);
				return rtrn;
			}
			
			log.warn("Not allowed to load file "+fileName+". Add it to the allowed list.");
		}
		return rtrn;
	}
}
