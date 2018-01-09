package es.gob.signaturereport.persistence.configuration.model.bo.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.persistence.configuration.cache.TemplateCache;
import es.gob.signaturereport.persistence.configuration.model.bo.interfaz.ITemplateModuleBO;
import es.gob.signaturereport.persistence.configuration.model.pojo.ApplicationPOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.TemplateContentPOJO;
import es.gob.signaturereport.persistence.configuration.model.pojo.TemplateReportPOJO;
import es.gob.signaturereport.persistence.em.IConfigurationEntityManager;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.utils.IParametersQueriesConstants;
import es.gob.signaturereport.persistence.utils.IQueriesNamesConstants;

/**
 * <p>Class manager for the Business Objects in template module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TemplateModuleBO implements ITemplateModuleBO {
		
	/**
	 * Attribute that represents . 
	 */
	private static final long serialVersionUID = 7695605661362890495L;

	/**
     * Attribute that represents the class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(TemplateModuleBO.class);
    
	/**
     * Attribute that allows to interact with the persistence configuration context.
     */
    @Inject
    private IConfigurationEntityManager em;
    
    
    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.ITemplateModuleBO#getTemplateReport(java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
	public TemplateReportPOJO getTemplateReport(String templateId) throws DatabaseException {
		if (templateId == null || templateId.equals("")) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_070));
		}
		
		try {
						
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(IParametersQueriesConstants.PARAM_TR_ID, templateId);
			List<TemplateReportPOJO> templateList = (List<TemplateReportPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_TEMPLATEREPORT_BY_ID, parameters);
			
			if (templateList.isEmpty()) {
				throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_071, new Object[ ] { templateId }));
			} else if (templateList.size() > 1) {
				throw new DatabaseException(DatabaseException.DUPLICATE_ITEM, Language.getFormatMessage(LanguageKeys.DB_072, new Object[ ] { templateId }));
			} else {
				TemplateReportPOJO dbTemplate = templateList.get(0);
				
				return dbTemplate;
			}
			

		} catch (PersistenceException e) {
			String msg = Language.getFormatMessage(LanguageKeys.DB_078, new Object[ ] { templateId });
			LOGGER.error(msg, e);
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg,e);
		} 
	}


	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.ITemplateModuleBO#getTemplateContent(java.lang.String)
	 */
	@Override
	public TemplateContentPOJO getTemplateContent(String templateId) throws DatabaseException {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters = new HashMap<String, Object>();
		parameters.put(IParametersQueriesConstants.PARAM_TEMPLATECONTENT_ID, templateId);
		TemplateContentPOJO tContent = (TemplateContentPOJO) em.namedQuerySingleResult(IQueriesNamesConstants.QUERYNAME_FIND_TEMPLATECONTENT_BY_ID, parameters);
		
		return tContent;
	}
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.ITemplateModuleBO#createTemplate(es.gob.signaturereport.configuration.items.TemplateData)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void createTemplate(TemplateReportPOJO templateReport, TemplateContentPOJO templateContent) throws DatabaseException {
		
		if (templateReport == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_079));
		}
		
		try {
					
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(IParametersQueriesConstants.PARAM_TR_ID, templateReport.getTrId());
			List<TemplateReportPOJO> templateList = (List<TemplateReportPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_TEMPLATEREPORT_BY_ID, parameters);
			
			if (!templateList.isEmpty()) {
				throw new DatabaseException(DatabaseException.DUPLICATE_ITEM, Language.getFormatMessage(LanguageKeys.DB_080, new Object[ ] { templateReport.getTrId() }));
			} else {
								
				if (templateContent != null) {
					em.persist(templateContent);
				} else {
					em.persist(templateReport.getTrContent());
				}
				em.persist(templateReport);
				TemplateCache.getInstance().addTemplate(templateReport.getTrContent().getContentId(), templateReport.getTrContent().getContent());
			}

		} catch (PersistenceException pe) {
			
			String msg = Language.getFormatMessage(LanguageKeys.DB_081, new Object[ ] { templateReport.getTrId() });
			LOGGER.error(msg, pe);
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
		} 
	}
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.ITemplateModuleBO#modifyTemplate(es.gob.signaturereport.configuration.items.TemplateData)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void modifyTemplate(TemplateReportPOJO templateReport, boolean newContent) throws DatabaseException {
		if (templateReport == null) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_079));
		}

		try {

			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(IParametersQueriesConstants.PARAM_TR_ID, templateReport.getTrId());
			List<TemplateReportPOJO> templateList = (List<TemplateReportPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_TEMPLATEREPORT_BY_ID, parameters);

			if (templateList.isEmpty()) {
				throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_071, new Object[ ] { templateReport.getTrId() }));
			} else if (templateList.size() > 1) {
				throw new DatabaseException(DatabaseException.DUPLICATE_ITEM, Language.getFormatMessage(LanguageKeys.DB_082, new Object[ ] { templateReport.getTrId() }));
			} else {
				TemplateReportPOJO aux = templateList.get(0);
				aux.setAttachment(templateReport.getAttachment());
				aux.setTrForce(templateReport.getTrForce());
				aux.setTrType(templateReport.getTrType());
				aux.setDiType(templateReport.getDiType());
				aux.setPageRange(templateReport.getPageRange());
				aux.setConcatrule(templateReport.getConcatrule());
				aux.setTrContent(templateReport.getTrContent());
				
				if (newContent) {
					em.persist(templateReport.getTrContent());
					TemplateCache.getInstance().addTemplate(templateReport.getTrContent().getContentId(), templateReport.getTrContent().getContent());
				}

				em.merge(aux);

			}

		} catch (PersistenceException e) {
			String msg = Language.getFormatMessage(LanguageKeys.DB_081, new Object[ ] { templateReport.getTrId() });
			LOGGER.error(msg, e);
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.ITemplateModuleBO#removeTemplate(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void removeTemplate(String templateId) throws DatabaseException {
		if (templateId == null || templateId.equals("")) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_070));
		}

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(IParametersQueriesConstants.PARAM_TR_ID, templateId);
			List<TemplateReportPOJO> templateList = (List<TemplateReportPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_TEMPLATEREPORT_BY_ID, parameters);

			if (templateList.isEmpty()) {
				throw new DatabaseException(DatabaseException.ITEM_NOT_FOUND, Language.getFormatMessage(LanguageKeys.DB_071, new Object[ ] { templateId }));
			} else if (templateList.size() > 1) {
				throw new DatabaseException(DatabaseException.DUPLICATE_ITEM, Language.getFormatMessage(LanguageKeys.DB_082, new Object[ ] { templateId }));
			} else {
				TemplateReportPOJO templateReport = templateList.get(0);
				Set<ApplicationPOJO> applications = templateReport.getApplications();
				if (applications.isEmpty()) {
					templateReport.setEndingtime(new Date());
					em.merge(templateReport);

				} else {
					throw new DatabaseException(DatabaseException.ITEM_ASSOCIATED, Language.getFormatMessage(LanguageKeys.DB_123, new Object[ ] { templateId }));
				}

			}

		} catch (PersistenceException pe) {

			String msg = Language.getFormatMessage(LanguageKeys.DB_081, new Object[ ] { templateId });
			LOGGER.error(msg, pe);
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.ITemplateModuleBO#getTemplateIds()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Integer> getTemplateIds() throws DatabaseException {

		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			List<TemplateReportPOJO> templateIds = (List<TemplateReportPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_ALL_ACTIVE_TEMPLATEREPORT, parameters);

			LinkedHashMap<String, Integer> list = new LinkedHashMap<String, Integer>();
			if (templateIds != null && !templateIds.isEmpty()) {

				final Iterator<TemplateReportPOJO> itTemplates = templateIds.iterator();

				while (itTemplates.hasNext()) {

					TemplateReportPOJO templateReport = (TemplateReportPOJO) itTemplates.next();
					list.put((String) templateReport.getTrId(), (Integer) templateReport.getTrType());
				}
			}

			return list;

		} catch (Exception e) {
			String msg = Language.getMessage(LanguageKeys.DB_084);
			LOGGER.error(msg, e);
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, e);
		}

	}
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.configuration.model.bo.interfaz.ITemplateModuleBO#existTemplate(java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public boolean existTemplate(String templateId) throws DatabaseException{ 
		if (templateId == null || templateId.equals("")) {
			throw new DatabaseException(DatabaseException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.DB_070));
		}
		
		try {
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put(IParametersQueriesConstants.PARAM_TR_ID, templateId);
			List<TemplateReportPOJO> templateList = (List<TemplateReportPOJO>) em.namedQuery(IQueriesNamesConstants.QUERYNAME_FIND_TEMPLATEREPORT_BY_ID, parameters);
			return templateList.size()>0;
		} catch (PersistenceException pe) {
			String msg = Language.getFormatMessage(LanguageKeys.DB_078, new Object[ ] { templateId });
			LOGGER.error(msg, pe);
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
		}  
	}

}
