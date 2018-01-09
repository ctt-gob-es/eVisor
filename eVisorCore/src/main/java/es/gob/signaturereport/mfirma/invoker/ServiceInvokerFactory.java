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
 * <b>File:</b><p>es.gob.signaturereport.mfirma.invoker.ServiceInvokerFactory.java.</p>
 * <b>Description:</b><p> This class implements a factory invocation of services published by the '@firma' platform.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>16/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 16/02/2011.
 */
package es.gob.signaturereport.mfirma.invoker;

import java.lang.reflect.Constructor;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import es.gob.signaturereport.configuration.access.ConfigurationFacade;
import es.gob.signaturereport.configuration.items.AfirmaData;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;

import es.gob.signaturereport.properties.SignatureReportPropertiesI;
import es.gob.signaturereport.properties.StaticSignatureReportProperties;


/** 
 * <p>This class implements a factory invocation of services published by the '@firma' platform.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 16/02/2011.
 */
public final class ServiceInvokerFactory {
    
    /**
     * Attribute that represents the object that manages the log of the class. 
     */
    private static final Logger LOGGER = Logger.getLogger(ServiceInvokerFactory.class);
    
    
    /**
     * Attribute that represents the property used to identify the invoker class implementation for '@firma' v5.3.1. 
     */
    private static final String IMPL_V5D3R1_PROPERTY = "signaturereport.invoker.afirma.v5d3r1.impl";
    /**
     * Attribute that represents the property used to identify the invoker class implementation for '@firma' v5.4. 
     */
    private static final String IMPL_V5D4_PROPERTY = "signaturereport.invoker.afirma.v5d4.impl";
    /**
     * Attribute that represents the property used to identify the invoker class implementation for '@firma' v5.5. 
     */
    private static final String IMPL_V5D5_PROPERTY = "signaturereport.invoker.afirma.v5d5.impl";
    
    /**
     * Constructor method for the class ServiceInvokerFactory.java. 
     */
    private ServiceInvokerFactory() {
	super();
    }
    
    /**
     * Gets a 'ServiceInvoker' implementation for invoking to services of '@firma'.
     * @param afirmaConf	Information for invocation  to '@firma' platform.
     * @return			A 'ServiceInvoker' implementation for invoking to services of '@firma'.
     * @throws ServiceInvokerException	If an error occurs during object creation.
     */
    public static ServiceInvokerI getServiceInvoker(AfirmaData afirmaConf) throws ServiceInvokerException{
	String property = null;
	String className = null;
	if(afirmaConf.getVersion().equals(ConfigurationFacade.VERSION_5_3_1)){
	    property = IMPL_V5D3R1_PROPERTY;
	}else if(afirmaConf.getVersion().equals(ConfigurationFacade.VERSION_5_4)){
	    property = IMPL_V5D4_PROPERTY;
	}else if(afirmaConf.getVersion().equals(ConfigurationFacade.VERSION_5_5)||afirmaConf.getVersion().equals(ConfigurationFacade.VERSION_5_6)){
	    property = IMPL_V5D5_PROPERTY;
	}
	if(property!=null){
	    className = StaticSignatureReportProperties.getProperty(property);
	}
	if(className!=null){
	    try {
		Constructor<?> constructor = Class.forName(className).getConstructor(new Class[] {AfirmaData.class});
		return (AServiceInvoker) constructor.newInstance(afirmaConf);
	    } catch (Exception e) {
		String msg = Language.getFormatMessage(LanguageKeys.INVK_007,new Object[]{className});
		LOGGER.error(msg,e);
		throw new ServiceInvokerException(ServiceInvokerException.UNKNOWN_ERROR, msg,e);
	    }
	}else{
	    String msg = Language.getFormatMessage(LanguageKeys.INVK_006,new Object[]{afirmaConf.getVersion()});
	    LOGGER.error(msg);
	    throw new ServiceInvokerException(ServiceInvokerException.UNKNOWN_ERROR, msg);
	}
    }
    

}
