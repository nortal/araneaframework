// Copyright 1991-2005 Mort Bay Consulting Pty. Ltd.

package org.araneaframework.http;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.apache.log4j.Logger;
import org.araneaframework.servlet.router.JGroupClusteredSessionRouterService;
import org.jgroups.Address;
import org.jgroups.Channel;
import org.jgroups.ChannelException;
import org.jgroups.JChannel;
import org.jgroups.Message;
import org.jgroups.Receiver;
import org.jgroups.View;
import org.jgroups.stack.IpAddress;
import org.mortbay.http.HttpContext;
import org.mortbay.http.HttpServer;
import org.mortbay.util.InetAddrPort;

/**
 * @author Taimo Peelo (taimo@ut.ee)
 */
public class JGroupsClusteringHttpProxyServer extends HttpServer {
	private static Logger log = Logger.getLogger(JGroupsClusteringHttpProxyServer.class);
	
	private static List nodes = Collections.synchronizedList(new ArrayList());
	
	private static Channel channel;
	
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
		
        if (log.isDebugEnabled())
        		log.debug("Starting clustering proxy at address." + args[0]);

		try {
			
			if (args.length==1)
			{
				// Create the server
				HttpServer server = new HttpServer();
				
				// Default is no virtual host
				String host=null;
				HttpContext context = server.getContext(host,"/");
				context.addHandler(new CustomProxyHandler(new LoadBalancer() {
					private Random rnd = new Random();

					public String getServingHost() {
						int nNodes;
						// XXX (TP):no need to sync random ??
						int nRnd = Math.abs(rnd.nextInt());
						synchronized (nodes) {
							nNodes = nodes.size();
							if (nNodes == 0)
								return null;
							return ((IpAddress)nodes.get(nRnd % nNodes)).getIpAddress().getHostAddress();
						}
					}
				}));
				
				InetAddrPort address = new InetAddrPort(args[0]);
				server.addListener(address);

				server.start();
			}
			
			String props =  "UDP:" + 
	        "PING(num_initial_members=2;timeout=3000):" + 
	        "FD:" + 
	        "STABLE:" + 
	        "NAKACK:" + 
	        "UNICAST:" + 
	        "FRAG:" + 
	        "FLUSH:" + 
	        "GMS:" + 
	        "VIEW_ENFORCER:" + 
	        "STATE_TRANSFER:" + 
	        "QUEUE";
	    
		    try {
				channel = new JChannel(props);
				channel.connect(JGroupClusteredSessionRouterService.JGROUP_NAME);
				
				channel.setReceiver(new Receiver() {
					public void block() {
						log.debug("block() called");
					}

					public void suspect(Address address) {
						log.debug("suspect(" + address + ") called");
					}

					public void viewAccepted(View new_view) {
						log.debug("New view received");
						updateNodes(channel.getView().getMembers());
					}

					public byte[] getState() {
						log.debug("getState() called");
						return null;
					}

					public void receive(Message msg) {
						log.debug("receive(" + msg + ") called ");
					}

					public void setState(byte[] arg0) {
						log.debug("setState("+ arg0+ ") called");
					}
				});
				
				updateNodes(channel.getView().getMembers());
				
			} catch (ChannelException x) {
				throw new NestableRuntimeException(x);
			} 
		}
		catch (Exception e)
		{
			log.warn("Stranger beware! An exception '" + e + "' was thrown.");
		}
	}
	
	private static void updateNodes(Collection members) {
		log.debug("Updating registered node information.");

		synchronized (nodes) {
			nodes.clear();
			
			//XXX (TP): do not add selves address (damn string comparison...)
			for (Iterator i = members.iterator(); i.hasNext(); ) {
				IpAddress memberAddress = (IpAddress)i.next();
				if (!memberAddress.toString().equals(channel.getLocalAddress().toString()))
					nodes.add(memberAddress);
			}
			
			System.out.println("***********************************************************");
			for (Iterator i = nodes.iterator(); i.hasNext(); ) {
				System.out.println(((IpAddress)i.next()));
			}
			System.out.println("***********************************************************");
		}
	}
}

