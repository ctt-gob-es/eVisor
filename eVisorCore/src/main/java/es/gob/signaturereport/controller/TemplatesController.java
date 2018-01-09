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
 * <b>File:</b><p>es.gob.signaturereport.controller.TemplatesController.java.</p>
 * <b>Description:</b><p>Class that manages </p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>07/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 07/02/2011.
 */
package es.gob.signaturereport.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeSet;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.richfaces.component.UITree;
import org.richfaces.event.AjaxSelectedEvent;
import org.richfaces.event.UploadEvent;
import org.richfaces.model.TreeNode;
import org.richfaces.model.TreeNodeImpl;
import org.richfaces.model.UploadItem;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.gob.signaturereport.configuration.access.TemplateConfiguration;
import es.gob.signaturereport.configuration.access.TemplateConfigurationFacadeI;
import es.gob.signaturereport.configuration.items.TemplateData;
import es.gob.signaturereport.controller.list.ComparatorName;
import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.messages.transform.impl.DSSVerifyTransformI;
import es.gob.signaturereport.mfirma.responses.ValidationSignatureResponse;
import es.gob.signaturereport.modes.parameters.Barcode;
import es.gob.signaturereport.mreport.ReportException;
import es.gob.signaturereport.mreport.ReportManagerFactory;
import es.gob.signaturereport.mreport.ReportManagerI;
import es.gob.signaturereport.persistence.exception.ConfigurationException;
import es.gob.signaturereport.properties.SignatureReportPropertiesI;
import es.gob.signaturereport.properties.StaticSignatureReportProperties;
import es.gob.signaturereport.tools.FileUtils;
import es.gob.signaturereport.tools.ToolsException;
import es.gob.signaturereport.tools.XMLUtils;
import es.gob.signaturereport.utils.UtilsJBoss;

/** 
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 11/10/2011.
 */
@Named("templates")
@SessionScoped
public class TemplatesController extends AbstractController {

	/**
	 * Class version
	 */
	private static final long serialVersionUID = 685744462758356005L;

	private static final Logger LOGGER = Logger.getLogger(TemplatesController.class);

	/**
	 * Attribute that represents . 
	 */
	private TemplateData templateEdit;

	/**
	 * Attribute that represents . 
	 */
	private int selectOneModeImg = 1;

	/**
	 * Attribute that represents the identifier for the default test. 
	 */
	private static final int DEFAULT_TEST = 0;

	/**
	 * Attribute that represents the identifier for the advance test. 
	 */
	private static final int ADVANCE_TEST = 1;

	/**
	 * Attribute that represents the name of the "ValidationResult" element. 
	 */
	private static final String VALIDATION_RESULT_NAME = "ValidationResult";

	/**
	 * Attribute that represents the name of the "ExternalParameters" element. 
	 */
	private static final String EXT_PARAMS_NAME = "ExternalParameters";

	/**
	 * Attribute that represents the name of the "Result" element. 
	 */
	private static final String RESULT_NAME = "Result";

	/**
	 * Attribute that represents the name of the "Major" element. 
	 */
	private static final String RESULT_MAJOR_NAME = "Major";

	/**
	 * Attribute that represents the name of the "Minor" element. 
	 */
	private static final String RESULT_MINOR_NAME = "Minor";

	/**
	 * Attribute that represents the name of the "Message" element. 
	 */
	private static final String RESULT_MESSAGE_NAME = "Message";

	/**
	 * Attribute that represents the name of the "IndividualSignature" element. 
	 */
	private static final String IND_SIGNATURE_NAME = "IndividualSignature";

	/**
	 * Attribute that represents the name of the "TimeStamp" element. 
	 */
	private static final String TIMESTAMP_NAME = "TimeStamp";

	private List<SelectItem> defaultParameterNames;

	private List<SelectItem> defaultDocumentNames;

	/**
	 * Attribute that represents the name of the "CertificateInfo" element. 
	 */
	private static final String CERT_INFO_NAME = "CertificateInfo";

	/**
	 * Attribute that represents the name of the "FieldId" element. 
	 */
	private static final String FIELD_ID_NAME = "FieldId";

	/**
	 * Attribute that represents the name of the "FieldValue" element. 
	 */
	private static final String FIELD_VALUE_NAME = "FieldValue";

	/**
	 * Attribute that represents the path to "ParameterId" element. 
	 */
	private static final String PARAMETER_ID_NAME = "ParameterId";

	/**
	 * Attribute that represents the path to "ParameterValue" element. 
	 */
	private static final String PARAMETER_VALUE_NAME = "ParameterValue";

