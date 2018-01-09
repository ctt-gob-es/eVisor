package es.gob.signaturereport.persistence.maudit.model.pojo;

import java.util.Date;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(TraceFieldPOJO.class)
public class TraceFieldPOJO_ {
	
	
	public static volatile SingularAttribute<TraceFieldPOJO, Long> traceFieldPk;
		
	public static volatile SingularAttribute<TraceFieldPOJO, TraceTransactionPOJO> traceTransaction;
		
	public static volatile SingularAttribute<TraceFieldPOJO, FieldPOJO> field;
	
	public static volatile SingularAttribute<TraceFieldPOJO, String> fieldValue;
		
	public static volatile SingularAttribute<TraceFieldPOJO, Date> creationTime;
	
	public static volatile SingularAttribute<TraceFieldPOJO, String> fieldValueType;
	
}
