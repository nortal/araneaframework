package org.araneaframework.servlet.filter;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.servlet.ServletOutputData;
import org.araneaframework.servlet.ServletOverridableOutputData;
import org.araneaframework.servlet.util.AtomicResponseHelper;

/**
 * @author Nikita Salnikov-Tarnovski
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class AjaxFilterService extends BaseFilterService {
	static private final Logger log = Logger.getLogger(AjaxFilterService.class);
	
	private String characterEncoding = "UTF-8";
	private List existingRegions = null;

	public static final String UPDATE_REGIONS_KEY = "updateRegions";


	public void setCharacterEncoding(String encoding) {
		characterEncoding = encoding;
	}

	protected void action(Path path, InputData input, OutputData output) throws Exception {
		AtomicResponseHelper arUtil =
			new AtomicResponseHelper((ServletOverridableOutputData)output);

		try {
			HttpServletResponse servletResponse = ((ServletOutputData) output).getResponse();

			String commaSeparatedRegions = (String)input.getGlobalData().get(UPDATE_REGIONS_KEY); 

			log.debug("AjaxFilterService found regions = " + commaSeparatedRegions);

			super.action(path, input, output);

			if(commaSeparatedRegions != null) {
				String response = new String(arUtil.getData(), characterEncoding);

				log.info("It was Ajax request. Rollbacking current response.");
				arUtil.rollback();

				String transactionElement = getTransactionElement(response);
				log.debug("Transaction element " + transactionElement);
				servletResponse.getOutputStream().write(transactionElement.getBytes(characterEncoding));
				log.debug("updating regions " + commaSeparatedRegions);

				String[] regions = commaSeparatedRegions.split(",");
				// adding the regions that may appear in the response but
				// we're not present in the request
				if (existingRegions != null) {
					for (Iterator iter = existingRegions.iterator(); iter.hasNext();) {
						regions = (String[])ArrayUtils.add(regions, (String) iter.next());
					}
				}

				for (int i = 0; i < regions.length; i++) {
					String regionId = regions[i];
					
					String regionContent = getContentById(response, regionId);
					servletResponse.getOutputStream().write(regionContent.getBytes(characterEncoding));
				}
			}
		}
		finally {
			log.info("Ajax filter commits response.");
			arUtil.commit();
		}
	}

	public void setExistingRegions(List preExistingRegions) {
		this.existingRegions = preExistingRegions;
	}
	
	private String getContentById(String source, String id) {
		String blockStart = "<!--BEGIN:" + id + "-->";
		int startIndex = source.indexOf(blockStart);

		if(startIndex == -1)
			return "";

		String blockEnd = "<!--END:" + id + "-->";

		int endIndex = source.indexOf(blockEnd);

		if(endIndex == -1)
			throw new IllegalStateException("Expected END block for AJAX update regiond with id '" + id + "'");

	    return source.substring(startIndex, endIndex + blockEnd.length());
	}
	
	private String getTransactionElement(String source) throws Exception {
		RE re = new RE("(<input name=\"transactionId\" type=\"hidden\" value=\"-?[0-9]+\"\\/>)");
		re.match(source);
		return re.getParen(1);
	}
}

