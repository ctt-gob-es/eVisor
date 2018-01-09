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
 * <b>File:</b><p>es.gob.signaturereport.tools.CertificateUtils.java.</p>
 * <b>Description:</b><p>Class that provides methods for manipulating X.509 certificates..</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>30/06/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 30/06/2011.
 */
package es.gob.signaturereport.tools;

import java.security.cert.X509Certificate;

/** 
 * <p>Class that provides methods for manipulating X.509 certificates..</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 30/06/2011.
 */
public final class CertificateUtils {

	/**
	 * Constructor method for the class CertificateUtils.java. 
	 */
	private CertificateUtils() {
	}

	/**
	 * Method that indicates whether some other certificate is "equal to" this one (true) or not (false).
	 * @param cert1 Parameter that represents the first certificate to compare.
	 * @param cert2 Parameter that represents the second certificate to compare.
	 * @return a boolean that indicates whether some other certificate is "equal to" this one (true) or not (false).
	 */
	public static boolean isEqual(X509Certificate cert1, X509Certificate cert2) {
		if ((cert1 == null && cert2 != null) || (cert1 != null && cert2 == null)) {
			return false;
		}
		if (cert1 != null && cert2 != null) {
			// Mismo emisor
			if ((cert1.getIssuerX500Principal() != null && cert2.getIssuerX500Principal() == null) || (cert1.getIssuerX500Principal() == null && cert2.getIssuerX500Principal() != null)) {
				return false;
			}
			if ((cert1.getIssuerX500Principal() != null && cert2.getIssuerX500Principal() != null) && ((cert1.getIssuerX500Principal().getName() != null && cert2.getIssuerX500Principal().getName() == null) || (cert1.getIssuerX500Principal().getName() == null && cert2.getIssuerX500Principal().getName() != null) || (cert1.getIssuerX500Principal().getName() != null && cert2.getIssuerX500Principal().getName() != null && !(cert1.getIssuerX500Principal().getName().equals(cert2.getIssuerX500Principal().getName()))))) {
				return false;
			}

			// Mismo n�mero de serie
			if ((cert1.getSerialNumber() != null && cert2.getSerialNumber() == null) || (cert1.getSerialNumber() == null && cert2.getSerialNumber() != null)) {
				return false;
			}
			if (cert1.getSerialNumber() != null && cert2.getSerialNumber() != null && cert1.getSerialNumber().compareTo(cert2.getSerialNumber()) != 0) {
				return false;
			}
		}
		return true;

	}

}
