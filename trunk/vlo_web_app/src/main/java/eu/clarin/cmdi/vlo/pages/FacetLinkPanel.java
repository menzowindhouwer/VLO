package eu.clarin.cmdi.vlo.pages;

import eu.clarin.cmdi.vlo.VloWebApplication;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.wicket.PageParameters;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class FacetLinkPanel extends Panel {

    private static final long serialVersionUID = 1L;

    public FacetLinkPanel(String id, IModel<Count> model, final SearchPageQuery query) {
        super(id, model);

        Count count = model.getObject();
        SearchPageQuery q = query.getShallowCopy();
        q.setFilterQuery(count);
        PageParameters params = q.getPageParameters();
        
        // get a reference to the web application object
        VloWebApplication webApp = (VloWebApplication) this.getApplication();
        // add the persistent parameters stored in it to the panel parameters
        params = webApp.addPersistentParameters(params);
        
        Link<Count> facetLink = new BookmarkablePageLink("facetLink", FacetedSearchPage.class, params);
        facetLink.add(new Label("facetLinkLabel", model.getObject().getName()));
        add(facetLink);
        add(new Label("facetCount", "" + model.getObject().getCount()));
    }

}
