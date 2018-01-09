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
 * <b>File:</b><p>es.gob.signaturereport.configuration.access.TemplateConfigurationFacade.java.</p>
 * <b>Description:</b><p>Class for the management of configuration information associated with templates for report creation.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>24/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 24/02/2011.
 */
package es.gob.signaturereport.configuration.access;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.apache.poi.hmef.Attachment;

import es.gob.signaturereport.configuration.items.TemplateData;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.malarm.AlarmManager;
import es.gob.signaturereport.persistence.configuration.cache.TemplateCache;
import es.gob.signaturereport.persistence.configuration.model.bo.interfaz.ITemplateModuleBO;
import es.gob.signaturereport.persistence.configuration.model.pojo.AttachmentPOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.TemplateContentPOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.TemplateReportPOJO;
import es.gob.signaturereport.persistence.exception.ConfigurationException;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.tools.UtilsBase64;

/** 
 * <p>Class for the management of configuration information associated with templates for report creation.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 24/02/2011.
 */
@Stateless
@TemplateConfiguration
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class TemplateConfigurationFacade implements TemplateConfigurationFacadeI {
	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(TemplateConfigurationFacade.class);
	
	/**
	 * Attribute that represents the hash algorithm used to calculate the template digest. 
	 */
	private static final String HASH_ALG = "SHA1";
	
	/**
	 * Attribute that represents the base 64 utility class. 
	 */
	private UtilsBase64 base64 = new UtilsBase64();
	
	/**
	 * Attribute that contains list of Attachment.
	 */
	private static LinkedHashMap<String, Integer> listAttachment = new LinkedHashMap<String, Integer>();
	
	/**
	 * Attribute that allows to operate with information about applications of the platform.
	 */
	@Inject
	private ITemplateModuleBO templateBO;
	
//	/**
//     * Attribute that represents the alarms manager.
//     */
//    @Inject
//    private AlarmManager alarmManager;
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.TemplateConfigurationFacadeI#getTemplate(java.lang.String)
	 */
	public  TemplateData getTemplate( String templateId) throws ConfigurationException {
		if (templateId == null || templateId.trim().equals("")){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_052));
		}
		try {
			TemplateReportPOJO dbTemplate = templateBO.getTemplateReport(templateId);
			
			//Vemos si la plantilla está en la cache
			byte[] trContent = TemplateCache.getInstance().getTemplate(dbTemplate.getTrContent().getContentId());
			if(trContent == null) {
				
				TemplateContentPOJO tContent = templateBO.getTemplateContent(dbTemplate.getTrContent().getContentId());
				
				if(tContent != null){
					TemplateCache.getInstance().addTemplate(tContent.getContentId(), tContent.getContent());
					trContent = tContent.getContent();
				}
			}
			
			return this.convertTemplateReport(dbTemplate,trContent);
						
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			String msg = Language.getFormatMessage(LanguageKeys.CONF_053, new Object[]{templateId});
			LOGGER.error(msg,e);
			if(e.getCode()==DatabaseException.ITEM_NOT_FOUND){
				return null;
			}else{
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR,e.getDescription(),e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.TemplateConfigurationFacadeI#createTemplate(es.gob.signaturereport.configuration.items.TemplateData)
	 */
	public  void createTemplate( TemplateData template) throws ConfigurationException {
		templateValidation(template);
		if(template.getTemplate()==null){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_061));
		}
		try {
					
			 TemplateReportPOJO templateReport = convertTempleateData(template,true);
			
			// Vemos si existe la plantilla
			TemplateContentPOJO templateContent = null;
			if (templateReport.getTrContent() != null) {
				templateContent = templateBO.getTemplateContent(templateReport.getTrContent().getContentId());
			}
							
			if (templateContent != null) {
				templateReport.setTrContent(templateContent);				
			}
			
			templateBO.createTemplate(templateReport, templateContent);			
			
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001,e.getMessage());
			 String msg = Language.getFormatMessage(LanguageKeys.CONF_055, new Object[]{template.getIdentifier()});
			LOGGER.error(msg, e);
			if(e.getCode() == DatabaseException.DUPLICATE_ITEM){
				throw new ConfigurationException(ConfigurationException.DUPLICATE_ITEM, Language.getFormatMessage(LanguageKeys.CONF_059, new Object[]{template.getIdentifier()}),e);
			}else if(e.getCode() == DatabaseException.INVALID_INPUT_PARAMETERS){
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			}else{
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, e.getDescription(), e);
			}
		} catch (NoSuchAlgorithmException e) {
			 String msg = Language.getFormatMessage(LanguageKeys.DB_081, new Object[ ] { template.getIdentifier() });
			LOGGER.error(msg, e);
			throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, e.getMessage(), e);
		}
		
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.TemplateConfigurationFacadeI#modifyTemplateData(es.gob.signaturereport.configuration.items.TemplateData)
	 */
	public  void modifyTemplateData( TemplateData template) throws ConfigurationException {
		templateValidation(template);
		try {
			
			TemplateReportPOJO templateReport = convertTempleateData(template,true);
				
			// Vemos si existe la plantilla
			TemplateContentPOJO templateContent = null;
			if (templateReport.getTrContent() != null) {
				templateContent = templateBO.getTemplateContent(templateReport.getTrContent().getContentId());
			}
					
			boolean newContent = false;
			
			if (templateContent != null) {
				templateReport.setTrContent(templateContent);				
			} else {
				//Si el contenido de la plantilla no existía en BD se añade a la caché.
				if (templateReport.getTrContent() != null) {
					newContent = true;
				}
			}
			
			templateBO.modifyTemplate(templateReport, newContent);
			
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			 String msg = Language.getFormatMessage(LanguageKeys.CONF_062, new Object[]{template.getIdentifier()});
			LOGGER.error(msg, e);
			if(e.getCode() == DatabaseException.ITEM_NOT_FOUND){
				throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, Language.getFormatMessage(LanguageKeys.CONF_063, new Object[]{template.getIdentifier()}),e);
			}else if(e.getCode() == DatabaseException.INVALID_INPUT_PARAMETERS){
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			}else{
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, e.getDescription(),e);
			}
		} catch (NoSuchAlgorithmException e) {
			 String msg = Language.getFormatMessage(LanguageKeys.DB_081, new Object[ ] { template.getIdentifier() });
			LOGGER.error(msg, e);
			throw new ConfigurationException(DatabaseException.UNKNOWN_ERROR, msg,e);
		}
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.TemplateConfigurationFacadeI#removeTemplate(java.lang.String)
	 */
	public  void removeTemplate( String templateId) throws ConfigurationException {
		if (templateId == null || templateId.trim().equals("")){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_052));
		}
		try {
						
			templateBO.removeTemplate(templateId);
			
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			AlarmManager.getInstance().communicateAlarm(AlarmManager.ALARM_001,e.getMessage());
			String msg = Language.getFormatMessage(LanguageKeys.CONF_065, new Object[]{templateId});
			LOGGER.error(msg, e);
			if(e.getCode() == DatabaseException.ITEM_NOT_FOUND){
				throw new ConfigurationException(ConfigurationException.ITEM_NO_FOUND, Language.getFormatMessage(LanguageKeys.CONF_063, new Object[]{templateId}),e);
			}else if(e.getCode() == DatabaseException.INVALID_INPUT_PARAMETERS){
				throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, e.getDescription(),e);
			}else if(e.getCode()==DatabaseException.ITEM_ASSOCIATED){
				throw new ConfigurationException(ConfigurationException.ITEM_ASSOCIATED, e.getDescription(),e);
			}else{
				throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, e.getDescription(),e);
			}
		}
		
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.TemplateConfigurationFacadeI#getTemplateIds()
	 */
	public  Map<String, Integer> getTemplateIds() throws ConfigurationException {
		try {
		
			return templateBO.getTemplateIds();
			
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			String msg = Language.getMessage(LanguageKeys.CONF_066);
			LOGGER.error(msg, e);
			throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR, msg,e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.configuration.access.TemplateConfigurationFacadeI#existTemplate(java.lang.String)
	 */
	public  boolean existTemplate( String templateId) throws ConfigurationException {
		if (templateId == null || templateId.trim().equals("")){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_052));
		}
		try {
					
			return templateBO.existTemplate(templateId);
			
		} catch (es.gob.signaturereport.persistence.exception.DatabaseException e) {
			String msg = Language.getFormatMessage(LanguageKeys.CONF_053, new Object[]{templateId});
			LOGGER.error(msg,e);
			throw new ConfigurationException(ConfigurationException.UNKNOWN_ERROR,e.getDescription(),e);
		}
	}

    /**
     * Method that verifies the validity of the supplied template.
     * @param template	Template to check.
     * @throws ConfigurationException	If the template is not valid.
     */
    private void templateValidation( TemplateData template) throws ConfigurationException{
    	if(template == null){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_054));
		}
		if(template.getIdentifier()==null){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.CONF_052));
		}
		if(template.getModeDocInclude()!=TemplateConfigurationFacadeI.INC_SIGNED_DOC_NONE && template.getModeDocInclude()!=TemplateConfigurationFacadeI.INC_SIGNED_DOC_CONCAT 
				&& template.getModeDocInclude()!=TemplateConfigurationFacadeI.INC_SIGNED_DOC_EMBED && template.getModeDocInclude()!=TemplateConfigurationFacadeI.INC_SIGNED_DOC_IMAGE){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.CONF_056, new Object[]{""+template.getModeDocInclude()}));
		}
		if(template.getReportType()!=TemplateConfigurationFacadeI.PDF_REPORT){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.CONF_057, new Object[]{""+template.getReportType()}));
		}
		if(template.getPagesRange()!=null && !template.getPagesRange().matches(RANGE_MASK)){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.CONF_058, new Object[]{template.getPagesRange()}));
		}
		if(template.getConcatenationRule()!=null && !template.getConcatenationRule().matches(CONCANT_MASK)){
			throw new ConfigurationException(ConfigurationException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.CONF_060, new Object[]{template.getConcatenationRule()}));
		}
    }

	
    /**
	 * Convert TemplateReportPOJO to TemplateData.
	 * @param tr TemplateReportPOJO to convert.
	 * @param templateContent Array of bytes that represents the template content.
	 * @return {@link TemplateData} object.
	 */
	private TemplateData convertTemplateReport(TemplateReportPOJO tr,byte[] templateContent) {
		TemplateData td = new TemplateData(tr.getTrId(), tr.getTrType());
		if (tr.getAttachment() != null) {
			td.setAttachCertificate(tr.getAttachment().getAttCert());
			td.setAttachDocument(tr.getAttachment().getAttDoc());
			td.setAttachSignature(tr.getAttachment().getAttSig());
			td.setAttchResponse(tr.getAttachment().getAttResp());
			td.setAttchDocInSignature(tr.getAttachment().getAttDocSig());
		}
		td.setConcatenationRule(tr.getConcatrule());
		td.setPagesRange(tr.getPageRange());
		td.setModeDocInclude(tr.getDiType());
		
		td.setForceGeneration(tr.getTrForce());
		td.setReportType(tr.getTrType());
		td.setTemplate(templateContent);
		return td;

	}
	
	/**
	 * Gets the Templatereport information registered into database.
	 * @param td	TemplateData.
	 * @param includeContent Indicates if the content is included. 
	 * @return	{@link Templatereport} object that contains the template information.
	 * @throws NoSuchAlgorithmException If an error occurs while it is created the content identifier.
	 */
	private TemplateReportPOJO convertTempleateData(TemplateData td, boolean includeContent) throws NoSuchAlgorithmException {
		TemplateReportPOJO tr = new TemplateReportPOJO();
		tr.setAttachment(getAttachment(td.isAttachSignature(), td.isAttachCertificate(), td.isAttachDocument(), td.isAttchResponse(),td.isAttchDocInSignature()));
		tr.setCreationtime(new Date());
		tr.setDiType(td.getModeDocInclude());
		tr.setConcatrule(td.getConcatenationRule());
		tr.setPageRange(td.getPagesRange());
		if (includeContent && td.getTemplate() != null) {
			
			final TemplateContentPOJO templateContent = new TemplateContentPOJO();
			templateContent.setContent(td.getTemplate());
			templateContent.setContentId(getTemplateContentId(td.getTemplate()));
			tr.setTrContent(templateContent);
			
		}
		tr.setTrForce(td.isForceGeneration());
		tr.setTrId(td.getPagesRange());
		tr.setTrType(td.getReportType());
		tr.setTrId(td.getIdentifier());
		return tr;
	}
	
	/**
	 * Gets the attachment information registered into database.
	 * @param attSig	If the signature is attachment.
	 * @param attCert	If the signer certificate is attachment.
	 * @param attDoc	If the signed document is attachment.
	 * @param attResp	If the XML response is attachment.
	 * @param attDocSig If the document included into XML Response is attachment.
	 * @return	{@link Attachment} object that contains the template information.
	 */
	private AttachmentPOJO getAttachment(boolean attSig, boolean attCert, boolean attDoc, boolean attResp,boolean attDocSig) {
		AttachmentPOJO att = null;
		String key = getAttachmentListKey(attSig, attCert, attDoc, attResp,attDocSig);
		Integer pkAtt = listAttachment.get(key);
		if (pkAtt != null) {
			
			att = new AttachmentPOJO();
			att.setAttCert(attCert);
			att.setAttDoc(attDoc);
			att.setAttDocSig(attDocSig);
			att.setAttPk(pkAtt);
			att.setAttResp(attResp);
			att.setAttSig(attSig);
						
		}
		return att;
	}
	
	/**
	 * Gets the template content identifier.
	 * @param template	Template file.
	 * @return	Encoded digest of the template.
	 * @throws NoSuchAlgorithmException	If an error occurs.
	 */
	public  String getTemplateContentId( byte[] template) throws NoSuchAlgorithmException{
		MessageDigest md = MessageDigest.getInstance(HASH_ALG);
		return base64.encodeBytes(md.digest(template));
	}
	
	/**
	 * Gets the key used in the attachment cache list.
	 * @param attSig	If the signature is attachment.
	 * @param attCert	If the signer certificate is attachment.
	 * @param attDoc	If the signed document is attachment.
	 * @param attResp	If the XML response is attachment.
	 * @param attDocSig If the document included into XML Response is attachment.
	 * @return	Unique identifier for the supplied parameters. 
	 */
	private String getAttachmentListKey(boolean attSig, boolean attCert, boolean attDoc, boolean attResp,boolean attDocSig) {
		return ""+attSig + attCert +attDoc+attResp+attDocSig;
	}
    
}
