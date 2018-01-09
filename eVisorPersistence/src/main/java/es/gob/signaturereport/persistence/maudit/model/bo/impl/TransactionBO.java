package es.gob.signaturereport.persistence.maudit.model.bo.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.Metamodel;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.persistence.em.IAuditEntityManager;
import es.gob.signaturereport.persistence.exception.DatabaseException;
import es.gob.signaturereport.persistence.maudit.model.interfaz.ITransactionBO;
import es.gob.signaturereport.persistence.maudit.model.pojo.FieldPOJO;
import es.gob.signaturereport.persistence.maudit.model.pojo.ServicesPOJO;
import es.gob.signaturereport.persistence.maudit.model.pojo.TraceFieldPOJO;
import es.gob.signaturereport.persistence.maudit.model.pojo.TraceFieldPOJO_;
import es.gob.signaturereport.persistence.maudit.model.pojo.TraceTransactionPOJO;
import es.gob.signaturereport.persistence.maudit.model.pojo.TraceTransactionPOJO_;
import es.gob.signaturereport.persistence.maudit.model.pojo.TransactionPOJO;
import es.gob.signaturereport.persistence.maudit.model.pojo.TransactionPOJO_;
import es.gob.signaturereport.persistence.utils.IAttributeEntityConstants;

/**
 * <p>Class manager for the Business Objects in configuration module.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class TransactionBO implements ITransactionBO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6724809981047722173L;
	
	/**
     * Attribute that represents the class logger.
     */
    private static final Logger LOGGER = Logger.getLogger(TransactionBO.class);
	
	/**
     * Attribute that allows to interact with the persistence configuration context.
     */
    // CHECKSTYLE:OFF -- Checkstyle rule "Design for Extension" is not applied
    // because injection needs not final access property.
    @Inject
    private IAuditEntityManager em;

	/* (non-Javadoc)
	 * @see es.gob.signaturereport.persistence.maudit.model.interfaz.ITransactionBO#save(es.gob.signaturereport.persistence.maudit.model.pojo.TransactionPOJO)
	 */
	@Override
	public void save(TransactionPOJO transaction) throws DatabaseException {

		try {
			em.persist(transaction);
		} catch (PersistenceException pe) {
			
			String msg = Language.getMessage(LanguageKeys.AUD_029);
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
		}
		
		
	}

	/* (non-Javadoc)
	 * @see es.gob.signaturereport.persistence.maudit.model.interfaz.ITransactionBO#getTransactions(java.util.Date, java.util.Date, java.lang.Integer, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<TransactionPOJO> getTransactions(Date beginningTime,
			Date endingDate, Integer service, String application,
			Integer firstResult, Integer maxResults, FieldPOJO appField) throws DatabaseException {
		
		TypedQuery<TransactionPOJO> queryTransactions = null;
		
		List<Predicate> predList = new LinkedList<Predicate>();
		

		// Preparo la sentencia dinámica
		CriteriaBuilder critBuilder = em.getCriteriaBuilder();
		CriteriaQuery<TransactionPOJO> criteria = critBuilder.createQuery(TransactionPOJO.class);
		Metamodel m = em.getMetamodel();
		Root<TransactionPOJO> transaction = criteria.from(TransactionPOJO.class);

		
		if (beginningTime != null) {
		
			predList.add(critBuilder.greaterThanOrEqualTo(transaction.<Date> get(IAttributeEntityConstants.ATTR_CREATIONTIME), beginningTime));
		}
		
		if (endingDate != null) {
			
			predList.add(critBuilder.lessThanOrEqualTo(transaction.<Date> get(IAttributeEntityConstants.ATTR_CREATIONTIME), endingDate));
		}
		
		if (service != null) {
			final ServicesPOJO servicePojo = new ServicesPOJO();
			servicePojo.setServicePk(service);
			predList.add(critBuilder.equal(transaction.<ServicesPOJO> get(IAttributeEntityConstants.ATTR_SERVICE), servicePojo));
		}
		
		if (application != null && !"".equals(application.trim())) {
			
			Join<TransactionPOJO, TraceTransactionPOJO> transactionJoinTraceTransaction = transaction.join(TransactionPOJO_.traces);
			Join<TraceTransactionPOJO, TraceFieldPOJO> traceTransactionJoinTraceField =  transactionJoinTraceTransaction.join(TraceTransactionPOJO_.traceFields);
						
			predList.add(critBuilder.and(critBuilder.equal(traceTransactionJoinTraceField.get(TraceFieldPOJO_.field), appField), critBuilder.like(traceTransactionJoinTraceField.get(TraceFieldPOJO_.fieldValue), application)));
			
			//crit.createCriteria(TRACES).createCriteria(TRACEFIELDS).add(Restrictions.and(Restrictions.eq(FIELD, appField), Restrictions.like(FIELDVALUE, application)));
		}
		
		criteria.orderBy(critBuilder.desc(transaction.get(TransactionPOJO_.creationTime)));
					
		Predicate[ ] predArray = predList.toArray(new Predicate[predList.size()]);
		
		// Combino las restricciones para formar el WHERE
		criteria = criteria.where(predArray);
		
		queryTransactions = em.createQuery(criteria);
		
		if (firstResult != null) {
			queryTransactions = queryTransactions.setFirstResult(firstResult.intValue());
			
		}
		if (maxResults != null) {
			queryTransactions = queryTransactions.setMaxResults(maxResults.intValue());
		}
		criteria.select(transaction);
		
		List<TransactionPOJO> transactions = null;
		
		try {
			
			transactions = queryTransactions.getResultList();
			
		} catch (PersistenceException pe) {
		
			String msg = Language.getMessage(LanguageKeys.AUD_041);
			throw new DatabaseException(DatabaseException.UNKNOWN_ERROR, msg, pe);
		}

		return transactions;
		
	}

	/* (non-Javadoc)
	 * @see es.gob.signaturereport.persistence.maudit.model.interfaz.ITransactionBO#getTransaction(long)
	 */
	@Override
	public TransactionPOJO getTransaction(long transactionId)
			throws DatabaseException {
			
		return (TransactionPOJO) em.load(TransactionPOJO.class, transactionId);
		
	}

}
