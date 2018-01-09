// Copyright (C) 2018, Gobierno de España
// This program is licensed and may be used, modified and redistributed under the terms
// of the European Public License (EUPL), either version 1.1 or (at your
// option) any later version as soon as they are approved by the European Commission.
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
// or implied. See the License for the specific language governing permissions and
// more details.
// You should have received a copy of the EUPL1.1 license
// along with this program; if not, you may find it at
// http://joinup.ec.europa.eu/software/page/eupl/licence-eupl

/** 
 * <b>File:</b><p>es.gob.signaturereport.mreport.items.AImage.java.</p>
 * <b>Description:</b><p> Class that contains information about an image that will be include in a signature report.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>07/04/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 07/04/2011.
 */
package es.gob.signaturereport.mreport.items;

import java.util.Arrays;


/** 
 * <p>Class that contains information about an image that will be include in a signature report.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 07/04/2011.
 */
public abstract class AImage {
    
    /**
     * Attribute that represents the URL location. 
     */
    private String location = null;
    
    /**
     * Attribute that represents the bytes array of the image. 
     */
    private byte[] content = null;
    
    /**
     * Attribute that represents the image content type. 
     */
    private String contentType = null;

    
    /**
     * Gets the value of the URL location.
     * @return the value of the URL location.
     */
    public String getLocation() {
        return location;
    }

    
    /**
     * Sets the value of the URL location.
     * @param url The value for the URL location.
     */
    public void setLocation(String url) {
        this.location = url;
    }

    
    /**
     * Gets the value of the bytes array of the image.
     * @return the value of the bytes array of the image.
     */
    public byte[ ] getContent() {
        return content;
    }

    
    /**
     * Sets the value of the bytes array of the image.
     * @param attContent The value for the bytes array of the image.
     */
    public void setContent(byte[ ] attContent) {
    	if(attContent!=null){
    		this.content = Arrays.copyOf(attContent, attContent.length) ;
    	}
    }

    
    /**
     * Gets the value of the image content type.
     * @return the value of the image content type.
     */
    public String getContentType() {
        return contentType;
    }

    
    /**
     * Sets the value of the image content type.
     * @param type The value for the image content type.
     */
    public void setContentType(String type) {
        this.contentType = type;
    }
}
