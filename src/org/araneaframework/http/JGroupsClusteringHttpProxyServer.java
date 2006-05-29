// Copyright 1991-2005 Mort Bay Consulting Pty. Ltd.

package org.araneaframework.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Enumeration;
import org.apache.commons.logging.Log;
import org.mortbay.http.HttpContext;
import org.mortbay.http.HttpException;
import org.mortbay.http.HttpFields;
import org.mortbay.http.HttpRequest;
import org.mortbay.http.HttpResponse;
import org.mortbay.http.HttpServer;
import org.mortbay.http.handler.AbstractHttpHandler;
import org.mortbay.log.LogFactory;
import org.mortbay.util.IO;
import org.mortbay.util.InetAddrPort;
import org.mortbay.util.LogSupport;
import org.mortbay.util.StringMap;

/**
 * org.mortbay.http.handler.ProxyHandler
 * @author Taimo Peelo (taimo@ut.ee)
 */
public class JGroupsClusteringHttpProxyServer extends HttpServer {
	private static Log log = LogFactory.getLog(HttpServer.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
        if (args.length==0 || args.length>1)
        {
            System.err.println
                ("\nUsage - org.araneaframework.http.JGroupsClusteringHttpProxyServer [<addr>:]<port>");
            System.exit(1);
        }
		
		System.out.println("Starting clustering proxy at address." + args[0]);

		try {
			
			if (args.length==1)
			{
				// Create the server
				HttpServer server = new HttpServer();
				
				// Default is no virtual host
				String host=null;
				HttpContext context = server.getContext(host,"/");
				context.addHandler(new JGroupsClusteringHttpProxyServer.JGroupProxyHandler());
				
				InetAddrPort address = new InetAddrPort(args[0]);
				server.addListener(address);

				server.start();
			}
			
		}
		catch (Exception e)
		{
			log.warn(LogSupport.EXCEPTION,e);
		}
	}
	
	protected static class JGroupProxyHandler extends AbstractHttpHandler {
	    protected StringMap _DontProxyHeaders = new StringMap();
	    {
	        Object o = new Object();
	        _DontProxyHeaders.setIgnoreCase(true);
	        _DontProxyHeaders.put(HttpFields.__ProxyConnection, o);
	        _DontProxyHeaders.put(HttpFields.__Connection, o);
	        _DontProxyHeaders.put(HttpFields.__KeepAlive, o);
	        _DontProxyHeaders.put(HttpFields.__TransferEncoding, o);
	        _DontProxyHeaders.put(HttpFields.__TE, o);
	        _DontProxyHeaders.put(HttpFields.__Trailer, o);
	        _DontProxyHeaders.put(HttpFields.__Upgrade, o);
	    }
		
		public void handle(String pathInContext, String pathParams, HttpRequest request, HttpResponse response) throws HttpException, IOException {
			System.out.println("SMTH");
			System.out.println("Request received: ");
			System.out.println("pathInContext : " + pathInContext);
			System.out.println("pathParams : " + pathParams);
			System.out.println("request URI :" + request.getURI());
			System.out.println("request URI parameternames:" + request.getURI().getParameterNames());
			System.out.println("request URL :" + request.getRequestURL());
			proxyHandle(pathInContext, pathParams, request, response);
		}
		
