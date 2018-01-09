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
 * <b>File:</b><p>es.gob.signaturereport.configuration.items.WSServiceData.java.</p>
 * <b>Description:</b><p> Class that contains the information necessary to invoke a Web Service "@firma".</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>08/04/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 08/04/2011.
 */
package es.gob.signaturereport.configuration.items;


/** 
 * <p>Class that contains the information necessary to invoke a Web Service "@firma".</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 08/04/2011.
 */
public class WSServiceData {
    
	/**
	 * Constant that represents the default timeout in milliseconds. 
	 */
	private static final int DEFAULT_TIMEOUT = 20000;
    /**
     * Attribute that represents the URL location of "@firma" services. 
     */
    private String servicesLocation = null;
    
    /**
     * Attribute that represents the name of operation associated to WS service of "@firma". 
     */
    private String operationName = null;
    
    /**
     * Attribute that represents the timeout of service invocation. 
     */
    private int timeOut = DEFAULT_TIMEOUT;
    
    
  /**
   * Gets the value of the URL location of "@firma" services.
   * @return the value of the URL location of "@firma" services.
   */
  public String getServicesLocation() {
      return servicesLocation;
  }


  
  /**
   * Sets the value of the URL location of "@firma" services.
   * @param location The value for the URL location of "@firma" services.
   */
  public void setServicesLocation(String location) {
      this.servicesLocation = location;
  }
  
  /**
   * Gets the value of the timeout of service invocation.
   * @return the value of the timeout of service invocation.
   */
  public int getTimeOut() {
      return timeOut;
  }


  
  /**
   * Sets the value of the timeout of service invocation.
   * @param time The value for the timeout of service invocation.
   */
  public void setTimeOut(int time) {
      this.timeOut = time;
  }





/**
 * Gets the value of operation associated to WS service of "@firma".
 * @return the value of operation associated to WS service of "@firma".
 */
public String getOperationName() {
    return operationName;
}




/**
 * Sets the value of operation associated to WS service of "@firma".
 * @param opName The value for operation associated to WS service of "@firma".
 */
public void setOperationName(String opName) {
    this.operationName = opName;
}
  
  

    
}
