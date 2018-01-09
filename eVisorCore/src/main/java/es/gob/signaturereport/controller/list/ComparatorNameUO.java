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
 * <b>File:</b><p>es.gob.signaturereport.controller.list.ComparatorNameUO.java.</p>
 * <b>Description:</b><p>Class that manages </p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>07/02/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 07/02/2011.
 */
package es.gob.signaturereport.controller.list;

import java.util.Comparator;

import es.gob.signaturereport.configuration.items.UnitOrganization;


/** 
 * <p>Class .</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI 
 * certificates and electronic signature.</p>
 * @version 1.0, 11/10/2011.
 */
public class ComparatorNameUO implements Comparator<UnitOrganization>{
   
    /**
     * {@inheritDoc}
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(UnitOrganization uo1, UnitOrganization uo2){
   
       
        String uo1Name = uo1.getUnitName().toUpperCase();        
        String uo2Name = uo2.getUnitName().toUpperCase();
       
        return (uo1Name.compareTo(uo2Name));
            
    }
   
}