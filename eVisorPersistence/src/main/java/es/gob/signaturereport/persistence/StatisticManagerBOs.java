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
 * <b>File:</b><p>es.gob.tsa.persistence.configuration.model.bo.StatisticManagerBOs.java.</p>
 * <b>Description:</b><p>Class manager for the Business Objects in statistics scheme.</p>
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

import es.gob.signaturereport.persistence.statistics.model.bo.interfaz.IGroupingsBO;

/**
 * <p>Class manager for the Business Objects in statistics scheme.</p>
   <b>Project:</b><p>Horizontal platform to generation signature reports 
   in legible format.</p>
 * @version 1.0, 14/05/2013.
 */
@Singleton
@ManagedBean
public class StatisticManagerBOs {

    /**
     * Attribute that represents the unique instance of this class.
     */
    private static StatisticManagerBOs instance = null;
    
    /**
     * Attribute that implements all the operations related with 
     * the management of applications.
     */
    @Inject
    private IGroupingsBO groupingBO;

	/**
     * Method that obtains the instance of the class.
     * @return the unique instance of the class.
     */
    public static StatisticManagerBOs getInstance() {
	if (instance == null) {
	    instance = new StatisticManagerBOs();
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
	 * Gets the value of the attribute {@link #groupingBO}.
	 * @return the value of the attribute {@link #groupingBO}.
	 */
	public IGroupingsBO getGroupingBO() {
		return groupingBO;
	}

	/**
	 * Sets the value of the attribute {@link #groupingBO}.
	 * @param pgroupingBO The value for the attribute {@link #groupingBO}.
	 */
	public void setGroupingBO(final IGroupingsBO pgroupingBO) {
		this.groupingBO = pgroupingBO;
	}
   
}
