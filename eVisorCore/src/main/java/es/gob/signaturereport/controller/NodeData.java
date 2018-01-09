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
 * <b>File:</b><p>es.gob.signaturereport.controller.NodeData.java.</p>
 * <b>Description:</b><p>Class that manages </p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>07/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 07/02/2011.
 */
package es.gob.signaturereport.controller;

/** 
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 11/10/2011.
 */
public class NodeData {

    /**
     * Attribute that represents . 
     */
    public static String NODETYPE_ROOT = "root";

    /**
     * Attribute that represents . 
     */
    public static String NODETYPE_UO = "uo";
    /**
     * Attribute that represents . 
     */
    public static String NODETYPE_APP = "app";

    /**
     * Attribute that represents . 
     */
    /**
     * Attribute that represents . 
     */
    public static String NODETYPE_EXTENSION = "extension";
    /**
     * Attribute that represents . 
     */
    public static String NODETYPE_PDF = "pdf";
   

    /**
     * Attribute that represents . 
     */
    public static String NODETYPE_OPERATION = "operation";
    /**
     * Attribute that represents . 
     */
    public static String NODETYPE_FIELD = "field";

    /**
     * Attribute that represents . 
     */
    private String text;
    /**
     * Attribute that represents . 
     */
    private String type;
    /**
     * Attribute that represents . 
     */
    private String id;

    /**
     * 
     * @return
     */
    public String getId() {
	return id;
    }

    /**
     * 
     * @param id
     */
    public void setId(String id) {
	this.id = id;
    }

    /**
     * Constructor method for the class NodeData.java.
     * @param text
     * @param type 
     */
    public NodeData(String text, String type) {
	super();
	this.text = text;
	this.type = type;
    }

    /**
     * 
     * @return
     */
    public String getText() {
	return text;
    }

    /**
     * 
     * @param text
     */
    public void setText(String text) {
	this.text = text;
    }

    /**
     * 
     * @return
     */
    public String getType() {
	return type;
    }

    /**
     * 
     * @param type
     */
    public void setType(String type) {
	this.type = type;
    }
}