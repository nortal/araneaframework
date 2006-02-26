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
import java.util.List;
import org.apache.log4j.Logger;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.servlet.ServletOutputData;
import org.araneaframework.servlet.filter.StandardServletFileImportFilterService;

/**
 * @author "Toomas Römer" <toomas@webmedia.ee>
 *
 */
public class ImageFileImporter extends DefaultFileImporter {
	private static final Logger log = Logger.getLogger(ImageFileImporter.class);
	
	public static final String TYPE = "imageFileImporter";
	public static final String LOAD_IMAGE_KEY = "loadImage";
	
	private String contentType = "image/gif";
	
	private static final List images = new ArrayList();
	static {
		images.add("gfx/i01.gif");
		images.add("gfx/i02.gif");
		images.add("gfx/i03.gif");
		images.add("gfx/i04.gif");
		images.add("gfx/i05.gif");
		images.add("gfx/i06.gif");
		images.add("gfx/i07.gif");
		images.add("gfx/i08.gif");
		images.add("gfx/i09.gif");
		images.add("gfx/i10.gif");
		images.add("gfx/i11.gif");
		images.add("gfx/i12.gif");
		images.add("gfx/i13.gif");
		images.add("gfx/i14.gif");
		
		images.add("gfx/i37.gif");
		images.add("gfx/i38.gif");
		images.add("gfx/i39.gif");
		images.add("gfx/i40.gif");
		
		images.add("gfx/ico_calendar.gif");
		images.add("gfx/ico_first.gif");
		images.add("gfx/ico_last.gif");
		images.add("gfx/ico_next.gif");
		images.add("gfx/ico_prev.gif");
		images.add("gfx/ico_sortdown.gif");
		images.add("gfx/ico_sortup.gif");
		images.add("gfx/logo_aranea_print.gif");
		images.add("gfx/logo_aranea_screen.jpg");
		images.add("gfx/logo_aranea_login.jpg");
				
		images.add("gfx/ico/ai.gif");
		images.add("gfx/ico/avi.gif");
		images.add("gfx/ico/bmp.gif");
		images.add("gfx/ico/cdr.gif");
		images.add("gfx/ico/csv.gif");
		images.add("gfx/ico/doc.gif");
		images.add("gfx/ico/eml.gif");
		images.add("gfx/ico/exe.gif");
		images.add("gfx/ico/gif.gif");
		images.add("gfx/ico/html.gif");
		images.add("gfx/ico/jpg.gif");
		images.add("gfx/ico/mdb.gif");
		images.add("gfx/ico/mp3.gif");
		images.add("gfx/ico/mpg.gif");
		images.add("gfx/ico/pdf.gif");
		images.add("gfx/ico/png.gif");
		images.add("gfx/ico/ppt.gif");
		images.add("gfx/ico/psd.gif");
		images.add("gfx/ico/rar.gif");
		images.add("gfx/ico/txt.gif");
		images.add("gfx/ico/xls.gif");
		images.add("gfx/ico/zip.gif");
		
		// TODO: check which of those below are actually used.
		
		images.add("gfx/bg16.gif");
		images.add("gfx/butt_accept.gif");
		images.add("gfx/butt_expand.gif");
		images.add("gfx/butt_kustuta.gif");
		images.add("gfx/butt_kustuta2.gif");
		images.add("gfx/butt_kustuta3.gif");
		images.add("gfx/butt_l6peta.gif");
		images.add("gfx/butt_lisa.gif");
		images.add("gfx/butt_lisa3.gif");
		images.add("gfx/butt_loobu.gif");
		images.add("gfx/butt_maximize.gif");
		images.add("gfx/butt_minimize.gif");
		images.add("gfx/butt_muuda.gif");
		images.add("gfx/butt_muuda2.gif");
		images.add("gfx/butt_muuda3.gif");
		images.add("gfx/butt_otsi.gif");
		images.add("gfx/butt_personal.gif");
		images.add("gfx/butt_privileegid.gif");
		images.add("gfx/butt_rollid.gif");
		images.add("gfx/butt_sisene.gif");
		images.add("gfx/butt_telli.gif");
		images.add("gfx/butt_teosta.gif");
		images.add("gfx/butt_vaata.gif");
		images.add("gfx/butt_vaheta.gif");
		images.add("gfx/calendar_close.gif");
		images.add("gfx/calendar_ico_left.gif");
		images.add("gfx/calendar_ico_open.gif");
		images.add("gfx/calendar_ico_right.gif");
		images.add("gfx/calendar_scrolldown.gif");
		images.add("gfx/calendar_scrollup.gif");
		images.add("gfx/dot01.gif");
		images.add("gfx/dot02.gif");
		images.add("gfx/dot03.gif");
		images.add("gfx/dot04.gif");
		images.add("gfx/dot04_disabled.gif");
		images.add("gfx/dot05.gif");
		images.add("gfx/dot05_red.gif");
		images.add("gfx/dot06.gif");
		images.add("gfx/dot07.gif");
		images.add("gfx/dot08.gif");
		images.add("gfx/dot09.gif");
		images.add("gfx/dot10.gif");
		images.add("gfx/dot11.gif");
		images.add("gfx/dot12.gif");
		images.add("gfx/dot13.gif");
		images.add("gfx/dot14.gif");
		images.add("gfx/dot15.gif");
		images.add("gfx/dot16.gif");
		images.add("gfx/dot17.gif");
		images.add("gfx/dot18.gif");
		images.add("gfx/dot19.gif");
		images.add("gfx/ico_add.gif");
		images.add("gfx/ico_add2.gif");
		images.add("gfx/ico_add3.gif");
		images.add("gfx/ico_delete.gif");
		images.add("gfx/ico_lipuke.gif");
		images.add("gfx/pages_b.gif");
		images.add("gfx/pages_bb.gif");
		images.add("gfx/pages_f.gif");
		images.add("gfx/pages_ff.gif");
	}
	
	public void setHeaders(OutputData output) {
		super.setHeaders(output);
		((ServletOutputData)output).getResponse().setContentType(contentType);
		log.debug("Setting content-type to " + contentType);
	}

	public List importFiles(InputData input) {
		List rtrn = new ArrayList();
		String fileName = (String)input.getGlobalData().get(LOAD_IMAGE_KEY);
		
		if (fileName!=null) {
			if (images.indexOf(fileName) != -1) {
				rtrn.add(fileName);
			}
			
			if (fileName.matches("^.*\\.jpg$")) {
				contentType = "image/jpeg";
			}
		}
		
		return rtrn;
	}
	
	/**
	 * Returns the string needed to import image with given name from within HTML.
	 * @param fileName
	 */
	public final static String getImportString(String fileName) {
		StringBuffer sb = new StringBuffer();
		sb.append("?");
		sb.append(ImageFileImporter.LOAD_IMAGE_KEY);
		sb.append("=");
		sb.append(fileName);
		sb.append("&");
		sb.append(StandardServletFileImportFilterService.IMPORTER_TYPE_KEY);
		sb.append("=");
		sb.append(ImageFileImporter.TYPE);

		return sb.toString();
	}
}
