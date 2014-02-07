package eu.clarin.cmdi.vlo.importer;

import eu.clarin.cmdi.vlo.config.VloConfig;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * Adds information about the affiliation of a metadata file to a national project (like CLARIN-X etc.) into facet nationalProject
 * @author Thomas Eckart
 *
 */
public class NationalProjectPostProcessor extends LanguageCodePostProcessor {
	private final static Logger LOG = LoggerFactory.getLogger(NationalProjectPostProcessor.class);

	private static Map<String, String> nationalProjectMap = null;

	/**
	 * Returns the national project based on the mapping in Configuration.getNationalProjectMapUrl()
	 * If no mapping was found empty String is returned
	 * @return
	 */
	@Override
    public String process(String value) {
        String result = value.trim();
        if (result != null && getMapping().containsKey(result)) {
            result = getMapping().get(result);
        } else {
        	result = "";
        }
        return result;
    }

	private Map<String, String> getMapping() {
		if(nationalProjectMap == null)
			nationalProjectMap = getNationalProjectMapping();
        return nationalProjectMap;
    }

    private Map<String, String> getNationalProjectMapping() {
        String projectsMappingFile = VloConfig.getNationalProjectMapping();

        if (projectsMappingFile.length() == 0) {
            // use the packaged project mapping file
            projectsMappingFile = "/nationalProjectsMapping.xml";
        }

        try {
            Map<String, String> result = new HashMap<String, String>();
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            domFactory.setNamespaceAware(true);

            DocumentBuilder builder = domFactory.newDocumentBuilder();
            Document doc;

            // first, try to open the packaged mapping file
            InputStream mappingFileAsStream;
            mappingFileAsStream = NationalProjectPostProcessor.class.getResourceAsStream(projectsMappingFile);

            if (mappingFileAsStream != null) {
                doc = builder.parse(mappingFileAsStream);
            } else {

                // the resource cannot be found inside the package, try outside
                File mappingAsFile;

                mappingAsFile = new File(projectsMappingFile);

                if (mappingAsFile == null) {
                    LOG.info("National project mapping file does not exist - using minimal test file.");
                    mappingAsFile = createMinimalMappingFile();
                }
                doc = builder.parse(mappingAsFile);
            }

            XPath xpath = XPathFactory.newInstance().newXPath();
            NodeList nodeList = (NodeList) xpath.evaluate("//nationalProjectMapping", doc, XPathConstants.NODESET);
            for (int i = 1; i <= nodeList.getLength(); i++) {
                String mdCollectionDisplayName = xpath.evaluate("//nationalProjectMapping[" + i + "]/MdCollectionDisplayName", doc).trim();
                String nationalProject = xpath.evaluate("//nationalProjectMapping[" + i + "]/NationalProject", doc).trim();
                result.put(mdCollectionDisplayName, nationalProject);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate postProcessor:", e);
        }
    }

    /**
     * Create temporary and minimal mapping file for testing purposes and as
     * backup solution
     *
     * @return minimal file for national projects mapping (e.g. ANDES ->
     * CLARIN-EU)
     */
    private File createMinimalMappingFile() {
        String content = "";
        content += "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n";
        content += "<nationalProjects>\n";
        content += "   <nationalProjectMapping><MdCollectionDisplayName>ANDES</MdCollectionDisplayName><NationalProject>CLARIN-EU</NationalProject></nationalProjectMapping>\n";
        content += "</nationalProjects>\n";

        File file = null;
        try {
            file = File.createTempFile("vlo.nationalTestMapping", ".map");
            FileUtils.writeStringToFile(file, content, "UTF-8");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            LOG.error("Could not create temporary national project mapping file");
        }

        return file;
    }
}