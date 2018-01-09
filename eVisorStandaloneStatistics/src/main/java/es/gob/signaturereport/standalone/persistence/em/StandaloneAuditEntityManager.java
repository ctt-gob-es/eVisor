package es.gob.signaturereport.standalone.persistence.em;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

/**
 * <p>Class used to interact with the persistence context. This instance is associated with the persistence context of the
 * audit database schema. A persistence context is a set of entity instances in which for any persistent entity identity there is a
 * unique entity instance. Within the persistence context, the entity instances and their lifecycle are managed. This class is used to create
 * and remove persistent entity instances, to find entities by their primary key, and to query over entities.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 15/07/2016.
 */
public final class StandaloneAuditEntityManager {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(StandaloneAuditEntityManager.class);

	/**
	 * Attribute that represents a class instance.
	 */
	private static StandaloneAuditEntityManager instance = null;
	/**
	 * Attribute that allows to interact with the persistence context associated to the transactional module.
	 */
	private EntityManager em = null;

	/**
	 * Attribute that allows to obtain an application-managed entity manager.
	 */
	private EntityManagerFactory emf = null;

	/**
	 * Constructor method for the class StandaloneTransEntityManager.java.
	 */
	private StandaloneAuditEntityManager() {
		createEntityManagerFactory();
		createEntityManager();
	}

	/**
	 * Gets a class instance.
	 * @return	An instance of the class.
	 */
	public static StandaloneAuditEntityManager getInstance() {
		if (instance == null) {
			instance = new StandaloneAuditEntityManager();
		}
		return instance;
	}

	/**
	 * Gets an entity manager for the transactional database schema.
	 * @return	An entity manager for the transactional database schema.
	 */
	public EntityManager getEntityManager() {
		if (!em.isOpen()) {
			if (!emf.isOpen()) {
				createEntityManagerFactory();
			}
			createEntityManager();
		}
		return em;

	}

	/**
	 * Creates an entity manager factory for the transactional database schema.
	 */
	private void createEntityManagerFactory() {
		try {
			emf = Persistence.createEntityManagerFactory("evisor-standalone-audit-persistence");
		} catch (Exception e) {
			//LOGGER.error(Language.getFormatResStandaloneAudit(StandaloneAuditLogConstants.STAND_AUDIT_LOG020, new Object[ ] { EntityManagerFactory.class.getSimpleName() }));
		}
	}

	/**
	 * Creates an entity manager for the transactional database schema.
	 */
	private void createEntityManager() {
		try {
			em = emf.createEntityManager();
		} catch (Exception e) {
			//LOGGER.error(Language.getFormatResStandaloneAudit(StandaloneAuditLogConstants.STAND_AUDIT_LOG020, new Object[ ] { EntityManagerFactory.class.getSimpleName() }));
		}
	}

}
