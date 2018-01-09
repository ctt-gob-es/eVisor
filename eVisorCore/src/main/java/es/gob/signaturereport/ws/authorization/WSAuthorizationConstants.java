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
 * <b>File:</b><p>es.gob.signaturereport.ws.authorization.WSAuthorizationConstants.java.</p>
 * <b>Description:</b><p> .</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>28/06/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 28/06/2011.
 */
package es.gob.signaturereport.ws.authorization;

import javax.xml.namespace.QName;

import org.apache.ws.security.WSConstants;


/** 
 * <p>Class that contains the constants used in the authentication Web Service process.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 28/06/2011.
 */
public class WSAuthorizationConstants {
	//
    // Fault codes defined in the WSS 1.1 spec under section 12, Error handling
    //
    
    /**
     * An unsupported token was provided
     */
    public static final QName UNSUPPORTED_SECURITY_TOKEN = 
        new QName(WSConstants.WSSE_NS, "UnsupportedSecurityToken");
    
    /**
     * An unsupported signature or encryption algorithm was used
     */
    public static final QName UNSUPPORTED_ALGORITHM  = 
        new QName(WSConstants.WSSE_NS, "UnsupportedAlgorithm");
    
    /**
     * An error was discovered processing the <Security> header
     */
    public static final QName INVALID_SECURITY = 
        new QName (WSConstants.WSSE_NS, "InvalidSecurity");
    
    /**
     * An invalid security token was provided
     */
    public static final QName INVALID_SECURITY_TOKEN = 
        new QName (WSConstants.WSSE_NS, "InvalidSecurityToken");
    
    /**
     * The security token could not be authenticated or authorized
     */
    public static final QName FAILED_AUTHENTICATION = 
        new QName (WSConstants.WSSE_NS, "FailedAuthentication");
    
    /**
     * The signature or decryption was invalid
     */
    public static final QName FAILED_CHECK = 
        new QName (WSConstants.WSSE_NS, "FailedCheck");
    
    /** 
     * Referenced security token could not be retrieved
     */
    public static final QName SECURITY_TOKEN_UNAVAILABLE = 
        new QName (WSConstants.WSSE_NS, "SecurityTokenUnavailable");
    
    /** 
     * The message has expired
     */
    public static final QName MESSAGE_EXPIRED = 
        new QName (WSConstants.WSSE_NS, "MessageExpired");

	/**
	 * Constructor method for the class WSAuthorizationConstants.java. 
	 */
	private WSAuthorizationConstants() {
	}

    //
}
