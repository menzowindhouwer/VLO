
package eu.clarin.cmdi.vlo.config;

import java.io.File;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * A dataRoot describes the met data sources.
 * 
 * In an XML file, a dataRoot is reflected like this:<br><br>
 * 
 * {@literal <DataRoot>} 
 *      {@literal<originName>}name{@literal</originName>}
 *      {@literal<rootFile>}topLevelMetadataDirectory/{@literal</rootFile>}
 *      {@literal<prefix>}startOfUrl{@literal</prefix>}
 *      {@literal<tostrip>}leftPartOfRootFile{@literal</tostrip>}
 *      {@literal<deleteFirst>}falseOrTrue{@literal</deleteFirst>}
 * {@literal</DataRoot>}
 *  
 * @author keeloo
 */
@Root // Simple directive 
public class DataRoot extends Object {
    
    /**
     * Constructor method
     */
    public DataRoot (){
    }
    
    /**
     * Constructor method
     * 
     * @param originName name describing the meta data
     * @param rootFile top level directory in which the meta data is stored
     * @param prefix left part of the rootFile
     * @param toStrip if you want to create the URL to the meta data, this is 
     * the part to be removed from the rootFile
     * @param deleteFirst 
     */
    public DataRoot(String originName, File rootFile, String prefix, String toStrip, Boolean deleteFirst) {
        this.originName = originName;
        this.rootFile = rootFile;
        this.prefix = prefix;
        this.tostrip = toStrip;
        this.deleteFirst = deleteFirst;
    }
    
    /**
     * Test for equality of the object itself and the object passed to it
     * 
     * @param dataRoot
     * @return true if the object equals this, false otherwise
     */
    @Override
    public boolean equals (Object object){
        boolean equal = false;
        
        if (object == null) {
            // define this object to be different from nothing
        } else {
            if (! (object instanceof DataRoot)) {
                // the object is not a DataRoot, define it not to be equal 
            } else {
                equal = this.originName.equals(((DataRoot) object).originName);
                equal = this.rootFile.equals(((DataRoot) object).rootFile) && equal;
                equal = this.prefix.equals(((DataRoot) object).prefix) && equal;
                equal = this.tostrip.equals(((DataRoot) object).tostrip) && equal;

                equal = this.deleteFirst == ((DataRoot) object).deleteFirst && equal;
            }
        }
        
        return equal;
    }

    /**
     * Generate by the ide
     * 
     * @return hash code
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.originName != null ? this.originName.hashCode() : 0);
        hash = 29 * hash + (this.rootFile != null ? this.rootFile.hashCode() : 0);
        hash = 29 * hash + (this.prefix != null ? this.prefix.hashCode() : 0);
        hash = 29 * hash + (this.tostrip != null ? this.tostrip.hashCode() : 0);
        hash = 29 * hash + (this.deleteFirst ? 1 : 0);
        return hash;
    }

    /**
     * name describing the meta data
     */
    @Element // Simple directive
    private String originName;
 
    /**
     * top level directory in which the meta data is stored
     */
    @Element
    private File rootFile;
    
    /**
     * Web equivalent of the toStrip. For example: 
     * 
     * /lat/apache/htdocs/
     */
    @Element  
    private String prefix;

    /**
     * Left part of the rootFile 
     *
     * By first removing {@literal tostrip} from {@literal rootFile} and then
     * append the result of that operation to the {@literal prefix} you obtain
     * the URL to the meta data.
     */
    @Element
    private String tostrip;
    
    /**
     * Flag to signal the removal of records from the Solr server
     * 
     * The value of this flag overrides the value defined in the {@lieteral 
     * VloConfig.xml} file. With the deleteFirst flag you can control the 
     * removal of the records originating from originName.
     */
    @Element
    private boolean deleteFirst = false;

    /**
     * Get the value of the prefix element<br><br>
     *
     * For a description of the element, refer to the general VLO
     * documentation.
     *
     * @return the value
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Set the value of the prefix element<br><br>
     *
     * For a description of the element, refer to the general VLO
     * documentation.
     *
     * @param prefix the value
     */
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Get the value of the {@literal tostrip} element<br><br>
     *
     * For a description of the element, refer to the general VLO
     * documentation.
     *
     * @return the value
     */
    public String getToStrip() {
        return tostrip;
    }

    /**
     * Set the value of the {@literal tostrip} element<br><br>
     *
     * For a description of the element, refer to the general VLO
     * documentation.
     *
     * @param tostrip the value
     */
    public void setTostrip(String tostrip) {
        this.tostrip = tostrip;
    }

    /**
     * Get the value of the originName element<br><br>
     *
     * For a description of the element, refer to the general VLO
     * documentation.
     *
     * @return the value
     */    
    public String getOriginName() {
        return originName;
    }

    /**
     * Set the value of the originName element<br><br>
     *
     * For a description of the element, refer to the general VLO
     * documentation.
     *
     * @param originName the value
     */    
    public void setOriginName(String originName) {
        this.originName = originName;
    }

    /**
     * Get the value of the rootFile element<br><br>
     *
     * For a description of the element, refer to the general VLO
     * documentation.
     *
     * @return the value
     */    
    public File getRootFile() {
        return rootFile;
    }

    /**
     * Set the value of the rootFile element<br><br>
     *
     * For a description of the element, refer to the general VLO
     * documentation.
     *
     * @param rootFile the value
     */    
    public void setRootFile(File rootFile) {
        this.rootFile = rootFile;
    }

    /**
     * Set the value of the deleteFirst element<br><br>
     *
     * For a description of the element, refer to the general VLO
     * documentation.
     *
     * @param deleteFirst the value
     */    
    public void setDeleteFirst(boolean deleteFirst) {
        this.deleteFirst = deleteFirst;
    }

    /**
     * Get the value of the deleteFirst element<br><br>
     *
     * For a description of the element, refer to the general VLO
     * documentation.
     *
     * @return the value
     */    
    public boolean deleteFirst() {
        return deleteFirst;
    }
}

