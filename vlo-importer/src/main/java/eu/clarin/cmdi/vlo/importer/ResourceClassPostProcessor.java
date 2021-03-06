package eu.clarin.cmdi.vlo.importer;

import java.util.ArrayList;
import java.util.List;

public class ResourceClassPostProcessor implements PostProcessor {
    /**
     * Postprocess ResourceClass values
     * @param value extracted ResourcClass information
     * @return Value with upper case first letter and some value normalisation
     */
    @Override
    public List<String> process(String value) {
        String result = value;
        
        // replace DCMI URLs with DCMI type
        result = result.replaceFirst("http://purl.org/dc/dcmitype/", "");
        
        // first letter should be upper case
        if(result.length() > 1) {
            result = result.substring(0, 1).toUpperCase().concat(result.substring(1, result.length()));
        }
        List<String> resultList = new ArrayList<String>();
        resultList.add(result);
        return resultList;
    }
}
