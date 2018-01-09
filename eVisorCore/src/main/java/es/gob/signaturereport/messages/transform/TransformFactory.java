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
 * <b>File:</b><p>es.gob.signaturereport.messages.transform.TransformFactory.java.</p>
 * <b>Description:</b><p>Class that provides implementation of "transform" object.</p>
 * <p>This implementation provides methods for create and read a message accords to web services of the "@firma".</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>09/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 09/02/2011.
 */
package es.gob.signaturereport.messages.transform;

import org.apache.log4j.Logger;

import es.gob.signaturereport.configuration.access.AfirmaConfigurationFacadeI;
import es.gob.signaturereport.configuration.access.ConfigurationFacade;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.messages.transform.exception.TransformException;

import es.gob.signaturereport.properties.StaticSignatureReportProperties;

/**
 * <p>Class that provides implementation of "transform" object.</p>
 * <p>This implementation provides methods for create and read a message accords to web services of the "@firma".</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 09/02/2011.
 */
public class TransformFactory {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(TransformFactory.class);

	/**
	 * Attribute that represents the property used to identify the class implementation that manages XML message associated to validation signature by '@firma' v 5.3.1.
	 */
	private static final String VAL_SIG_V5D3R1_PROPERTY = "signaturereport.transform.afirma.v5d3r1.validateSignature.impl";

	/**
	 * Attribute that represents the property used to identify the class implementation that manages XML message associated to validation signature by '@firma' v 5.4.
	 */
	private static final String VAL_SIG_V5D4_PROPERTY = "signaturereport.transform.afirma.v5d4.validateSignature.impl";
	/**
	 * Attribute that represents the property used to identify the class implementation that manages XML message associated to validation signature by '@firma' v 5.5.
	 */
	private static final String VAL_SIG_V5D5_PROPERTY = "signaturereport.transform.afirma.v5d5.validateSignature.impl";

	/**
	 * Attribute that represents the property used to identify the class implementation that manages XML message associated to generation signature report.
	 */
	private static final String GENERATION_PROPERTY = "signaturereport.transform.generationReport.impl";

	/**
	 * Attribute that represents the property used to identify the class implementation that manages XML message associated to validation signature report.
	 */
	private static final String VALIDATION_PROPERTY = "signaturereport.transform.validationReport.impl";
	
	/**
	 * Constructor method for the class TransformFactoryBack.java.
	 */
	private TransformFactory() {
		super();
	}

	/**
	 * This method provides implementation of "transform" object.<br/>
	 * This implementation provides methods for create and read a message accords to web services of platform to invoke.
	 * @param serviceId	Identifier of web service that will be invoked.
	 * @param version	Version of platform.
	 * @param platform  Platform to invoke. The allowed values are {@link AfirmaConfigurationFacadeI#AFIRMA_PLATFORM} and {@link ConfigurationFacade#SIGNATURE_REPORT_PLATFORM}.
	 * @return	Implementation of "transform" object.
	 * @throws TransformException 	If an error occurs in the process of generation of "Transform" objects.
	 */
	public static TransformI getTransform(String serviceId, String version, String platform) throws TransformException {
		LOGGER.debug(Language.getFormatMessage(LanguageKeys.TRAN_001, new Object[ ] { serviceId, version }));
		String property = null;
		String className = null;
		if (serviceId != null) {
			if(platform !=null){
				if(platform.equals(AfirmaConfigurationFacadeI.AFIRMA_PLATFORM)){
					if (serviceId.equals(AfirmaConfigurationFacadeI.VALIDATE_SIGNATURE_SERVICE)) {
						if (version.equals(AfirmaConfigurationFacadeI.VERSION_5_3_1)) {
							property = VAL_SIG_V5D3R1_PROPERTY;
						} else if (version.equals(AfirmaConfigurationFacadeI.VERSION_5_4)) {
							property = VAL_SIG_V5D4_PROPERTY;
						} else if (version.equals(AfirmaConfigurationFacadeI.VERSION_5_5)||version.equals(AfirmaConfigurationFacadeI.VERSION_5_6)) {
							property = VAL_SIG_V5D5_PROPERTY;
						}
					}
				}else if(platform.equals(ConfigurationFacade.SIGNATURE_REPORT_PLATFORM)){
					if(serviceId.equals(ConfigurationFacade.GENERATION_REPORT_SERVICE)){
						property = GENERATION_PROPERTY;
					}else if(serviceId.equals(ConfigurationFacade.VALIDATION_REPORT_SERVICE)){
						property = VALIDATION_PROPERTY;
					}
				}
			}

		}

		if (property != null) {
			className = StaticSignatureReportProperties.getProperty(property);
		}
		if (className == null) {
			throw new TransformException(TransformException.CLASS_NOT_FOUND, Language.getFormatMessage(LanguageKeys.TRAN_002, new Object[ ] { serviceId, version }));
		} else {
			try {
				LOGGER.debug(Language.getFormatMessage(LanguageKeys.TRAN_003, new Object[ ] { className }));
				return (TransformI) Class.forName(className).newInstance();
			} catch (Exception e) {
				String msg = Language.getFormatMessage(LanguageKeys.TRAN_004, new Object[ ] { className });
				LOGGER.error(msg, e);
				throw new TransformException(TransformException.INSTANCE_ERROR, msg);
			}
		}
	}
}
