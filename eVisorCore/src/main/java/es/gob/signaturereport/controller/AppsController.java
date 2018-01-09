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
 * <b>File:</b><p>es.gob.signaturereport.controller.AppsController.java.</p>
 * <b>Description:</b><p>Class that manages </p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>07/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 07/02/2011.
 */
package es.gob.signaturereport.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.richfaces.component.UIScrollableDataTable;
import org.richfaces.component.UITree;
import org.richfaces.event.AjaxSelectedEvent;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;

import es.gob.signaturereport.configuration.access.AfirmaConfiguration;
import es.gob.signaturereport.configuration.access.AfirmaConfigurationFacadeI;
import es.gob.signaturereport.configuration.access.ApplicationConfiguration;
import es.gob.signaturereport.configuration.access.ApplicationConfigurationFacadeI;
import es.gob.signaturereport.configuration.access.KeystoreConfiguration;
import es.gob.signaturereport.configuration.access.KeystoreConfigurationFacadeI;
import es.gob.signaturereport.configuration.access.TemplateConfiguration;
import es.gob.signaturereport.configuration.access.TemplateConfigurationFacadeI;
import es.gob.signaturereport.configuration.access.UserConfiguration;
import es.gob.signaturereport.configuration.access.UserConfigurationFacadeI;
import es.gob.signaturereport.configuration.items.ApplicationData;
import es.gob.signaturereport.configuration.items.CertificateItem;
import es.gob.signaturereport.configuration.items.KeystoreItem;
import es.gob.signaturereport.configuration.items.Person;
import es.gob.signaturereport.configuration.items.UnitOrganization;
import es.gob.signaturereport.controller.list.ComparatorName;
import es.gob.signaturereport.controller.list.ComparatorNameUO;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.persistence.exception.ConfigurationException;

/** 
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 11/10/2011.
 */
@Named("apps")
@SessionScoped
public class AppsController extends AbstractController {
    
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 4301688082980658874L;

	String aliasAuthCert = null;
    

    
    public String getAliasAuthCert() {
        return aliasAuthCert;
    }

    
    public void setAliasAuthCert(String aliasAuthCert) {
        this.aliasAuthCert = aliasAuthCert;
    }

    /**
     * Attribute that represents . 
     */
    UnitOrganization uoEdit = null;

    /**
     * Attribute that represents . 
     */
    ApplicationData appEdit = null;

    /**
     * Attribute that represents . 
     */
    String appEditId = null;

    /**
     * Attribute that represents . 
     */
    String st_ini = null;
    /**
     * Attribute that represents . 
     */
    String st_end = null;

    /**
     * Attribute that represents . 
     */
    String templateAdd = null;

    /**
     * Attribute that represents . 
     */
    int statusPerson = STATUS_NONE;

    /**
     * Attribute that represents . 
     */
    TreeNode<NodeData> realRoot = new TreeNodeImpl<NodeData>();

    /**
     * Attribute that represents . 
     */
    Person personEdit = null;
    /**
     * Attribute that represents . 
     */
    org.richfaces.model.selection.Selection personSelected;
    /**
     * Attribute that represents . 
     */
    UIScrollableDataTable personTable;

    /**
     * Attribute that represents . 
     */
    org.richfaces.model.selection.Selection templateSelected;
    /**
     * Attribute that represents . 
     */
    UIScrollableDataTable templatesTable;
    
    /**
     * Attribute that represents . 
     */
    @Inject @TemplateConfiguration
    protected TemplateConfigurationFacadeI confTemplate;
    
    /**
     * Attribute that represents . 
     */
    @Inject @UserConfiguration
    protected UserConfigurationFacadeI confUser;
    
    /**
     * Attribute that represents . 
     */
    @Inject @ApplicationConfiguration
    protected ApplicationConfigurationFacadeI confApps;
    
    /**
     * Attribute that represents . 
     */
    @Inject @AfirmaConfiguration
    protected AfirmaConfigurationFacadeI confAfirma;
    
