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
 * <b>File:</b><p>es.gob.signaturereport.tools.UtilsResources.java.</p>
 * <b>Description:</b><p>Class that provides functionality to control resources.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>15/04/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 15/04/2011.
 */
package es.gob.signaturereport.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;

/** 
 * <p>Class that provides functionality to control resources.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 15/04/2011.
 */
public final class UtilsResources {

	/**
	 * Attribute that represents the object that manages the log of the class. 
	 */
	private static final Logger LOGGER = Logger.getLogger(UtilsResources.class);

	/**
	 * Constructor method for the class UtilsResources.java. 
	 */
	private UtilsResources() {
	}

	/**
	 * Method that handles the closing of a {@link OutputStream} resource.
	 * @param os Parameter that represents a {@link OutputStream} resource.
	 */
	public static void safeCloseOutputStream(OutputStream os) {
		if (os != null) {
			try {
				os.close();
			} catch (IOException e) {

				LOGGER.error(Language.getMessage(LanguageKeys.UTIL_003), e);
			}
		}
	}

	/**
	 * Method that handles the closing of a {@link InputStream} resource.
	 * @param is Parameter that represents a {@link InputStream} resource.
	 */
	public static void safeCloseInputStream(InputStream is) {
		if (is != null) {
			try {
				is.close();
			} catch (IOException e) {
				LOGGER.error(Language.getMessage(LanguageKeys.UTIL_003), e);
			}
		}
	}

	/**
	 * Method that handles the closing of a {@link Reader} resource.
	 * @param reader Parameter that represents a {@link Reader} resource.
	 */
	public static void safeCloseReader(Reader reader) {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				LOGGER.error(Language.getMessage(LanguageKeys.UTIL_003), e);
			}
		}
	}

	/**
	 * Close the session.
	 * @param session	Session to close.
	 */
//	public static void safeCloseSession(Session session) {
//		if (session != null && session.isOpen()) {
//			try {
//				session.close();
//			} catch (HibernateException he) {
//				LOGGER.error(Language.getMessage(LanguageKeys.UTIL_031), he);
//			}
//		}
//	}
	/**
	 * Roolback of transaction.
	 * @param tx	Database transaction.	 
	 */
//	public static void safeRollbackTransaction (Transaction tx){
//		if(tx != null && tx.isActive() && !tx.wasRolledBack()){
//			try {
//				tx.rollback();
//			} catch (HibernateException he) {
//				LOGGER.error(Language.getMessage(LanguageKeys.UTIL_032), he);
//			}
//		}
//	}
}
