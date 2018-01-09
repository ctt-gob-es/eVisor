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
 * <b>File:</b><p>es.gob.signaturereport.persistence.em.IConfigurationEntityManager.java.</p>
 * <b>Description:</b><p>Interface with the persistence context available methods related with the configuration database schema.</p>
 * @author Gobierno de España.
 * @version 1.0.
 */
package es.gob.signaturereport.persistence.em;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

/**
 * <p>Interface with the persistence context available methods related with the configuration database schema.</p>
 * @version 1.0, 06/05/2013.
 */
public interface IAuditEntityManager {
	 
    /**
     * Method that obtains an object by primary key. Search for an entity of the specified class and primary key.
     * If the entity instance is contained in the persistence context, it is returned from there.
     * @param clazz Parameter that represents the entity class.
     * @param id Parameter that represents the primary key.
     * @return the found entity instance or null if the entity does not exist.
     */
    Object load(Class<?> clazz, Object id);

    /**
     * Method that makes an instance managed and persistent.
     * @param obj Parameter that represents the entity instance.
     */
    void persist(Object obj);

    /**
     * Method that merges the state of the given entity into the current persistence context.
     * @param obj Parameter that represents the entity instance.
     * @return the managed instance that the state was merged to.
     */
    Object merge(Object obj);

    /**
     * Method that removes an entity instance by primary key.
     * @param clazz Parameter that represents the entity class.
     * @param id Parameter that represents the primary key.
     */
    void remove(Class<?> clazz, Object id);

    /**
     * Method that creates an instance of Query for executing a named query (in the Java Persistence query language or in native SQL)
     * and returns a list with the obtained entities.
     * @param name Parameter that represents the name of a query defined in meta-data.
     * @param parameters Map with all the parameters used for executing the named query.
     * @return a list with the detached entities.
     */
    List<?> namedQuery(String name, Map<String, Object> parameters);

    /**
     * Method that creates an instance of Query for executing a named query (in the Java Persistence query language or in native SQL)
     * and returns a unique result with the obtained entity.
     * @param name Parameter that represents the name of a query defined in meta-data.
     * @param parameters Map with all the parameters used for executing the named query.
     * @return unique result with the detached entity.
     */
    Object namedQuerySingleResult(String name, Map<String, Object> parameters);

    /**
     * Method that executes an update or delete statement by a named query.
     * @param namedQuery Parameter that represents the name of a query defined in meta-data.
     * @param parameters Map with all the parameters used for executing the named query.
     */
    void executeNamedQuery(String namedQuery, Map<String, Object> parameters);

    /**
     * Method that executes a HQL query.
     * @param hqlQuery Parameter that represents the HQL query to execute.
     * @param parameters Map with all the parameters used for executing the HQL query.
     * @return a list with the returned elements.
     */
    List<?> executeHQLQuery(String hqlQuery, Map<String, Object> parameters);

    /**
	 * Method that gets a set with all the declared entities in the entity manager.
	 * @return {@link Set} of {@link EntityType} with all the entities declared in the
	 * entity manager.
	 */
	Set<EntityType<?>> getDeclaredEntities();

	/**
	 * Method that executes an update or delete statement by a not named query.
	 * @param query Parameter that represents the a string with the query to execute.
	 * @param parameters Map with all the parameters used for executing the query.
	 */
	void executeQuery(String query, Map<String, Object> parameters);

	/**
     * Return an instance of <code>CriteriaBuilder</code> for the creation of
     * <code>CriteriaQuery</code> objects.
     * @return CriteriaBuilder instance
     * @throws IllegalStateException if the entity manager has
     *         been closed
     * @since Java Persistence 2.0
     */
    CriteriaBuilder getCriteriaBuilder();

    /**
     * Create an instance of <code>TypedQuery</code> for executing a
     * criteria query.
     * @param criteriaQuery  a criteria query object
     * @return the new query instance
     * @throws IllegalArgumentException if the criteria query is
     *         found to be invalid
     * @since Java Persistence 2.0
     */
    <T> TypedQuery<T> createQuery(CriteriaQuery<T> criteriaQuery);

    /**
     * Return an instance of <code>Metamodel</code> interface for access to the
     * metamodel of the persistence unit.
     * @return Metamodel instance
     * @throws IllegalStateException if the entity manager has
     *         been closed
     * @since Java Persistence 2.0
     */
    Metamodel getMetamodel();
    
    /**
	 * Method that obtains all the entities by class.
	 * @param clazz Parameter that represents the entity class.
	 * @return a list with all the entities.
	 */
	List<?> findAll(Class<?> clazz);
	
	/**
     * Method that creates and executes a HQL query.
     * @param hqlQuery Parameter that represents the HQL query to execute.
     * @param parameters Map with all the parameters used for executing the HQL query.
     * @return a list with the returned elements.
     */
    List<Object[]> createHQLQuery(String hqlQuery, Map<String, Object> parameters);
	        
}