	/**
	 * Attribute that represents the path to "Type" element. 
	 */
	private static final String TYPE_NAME = "Type";

	/**
	 * Attribute that represents the path to "Message" element. 
	 */
	private static final String MESSAGE_NAME = "Message";

	/**
	 * Attribute that represents the path to "Configuration" element. 
	 */
	private static final String CONFIGURATION_NAME = "Configuration";

	/**
	 * Attribute that represents the "Barcodes" element. 
	 */
	private static final String BARCODES_NAME = "Barcodes";

	/**
	 * Attribute that represents the test mode. 
	 */
	private int testMode = DEFAULT_TEST;

	/**
	 * Attribute that represents the XML file used for testing the template. 
	 */
	private byte[ ] xmlTest = null;

	/**
	 * Attribute that represents the document file used for testing the template. 
	 */
	private byte[ ] docTest = null;

	/**
	 * Attribute that represents a default.parameter.names property defined in signaturereport.properties. 
	 */
	private static final String DEFAULT_PARAMETER_NAMES = "default.parameter.names";

	/**
	 * Attribute that represents a default.parameter.files property defined in signaturereport.properties. 
	 */
	private static final String DEFAULT_PARAMETER_FILES = "default.parameter.files";

	/**
	 * Attribute that represents a default.document.names property defined in signaturereport.properties. 
	 */
	private static final String DEFAULT_DOCUMENT_NAMES = "default.document.names";

	/**
	 * Attribute that represents a default.document.files property defined in signaturereport.properties. 
	 */
	private static final String DEFAULT_DOCUMENT_FILES = "default.document.files";

	/**
	 * Attribute that represents a content folder that contains default files used to test a template.
	 */
	private static final String DEFAULT_CONTENT_FOLDER = File.separator+"defaultContent"+File.separator;

	private String defaultSelectedParam;

	private String defaultSelectedDoc;
	
	/**
     * Attribute that represents . 
     */
    @Inject @TemplateConfiguration
    private TemplateConfigurationFacadeI confTemplate;

	public TemplatesController() {

		//SignatureReportProperties props = SignatureReportProperties.getInstance();
		loadDefaultParameterNames();
		loadDefaultDocumentNames();
	}

	private void loadDefaultParameterNames() {
		defaultParameterNames = new ArrayList<SelectItem>();
		String names = StaticSignatureReportProperties.getProperty(DEFAULT_PARAMETER_NAMES);
		String files = StaticSignatureReportProperties.getProperty(DEFAULT_PARAMETER_FILES);
		if (names != null && files != null && !names.isEmpty() && !files.isEmpty()) {
			String[ ] n = names.split(";");
			String[ ] f = files.split(";");
			int numElems = Math.min(n.length, f.length);
			for (int i = 0; i < numElems; i++) {
				defaultParameterNames.add(new SelectItem(f[i], n[i]));
			}
			defaultSelectedParam = (String) defaultParameterNames.get(0).getValue();
		}
	}

	private void loadDefaultDocumentNames() {
		defaultDocumentNames = new ArrayList<SelectItem>();
		String names = StaticSignatureReportProperties.getProperty(DEFAULT_DOCUMENT_NAMES);
		String files = StaticSignatureReportProperties.getProperty(DEFAULT_DOCUMENT_FILES);
		if (names != null && files != null && !names.isEmpty() && !files.isEmpty()) {
			String[ ] n = names.split(";");
			String[ ] f = files.split(";");
			int numElems = Math.min(n.length, f.length);
			for (int i = 0; i < numElems; i++) {
				defaultDocumentNames.add(new SelectItem(f[i], n[i]));
			}
			defaultSelectedDoc = (String) defaultDocumentNames.get(0).getValue();
		}
	}

	/**
	 * Gets the value of the attribute {@link #defaultSelectedParam}.
	 * @return the value of the attribute {@link #defaultSelectedParam}.
	 */
	public String getDefaultSelectedParam() {
		return defaultSelectedParam;
	}

	/**
	 * Gets the value of the attribute {@link #defaultParameterNames}.
	 * @return the value of the attribute {@link #defaultParameterNames}.
	 */
	public List<SelectItem> getDefaultParameterNames() {
		return defaultParameterNames;
	}

	/**
	 * Sets the value of the attribute {@link #defaultSelectedParam}.
	 * @param defaultSelectedParam The value for the attribute {@link #defaultSelectedParam}.
	 */
	public void setDefaultSelectedParam(String defaultSelectedParam) {
		this.defaultSelectedParam = defaultSelectedParam;
	}

