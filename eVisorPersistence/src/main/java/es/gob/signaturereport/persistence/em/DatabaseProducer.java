package es.gob.signaturereport.persistence.em;

import javax.annotation.ManagedBean;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import eVisorEar.EntityManagerAudit;

@ManagedBean
public class DatabaseProducer {
		 
	    @Produces
	    @PersistenceContext(unitName = "evisor-configuration-persistence", type = PersistenceContextType.TRANSACTION)
	    @EntityManagerConfiguration
	    private EntityManager emConfiguration;
	    
	    @Produces
	    @PersistenceContext(unitName = "evisor-audit-persistence", type = PersistenceContextType.TRANSACTION)
	    @EntityManagerAudit
	    private EntityManager emAudit;

}
