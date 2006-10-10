package org.araneaframework.http.filter;

import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;
import org.apache.log4j.Logger;
import org.apache.regexp.RE;
import org.araneaframework.InputData;
import org.araneaframework.OutputData;
import org.araneaframework.Path;
import org.araneaframework.framework.core.BaseFilterService;
import org.araneaframework.http.HttpOutputData;
import org.araneaframework.http.util.AtomicResponseHelper;

/**
 * Update region filter, supporting updating of HTML page regions. It processes received request
 * in usual way&mdash;but the response will only contain the updated regions.
 * 
 * @author Nikita Salnikov-Tarnovski
 * @author "Toomas Römer" <toomas@webmedia.ee>
 */
public class StandardUpdateRegionFilterService extends BaseFilterService {
	static private final Logger log = Logger.getLogger(StandardUpdateRegionFilterService.class);
	
	private String characterEncoding = "UTF-8";
	private List existingRegions = null;

	public static final String UPDATE_REGIONS_KEY = "updateRegions";
	public static final String AJAX_REQUEST_ID_KEY = "ajaxRequestId";
	public static final String AJAX_RESPONSE_ID_KEY = "ajaxResponseId";

	public void setCharacterEncoding(String encoding) {
		characterEncoding = encoding;
	}

	protected void action(Path path, InputData input, OutputData output) throws Exception {
		AtomicResponseHelper arUtil =
			new AtomicResponseHelper(output);

		try {
			HttpOutputData httpOutput = (HttpOutputData) output;

			String commaSeparatedRegions = (String)input.getGlobalData().get(UPDATE_REGIONS_KEY); 

			if (log.isDebugEnabled())
			  log.debug("Received request to update regions = " + commaSeparatedRegions);

			super.action(path, input, output);

			if(commaSeparatedRegions != null) {
				String response = new String(arUtil.getData(), characterEncoding);
				String requestId = (String)input.getGlobalData().get(AJAX_REQUEST_ID_KEY);

				if (log.isDebugEnabled()) {
				  log.debug("It was Ajax request with id='" + requestId + "'. Will rollback current response and attempt to extract updated page regions.");
				}

				arUtil.rollback();

				String transactionElement = getTransactionElement(response);
				if (transactionElement != null)
				  httpOutput.getOutputStream().write(transactionElement.getBytes(characterEncoding));

				// ajax response id is the same as incoming request id.
				if (requestId != null) {
				  String responseIdElement = getResponseIdElement(requestId);
                  httpOutput.getOutputStream().write(responseIdElement.getBytes(characterEncoding));
                }

				String[] regions = commaSeparatedRegions.split(",");
				// adding the regions that may appear in the response but
				// were not present in the request
				if (existingRegions != null) {
					for (Iterator iter = existingRegions.iterator(); iter.hasNext();) {
						regions = (String[])ArrayUtils.add(regions, iter.next());
					}
				}

				for (int i = 0; i < regions.length; i++) {
					String regionId = regions[i];
					
					String regionContent = getContentById(response, regionId);
					httpOutput.getOutputStream().write(regionContent.getBytes(characterEncoding));
				}
			}
		}
		finally {
			log.info("Committing response.");
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
			throw new IllegalStateException("Expected END block for AJAX update region with id '" + id + "'.");

		if (log.isDebugEnabled())
			log.debug("Successfully extracted region '" + id + "' to be included in response.");

	    return source.substring(startIndex, endIndex + blockEnd.length());
	}
	
	protected String getTransactionElement(String source) throws Exception {
		RE re = new RE("(<input name=\"transactionId\" type=\"hidden\" value=\"-?[0-9]+\"\\/>)");
		re.match(source);
		return re.getParen(1);
	}
	
	protected String getResponseIdElement(String requestId) {
		return "<input name=\"" + AJAX_RESPONSE_ID_KEY + "\" type=\"hidden\" value=\"" + requestId +"\"/>";
	}
}