	/**
	 * Gets the value of the attribute {@link #defaultSelectedDoc}.
	 * @return the value of the attribute {@link #defaultSelectedDoc}.
	 */
	public String getDefaultSelectedDoc() {
		return defaultSelectedDoc;
	}

	/**
	 * Sets the value of the attribute {@link #defaultSelectedDoc}.
	 * @param defaultSelectedDoc The value for the attribute {@link #defaultSelectedDoc}.
	 */
	public void setDefaultSelectedDoc(String defaultSelectedDoc) {
		this.defaultSelectedDoc = defaultSelectedDoc;
	}

	/**
	 * Gets the value of the attribute {@link #defaultDocumentNames}.
	 * @return the value of the attribute {@link #defaultDocumentNames}.
	 */
	public List<SelectItem> getDefaultDocumentNames() {
		return defaultDocumentNames;
	}

	/**
	 * 
	 * @return
	 */
	public int getSelectOneModeImg() {
		return selectOneModeImg;

	}

	/**
	 * 
	 * @return
	 */
	public String getTemplateSize() {
		if (templateEdit != null) {
			if (templateEdit.getTemplate() != null) {
				return Integer.toString(templateEdit.getTemplate().length);
			}
		}

		return "0";
	}

	/**
	 * 
	 * @param selectOneModeImg
	 */
	public void setSelectOneModeImg(int selectOneModeImg) {
		this.selectOneModeImg = selectOneModeImg;
	}

	/**
	 * 
	 * @return
	 */
	public TemplateData getTemplateEdit() {
		return templateEdit;
	}

	/**
	 * 
	 * @param templateEdit
	 */
	public void setTemplateEdit(TemplateData templateEdit) {
		this.templateEdit = templateEdit;
	}

