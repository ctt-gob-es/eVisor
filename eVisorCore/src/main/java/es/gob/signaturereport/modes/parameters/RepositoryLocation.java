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
 * <b>File:</b><p>es.gob.signaturereport.modes.parameters.RepositoryLocation.java.</p>
 * <b>Description:</b><p>Class that represents a location object into external repository.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>22/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 22/02/2011.
 */
package es.gob.signaturereport.modes.parameters;


/** 
 * <p>Class that represents a location object into external repository.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 22/02/2011.
 */
public class RepositoryLocation {
    
    /**
     * Attribute that represents the unique identifier that represents a object located into a external repository. 
     */
    private String uuid = null;
    
    /**
     * Attribute that represents the identifier of external repository that contains the element. 
     */
    private String repositoryId = null;

    /**
     * Constructor method for the class RepositoryLocation.java.
     * @param uuidObject		Unique identifier that represents a object located into a external repository.
     * @param repository 	Identifier of external repository that contains the element. 
     */
    public RepositoryLocation(String uuidObject, String repository) {
	super();
	this.uuid = uuidObject;
	this.repositoryId = repository;
    }

    
    /**
     * Gets the value of the unique identifier that represents a object located into a external repository.
     * @return the value of the unique identifier that represents a object located into a external repository.
     */
    public String getUuid() {
        return uuid;
    }

    
    /**
     * Sets the value of the unique identifier that represents a object located into a external repository.
     * @param uuidObject The value for the unique identifier that represents a object located into a external repository.
     */
    public void setUuid(String uuidObject) {
        this.uuid = uuidObject;
    }

    
    /**
     * Gets the value of the identifier of external repository that contains the element.
     * @return the value of the identifier of external repository that contains the element.
     */
    public String getRepositoryId() {
        return repositoryId;
    }

    
    /**
     * Sets the value of the identifier of external repository that contains the element.
     * @param repository The value for the identifier of external repository that contains the element.
     */
    public void setRepositoryId(String repository) {
        this.repositoryId = repository;
    }
    
    
}