    /**
     * Attribute that represents . 
     */
    @Inject @KeystoreConfiguration
    protected KeystoreConfigurationFacadeI confKS;

    /**
     * 
     * @return
     */
    public org.richfaces.model.selection.Selection getTemplateSelected() {
	return templateSelected;
    }

    /**
     * 
     * @param templateSelected
     */
    public void setTemplateSelected(org.richfaces.model.selection.Selection templateSelected) {
	this.templateSelected = templateSelected;
    }

    /**
     * 
     * @return
     */
    public UIScrollableDataTable getTemplatesTable() {
	return templatesTable;
    }

    /**
     * 
     * @param templatesTable
     */
    public void setTemplatesTable(UIScrollableDataTable templatesTable) {
	this.templatesTable = templatesTable;
    }

    /**
     * 
     * @return
     */
    public String getSt_ini() {
	return st_ini;
    }

    /**
     * 
     * @param st_ini
     */
    public void setSt_ini(String st_ini) {
	this.st_ini = st_ini;
    }

    /**
     * 
     * @return
     */
    public String getSt_end() {
	return st_end;
    }

    /**
     * 
     * @param st_end
     */
    public void setSt_end(String st_end) {
	this.st_end = st_end;
    }

    /**
     * 
     * @return
     */
    public int getStatusPerson() {
	return statusPerson;
    }

    /**
     * 
     * @param statusPerson
     */
    public void setStatusPerson(int statusPerson) {
	this.statusPerson = statusPerson;
    }

    /**
     * 
     * @return
     */
    public org.richfaces.model.selection.Selection getPersonSelected() {
	return personSelected;
    }

    /**
     * 
     * @param personSelected
     */
    public void setPersonSelected(org.richfaces.model.selection.Selection personSelected) {
	this.personSelected = personSelected;
    }

    /**
     * 
     * @return
     */
    public UIScrollableDataTable getPersonTable() {
	return personTable;
    }

    /**
     * 
     * @param personTable
     */
    public void setPersonTable(UIScrollableDataTable personTable) {
	this.personTable = personTable;
    }

    /**
     * 
     * @return
     */
    public Person getPersonEdit() {
	if (personEdit == null) {
	    personEdit = new Person();
	}
	return personEdit;
    }

    /**
     * 
     * @param personEdit
     */
    public void setPersonEdit(Person personEdit) {
	if (personEdit == null) {
	    personEdit = new Person();
	}
	this.personEdit = personEdit;
    }

    /**
     * 
     * @return
     */
    public String getAppEditId() {
	return appEditId;
    }

    /**
     * 
     * @param appEditId
     */
    public void setAppEditId(String appEditId) {
	this.appEditId = appEditId;
    }

    /**
     * 
     * @return
     */
    public UnitOrganization getUoEdit() {
	return uoEdit;
    }

    /**
     * 
     * @param uoEdit
     */
    public void setUoEdit(UnitOrganization uoEdit) {
	this.uoEdit = uoEdit;
    }

    /**
     * 
     * @return
     */
    public String onNewUo() {

	if (st_end == null) {
	    st_ini = null;
	} else {
	    st_ini = uoEdit.getUnitId().concat(".");
	}

	st_end = "";

	uoEdit = new UnitOrganization();
	
	setStatus(STATUS_NEW_UO);
	return null;
    }

