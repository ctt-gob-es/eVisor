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
 * <b>File:</b><p>es.gob.signaturereport.controller.LogController.java.</p>
 * <b>Description:</b><p>Class that manages </p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>07/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 07/02/2011.
 */
package es.gob.signaturereport.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

import es.gob.signaturereport.configuration.access.TemplateConfigurationFacadeI;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.maudit.AuditException;
import es.gob.signaturereport.maudit.AuditManagerI;
import es.gob.signaturereport.maudit.item.AuditField;
import es.gob.signaturereport.maudit.item.AuditOperation;
import es.gob.signaturereport.maudit.item.AuditService;
import es.gob.signaturereport.maudit.item.AuditTransaction;
import es.gob.signaturereport.maudit.log.traces.TraceFieldValue;
import es.gob.signaturereport.maudit.log.traces.TraceI;

/** 
 * <b>File:</b><p>es.gob.signaturereport.controller.LogController.java.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 13/10/2011.
 */
@Named("log")
@SessionScoped
public class LogController extends AbstractController {

    /**
	 * Attribute that represents class version. 
	 */
	private static final long serialVersionUID = 1616593096058260657L;
	/**
     * Attribute that represents . 
     */
    private static int CONSULT_TYPE_DATE = 0;
    /**
     * Attribute that represents . 
     */
    private static int CONSULT_TYPE_IDTRANS = 1;

    /**
     * Attribute that represents . 
     */
    private List<AuditTransaction> transactions = null;
    /**
     * Attribute that represents . 
     */
    org.richfaces.model.selection.Selection transactionSelected;

    /**
     * Attribute that represents . 
     */
    AuditTransaction actualTrx = null;
    /**
     * Attribute that represents . 
     */
    TreeNode<NodeData> actualTrxTraces = null;

    /**
     * Attribute that represents . 
     */
    private int consultType = CONSULT_TYPE_DATE;

    /**
     * Attribute that represents . 
     */
    private Date iniDate = new Date();
    /**
     * Attribute that represents . 
     */
    private Date endDate = new Date();
    /**
     * Attribute that represents . 
     */
    private String idApp = "";
    /**
     * Attribute that represents . 
     */
    private int service = -9;
    /**
     * Attribute that represents . 
     */
    private String idTrans = null;
    
    /**
     * Attribute that represents . 
     */
    @Inject @Singleton
    private transient AuditManagerI auditManager;

    /**
     * 
     * @return
     */
    public TreeNode<NodeData> getActualTrxTraces() {
	return actualTrxTraces;
    }

    /**
     * 
     * @param actualTrxTraces
     */
    public void setActualTrxTraces(TreeNode<NodeData> actualTrxTraces) {
	this.actualTrxTraces = actualTrxTraces;
    }

    /**
     * 
     * @return
     */
    public AuditTransaction getActualTrx() {
	return actualTrx;
    }

    /**
     * 
     * @param actualTrx
     */
    public void setActualTrx(AuditTransaction actualTrx) {
	this.actualTrx = actualTrx;
    }

    /**
     * 
     * @return
     */
    public org.richfaces.model.selection.Selection getTransactionSelected() {
	return transactionSelected;
    }

    /**
     * 
     * @param transactionSelected
     */
    public void setTransactionSelected(org.richfaces.model.selection.Selection transactionSelected) {
	this.transactionSelected = transactionSelected;
    }

    /**
     * 
     * @return
     */
    public List<AuditTransaction> getTransactions() {
	return transactions;
    }

    /**
     * 
     * @param transactions
     */
    public void setTransactions(List<AuditTransaction> transactions) {
	this.transactions = transactions;
    }

    /**
     * 
     * @return
     */
    public List<SelectItem> getServices() {

	List<SelectItem> l = new ArrayList<SelectItem>();
	AuditService[ ] services = auditManager.getServices();

	for (int i = 0; i < services.length; i++) {
	    SelectItem item = new SelectItem();
	    item.setDescription(services[i].getDescription());
	    item.setLabel(services[i].getDescription());
	    item.setValue(services[i].getServiceId());
	    l.add(item);

	    if ((i == 0) & (service == -9)) {
		service = services[i].getServiceId();
	    }
	}
	return l;
    }

    /**
     * 
     * @return
     */
    public int getConsultType() {
	return consultType;
    }

    /**
     * 
     * @param consultType
     */
    public void setConsultType(int consultType) {
	this.consultType = consultType;
    }

    /**
     * 
     * @return
     */
    public Date getIniDate() {
	return iniDate;
    }

    /**
     * 
     * @param iniDate
     */
    public void setIniDate(Date iniDate) {
	this.iniDate = iniDate;
    }

    /**
     * 
     * @return
     */
    public Date getEndDate() {
	return endDate;
    }

    /**
     * 
     * @param endDate
     */
    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    /**
     * 
     * @return
     */
    public String getIdApp() {
	return idApp;
    }

    /**
     * 
     * @param idApp
     */
    public void setIdApp(String idApp) {
	this.idApp = idApp;
    }

    /**
     * 
     * @return
     */
    public int getService() {
	return service;
    }

    /**
     * 
     * @param service
     */
    public void setService(int service) {
	this.service = service;
    }

    /**
     * 
     * @return
     */
    public String getIdTrans() {
	return idTrans;
    }

    /**
     * 
     * @param idTrans
     */
    public void setIdTrans(String idTrans) {
	this.idTrans = idTrans;
    }

    /**
     * 
     * @return
     */
    public String onFind() {
	if (consultType == CONSULT_TYPE_DATE) {

	    try {
		transactions = auditManager.getTransactions(iniDate, endDate, service, idApp, null, 500);
	    } catch (AuditException e) {
		addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG126), e.getDescription());
		return null;
	    }