	/**
	 * 
	 * @param event
	 */
	public void onUploadComplete(UploadEvent event) {
		UploadItem item = event.getUploadItem();
		templateEdit.setTemplate(item.getData());

		// if (templateEdit.getTemplate() == null) {
		File file = item.getFile();
		try {
			templateEdit.setTemplate(getBytesFromFile(file));
		} catch (IOException e) {
			addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG085), e.getMessage());
		}
		// }

	}

	public void onUpLoadXMLComplete(UploadEvent event) {
		UploadItem item = event.getUploadItem();
		File file = item.getFile();
		try {
			this.xmlTest = getBytesFromFile(file);
			if (this.xmlTest != null) {
				LOGGER.info("TAMA�O del XML: " + this.xmlTest.length);
			}

		} catch (IOException e) {
			addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG085), e.getMessage());
		}

	}

	public void onUpLoadDocComplete(UploadEvent event) {
		UploadItem item = event.getUploadItem();
		File file = item.getFile();
		try {
			this.docTest = getBytesFromFile(file);
			if (this.docTest != null) {
				LOGGER.info("TAMA�O del DOC: " + this.docTest.length);
			}

		} catch (IOException e) {
			addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG085), e.getMessage());
		}

	}

	/**
	 * 
	 * @return
	 */
	public String onNewTemplateAcept() {

		// templateEdit.setIdentifier(templateEdit.getIdentifier().trim());
		// if (templateEdit.getIdentifier().equals("")) {
		// addMessage(FacesMessage.SEVERITY_WARN,
		// Language.getMessage(LanguageKeys.WTEXTFIELDRSUMMARY),
		// "Es necesario indicar el Identificador de la Plantilla");
		// context.addMessage(null, message);
		// return null;
		// }
		//
		// try {
		// TemplateData template =
		// confTemplate.getTemplate(templateEdit.getIdentifier());
		// if (template != null) {
		// addMessage(FacesMessage.SEVERITY_WARN, "Datos incorrectos.",
		// "Ya existe una plantilla con el identificador indicado");
		// context.addMessage(null, message);
		// return null;
		// }
		// } catch (ConfigurationException e) {
		// //
		// }
		//
		// if (getTemplateSize().equalsIgnoreCase("0")) {
		// addMessage(FacesMessage.SEVERITY_WARN,
		// Language.getMessage(LanguageKeys.WTEXTFIELDRSUMMARY),
		// "Es necesario indicar el fichero con la plantilla");
		// context.addMessage(null, message);
		// return null;
		// }

		if (selectOneModeImg == 0) {
			templateEdit.setPagesRange(null);
		}

		// if (templateEdit.getModeDocInclude() ==
		// confTemplate.INC_SIGNED_DOC_CONCAT) {
		// if (templateEdit.getConcatenationRule() != null &&
		// !templateEdit.getConcatenationRule().matches(confTemplate.CONCANT_MASK))
		// {
		// addMessage(FacesMessage.SEVERITY_WARN, "Valor invalido.",
		// "La Regla de Concatenaci�n tiene un formato no v�lido");
		// context.addMessage(null, message);
		// return null;
		// }
		// }
		//
		// if ((templateEdit.getModeDocInclude() ==
		// confTemplate.INC_SIGNED_DOC_IMAGE) &&
		// (templateEdit.getPagesRange() != null)) {
		// if (templateEdit.getPagesRange() != null &&
		// !templateEdit.getPagesRange().matches(confTemplate.RANGE_MASK))
		// {
		// addMessage(FacesMessage.SEVERITY_WARN, "Valor inv�lido.",
		// "El rango de p�ginas tiene un formato no v�lido");
		// context.addMessage(null, message);
		// return null;
		// }
		//
		// }

		if (templateEdit.getReportType() == TemplateConfigurationFacadeI.PDF_REPORT) {

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);

			try {
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document = builder.parse(new ByteArrayInputStream(templateEdit.getTemplate()));
				document = null;
			} catch (ParserConfigurationException e) {
				addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG086), Language.getMessage(LanguageKeys.WMSG087));
				return null;
			} catch (SAXException e) {
				addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG086), Language.getMessage(LanguageKeys.WMSG087));
				return null;
			} catch (IOException e) {
				addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG086), Language.getMessage(LanguageKeys.WMSG087));
				return null;
			}

		}
	

		try {
			confTemplate.createTemplate(templateEdit);
			addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG089), Language.getMessage(LanguageKeys.WMSG090) + templateEdit.getIdentifier());
			setStatus(STATUS_NONE);
			if (templateEdit.getReportType() == TemplateConfigurationFacadeI.PDF_REPORT) {
				setStatus(STATUS_EDIT_PDF);
			}

		} catch (ConfigurationException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG091), e.getDescription());
			return null;
		}

		return null;
	}

	/**
	 * 
	 * @param event
	 */
	public void selectionTreeListener(org.richfaces.event.NodeSelectedEvent event) {
		if (event instanceof AjaxSelectedEvent) {
			UITree tree = (UITree) event.getComponent();
			NodeData node = ((NodeData) tree.getRowData());

			setStatus(STATUS_NONE);

			if (!node.getType().equals(NodeData.NODETYPE_EXTENSION)) {
				try {
					templateEdit = confTemplate.getTemplate(node.getId());
				} catch (ConfigurationException e) {
					addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG092), e.getMessage());
				}

				//confTemplate;
				if (templateEdit.getReportType() == TemplateConfigurationFacadeI.PDF_REPORT) {
					setStatus(STATUS_EDIT_PDF);
					
				}
				//confTemplate;

			}
		}
	}

	/**
	 * 
	 * @return
	 */
	public TreeNode<NodeData> getTreeData() {

		TreeNode<NodeData> tree_root = new TreeNodeImpl<NodeData>();

		NodeData nd = new NodeData(Language.getMessage(LanguageKeys.WMSG093), NodeData.NODETYPE_ROOT);
		tree_root.setData(nd);

		NodeData nd_pdf = new NodeData(Language.getMessage(LanguageKeys.WMSG094), NodeData.NODETYPE_EXTENSION);
		
		TreeNode<NodeData> node_pdf = new TreeNodeImpl<NodeData>();
		
		node_pdf.setData(nd_pdf);
		
		tree_root.addChild(0, node_pdf);
		
		LinkedHashMap<String, Integer> templatesId = null;
		try {
			templatesId = (LinkedHashMap<String, Integer>) confTemplate.getTemplateIds();
		} catch (ConfigurationException e) {
			addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG096), e.getMessage());
		}

		Collection<String> c = templatesId.keySet();

		TreeSet<String> orderTemplates = new TreeSet<String>(new ComparatorName());
		orderTemplates.addAll(c);

		// obtain an Iterator for Collection
		Iterator<String> itr = orderTemplates.iterator();

		// iterate through LinkedHashMap values iterator
		while (itr.hasNext()) {
			String key = (String) itr.next();
			Integer value = templatesId.get(key);

			TreeNode<NodeData> node = new TreeNodeImpl<NodeData>();

			//confTemplate;
			if (value == TemplateConfigurationFacadeI.PDF_REPORT) {
				NodeData nodedata = new NodeData(key, NodeData.NODETYPE_PDF);
				nodedata.setId(key);
				node.setData(nodedata);
				node_pdf.addChild(node.getData().getId(), node);

			}
			//confTemplate;

		}

		return tree_root;
	}

	/**
	 * 
	 * @return
	 */
	public String onStartNewTemplate() {

		//confTemplate;
		templateEdit = new TemplateData("", TemplateConfigurationFacadeI.PDF_REPORT);
		selectOneModeImg = 1;
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public String onCancelNewTemplate() {
		setStatus(STATUS_NONE);
		return null;
	}

	/**
	 * 
	 * @return
	 */
	public String onNewTemplate() {

		templateEdit.setIdentifier(templateEdit.getIdentifier().trim());
		if (templateEdit.getIdentifier().equals("")) {
			addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG097), Language.getMessage(LanguageKeys.WMSG098));
			return null;
		}

		try {
			if (confTemplate.existTemplate(templateEdit.getIdentifier())) {
				addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG091), Language.getMessage(LanguageKeys.WMSG099));
				return null;
			}
		} catch (ConfigurationException e) {
			//
		}

		if (getTemplateSize().equalsIgnoreCase("0")) {
			addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG097), Language.getMessage(LanguageKeys.WMSG100));
			return null;
		}

		//confTemplate;
		if (templateEdit.getReportType() == TemplateConfigurationFacadeI.PDF_REPORT) {
			setStatus(STATUS_NEW_PDF);
		}

		return null;
	}

	/**
	 * 
	 * @return
	 */
	public String onDownload() {
		HttpServletResponse response = (HttpServletResponse) getContext().getExternalContext().getResponse();

		writeOutContent(response, templateEdit.getTemplate(), templateEdit.getIdentifier());
		getContext().responseComplete();

		return null;
	}

	/**
	 * 
	 * @return
	 */
	public String onDeleteTemplate() {
		String id = templateEdit.getIdentifier();

		try {
			confTemplate.removeTemplate(id);
			setStatus(STATUS_NONE);
			addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG101), Language.getMessage(LanguageKeys.WMSG102) + id);
			return null;
		} catch (ConfigurationException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG103), e.getDescription());
			return null;
		}

	}

	/**
	 * 
	 * @return
	 */
	public String onModifyTemplate() {

		try {
			
			if (templateEdit.getReportType() == TemplateConfigurationFacadeI.PDF_REPORT) {

				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				factory.setValidating(false);
				factory.setNamespaceAware(true);

				try {
					DocumentBuilder builder = factory.newDocumentBuilder();
					builder.parse(new ByteArrayInputStream(templateEdit.getTemplate()));
				} catch (ParserConfigurationException e) {
					addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG104), Language.getMessage(LanguageKeys.WMSG087));
					return null;
				} catch (SAXException e) {
					addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG104), Language.getMessage(LanguageKeys.WMSG087));
					return null;
				} catch (IOException e) {
					addMessage(FacesMessage.SEVERITY_WARN, Language.getMessage(LanguageKeys.WMSG104), Language.getMessage(LanguageKeys.WMSG087));
					return null;
				}

			}
			if (this.selectOneModeImg == 1) {
				templateEdit.setPagesRange(null);
			}
			confTemplate.modifyTemplateData(templateEdit);
			addMessage(FacesMessage.SEVERITY_INFO, Language.getMessage(LanguageKeys.WMSG105), Language.getMessage(LanguageKeys.WMSG106) + templateEdit.getIdentifier());
		} catch (ConfigurationException e) {
			addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG107), e.getDescription());
			return null;
		}

		return null;
	}

	/**
	 * Method for previewing the template result.
	 * @return	Result message.
	 */
	public String onTemplateTest() {
		Document doc = null;
		byte[ ] signedDoc = this.docTest;
		if (testMode == DEFAULT_TEST) {
			try {
				signedDoc = getDefaultFileContent(defaultSelectedDoc);
				xmlTest = getDefaultFileContent(defaultSelectedParam);
			} catch (IOException e) {
				addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG166), Language.getMessage(LanguageKeys.WMSG168));
				return null;
			}
		}
		if (xmlTest != null && xmlTest.length > 0) {
			try {
				doc = XMLUtils.getDocument(xmlTest);
			} catch (ToolsException e1) {
				addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG166), Language.getMessage(LanguageKeys.WMSG168));
				return null;
			}
		}
		if (doc != null) {
			try {
				ReportManagerI reportManager = ReportManagerFactory.getReportManager(templateEdit.getReportType());
				ValidationSignatureResponse validationResponse = getValidationSignatureResponse(doc);
				if (validationResponse != null) {
					HashMap<String, String> additionalParameters = getParameters(doc);
					ArrayList<Barcode> barcodes = getBarcodes(doc);
					byte[ ] report = reportManager.createReport(validationResponse, templateEdit, null, signedDoc, barcodes, additionalParameters);
					HttpServletResponse response = (HttpServletResponse) getContext().getExternalContext().getResponse();
					if (templateEdit.getReportType() == TemplateConfigurationFacadeI.PDF_REPORT) {
						writeOutContent(response, report, "report", FileUtils.PDF_MEDIA_TYPE);
					} else {
						writeOutContent(response, report, "report", FileUtils.ODT_MEDIA_TYPE);
					}
					getContext().responseComplete();
				}

			} catch (ReportException e) {
				addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG166), e.getMessage());
				return null;
			}
		} else {
			addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG166), Language.getMessage(LanguageKeys.WMSG167));
		}
		return null;
	}

	private byte[ ] getDefaultFileContent(String fileName) throws IOException {
		if (fileName == null || fileName.equals("---")) {
			return null;
		}
		FileInputStream is = null;
		ByteArrayOutputStream baos = null;
		byte[ ] data = null;
		try {
		is = new FileInputStream(UtilsJBoss.createAbsolutePath(UtilsJBoss.getJBossServerConfigDir() + DEFAULT_CONTENT_FOLDER, fileName));
		
		if (is != null) {
			baos = new ByteArrayOutputStream();
			byte[ ] buffer = new byte[2048];
			int nRead = -1;
			while ((nRead = is.read(buffer, 0, buffer.length)) != -1) {
				baos.write(buffer, 0, nRead);
			}

			baos.flush();
			data = baos.toByteArray();
			
		}
		} catch (IOException e) {
			throw e;
		} finally {
			if (baos != null) {
				baos.close();
			}
			if (is != null) {
				is.close();
			}
		}
		return data;
	}

	/**
	 * Gets the Bar Codes include in the XML for testing
	 * @param doc	Supplied XML.
	 * @return	List that contains the Bar Codes.
	 */
	private ArrayList<Barcode> getBarcodes(Document doc) {
		NodeList childList = doc.getDocumentElement().getChildNodes();
		Node barcodesNode = null;
		for (int i = 0; i < childList.getLength(); i++) {
			if (childList.item(i).getLocalName() != null && childList.item(i).getLocalName().equals(BARCODES_NAME)) {
				barcodesNode = childList.item(i);
			}
		}
		if (barcodesNode != null) {
			NodeList nl = barcodesNode.getChildNodes();
			if (nl != null && nl.getLength() > 0) {
				ArrayList<Barcode> barcodes = new ArrayList<Barcode>();
				for (int i = 0; i < nl.getLength(); i++) {
					Node barcodeNode = nl.item(i);
					NodeList barChilds = barcodeNode.getChildNodes();
					String barcodeType = null;
					String codeMessage = null;
					LinkedHashMap<String, String> confParameters = null;
					for (int j = 0; j < barChilds.getLength(); j++) {
						if (barChilds.item(j).getLocalName() != null && barChilds.item(j).getLocalName().equals(TYPE_NAME)) {
							barcodeType = barChilds.item(j).getFirstChild().getNodeValue();
						}
						if (barChilds.item(j).getLocalName() != null && barChilds.item(j).getLocalName().equals(MESSAGE_NAME)) {
							codeMessage = barChilds.item(j).getFirstChild().getNodeValue();
						}
						if (barChilds.item(j).getLocalName() != null && barChilds.item(j).getLocalName().equals(CONFIGURATION_NAME)) {
							confParameters = new LinkedHashMap<String, String>();
							NodeList parametersList = barChilds.item(j).getChildNodes();
							for (int k = 0; k < parametersList.getLength(); k++) {
								NodeList parameterValueList = parametersList.item(k).getChildNodes();
								String paramId = null;
								String paramValue = null;
								for (int z = 0; z < parameterValueList.getLength(); z++) {
									if (parameterValueList.item(z).getLocalName() != null && parameterValueList.item(z).getLocalName().equals(PARAMETER_ID_NAME)) {
										paramId = parameterValueList.item(z).getFirstChild().getNodeValue();
									} else if (parameterValueList.item(z).getLocalName() != null && parameterValueList.item(z).getLocalName().equals(PARAMETER_VALUE_NAME)) {
										paramValue = parameterValueList.item(z).getFirstChild().getNodeValue();
									}
								}
								if (paramId != null && paramValue != null) {
									confParameters.put(paramId, paramValue);
								}
							}
						}

					}
					if (barcodeType != null && codeMessage != null) {
						Barcode barcode = new Barcode(barcodeType, codeMessage);
						barcode.setConfiguration(confParameters);
						barcodes.add(barcode);
					}
				}
				return barcodes;
			}
		}
		return null;
	}

	/**
	 * Gets the additional parameter include in the XML for testing
	 * @param doc	Supplied XML.
	 * @return	Map that contains the additional parameters.
	 */
	private HashMap<String, String> getParameters(Document doc) {
		NodeList childList = doc.getDocumentElement().getChildNodes();
		Node epNode = null;
		for (int i = 0; i < childList.getLength(); i++) {
			if (childList.item(i).getLocalName() != null && childList.item(i).getLocalName().equals(EXT_PARAMS_NAME)) {
				epNode = childList.item(i);
			}
		}
		if (epNode != null) {
			NodeList nl = epNode.getChildNodes();
			if (nl != null && nl.getLength() > 0) {
				HashMap<String, String> params = new HashMap<String, String>();
				for (int i = 0; i < nl.getLength(); i++) {
					NodeList parList = nl.item(i).getChildNodes();
					String key = null;
					String value = null;
					for (int j = 0; j < parList.getLength(); j++) {
						if (parList.item(j).getLocalName() != null && parList.item(j).getLocalName().equals(PARAMETER_ID_NAME)) {
							key = parList.item(j).getFirstChild().getNodeValue();
						} else if (parList.item(j).getLocalName() != null && parList.item(j).getLocalName().equals(PARAMETER_VALUE_NAME)) {
							value = parList.item(j).getFirstChild().getNodeValue();
						}
					}
					if (key != null && value != null) {
						params.put(key, value);
					}
				}
				return params;
			}
		}
		return null;
	}

	/**
	 * Gets the {@link ValidationSignatureResponse} object.
	 * @param doc	Supplied XML file.
	 * @return	A {@link ValidationSignatureResponse} object.
	 */
	private ValidationSignatureResponse getValidationSignatureResponse(Document doc) {
		ValidationSignatureResponse response;
		NodeList validationResultList = doc.getDocumentElement().getChildNodes();
		Node validationResultNode = null;
		for (int i = 0; i < validationResultList.getLength(); i++) {
			if (validationResultList.item(i).getLocalName() != null && validationResultList.item(i).getLocalName().equals(VALIDATION_RESULT_NAME)) {
				validationResultNode = validationResultList.item(i);
			}
		}
		if (validationResultNode != null) {
			LinkedHashMap<String, Object> valInfo = new LinkedHashMap<String, Object>();
			response = new ValidationSignatureResponse(new String(xmlTest), valInfo);
			NodeList vrChildList = validationResultNode.getChildNodes();
			ArrayList<LinkedHashMap<String, Object>> signatures = new ArrayList<LinkedHashMap<String, Object>>();
			for (int i = 0; i < vrChildList.getLength(); i++) {
				if (vrChildList.item(i).getLocalName() != null && vrChildList.item(i).getLocalName().equals(RESULT_NAME)) {
					NodeList resultChilds = vrChildList.item(i).getChildNodes();
					for (int j = 0; j < resultChilds.getLength(); j++) {
						if (resultChilds.item(j).getLocalName() != null && resultChilds.item(j).getLocalName().equals(RESULT_MAJOR_NAME)) {
							valInfo.put(DSSVerifyTransformI.RESULT_MAJOR_RES, resultChilds.item(j).getFirstChild().getNodeValue());
						} else if (resultChilds.item(j).getLocalName() != null && resultChilds.item(j).getLocalName().equals(RESULT_MINOR_NAME)) {
							valInfo.put(DSSVerifyTransformI.RESULT_MINOR_RES, resultChilds.item(j).getFirstChild().getNodeValue());
						} else if (resultChilds.item(j).getLocalName() != null && resultChilds.item(j).getLocalName().equals(RESULT_MESSAGE_NAME)) {
							valInfo.put(DSSVerifyTransformI.RESULT_MSG_RES, resultChilds.item(j).getFirstChild().getNodeValue());
						}
					}
				} else if (vrChildList.item(i).getLocalName() != null && vrChildList.item(i).getLocalName().equals(IND_SIGNATURE_NAME)) {
					LinkedHashMap<String, Object> indValInfo = new LinkedHashMap<String, Object>();
					NodeList isChilds = vrChildList.item(i).getChildNodes();
					for (int j = 0; j < isChilds.getLength(); j++) {
						if (isChilds.item(j).getLocalName() != null && isChilds.item(j).getLocalName().equals(RESULT_NAME)) {
							NodeList resultChilds = vrChildList.item(j).getChildNodes();
							for (int k = 0; k < resultChilds.getLength(); k++) {
								if (resultChilds.item(k).getLocalName() != null && resultChilds.item(k).getLocalName().equals(RESULT_MAJOR_NAME)) {
									indValInfo.put(DSSVerifyTransformI.RESULT_MAJOR_RES, resultChilds.item(k).getFirstChild().getNodeValue());
								} else if (resultChilds.item(k).getLocalName() != null && resultChilds.item(j).getLocalName().equals(RESULT_MINOR_NAME)) {
									indValInfo.put(DSSVerifyTransformI.RESULT_MINOR_RES, resultChilds.item(k).getFirstChild().getNodeValue());
								} else if (resultChilds.item(k).getLocalName() != null && resultChilds.item(j).getLocalName().equals(RESULT_MESSAGE_NAME)) {
									indValInfo.put(DSSVerifyTransformI.RESULT_MSG_RES, resultChilds.item(k).getFirstChild().getNodeValue());
								}
							}
						} else if (isChilds.item(j).getLocalName() != null && isChilds.item(j).getLocalName().equals(TIMESTAMP_NAME)) {
							indValInfo.put(DSSVerifyTransformI.CREATION_TIME_RES, isChilds.item(j).getFirstChild().getNodeValue());
						} else if (isChilds.item(j).getLocalName() != null && isChilds.item(j).getLocalName().equals(CERT_INFO_NAME)) {
							LinkedHashMap<String, String> certInfo = new LinkedHashMap<String, String>();
							NodeList fieldList = isChilds.item(j).getChildNodes();
							for (int k = 0; k < fieldList.getLength(); k++) {
								NodeList fl = fieldList.item(k).getChildNodes();
								String id = null;
								String value = null;
								for (int z = 0; z < fl.getLength(); z++) {
									if (fl.item(z).getLocalName() != null && fl.item(z).getLocalName().equals(FIELD_ID_NAME)) {
										id = fl.item(z).getFirstChild().getNodeValue();
									} else if (fl.item(z).getLocalName() != null && fl.item(z).getLocalName().equals(FIELD_VALUE_NAME)) {
										value = fl.item(z).getFirstChild().getNodeValue();
									}
								}
								if (id != null && value != null) {
									certInfo.put(id, value);
								}
							}
							indValInfo.put(DSSVerifyTransformI.CERT_INFO_RES, certInfo);
						}
					}
					signatures.add(indValInfo);
				}
			}
			if (!signatures.isEmpty()) {
				valInfo.put(DSSVerifyTransformI.IND_SIG_REPORT_RES, signatures);
			}
			response.setValidationInfo(valInfo);

		} else {
			addMessage(FacesMessage.SEVERITY_ERROR, Language.getMessage(LanguageKeys.WMSG166), Language.getFormatMessage(LanguageKeys.WMSG169, new Object[ ] { VALIDATION_RESULT_NAME }));
			return null;
		}
		return response;
	}

	/**
	 * Gets the value of the attribute 'testMode'.
	 * @return the value of the attribute 'testMode'.
	 */
	public int getTestMode() {
		return testMode;
	}

	/**
	 * Sets the value of the attribute 'testMode'.
	 * @param testMode The value for the attribute 'testMode'.
	 */
	public void setTestMode(int testMode) {
		this.testMode = testMode;
	}

	/**
	 * Method that returns the size of the XML test file.
	 * @return	Size of the file.
	 */
	public int getSizeXMLFile() {
		if (this.xmlTest != null) {
			return this.xmlTest.length;
		} else {
			return 0;
		}
	}

	/**
	 * Method that returns the size of the document test file.
	 * @return	Size of the file.
	 */
	public int getSizeDocFile() {
		if (this.docTest != null) {
			return this.docTest.length;
		} else {
			return 0;
		}
	}

	public boolean getHasDefaultDataToTest() {
		return defaultParameterNames != null && !defaultParameterNames.isEmpty();
	}

}
