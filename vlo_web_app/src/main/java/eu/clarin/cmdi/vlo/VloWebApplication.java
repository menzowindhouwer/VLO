package eu.clarin.cmdi.vlo;

import eu.clarin.cmdi.vlo.config.VloConfig;
import eu.clarin.cmdi.vlo.config.VloContextConfig;
import eu.clarin.cmdi.vlo.dao.SearchResultsDao;
import eu.clarin.cmdi.vlo.pages.FacetedSearchPage;
import javax.servlet.ServletContext;
import org.apache.wicket.protocol.http.WebApplication;

/**
 * {@literal VLO} web application.<br><br>
 *
 * Because the VloWebApplication class extends WebApplication, a class instance
 * will normally reside inside a web server container. By running the Start
 * class however, an instance of the application will reside outside a server
 * container.
 *
 */
public class VloWebApplication extends WebApplication {

    private SearchResultsDao searchResults;
    
    // flag indicating whether or not the application object lives in a context
    boolean inContext;

    /**
     * Method that will be invoked when the application starts<br><br>
     *
     */
    @Override
    public void init() {

        if (inContext) {
            
            // get the servlet's context
            
            ServletContext servletContext;
            servletContext = this.getServletContext();
            
            /**
             * Send the application context to the configuration object to
             * enable it to read an external {@literal VloConfig.xml}
             * configuration file.
             */
            
            VloContextConfig.switchToExternalConfig(servletContext);
        }

        // start the application

        searchResults = new SearchResultsDao();
    }

    /**
     * Web application constructor<br><br>
     *
     * Create an application instance configured to be living inside a web
     * server container.
     */
    public VloWebApplication() {

        /**
         * Read the application's packaged configuration. 
         * 
         * Because on instantiation a web application cannot said to be living 
         * in a web server context, parameters defined in the context can only 
         * be added to the configuration later, in this case: when the {@literal
         * init()} method will be invoked.
         */
        
        VloConfig.readPackagedConfig();

        // let the {@literal init()} method know that there will be a context

        inContext = true;  
    }

    /**
     * Web application constructor<br><br>
     *
     * Create an application instance configured to be living without a web
     * server container context.<br><br>
     *
     * @param testConfig a configuration that could be different from the
     * packaged parameters
     *
     * An instance like this can for example be used for testing purposes. In
     * the case of testing, the constructor is invoked from a class in a test
     * package. Within such a class a configuration object can be manipulated
     * according to specific needs. <br><br>
     *
     * Please note that in the case of a test configuration, while the
     * application object could reside inside a web server container, the
     * context associated with this container will be ignored.
     */
    public VloWebApplication(Boolean param) {

        // remember that the application does not live in a web server context
        
        inContext = param;

        searchResults = new SearchResultsDao();
    }

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<FacetedSearchPage> getHomePage() {
        return FacetedSearchPage.class;
    }

    public SearchResultsDao getSearchResultsDao() {
        return searchResults;
    }
}