	    return null;
	}

	if (consultType == CONSULT_TYPE_IDTRANS) {

	    try {

		Long trxId = Long.valueOf(idTrans);
		AuditTransaction trx = auditManager.getTransaction(trxId);
		if (trx == null) {
		    transactions = null;
		    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG127), Language.getMessage(LanguageKeys.WMSG128) + trxId);

		    return null;
		} else {
		    transactions = new ArrayList<AuditTransaction>();
		    transactions.add(trx);
		}
	    } catch (AuditException e) {
		addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG126), e.getDescription());
		return null;
	    } catch (NumberFormatException e) {
		addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG130), Language.getMessage(LanguageKeys.WMSG131));
		return null;
	    }
	    return null;
	}

	return null;
    }

   
    /**
     * 
     * @param event
     */
    public void onSelectTrx(ActionEvent event) {

	FacesContext context = FacesContext.getCurrentInstance();
	HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
	String idTrx = request.getParameter(PARAM1);

	try {
	    actualTrx = auditManager.getTransaction(Long.parseLong(idTrx));

	    // Crear el arbol de trazas
	    actualTrxTraces = new TreeNodeImpl<NodeData>();
	    NodeData nd = new NodeData(Language.getMessage(LanguageKeys.WMSG132), NodeData.NODETYPE_ROOT);
	    actualTrxTraces.setData(nd);

	    // Recorrer las operationes
	    AuditOperation[ ] operations = auditManager.getOperations();

	    Iterator<TraceI> itTraces = actualTrx.getTraces().iterator();

	    int count = 0;
	    while (itTraces.hasNext()) {

		TraceI trace = (TraceI) itTraces.next();

		boolean end = false;
		int i = 0;

		while ((i < operations.length) && (!end)) {
		    if (operations[i].getOperationId() == trace.getActionId()) {
			end = true;
		    }
		    if (!end) {
			i++;
		    }
		}

		if (end) {
		    TreeNode<NodeData> node_operation = new TreeNodeImpl<NodeData>();
		    nd = new NodeData(operations[i].getDescription() + " (" + trace.getCreationTime() + ")", NodeData.NODETYPE_OPERATION);
		    node_operation.setData(nd);

		    List<String> traceFieldsKeys = new ArrayList<String>(trace.getFields().keySet());
		    int countFields = 0;
		    for (Iterator<String> iterator = traceFieldsKeys.iterator(); iterator.hasNext();) {

			String key = (String) iterator.next();

			TreeNode<NodeData> node_field = new TreeNodeImpl<NodeData>();

			int listType = 0;

			List<TraceFieldValue> value = trace.getFields().get(key);

			listType = 1;
			if (value.get(0).getFieldValueType() != null) {
			    listType = 3;
			}

			if ((value.size() > 1) && (listType != 3)) {
			    listType = 2;
			}

			if (listType == 1) {
				if(key.equals(TraceI.TEMPLATE_TYPE)){
					String valueDes = "";

					if(String.valueOf(TemplateConfigurationFacadeI.PDF_REPORT).equals(value.get(0).getFieldValue())){
						valueDes = "PDF";
					}
					nd = new NodeData(getFieldDescription(key) + "=" + valueDes, NodeData.NODETYPE_FIELD);
				}else{
					nd = new NodeData(getFieldDescription(key) + "=" + value.get(0).getFieldValue(), NodeData.NODETYPE_FIELD);
				}
			    
			    node_field.setData(nd);
			    node_operation.addChild(countFields, node_field);
			    countFields++;
			}

			if (listType == 2) {
			    nd = new NodeData(getFieldDescription(key), NodeData.NODETYPE_FIELD);
			    node_field.setData(nd);
			    int countChildValues = 0;

			    for (Iterator<TraceFieldValue> iteratorValues = value.iterator(); iteratorValues.hasNext();) {
				TreeNode<NodeData> node_value = new TreeNodeImpl<NodeData>();
				TraceFieldValue traceFieldValue = (TraceFieldValue) iteratorValues.next();
				nd = new NodeData(traceFieldValue.getFieldValue(), NodeData.NODETYPE_FIELD);
				node_value.setData(nd);
				node_field.addChild(countChildValues, node_value);
				countChildValues++;
			    }

			    node_operation.addChild(countFields, node_field);
			    countFields++;
			}

			if (listType == 3) {
			    nd = new NodeData(getFieldDescription(key), NodeData.NODETYPE_FIELD);
			    node_field.setData(nd);
			    int countChildValues = 0;

			    for (Iterator<TraceFieldValue> iteratorValues = value.iterator(); iteratorValues.hasNext();) {
				TreeNode<NodeData> node_value = new TreeNodeImpl<NodeData>();
				TraceFieldValue traceFieldValue = (TraceFieldValue) iteratorValues.next();
				nd = new NodeData(traceFieldValue.getFieldValueType() + "=" + traceFieldValue.getFieldValue(), NodeData.NODETYPE_FIELD);
				node_value.setData(nd);
				node_field.addChild(countChildValues, node_value);
				countChildValues++;
			    }

			    node_operation.addChild(countFields, node_field);
			    countFields++;
			}

		    }

		    actualTrxTraces.addChild(count, node_operation);
		    count++;
		}

	    }

	} catch (NumberFormatException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG133), e.getMessage());

	} catch (AuditException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG133), e.getMessage());

	}

    }

    /**
     * 
     * @param fieldId
     * @return
     */
    private String getFieldDescription(String fieldId) {

	String desc = null;

	AuditField[ ] fields = auditManager.getFields();

	int i = 0;

	while ((i < fields.length) && (desc == null)) {
	    if (fields[i].getFieldId().equals(fieldId)) {
		desc = fields[i].getDescription();
	    }

	    i++;
	}

	return desc;

    }

}
