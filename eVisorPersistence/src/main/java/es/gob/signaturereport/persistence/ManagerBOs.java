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
 * <b>File:</b><p>es.gob.signaturereport.persistence.ManagerBOs.java.</p>
 * <b>Description:</b><p> Class manager for the different manager of the BOs.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @author Gobierno de España.
 * @version 1.0, 14/05/2013.
 */
package es.gob.signaturereport.persistence;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * <p>Class manager for the different manager of the BOs.</p>
   <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 14/05/2013.
 */
@Singleton
@ManagedBean
public class ManagerBOs {

    /**
     * Static attribute that represents the unique instance of this class.
     */
    private static ManagerBOs instance = null;

    
    /**
     * Attribute that represents the configuration scheme manager BOs.
     */
    @Inject
    private ConfigurationManagerBOs configurationManagerBOs;
    
    /**
     * Attribute that represents the configuration scheme manager BOs.
     */
    @Inject
    private AuditManagerBOs auditManagerBOs;
   
    /**
     * Static method to get the singleton of the class.
     * @return Unique instance of this class.
     */
    public static ManagerBOs getInstance() {
    	if (instance == null) {
    	    instance = new ManagerBOs();
    	}
    	return instance;
    }

    /**
     * Method that initializes the manager of the cluster.
     */
    @PostConstruct
    public final void init() {
	instance = this;
    }

    /**
     * Method that destroy the singleton of this class.
     */
    @PreDestroy
    public final void destroy() {
	instance = null;
    }

	
	/**
	 * Gets the value of the attribute {@link #configurationManagerBOs}.
	 * @return the value of the attribute {@link #configurationManagerBOs}.
	 */
	public ConfigurationManagerBOs getConfigurationManagerBOs() {
		return configurationManagerBOs;
	}

	
	/**
	 * Sets the value of the attribute {@link #configurationManagerBOs}.
	 * @param configurationManagerBOs The value for the attribute {@link #configurationManagerBOs}.
	 */
	public void setConfigurationManagerBOs(ConfigurationManagerBOs configurationManagerBOs) {
		this.configurationManagerBOs = configurationManagerBOs;
	}

	
	/**
	 * Gets the value of the attribute {@link #auditManagerBOs}.
	 * @return the value of the attribute {@link #auditManagerBOs}.
	 */
	public AuditManagerBOs getAuditManagerBOs() {
		return auditManagerBOs;
	}

	
	/**
	 * Sets the value of the attribute {@link #auditManagerBOs}.
	 * @param auditManagerBOs The value for the attribute {@link #auditManagerBOs}.
	 */
	public void setAuditManagerBOs(AuditManagerBOs auditManagerBOs) {
		this.auditManagerBOs = auditManagerBOs;
	}
}
