/*
 * Copyright (C) 2014 CLARIN
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.clarin.cmdi.vlo.service.impl;

import eu.clarin.cmdi.vlo.pojo.QueryFacetsSelection;
import eu.clarin.cmdi.vlo.service.SearchResultsDao;
import eu.clarin.cmdi.vlo.service.SolrDocumentQueryFactory;
import eu.clarin.cmdi.vlo.service.SolrDocumentService;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.common.SolrDocument;

/**
 *
 * @author twagoo
 */
public class SolrDocumentServiceImpl implements SolrDocumentService {

    private final SearchResultsDao searchResultsDao;
    private final SolrDocumentQueryFactory queryFatory;

    public SolrDocumentServiceImpl(SearchResultsDao searchResultsDao, SolrDocumentQueryFactory queryFatory) {
        this.searchResultsDao = searchResultsDao;
        this.queryFatory = queryFatory;
    }

    @Override
    public SolrDocument getDocument(String docId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<SolrDocument> getDocuments(QueryFacetsSelection selection, int first, int count) {
        SolrQuery query = queryFatory.createDocumentQuery(selection,first, count);
        return searchResultsDao.getDocuments(query);
    }

    @Override
    public long getDocumentCount(QueryFacetsSelection selection) {
        SolrQuery query = queryFatory.createDocumentQuery(selection,0,0);
        return searchResultsDao.getDocuments(query).getNumFound();
    }

}
