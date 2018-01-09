package es.gob.signaturereport.persistence.em;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import eVisorEar.EntityManagerAudit;

/**
 * <p>Class used to interact with the persistence context. This instance is associated with the persistence context of the
 * audit database schema. A persistence context is a set of entity instances in which for any persistent entity identity there is a
 * unique entity instance. Within the persistence context, the entity instances and their lifecycle are managed. This class is used to create
 * and remove persistent entity instances, to find entities by their primary key, and to query over entities.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 13/05/2013.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class AuditEntityManager implements IAuditEntityManager {

	/**
     * Attribute that allows to interact with the persistence context.
     */
    @Inject @EntityManagerAudit
    private EntityManager emAudit;
    
      
    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#load(java.lang.Class, java.lang.Object)
     */
    
    public Object load(final Class<?> clazz, final Object id) {
		return emAudit.find(clazz, id);
    }

    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#persist(java.lang.Object)
     */
    
    public void persist(final Object obj) {
    	emAudit.persist(obj);

    }


    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#merge(java.lang.Object)
     */
    
    public Object merge(final Object obj) {
    	return emAudit.merge(obj);
    }


    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#remove(java.lang.Class, java.lang.Object)
     */
    
    public void remove(final Class<?> clazz, final Object id) {
    	Object toDelete = emAudit.find(clazz, id);
    	if (toDelete != null) {
    	    emAudit.remove(toDelete);
    	}

    }

    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#namedQuery(java.lang.String, java.util.Map)
     */
    
    public List<?> namedQuery(final String name, final Map<String, Object> parameters) {

		Query query = emAudit.createNamedQuery(name);
		if (parameters != null) {
			Iterator<String> it = parameters.keySet().iterator();
			String elem;
			while (it.hasNext()) {
				elem = it.next();
				query.setParameter(elem, parameters.get(elem));
			}
		}
		return query.getResultList();
    }

    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#namedQuerySingleResult(java.lang.String, java.util.Map)
     */
    
    public Object namedQuerySingleResult(final String name, final Map<String, Object> parameters) {

		List<?> listResult = namedQuery(name, parameters);
		if (listResult != null && listResult.size() > 0) {
			return listResult.get(0);
		}
		return null;
    }

    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#executeNamedQuery(java.lang.String, java.util.Map)
     */
    
    public void executeNamedQuery(final String namedQuery, final Map<String, Object> parameters) {

		Query query = emAudit.createNamedQuery(namedQuery);
		if (parameters != null) {
			Iterator<String> it = parameters.keySet().iterator();
			String elem;
			while (it.hasNext()) {
				elem = it.next();
				query.setParameter(elem, parameters.get(elem));
			}
		}
		query.executeUpdate();

    }

    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#executeHQLQuery(java.lang.String, java.util.Map)
     */
    
    public List<?> executeHQLQuery(final String hqlQuery, final Map<String, Object> parameters) {

		Query query = emAudit.createQuery(hqlQuery);
		if (parameters != null) {
			Iterator<String> it = parameters.keySet().iterator();
			String elem;
			while (it.hasNext()) {
				elem = it.next();
				query.setParameter(elem, parameters.get(elem));
			}
		}
		return query.getResultList();
    }

    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#getDeclaredEntities()
     */
    
	public Set<EntityType<?>> getDeclaredEntities() {

		return emAudit.getMetamodel().getEntities();
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#executeQuery(java.lang.String, java.util.Map)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void executeQuery(final String query, final Map<String, Object> parameters) {

		Query q = emAudit.createQuery(query);
		if (parameters != null) {
			Iterator<String> it = parameters.keySet().iterator();
			String elem;
			while (it.hasNext()) {
				elem = it.next();
				q.setParameter(elem, parameters.get(elem));
			}
		}
		q.executeUpdate();

	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#getCriteriaBuilder()
	 */
	
	public CriteriaBuilder getCriteriaBuilder() {

		return emAudit.getCriteriaBuilder();
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#createQuery(javax.persistence.criteria.CriteriaQuery)
	 */
	
	public <T> TypedQuery<T> createQuery(final CriteriaQuery<T> criteriaQuery) {

		return emAudit.createQuery(criteriaQuery);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#getMetamodel()
	 */
	
	public Metamodel getMetamodel() {

		return emAudit.getMetamodel();
	}

	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.em.IAuditEntityManager#findAll(java.lang.Class)
	 */
	public List<?> findAll(Class<?> clazz) {

		String consulta = "SELECT obj FROM " + clazz.getName() + " AS obj ";
		Query query = emAudit.createQuery(consulta);
		return query.getResultList();

	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.em.IAuditEntityManager#createHQLQuery(java.lang.String, java.util.Map)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public List<Object[ ]> createHQLQuery(String hqlQuery, Map<String, Object> parameters) {
		
		Query query = emAudit.createQuery(hqlQuery);
		
		if (parameters != null) {
			final Iterator<String> it = parameters.keySet().iterator();
			String elem;
			while (it.hasNext()) {
				elem = it.next();
				query.setParameter(elem, parameters.get(elem));
			}
		}
		return query.getResultList();
	}

}