    /**
     * 
     * @return
     */
    public String onNewUoAcept() {

	String uoId = null;

	if (st_ini == null) {
	    uoId = st_end;
	} else {
	    uoId = st_ini.concat(st_end);
	}

	st_end = st_end.trim();

	if ((!uoId.equalsIgnoreCase("")) && (!st_end.matches(ID_REG_EXP))) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG038), Language.getMessage(LanguageKeys.WMSG039));
	    return null;
	}

	if (uoEdit.getUnitName() == null) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG038), Language.getMessage(LanguageKeys.WMSG040));
	    return null;
	}

	uoEdit.setUnitName(uoEdit.getUnitName().trim());
	if (uoEdit.getUnitName().equals("")) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG038), Language.getMessage(LanguageKeys.WMSG040));
	    return null;
	}

	try {
	    confApps.createUO(uoId, uoEdit.getUnitName());
	    uoEdit.setUnitId(uoId);
	    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG042), Language.getMessage(LanguageKeys.WMSG041) + uoId);
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG043), e.getDescription());
	    return null;
	}
	setStatus(STATUS_EDIT_UO);
	return null;

    }

    /**
     * 
     * @return
     */
    public String onNewApp() {

	if (st_end == null) {
	    st_ini = "";
	} else {
	    st_ini = uoEdit.getUnitId().concat(".");
	}

	st_end = "";

	appEdit = new ApplicationData();
	personEdit = null;

	setStatus(STATUS_NEW_APP);
	return null;
    }

    /**
     * 
     * @return
     */
    public String onNewAppAcept() {

	try {
	    // TODO VALIDACIONES
	    String appId = (st_ini.concat(st_end)).trim();
	    st_end = st_end.trim();

	    // TODO Comprobar que se ha seleccionado un certificado en
	    // autenticacion X509

	    if ((appId.equalsIgnoreCase(""))) {
		addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG043), Language.getMessage(LanguageKeys.WMSG044));
		return null;
	    }
	    if ((!appId.equalsIgnoreCase("")) && (!st_end.matches(ID_REG_EXP))) {
		addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG043), Language.getMessage(LanguageKeys.WMSG039));
		return null;
	    }
	    confApps.createApplication(appId, appEdit);
	    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG042), Language.getMessage(LanguageKeys.WMSG045) + appId);

	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG043), e.getDescription());
	    return null;
	}
	setStatus(STATUS_EDIT_APP);
	return null;
    }

    /**
     * 
     * @return
     */
    public String onModifyAppAcept() {
	
	
	try {
	   
	    // TODO VALIDACIONES
	    String appId = st_ini.concat(st_end);

	    confApps.modifyApplication(appId, appEdit);

	    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG046), Language.getMessage(LanguageKeys.WMSG047) + appId);

	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG048), e.getDescription());
	    return null;
	}
	setStatus(STATUS_EDIT_APP);
	return null;
    }

    /**
     * 
     * @return
     */
    public String onModUo() {

	try {

	    if ((uoEdit.getUnitName() == null)) {
		addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG049), Language.getMessage(LanguageKeys.WMSG040));
		return null;
	    }

	    uoEdit.setUnitName(uoEdit.getUnitName().trim());

	    if ((uoEdit.getUnitName().equals(""))) {
		addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG049), Language.getMessage(LanguageKeys.WMSG040));
		return null;
	    }

	    confApps.modifyUO(uoEdit.getUnitId(), uoEdit.getUnitName());

	    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG050), Language.getMessage(LanguageKeys.WMSG071) + uoEdit.getUnitId());

	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG049), e.getDescription());
	    return null;
	}

	return null;
    }

    /**
     * 
     * @return
     */
    public ApplicationData getAppEdit() {

	return appEdit;
    }

    /**
     * 
     * @param appEdit
     */
    public void setAppEdit(ApplicationData appEdit) {
	this.appEdit = appEdit;
    }

    /**
     * 
     * @param event
     */
    public void selectionTreeListener(org.richfaces.event.NodeSelectedEvent event) {
    	if(event instanceof AjaxSelectedEvent){
    		 UITree tree = (UITree) event.getComponent();
    			NodeData node = ((NodeData) tree.getRowData());

    			st_ini = null;
    			st_end = null;

    			setStatus(STATUS_NONE);
    			statusPerson = STATUS_NONE;
    			appEdit = null;
    			uoEdit = null;
    			personEdit = null;

    			if (node.getType().equals(NodeData.NODETYPE_UO)) {
    			    try {
    				uoEdit = confApps.getUnitOrganization(node.getId(), false);
    			    } catch (ConfigurationException e) {
    				addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG051), e.getDescription());
    			    }
    			    
    			    setStatus(STATUS_EDIT_UO);
    			    if (uoEdit.getUnitId().lastIndexOf(".") == -1) {
    				st_end = uoEdit.getUnitId();
    				st_ini = "";

    			    } else {
    				
    				st_ini = "";
    				
    				StringTokenizer st = new StringTokenizer(uoEdit.getUnitId(), ".");

    				int num = st.countTokens();
    				for (int i = 1; i < num; i++) {
    				    st_ini = st_ini.concat(st.nextToken().concat("."));

    				}
    				st_end = st.nextToken();
    			    }
    			}

    			if (node.getType().equals(NodeData.NODETYPE_APP)) {
    			    try {
    				appEdit = confApps.getApplicationData(node.getId());
    			    } catch (ConfigurationException e) {
    				addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG052) + node.getId(), e.getDescription());
    			    }
    			    personEdit = appEdit.getManager();
    			    appEditId = node.getId();
    			    
    			    setStatus(STATUS_EDIT_APP);
    			    if (appEditId.lastIndexOf('.') == -1) {
    				st_end = appEditId;
    				st_ini = "";

    			    } else {
    				st_ini = "";
    				StringTokenizer st = new StringTokenizer(appEditId, ".");

    				int num = st.countTokens();
    				for (int i = 1; i < num; i++) {
    				    st_ini = st_ini.concat(st.nextToken().concat("."));
    				}
    				st_end = st.nextToken();
    			    }

    			}
    	}
    }

    /**
     * 
     * @return
     */
	public TreeNode<NodeData> getRealRoot() {

		realRoot = new TreeNodeImpl<NodeData>();

	
		NodeData nd = new NodeData(
				ApplicationConfigurationFacadeI.ROOT_UNIT_ID,
				NodeData.NODETYPE_ROOT);
	
		nd.setId(ApplicationConfigurationFacadeI.ROOT_UNIT_ID);
		realRoot.setData(nd);

		TreeNodeImpl<NodeData> tree_root = new TreeNodeImpl<NodeData>();
		tree_root.addChild(0, realRoot);

		UnitOrganization uo_root = null;
		try {
		
			uo_root = confApps.getUnitOrganization(
					ApplicationConfigurationFacadeI.ROOT_UNIT_ID, true);
		} catch (ConfigurationException e) {
			addMessage(FacesMessage.SEVERITY_ERROR,
					Language.getMessage(LanguageKeys.WMSG053),
					e.getDescription());
		}

		// Obtener las unidades hijas
		getTreeUO(uo_root, realRoot);

		return tree_root;
	}

    /**
     * 
     * @param uo_root
     * @param treeData
     */
    private void getTreeUO(UnitOrganization uo_root, TreeNode<NodeData> treeData) {

	if (uo_root.getSubunits().size() > 0) {

	    // Ordenar el LinkedHashMap
	    HashMap<String, UnitOrganization> orderUOs = new LinkedHashMap<String, UnitOrganization>();
	    List<String> mapKeys = new ArrayList<String>(uo_root.getSubunits().keySet());
	    List<UnitOrganization> mapValues = new ArrayList<UnitOrganization>(uo_root.getSubunits().values());

	    // TreeSet<UnitOrganization> conjuntoOrdenado = new
	    // TreeSet<UnitOrganization>(mapValues);
	    TreeSet<UnitOrganization> orderSet = new TreeSet<UnitOrganization>(new ComparatorNameUO());
	    orderSet.addAll(mapValues);

	    Object[ ] orderArray = orderSet.toArray();
	    int size = orderArray.length;
	    for (int i = 0; i < size; i++) {
		orderUOs.put(mapKeys.get(mapValues.indexOf(orderArray[i])), (UnitOrganization) orderArray[i]);
	    }

	    Iterator<String> itUo = orderUOs.keySet().iterator();

	    while (itUo.hasNext()) {

		String idUo = (String) itUo.next();
		UnitOrganization uo = (UnitOrganization) orderUOs.get(idUo);

		TreeNode<NodeData> nodeUo = new TreeNodeImpl<NodeData>();

		NodeData nd = new NodeData(uo.getUnitName(), NodeData.NODETYPE_UO);
		nd.setId(uo.getUnitId());
		nodeUo.setData(nd);

		getTreeUO(uo, nodeUo);

		treeData.addChild(idUo, nodeUo);

	    }
	}

	if (uo_root.getApplications().size() > 0) {
	    // Ordenar el LinkedHashMap
	    HashMap<String, String> orderApps = new LinkedHashMap<String, String>();
	    List<String> mapKeys = new ArrayList<String>(uo_root.getApplications().keySet());
	    List<String> mapValues = new ArrayList<String>(uo_root.getApplications().values());

	    // TreeSet<String> orderSet = new TreeSet<String>(mapValues);
	    TreeSet<String> orderSet = new TreeSet<String>(new ComparatorName());
	    orderSet.addAll(mapValues);

	    Object[ ] orderArray = orderSet.toArray();

	    int size = orderArray.length;
	    for (int i = 0; i < size; i++) {
		orderApps.put(mapKeys.get(mapValues.indexOf(orderArray[i])), (String) orderArray[i]);
	    }

	    Iterator<String> itApp = orderApps.keySet().iterator();
	    while (itApp.hasNext()) {

		String idApp = (String) itApp.next();
		String nameApp = (String) orderApps.get(idApp);

		TreeNode<NodeData> nodeApp = new TreeNodeImpl<NodeData>();
		NodeData nd = new NodeData(nameApp, NodeData.NODETYPE_APP);
		nd.setId(idApp);
		nodeApp.setData(nd);

		treeData.addChild(idApp, nodeApp);
	    }
	}

    }

    /**
     * 
     * @return
     */
    public String onDeleteUO() {

    	try {
    	    confApps.deleteUO(uoEdit.getUnitId());
    	    setStatus( STATUS_NONE);
    	    statusPerson = STATUS_NONE;
    	    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG054), Language.getMessage(LanguageKeys.WMSG055) + uoEdit.getUnitId());
    	    uoEdit = null;
    	    st_ini = null;
    	    st_end = null;
    	    return null;
    	} catch (ConfigurationException e) {
    	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG056), e.getMessage());
    	    return null;
    	}
    }

    /**
     * 
     * @return
     */
    public String onDeleteApp() {

	try {
	    confApps.removeApplication(appEditId);
	    appEdit = null;
	    personEdit = null;
	    setStatus(STATUS_NONE);
	    statusPerson = STATUS_NONE;
	    st_end = null;
	    st_ini = null;
	    uoEdit =null;
	    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG057), Language.getMessage(LanguageKeys.WMSG058) + appEditId);
	    appEditId = null;
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG059), e.getMessage());
	    return null;
	}
	return null;
    }

    /**
     * 
     * @return
     */
    public List<SelectItem> getCertsWS() {
	List<SelectItem> resultList = new ArrayList<SelectItem>();

	KeystoreItem keystore;
	try {
	    keystore = confKS.getKeystore("AUTORIZACION WS");
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG060), e.getMessage());
	    return null;
	}
	LinkedHashMap<String, CertificateItem> certificatesId = keystore.getCertificates();

	Collection<String> c = certificatesId.keySet();

	// obtain an Iterator for Collection
	Iterator<String> itr = c.iterator();
	while (itr.hasNext()) {
	    String key = (String) itr.next();
	    SelectItem item = new SelectItem(key, key, key);
	    resultList.add(item);
	}

	return resultList;
    }

    /**
     * 
     * @return
     */
    public List<SelectItem> getTemplates() {
	List<SelectItem> resultList = new ArrayList<SelectItem>();

	try {
	    TreeMap<String, Integer> mapSort = new TreeMap<String, Integer>();
	    mapSort.putAll(confTemplate.getTemplateIds());

	    Collection<String> c = mapSort.keySet();

	    // obtain an Iterator for Collection
	    Iterator<String> itr = c.iterator();

	    // iterate through LinkedHashMap values iterator
	    while (itr.hasNext()) {
		String key = (String) itr.next();
		// Integer value = templatesId.get(key);
		resultList.add(new SelectItem(key, key));

	    }

	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG061), e.getMessage());
	    return null;
	}

	return resultList;
    }

    /**
     * 
     * @return
     */
    public String getTemplateAdd() {
	return templateAdd;
    }

    /**
     * 
     * @param templateAdd
     */
    public void setTemplateAdd(String templateAdd) {
	this.templateAdd = templateAdd;
    }

    /**
     * 
     * @return
     */
    public List<SelectItem> getPlatforms() {
	List<SelectItem> resultList = new ArrayList<SelectItem>();

	String[ ] platforms;
	try {
	    platforms = confAfirma.getAfirmaIds();
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG062), e.getMessage());
	    return null;
	}

	// obtain an Iterator for Collection

	for (int i = 0; i < platforms.length; i++) {
	    String key = platforms[i];
	    SelectItem item = new SelectItem(key, key);
	    resultList.add(item);
	}

	return resultList;
    }

    /**
     * 
     * @return
     */
    public Person[ ] getPersonsList() {
	try {
	    return confUser.getAllPersons();
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG063), e.getMessage());

	}
	return null;
    }

    /**
     * 
     * @return
     */
    public String onStartSelectPerson() {
	statusPerson = STATUS_NONE;
	return null;
    }

    /**
     * 
     * @return
     */
    public String onStartNewPerson() {
	personEdit = new Person();
	statusPerson = STATUS_NEW;
	return null;
    }

    /**
     * 
     * @return
     */
    public String onStartModifyPerson() {

	if (personSelected == null) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG064), Language.getMessage(LanguageKeys.WMSG065));
	    return null;
	}

	if (personSelected.size() < 1) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG064), Language.getMessage(LanguageKeys.WMSG065));
	    return null;
	}

	Long personId = new Long(0);
	Iterator<Object> iterator = getPersonSelected().getKeys();
	while (iterator.hasNext()) {
	    Object key = iterator.next();
	    personTable.setRowKey(key);
	    if (personTable.isRowAvailable()) {
		personId = ((Person) personTable.getRowData()).getPersonId();
	    }
	}

	Person[ ] pList = getPersonsList();
	boolean end = false;
	int i = 0;
	Person pd = null;

	while ((i < pList.length) && (!end)) {

	    if (pList[i].getPersonId().intValue() == personId.intValue()) {

		pd = pList[i];
		end = true;
	    }
	    i++;
	}
	personEdit = pd;

	statusPerson = STATUS_EDIT;

	return null;
    }

    /**
     * 
     * @return
     */
    public String onAssignPerson() {

	if (personSelected == null) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG066), Language.getMessage(LanguageKeys.WMSG065));
	    return null;
	}

	if (personSelected.size() < 1) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG066), Language.getMessage(LanguageKeys.WMSG065));
	    return null;
	}

	Iterator<Object> iterator = getPersonSelected().getKeys();
	while (iterator.hasNext()) {
	    Object key = iterator.next();
	    personTable.setRowKey(key);
	    if (personTable.isRowAvailable()) {
		appEdit.setManager((Person) personTable.getRowData());
	    }
	}

	personEdit = appEdit.getManager();
	return null;
    }
    
    
    /**
     * 
     * @return
     */
    public String onDelPerson() {

	if (personSelected == null) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG163), Language.getMessage(LanguageKeys.WMSG065));
	    return null;
	}

	if (personSelected.size() < 1) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG163), Language.getMessage(LanguageKeys.WMSG065));
	    return null;
	}

	Iterator<Object> iterator = getPersonSelected().getKeys();
	while (iterator.hasNext()) {
	    Object key = iterator.next();
	    personTable.setRowKey(key);
	    if (personTable.isRowAvailable()) {
		try {
		    confUser.deleteUser(((Person) personTable.getRowData()).getPersonId());
		    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG164), "");
		} catch (ConfigurationException e) {
		    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG165), e.getDescription());
		}
		
	    }
	}
	
	personEdit = appEdit.getManager();
	return null;
    }

    /**
     * 
     * @return
     */
    public String onAceptModifyPersonAndAssign() {

	// TODO Validaciones y conversiones
	try {
	    confUser.modifyUser(personEdit.getPersonId(), personEdit.getName(), personEdit.getSurname(), personEdit.getPhone(), personEdit.getEmail());
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG067), e.getDescription());
	    return null;
	}
	appEdit.setManager(personEdit);
	statusPerson = STATUS_NONE;
	return null;
    }

    /**
     * 
     * @return
     */
    public String onAceptModifyPerson() {
	// TODO Validaciones y conversiones
	try {
	    confUser.modifyUser(personEdit.getPersonId(), personEdit.getName(), personEdit.getSurname(), personEdit.getPhone(), personEdit.getEmail());
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG067), e.getDescription());

	    personEdit = appEdit.getManager();
	    return null;

	}

	statusPerson = STATUS_NONE;
	return null;
    }

    /**
     * 
     * @return
     */
    public String onAceptNewPerson() {
	// TODO Validaciones y conversiones

	try {
	    confUser.addUser(personEdit.getName(), personEdit.getSurname(), personEdit.getPhone(), personEdit.getEmail());
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG068), e.getDescription());

	    personEdit = appEdit.getManager();
	    return null;
	}
	statusPerson = STATUS_NONE;
	return null;
    }

    /**
     * 
     * @return
     */
    public String onAceptNewPersonAndAssign() {
	// TODO Validaciones y conversiones
	try {
	    personEdit.setPersonId(confUser.addUser(personEdit.getName(), personEdit.getSurname(), personEdit.getPhone(), personEdit.getEmail()));
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG068), e.getDescription());
	    return null;
	}
	appEdit.setManager(personEdit);
	statusPerson = STATUS_NONE;
	return null;
    }

    /**
     * 
     * @return
     */
    public String onCancelActionPerson() {
	personEdit = appEdit.getManager();
	statusPerson = STATUS_NONE;
	return null;
    }

    /**
     * 
     * @return
     */
    public String onAddTemplate() {

	if (templateAdd == null) {
	    return null;
	}

	if (templateAdd.trim().equals("")) {
	    templateAdd = null;
	    return null;
	}

	if (appEdit.getTemplates().contains(templateAdd)) {
	    // No es necesaria esta alerta
	    // FacesMessage message = new
	    // FacesMessage(FacesMessage.SEVERITY_WARN,
	    // "La plantilla seleccionada ya se encuentra en la lista de plantillas.",
	    // null);
	    // context.addMessage(null, message);
	    templateAdd = null;
	    return null;
	}

	appEdit.getTemplates().add(templateAdd);

	templateAdd = null;
	return null;
    }

    /**
     * 
     * @return
     */
    public String onRemoveTemplate() {
	String template = null;

	if (templateSelected == null) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG069), Language.getMessage(LanguageKeys.WMSG070));
	    return null;
	}

	if (templateSelected.size() < 1) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG069), Language.getMessage(LanguageKeys.WMSG070));
	    return null;
	}

	Iterator<Object> iterator = getTemplateSelected().getKeys();
	while (iterator.hasNext()) {
	    Object key = iterator.next();
	    templatesTable.setRowKey(key);
	    if (templatesTable.isRowAvailable()) {
		template = ((String) templatesTable.getRowData());
	    }
	}

	appEdit.getTemplates().remove(template);

	return null;
    }

}