		public void proxyHandle(String pathInContext, String pathParams, HttpRequest request, HttpResponse response) throws HttpException, IOException {
	        // Ignore CONNECT
	        if (HttpRequest.__CONNECT.equalsIgnoreCase(request.getMethod()))
	        		return;

	        try
	        {
	            //URL thisURL = new URL(request.getRequestURL().toString());
	        		URL url = null;
	        		int seconds = new Date().getSeconds();
	        		if ((seconds % 4) == 0) {
	        			url = new URL("http", "192.168.40.193", 12000, request.getURI().toString());
	        		} else{
	        			url = new URL("http", "192.168.40.167", 12000, request.getURI().toString());
	        		} 
	        		
	        		System.out.println("Proxying request to URL : " + url);
	        		
	            if (url == null)
	                return;

	            if (log.isDebugEnabled())
	                log.debug("PROXY URL=" + url);

	            URLConnection connection = url.openConnection();
	            connection.setAllowUserInteraction(false);

	            // Set method
	            HttpURLConnection http = null;
	            if (connection instanceof HttpURLConnection)
	            {
	                http = (HttpURLConnection) connection;
	                http.setRequestMethod(request.getMethod());
	                http.setInstanceFollowRedirects(false);
	            }

	            // check connection header
	            String connectionHdr = request.getField(HttpFields.__Connection);
	            if (connectionHdr != null && (connectionHdr.equalsIgnoreCase(HttpFields.__KeepAlive) || connectionHdr.equalsIgnoreCase(HttpFields.__Close)))
	                connectionHdr = null;

	            // copy headers
	            boolean xForwardedFor = false;
	            boolean hasContent = false;
	            Enumeration enm = request.getFieldNames();
	            while (enm.hasMoreElements())
	            {
	                // TODO could be better than this!
	                String hdr = (String) enm.nextElement();

	                if (_DontProxyHeaders.containsKey(hdr))
	                    continue;

	                if (connectionHdr != null && connectionHdr.indexOf(hdr) >= 0)
	                    continue;

	                if (HttpFields.__ContentType.equals(hdr))
	                    hasContent = true;

	                Enumeration vals = request.getFieldValues(hdr);
	                while (vals.hasMoreElements())
	                {
	                    String val = (String) vals.nextElement();
	                    if (val != null)
	                    {
	                        connection.addRequestProperty(hdr, val);
	                        xForwardedFor |= HttpFields.__XForwardedFor.equalsIgnoreCase(hdr);
	                    }
	                }
	            }

                connection.setRequestProperty("Via", "1.1 (jetty)");
	            if (!xForwardedFor)
	                connection.addRequestProperty(HttpFields.__XForwardedFor, request.getRemoteAddr());

	            // a little bit of cache control
	            String cache_control = request.getField(HttpFields.__CacheControl);
	            if (cache_control != null && (cache_control.indexOf("no-cache") >= 0 || cache_control.indexOf("no-store") >= 0))
	                connection.setUseCaches(false);

	            try
	            {
	                connection.setDoInput(true);

	                // do input thang!
	                InputStream in = request.getInputStream();
	                if (hasContent)
	                {
	                    connection.setDoOutput(true);
	                    IO.copy(in, connection.getOutputStream());
	                }

	                // Connect
	                connection.connect();
	            }
	            catch (Exception e)
	            {
	                LogSupport.ignore(log, e);
	            }

	            InputStream proxy_in = null;

	            // handler status codes etc.
	            int code = HttpResponse.__500_Internal_Server_Error;
	            if (http != null)
	            {
	                proxy_in = http.getErrorStream();

	                code = http.getResponseCode();
	                response.setStatus(code);
	                response.setReason(http.getResponseMessage());
	            }

	            if (proxy_in == null)
	            {
	                try
	                {
	                    proxy_in = connection.getInputStream();
	                }
	                catch (Exception e)
	                {
	                    LogSupport.ignore(log, e);
	                    proxy_in = http.getErrorStream();
	                }
	            }

	            // clear response defaults.
	            response.removeField(HttpFields.__Date);
	            response.removeField(HttpFields.__Server);

	            // set response headers
	            int h = 0;
	            String hdr = connection.getHeaderFieldKey(h);
	            String val = connection.getHeaderField(h);
	            while (hdr != null || val != null)
	            {
	                if (hdr != null && val != null && !_DontProxyHeaders.containsKey(hdr))
	                    response.addField(hdr, val);
	                h++;
	                hdr = connection.getHeaderFieldKey(h);
	                val = connection.getHeaderField(h);
	            }

                response.setField("Via", "1.1 (jetty)");

	            // Handled
	            request.setHandled(true);
	            if (proxy_in != null)
	                IO.copy(proxy_in, response.getOutputStream());

	        }
	        catch (Exception e)
	        {
	            log.warn(e.toString());
	            LogSupport.ignore(log, e);
	            if (!response.isCommitted())
	                response.sendError(HttpResponse.__400_Bad_Request);
	        }

		}
	}
}
