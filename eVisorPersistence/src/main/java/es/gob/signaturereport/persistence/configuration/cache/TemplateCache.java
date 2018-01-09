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
 * <b>File:</b><p>es.gob.signaturereport.configuration.persistence.cache.TemplateCache.java.</p>
 * <b>Description:</b><p> Cache used to include the templates used by the platform.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>25/04/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 25/04/2011.
 */
package es.gob.signaturereport.persistence.configuration.cache;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.properties.StaticSignatureReportProperties;


/** 
 * <p>Cache used to include the templates used by the platform.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 25/04/2011.
 */
public final class TemplateCache {

	 /**
     * Attribute that represents the object that manages the log of the class. 
     */
    private static final Logger LOGGER = Logger.getLogger(TemplateCache.class);
    
	/**
	 * Attribute that represents the template cache. 
	 */
	private LinkedHashMap <String, byte[]> cache = new LinkedHashMap<String, byte[]>();
	
	/**
	 * Attribute that represents the template identifiers. 
	 */
	private ArrayList <String> ids = new ArrayList<String>();
	/**
	 * Attribute that represents an instance of the class. 
	 */
	private static TemplateCache instance = null;
	
	/**
	 * Attribute that represents the property that contains the maximum number of entries in the cache. 
	 */
	private static final String ENTRIESNUMBER_PROP = "signaturereport.cache.template.entriesNumber";
	
	/**
	 * Attribute that represents the property that contains the maximum size of entries in the cache. 
	 */
	private static final String MAXSIZEENTRY_PROP = "signaturereport.cache.template.maxSizeEntry";
	
	/**
	 * Attribute that represents the maximum size of entries in the cache. 
	 */
	private int size = 0;
	
	/**
	 * Attribute that represents the maximum number of entries in the cache. 
	 */
	private int number = 0;
	
	/**
	 * Attribute that represents operation mode associated to get template. 
	 */
	private static final int GET_MODE = 0;
	
	/**
	 * Attribute that represents operation mode associated to add template. 
	 */
	private static final int ADD_MODE = 1;
	
	/**
	 * Attribute that represents operation mode associated to remove template. 
	 */
	private static final int REMOVE_MODE = 2;
	
		
	/**
	 * Gets an instance of the class.
	 * @return	A {@link TemplateCache} object.
	 */
	public static TemplateCache getInstance(){
		if(instance==null){
			instance = new TemplateCache();
		}
		return instance;
	}

	/**
	 * Constructor method for the class TemplateCache.java. 
	 */
	private TemplateCache() {
		cache.clear();
		ids.clear();
		String numberStr = StaticSignatureReportProperties.getProperty(ENTRIESNUMBER_PROP);
		if(numberStr!=null){
			try{
				number = Integer.parseInt(numberStr);
			}catch(NumberFormatException e){
				LOGGER.error(Language.getFormatMessage(LanguageKeys.CONF_018,new Object[]{ENTRIESNUMBER_PROP}),e);
			}
		}else{
			LOGGER.error(Language.getFormatMessage(LanguageKeys.CONF_018,new Object[]{ENTRIESNUMBER_PROP}));
		}
		String sizeStr = StaticSignatureReportProperties.getProperty(MAXSIZEENTRY_PROP);
		if(sizeStr != null ){
			try{
				size = Integer.parseInt(sizeStr);
			}catch(NumberFormatException e){
				LOGGER.error(Language.getFormatMessage(LanguageKeys.CONF_018,new Object[]{MAXSIZEENTRY_PROP}),e);
			}
		}else{
			LOGGER.error(Language.getFormatMessage(LanguageKeys.CONF_018,new Object[]{MAXSIZEENTRY_PROP}));
		}
	}
	 
	/**
	 * Gets a template.
	 * @param id	Unique identifier of template.
	 * @return	Array the bytes of the template if the template exist. Otherwise null.
	 */
	public byte[] getTemplate(String id){
		return doOperation(id, null , GET_MODE);
	}
	
	/**
	 * Add a template to the cache. 
	 * @param id		Unique identifier of template.
	 * @param template	Array of bytes that represents the template.
	 */
	public void addTemplate(String id, byte[] template){
		doOperation(id, template , ADD_MODE);
	}
	
	/**
	 * Deletes a template from the cache.
	 * @param id templateIdentifier
	 */
	public void removeTemplate(String id){
		doOperation(id, null , REMOVE_MODE);
	}
	/**
	 * Method that performs operations to include, delete or obtain a template.
	 * @param id		Unique identifier of template.
	 * @param template	Array of bytes that represents the template.
	 * @param operationMode	Operation mode. Allowed values:<br/>
	 * 			{@link TemplateCache#ADD_MODE} to add a template. <br/>
	 * 			{@link TemplateCache#GET_MODE} to get a template. <br/>
	 * 			{@link TemplateCache#REMOVE_MODE} to remove a template. 
	 * @return	Array the bytes of the template if the operation mode is obtain template. Otherwise null.
	 */
	private synchronized byte[] doOperation(String id,byte[] template, int operationMode ){
		if(operationMode == GET_MODE ){
			if(ids.contains(id)){
				byte[] t = cache.get(id);
				return t.clone();
			}
		}else if(operationMode == ADD_MODE){
			if(template.length<=size){
				if(ids.contains(id)){
					cache.put(id, template);
				}else{
					ids.add(0, id);
					cache.put(id, template);
					if(ids.size()>number){
						int posToRem = ids.size()-1;
						while(ids.size()>number){
							String idToRem = ids.remove(posToRem);
							cache.remove(idToRem);
							posToRem = ids.size()-1;
						}
					}
				}
			}
		}else if(operationMode == REMOVE_MODE){
				cache.remove(id);
				ids.remove(id);
		}
		return null;
	}
}
