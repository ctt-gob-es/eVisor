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
 * <b>File:</b><p>es.gob.signaturereport.evisortopdfws.ws.OOfficeConnection.java.</p>
 * <b>Description:</b><p> Class that allows connect to a Open Office Server.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>08/08/2016.</p>
 * @author Spanish Government.
 * @version 1.0, 08/08/2016.
 */
package es.gob.signaturereport.evisortopdfws.ws;

import org.apache.log4j.Logger;

import com.sun.star.beans.XPropertySet;
import com.sun.star.bridge.XBridge;
import com.sun.star.bridge.XBridgeFactory;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.connection.XConnection;
import com.sun.star.connection.XConnector;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XEventListener;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;


/** 
 * <p>Class that allows connect to a Open Office Server.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 08/08/2016.
 */
class OOfficeConnection {

    /**
     * Attribute that represents the server host. 
     */
    private final String host;
    
    /**
     * Attribute that represents the server port. 
     */
    private final int port;

    /**
     * Attribute that represents a Open Office Bridge. 
     */
    private XComponent bridgeComponent;
    /**
     * Attribute that represents a Open Office Service Manage. 
     */
    private XMultiComponentFactory serviceManager;
    
    /**
     * Attribute that represents the execution context. 
     */
    private XComponentContext componentContext;

    /**
	 * Attribute that represents the object that manages the log of the class.
	 */
    private final Logger logger = Logger.getLogger(OOfficeConnection.class);

    
    /**
     * Attribute that represents a flag that indicates if is connected. 
     */
    private volatile boolean connected = false;

    /**
     * Attribute that represents the bridge listener. 
     */
    private XEventListener bridgeListener = new XEventListener() {
        public void disposing(EventObject event) {
            if (connected) {
                connected = false;
                logger.info(LanguageWS.getFormatMessage(LanguageWSKeys.UTIL_048, new Object[]{host,String.valueOf(port)}));
            }
        }
        
    };

   
    /**
     * Constructor method for the class OOfficeConnection.java.
     * @param serverHost	Server host.
     * @param serverPort 	Server port.
     */
    public OOfficeConnection(String serverHost,int serverPort) {
        this.host = serverHost;
        this.port = serverPort;
    }

    
    /**
     * Connects to Open Office Server.
     * @throws OOfficeConnectionException If an error occurs.
     */
    public void connect() throws OOfficeConnectionException {
        String connectionStr = "socket,host="+host+",port="+port+",tcpNoDelay=1" ;
        logger.debug(LanguageWS.getFormatMessage(LanguageWSKeys.UTIL_049, new Object[]{host,String.valueOf(port)}));
        try {
            XComponentContext localContext = Bootstrap.createInitialComponentContext(null);
            XMultiComponentFactory localServiceManager = localContext.getServiceManager();
            XConnector connector = UnoRuntime.queryInterface(XConnector.class, localServiceManager.createInstanceWithContext("com.sun.star.connection.Connector", localContext));
            XConnection connection = connector.connect(connectionStr);
            XBridgeFactory bridgeFactory = UnoRuntime.queryInterface(XBridgeFactory.class, localServiceManager.createInstanceWithContext("com.sun.star.bridge.BridgeFactory", localContext));
            String bridgeName = "evisor_" + UniqueNumberWSGenerator.getInstance().getNumber();
            XBridge bridge = bridgeFactory.createBridge(bridgeName, "urp", connection, null);
            bridgeComponent = UnoRuntime.queryInterface(XComponent.class, bridge);
            bridgeComponent.addEventListener(bridgeListener);
            serviceManager = UnoRuntime.queryInterface(XMultiComponentFactory.class, bridge.getInstance("StarOffice.ServiceManager"));
            XPropertySet properties = UnoRuntime.queryInterface(XPropertySet.class, serviceManager);
            componentContext = UnoRuntime.queryInterface(XComponentContext.class, properties.getPropertyValue("DefaultContext"));
            connected = true;
            logger.info(LanguageWS.getFormatMessage(LanguageWSKeys.UTIL_050, new Object[]{host,String.valueOf(port)}));
        } catch (Exception e) {
        	String msg = LanguageWS.getFormatMessage(LanguageWSKeys.UTIL_051, new Object[]{host,String.valueOf(port)});
        	logger.error(msg,e);
            throw new OOfficeConnectionException( msg,e);
        } 
    }

    /**
     * Indicates if is connected.
     * @return	True if is connected, otherwise false.
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Disconnects to Open Office Server.
     */
    public synchronized void disconnect() {
    	logger.debug(LanguageWS.getFormatMessage(LanguageWSKeys.UTIL_052, new Object[]{host,String.valueOf(port)}));
        bridgeComponent.dispose();
    }

    /**
     * Gets a Open Office Service.
     * @param serviceName	Service name.
     * @return	Open Office Service.
     * @throws OOfficeConnectionException If an error occurs.
     */
    public Object getService(String serviceName) throws OOfficeConnectionException {
        try {
            return serviceManager.createInstanceWithContext(serviceName, componentContext);
        } catch (Exception e) {
        	String msg = LanguageWS.getFormatMessage(LanguageWSKeys.UTIL_053, new Object[]{serviceName});
            logger.error(msg,e);
            throw new OOfficeConnectionException( msg,e);
        }
    }
	
	

}
