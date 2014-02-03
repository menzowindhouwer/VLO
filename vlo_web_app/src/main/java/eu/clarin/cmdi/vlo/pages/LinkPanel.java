
package eu.clarin.cmdi.vlo.pages;

import eu.clarin.cmdi.vlo.FacetConstants;
import eu.clarin.cmdi.vlo.config.VloConfig;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.apache.wicket.markup.html.panel.Panel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author keeloo
 */
public class LinkPanel extends Panel {
        
    private final static String URN_NBN_RESOLVER_URL = "http://www.nbn-resolving.org/redirect/";

    private final static Logger LOG = LoggerFactory.getLogger(ResourceLinkPanel.class);
    
    /**
     *
     */
    public LinkPanel(String id) {
        super(id);
    }

    /**
     * Modifies resourceLink if necessary (adds support for different URN
     * resolvers)
     *
     * @param resourceLink
     * @return Modified resourceLink, if no modifications are necessary original
     * parameter resourceLink is returned
     */
    public String getHref(String resourceLink) {
        String result = resourceLink;
        if (resourceLink != null) {
            if (resourceLink.startsWith(FacetConstants.HANDLE_PREFIX)) {
                String handle = resourceLink.substring(FacetConstants.HANDLE_PREFIX.length());
                result = VloConfig.getHandleServerUrl() + handle;
            } else if(resourceLink.startsWith(FacetConstants.URN_NBN_PREFIX)) {
                result = URN_NBN_RESOLVER_URL+resourceLink;
            }
        }
        return result;
    }

    // copied from ResourceLinkPanel
    
    protected String getNameFromLink(String resourceLink) {
        String result = resourceLink;
        // We ALWAYS backoff to the resourceLink as default thingy.

        // HandleResolver does not work at the moment on the clarin server see http://trac.clarin.eu/ticket/136, Disabled it for the release.
//      if (resourceLink != null) {
//          if (resourceLink.startsWith(FacetConstants.HANDLE_PREFIX)) {
//              try {
//                  String handle = resourceLink.substring(FacetConstants.HANDLE_PREFIX.length());
//                  HandleResolver handleResolver = new HandleResolver();
//                  handleResolver.setTcpTimeout(5000);//5 secs, default is one minute
//                  HandleValue values[] = handleResolver.resolveHandle(handle, new String[] { "URL" }, null);
//
//                  for (HandleValue handleValue : values) {
//                      String url = handleValue.getDataAsString();
//                      int index = url.lastIndexOf('/');
//                      if (index != -1) {
//                          String name = url.substring(index + 1).trim();
//                          if (name.length() > 1) {
//                              result = name + " (" + resourceLink + ")";
//                          }
//                          break;
//                      }
//                  }
//              } catch (HandleException e) {
//                  LOG.warn("Error trying to get the name of the handle", e);
//              }
//          }
//      }

        /** NOTE: We are trying a different approach from the "official" one.
         * Will use the REST interface of hdl.handle.net.
         */
        if (resourceLink != null) {
            if (resourceLink.startsWith(FacetConstants.HANDLE_PREFIX)) {
                String handle = resourceLink.substring(FacetConstants.HANDLE_PREFIX.length());
                resourceLink = VloConfig.getHandleServerUrl() + handle;
                // Now points to something like http://hdl.handle.net/1839/00-0000-0000-0004-3357-F
                HttpURLConnection con = null;
                URL u;
                try{
                    u = new URL(resourceLink);
                    URLConnection c = u.openConnection();
                    if(c instanceof HttpURLConnection){
                        con = (HttpURLConnection) c;
                        con.setInstanceFollowRedirects(false);
                    }
                    if(con != null){
                        if(con.getResponseCode() == HttpURLConnection.HTTP_MOVED_PERM ||
                                con.getResponseCode() == HttpURLConnection.HTTP_MOVED_TEMP ||
                                con.getResponseCode() == HttpURLConnection.HTTP_SEE_OTHER) {
                        	String location = con.getHeaderField("location");
            				if(location != null)
            					result = location;
                        }
                    }
                } catch (MalformedURLException e) {
                    LOG.warn("Error trying to get the name of the handle", e);
                } catch (IOException e) {
                    LOG.warn("Error trying to get the name of the handle", e);
                }
            }
        }
        return result;
    }
}