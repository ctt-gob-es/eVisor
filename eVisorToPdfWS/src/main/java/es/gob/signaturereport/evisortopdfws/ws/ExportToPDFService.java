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
 * <b>File:</b><p>es.gob.signaturereport.evisortopdfws.ws.ExportToPDFService.java.</p>
 * <b>Description:</b><p> Class that exports the file received to pdf.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>08/08/2016.</p>
 * @author Spanish Government.
 * @version 1.0, 08/08/2016.
 */
package es.gob.signaturereport.evisortopdfws.ws;

/** 
 * <p>Class that exports the file received to pdf.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 8/8/2016.
 */
public class ExportToPDFService {

	/**
	 * Method that exports the file received to pdf.
	 * @param document Document to convert.
	 * @return converted pdf document.
	 */
	public final byte[ ] exportToPDF(byte[ ] document) {

		byte[ ] pdf = null;
		try {
			if (OOPDFUtils.getInstance().isAllowedPDFExport()) {
				pdf = OOPDFUtils.getInstance().exportToPDF(document);
			}
		} catch (OOPDFException e) {
			// Vacío intencionadamente. Se crean entradas de log al lanzar la
			// excepción.
		}

		return pdf;
	}
}
