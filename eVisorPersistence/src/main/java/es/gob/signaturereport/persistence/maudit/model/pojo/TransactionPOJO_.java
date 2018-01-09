package es.gob.signaturereport.persistence.maudit.model.pojo;

import java.util.Date;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(TransactionPOJO.class)
public class TransactionPOJO_ {
		
	public static volatile SingularAttribute<TransactionPOJO, Long> transactionPk;
			
	public static volatile SingularAttribute<TransactionPOJO, ServicesPOJO> service;
	
	public static volatile SingularAttribute<TransactionPOJO, Date> creationTime;
		
	public static volatile SingularAttribute<TransactionPOJO, Long> eventId;
	
	public static volatile SetAttribute<TransactionPOJO, TraceTransactionPOJO> traces;
	
	
}
