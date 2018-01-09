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
 * <b>File:</b><p>es.gob.signaturereport.persistence.em.ConfigurationEntityManager.java.</p>
 * <p>Class used to interact with the persistence context. This instance is associated with the persistence context of the
 * configuration database schema. A persistence context is a set of entity instances in which for any persistent entity identity there is a
 * unique entity instance. Within the persistence context, the entity instances and their lifecycle are managed. This class is used to create
 * and remove persistent entity instances, to find entities by their primary key, and to query over entities.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @author Gobierno de España.
 * @version 1.0, 14/05/2013.
 */
package es.gob.signaturereport.persistence.em;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

/**
 * <p>Class used to interact with the persistence context. This instance is associated with the persistence context of the
 * configuration database schema. A persistence context is a set of entity instances in which for any persistent entity identity there is a
 * unique entity instance. Within the persistence context, the entity instances and their lifecycle are managed. This class is used to create
 * and remove persistent entity instances, to find entities by their primary key, and to query over entities.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 13/05/2013.
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ConfigurationEntityManager implements IConfigurationEntityManager {

    /**
     * Attribute that allows to interact with the persistence context.
     */
    @Inject @EntityManagerConfiguration
    private EntityManager emConfiguration;
   
            
    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#load(java.lang.Class, java.lang.Object)
     */
    public Object load(final Class<?> clazz, final Object id) {

    	return emConfiguration.find(clazz, id);
    }

   
    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#persist(java.lang.Object)
     */
    
    public void persist(final Object obj) {
    	
    	emConfiguration.persist(obj);

    }

    
    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#merge(java.lang.Object)
     */
    
    public Object merge(final Object obj) {
	
    	return emConfiguration.merge(obj);
    }

   
    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#remove(java.lang.Class, java.lang.Object)
     */
    
    public void remove(final Class<?> clazz, final Object id) {
    	
    	Object toDelete = emConfiguration.find(clazz, id);
    	if (toDelete != null) {
    	    emConfiguration.remove(toDelete);
    	}

    }

   
    /**
     * {@inheritDoc}
     * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#namedQuery(java.lang.String, java.util.Map)
     */
    
    public List<?> namedQuery(final String name, final Map<String, Object> parameters) {
		Query query = emConfiguration.createNamedQuery(name);
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
    
    public void executeNamedQuery(String namedQuery, Map<String, Object> parameters) {
	
		Query query = emConfiguration.createNamedQuery(namedQuery);
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
	
		Query query = emConfiguration.createQuery(hqlQuery);
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
		
		return emConfiguration.getMetamodel().getEntities();
	}

	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#executeQuery(java.lang.String, java.util.Map)
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void executeQuery(final String query, final Map<String, Object> parameters) {
		
		Query q = emConfiguration.createQuery(query);
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
		return emConfiguration.getCriteriaBuilder();
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#createQuery(javax.persistence.criteria.CriteriaQuery)
	 */
	
	public <T> TypedQuery<T> createQuery(final CriteriaQuery<T> criteriaQuery) {
		return createQuery(criteriaQuery);
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#getMetamodel()
	 */
	
	public Metamodel getMetamodel() {
		return getMetamodel();
	}

	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.persistence.em.IConfigurationEntityManager#findAll(java.lang.Class)
	 */
	public List<?> findAll(Class<?> clazz) {
		
		String consulta = "SELECT obj FROM " + clazz.getName() + " AS obj ";
		Query query = emConfiguration.createQuery(consulta);
		return query.getResultList();
	}

}
