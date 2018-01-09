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
  * <b>File:</b><p>es.gob.signaturereport.maudit.statistics.item.Restriction.java.</p>
 * <b>Description:</b><p> Class that represents a query restriction.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>19/09/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 19/09/2011.
 */
package es.gob.signaturereport.maudit.statistics.item;

import es.gob.signaturereport.maudit.statistics.StatisticsManager;

/** 
 * <p>Class that represents a query restriction.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 19/09/2011.
 */
public final class Restriction implements RestrictionI{

	/**
	 * Attribute that represents the field restriction type. 
	 */
	private int fieldType = 0;
	
	/**
	 * Attribute that represents the field value. 
	 */
	private String fieldValue = null;
	
	/**
	 * Attribute that represents the operation used to compare the field with the value. 
	 */
	private String operation = null;
	
	/**
	 * Attribute that represents the union type with the previous restriction. 
	 */
	private String union = null;
	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.statistics.item.RestrictionI#getFieldOperation()
	 */
	public String getFieldOperation() {
		return this.operation;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.statistics.item.RestrictionI#getRestrictionUnion()
	 */
	public String getRestrictionUnion() {
		return this.union;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.statistics.item.RestrictionI#getFieldType()
	 */
	public int getFieldType() {
		return this.fieldType;
	}

	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.maudit.statistics.item.RestrictionI#getFieldValue()
	 */
	public String getFieldValue() {
		return this.fieldValue;
	}

	/**
	 * Constructor method for the class Restriction.java.
	 * @param field		Field restriction type.
	 * @param value	Field value.
	 * @param op		Operation used to compare the field with the value.
	 * @param unionRestriction 		The union type with the previous restriction.
	 */
	private Restriction(int field, String value, String op, String unionRestriction) {
		super();
		this.fieldType = field;
		this.fieldValue = value;
		this.operation = op;
		this.union = unionRestriction;
	}
	
	/**
	 * Gets a {@link RestrictionI} for the "application field".
	 * @param appId			Application identifier.
	 * @param operation		The operation used to compare the field with the value. The allowed values are:
	 * <br/>				{@link StatisticsManager#EQUAL_OPERATION} or {@link StatisticsManager#DISTINCT_OPERATION};
	 * @param union			Union type with the previous restriction. The allowed values are:
	 * <br/>				{@link StatisticsManager#OR_UNION} or {@link StatisticsManager#AND_UNION};
	 * @return	An {@link RestrictionI} object.
	 */
	public static RestrictionI createApplicationRestriction(String appId, String operation,String union){
		return new Restriction(StatisticsManager.APPLICATION_FIELD_TYPE, appId, operation, union);
	}
	
	/**
	 * Gets a {@link RestrictionI} for the "organization unit field".
	 * @param uoId			Organization unit identifier.
	 * @param operation		The operation used to compare the field with the value. The allowed values are:
	 * <br/>				{@link StatisticsManager#EQUAL_OPERATION} or {@link StatisticsManager#DISTINCT_OPERATION};
	 * @param union			Union type with the previous restriction. The allowed values are:
	 * <br/>				{@link StatisticsManager#OR_UNION} or {@link StatisticsManager#AND_UNION};
	 * @return	An {@link RestrictionI} object.
	 */
	public static RestrictionI createUORestriction(String uoId, String operation,String union){
		return new Restriction(StatisticsManager.UO_FIELD_TYPE, uoId, operation, union);
	}
	
	/**
	 * Gets a {@link RestrictionI} for the "service identifier field".
	 * @param serviceIdentifier	Service identifier. The allowed values are:
	 * <br/>	{@link AuditManagerI#GENERATION_REPORT_SRVC} or {@link AuditManagerI#VALIDATION_REPORT_SRVC}.
	 * @param operation		The operation used to compare the field with the value. The allowed values are:
	 * <br/>				{@link StatisticsManager#EQUAL_OPERATION} or {@link StatisticsManager#DISTINCT_OPERATION};
	 * @param union			Union type with the previous restriction. The allowed values are:
	 * <br/>				{@link StatisticsManager#OR_UNION} or {@link StatisticsManager#AND_UNION};
	 * @return	An {@link RestrictionI} object.
	 */
	public static RestrictionI createServiceRestriction(int serviceIdentifier, String operation,String union){
		return new Restriction(StatisticsManager.SERVICE_FIELD_TYPE, String.valueOf(serviceIdentifier), operation, union);
	}
	/**
	 * Gets a {@link RestrictionI} for the "Is signed field".
	 * @param isSign		Flag that indicates if the generated report is signed.
	 * @param operation		The operation used to compare the field with the value. The allowed values are:
	 * <br/>				{@link StatisticsManager#EQUAL_OPERATION} or {@link StatisticsManager#DISTINCT_OPERATION};
	 * @param union			Union type with the previous restriction. The allowed values are:
	 * <br/>				{@link StatisticsManager#OR_UNION} or {@link StatisticsManager#AND_UNION};
	 * @return	An {@link RestrictionI} object.
	 */
	public static RestrictionI createSignRestriction(boolean isSign, String operation,String union){
		return new Restriction(StatisticsManager.SIGN_FIELD_TYPE, String.valueOf(isSign), operation, union);
	}
	
	/**
	 * Gets a {@link RestrictionI} for the "result code field".
	 * @param code		Result code of web service. The allowed value are:
	 * <br/> Generation Report:
	 * <br/> {@link ReportGenerationResponse#PROCESS_OK}.
	 * <br/> {@link ReportGenerationResponse#UNKNOWN_ERROR}.
	 * <br/> {@link ReportGenerationResponse#INVALID_INPUT_PARAMETERS}.
	 * <br/> {@link ReportGenerationResponse#INVALID_SOAP_SIGNATURE}.
	 * <br/> {@link ReportGenerationResponse#INVALID_SIGNATURE}.
	 * <br/> {@link ReportGenerationResponse#INVALID_PAGE_NUMBER}.
	 * <br/> {@link ReportGenerationResponse#INVALID_SIGNED_DOCUMENT}.
	 * <br/> {@link ReportGenerationResponse#INVALID_TEMPLATE}.
	 * <br/> Validation Report:
	 * <br/> {@link ReportValidationResponse#VALID_SIGNATURE}.
	 * <br/> {@link ReportValidationResponse#WARNING_SIGNATURE}.
	 * <br/> {@link ReportValidationResponse#INVALID_SIGNATURE}.
	 * <br/> {@link ReportValidationResponse#UNKNOWN_ERROR}.
	 * <br/> {@link ReportValidationResponse#INVALID_INPUT_PARAMETERS}.
	 * <br/> {@link ReportValidationResponse#UNKNOWN_STATUS}.
	 * @param operation		The operation used to compare the field with the value. The allowed values are:
	 * <br/>				{@link StatisticsManager#EQUAL_OPERATION} or {@link StatisticsManager#DISTINCT_OPERATION};
	 * @param union			Union type with the previous restriction. The allowed values are:
	 * <br/>				{@link StatisticsManager#OR_UNION} or {@link StatisticsManager#AND_UNION};
	 * @return	An {@link RestrictionI} object.
	 */
	public static RestrictionI createResultRestriction(int code, String operation,String union){
		return new Restriction(StatisticsManager.RESULT_CODE_FIELD_TYPE, String.valueOf(code), operation, union);
	}
}
