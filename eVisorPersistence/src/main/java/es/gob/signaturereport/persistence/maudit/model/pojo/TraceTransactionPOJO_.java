package es.gob.signaturereport.persistence.maudit.model.pojo;

import java.util.Date;

import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(TraceTransactionPOJO.class)
public class TraceTransactionPOJO_ {
	
	public static volatile SingularAttribute<TraceTransactionPOJO, Long> tracePk;
	
	public static volatile SingularAttribute<TraceTransactionPOJO, Long> transaction;
		
	public static volatile SingularAttribute<TraceTransactionPOJO, ActionTypePOJO> actionType;
		
	public static volatile SingularAttribute<TraceTransactionPOJO, Date> creationTime;
		
	public static volatile SetAttribute<TraceTransactionPOJO, TraceFieldPOJO> traceFields;
}
