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
 * <b>File:</b><p>es.gob.signaturereport.controller.list.StatDataTabular.java.</p>
 * <b>Description:</b><p>Class that manages </p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>07/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 07/02/2011.
 */
package es.gob.signaturereport.controller.list;

/** 
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 11/10/2011.
 */
public class StatDataTabular {

    /**
     * Attribute that represents . 
     */
    String rowName = null;
    /**
     * Attribute that represents . 
     */
    String columnName = null;
    /**
     * Attribute that represents . 
     */
    Long value = new Long(0);

    /**
     * 
     * @return
     */
    public String getRowName() {
	return rowName;
    }

    /**
     * Constructor method for the class StatDataTabular.java.
     * @param rowName
     * @param columnName
     * @param value 
     */
    public StatDataTabular(String rowNamein, String columnNamein, Long valuein) {
	super();
	this.rowName = rowNamein;
	this.columnName = columnNamein;
	this.value = valuein;
    }

    /**
     * 
     * @param rowName
     */
    public void setRowName(String rowNamein) {
	this.rowName = rowNamein;
    }

    /**
     * 
     * @return
     */
    public String getColumnName() {
	return columnName;
    }

    /**
     * 
     * @param columnName
     */
    public void setColumnName(String columnNamein) {
	this.columnName = columnNamein;
    }

    /**
     * 
     * @return
     */
    public Long getValue() {
	return value;
    }

    /**
     * 
     * @param value
     */
    public void setValue(Long valuein) {
	this.value = valuein;
    }

}
