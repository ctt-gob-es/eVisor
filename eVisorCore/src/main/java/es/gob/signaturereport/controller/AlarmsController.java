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
 * <b>File:</b><p>es.gob.signaturereport.controller.AlarmsController.java.</p>
 * <b>Description:</b><p>Class that manages </p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>07/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 07/02/2011.
 */
package es.gob.signaturereport.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.component.UIScrollableDataTable;

import es.gob.signaturereport.configuration.access.AlarmConfiguration;
import es.gob.signaturereport.configuration.access.AlarmConfigurationFacadeI;
import es.gob.signaturereport.configuration.items.Alarm;
import es.gob.signaturereport.configuration.items.AlarmIdentifier;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.persistence.exception.ConfigurationException;

/** 
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 11/10/2011.
 */
@Named("alarms")
@SessionScoped
public class AlarmsController extends AbstractController {

    /**
	 * Attribute that represents class version. 
	 */
	private static final long serialVersionUID = -5248207058621873146L;
	/**
     * Attribute that represents . 
     */
    Alarm alarmEdit;
    /**
     * Attribute that represents . 
     */
    String recEdit;
    /**
     * Attribute that represents . 
     */
    org.richfaces.model.selection.Selection alarmSelected;
    /**
     * Attribute that represents . 
     */
    org.richfaces.model.selection.Selection recSelected;

    /**
     * Attribute that represents . 
     */
    private UIScrollableDataTable recTable;
    
    /**
     * Attribute that represents the injected instance of the alarm configuration facade. 
     */
    @Inject @AlarmConfiguration
    private AlarmConfigurationFacadeI alarmConfig;

    /**
     * 
     * @return
     */
    public UIScrollableDataTable getRecTable() {
	return recTable;
    }

    /**
     * 
     * @param recTable
     */
    public void setRecTable(UIScrollableDataTable recTable) {
	this.recTable = recTable;
    }

    /**
     * 
     * @return
     */
    public String getRecEdit() {
	return recEdit;
    }

    /**
     * 
     * @param recEdit
     */
    public void setRecEdit(String recEdit) {
	this.recEdit = recEdit;
    }

    /**
     * 
     * @return
     */
    public org.richfaces.model.selection.Selection getRecSelected() {
	return recSelected;
    }

    /**
     * 
     * @param recSelected
     */
    public void setRecSelected(org.richfaces.model.selection.Selection recSelected) {
	this.recSelected = recSelected;
    }

    /**
     * 
     * @return
     */
    public AlarmIdentifier[ ] getAlarmsList() {

	try {
	    return alarmConfig.getSystemAlarms();
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG027), e.getMessage());
	}

	return null;
    }

    /**
     * 
     * @return
     */
    public Alarm getAlarmEdit() {
	return alarmEdit;
    }

    /**
     * 
     * @param alarmEdit
     */
    public void setAlarmEdit(Alarm alarmEdit) {
	this.alarmEdit = alarmEdit;
    }

    /**
     * 
     * @return
     */
    public org.richfaces.model.selection.Selection getAlarmSelected() {
	return alarmSelected;
    }

    /**
     * 
     * @param alarmSelected
     */
    public void setAlarmSelected(org.richfaces.model.selection.Selection alarmSelected) {
	this.alarmSelected = alarmSelected;
    }

    /**
     * 
     * @return
     */
    public String onClickAlarmList() {
	HttpServletRequest request = (HttpServletRequest) getContext().getExternalContext().getRequest();
	String alarmId = request.getParameter(PARAM1);

	try {
	    alarmEdit = alarmConfig.getAlarm(alarmId);
	    recEdit = null;
	    setStatus(STATUS_EDIT);
	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG028), e.getMessage());
	}

	return null;
    }

    /**
     * 
     * @return
     */
    public String onClickRecList() {
	HttpServletRequest request = (HttpServletRequest) getContext().getExternalContext().getRequest();
	String rec = request.getParameter(PARAM1);
	recEdit = rec;

	return null;
    }

    /**
     * 
     * @return
     */
    public String onAddRec() {

	if (recEdit == null) {
	    recEdit = "";
	}
	recEdit = recEdit.trim();
	recEdit = recEdit.toLowerCase();

	if (recEdit.equals("")) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG029), Language.getMessage(LanguageKeys.WMSG030));
	    return null;
	}

	Pattern pattern;
	Matcher matcher;
	pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	matcher = pattern.matcher(recEdit);

	if (!matcher.matches()) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG029), Language.getMessage(LanguageKeys.WMSG031));
	    return null;
	}

	if (alarmEdit.getReceivers().contains(recEdit)) {
	    addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG029), Language.getMessage(LanguageKeys.WMSG032));
	} else {

	    List<String> aList = new ArrayList<String>();
	    aList.addAll(alarmEdit.getReceivers());
	    aList.add(recEdit);
	    alarmEdit.setReceivers(aList);
	}

	return null;
    }

    /**
     * 
     * @return
     */
    public String onModifyAlarm() {
	try {
	    alarmConfig.setAlarmConfiguration(alarmEdit.getIdentifier().getAlarmId(), alarmEdit.getReceivers(), alarmEdit.isLock(), new Long(alarmEdit.getStandbyPeriod()).intValue());
	    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG033), Language.getMessage(LanguageKeys.WMSG034));
	    recEdit = "";

	} catch (ConfigurationException e) {
	    addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG035), e.getMessage());
	}
	return null;
    }

    /**
     * 
     * @return
     */
    public String onDelRec() {

	Iterator<Object> iterator = getRecSelected().getKeys();

	if (!iterator.hasNext()) {
	    addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG036), Language.getMessage(LanguageKeys.WMSG037));
	    return null;
	}

	while (iterator.hasNext()) {
	    Object key = iterator.next();
	    recTable.setRowKey(key);
	    if (recTable.isRowAvailable()) {
		String del = (String) recTable.getRowData();

		List<String> aList = new ArrayList<String>();
		aList.addAll(alarmEdit.getReceivers());
		aList.remove(del);
		alarmEdit.setReceivers(aList);

	    }
	}

	return null;
    }
}
