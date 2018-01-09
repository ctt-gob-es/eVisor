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
 * <b>File:</b><p>es.gob.signaturereport.mfirma.generators.SignatureGeneratorFactory.java.</p>
 * <b>Description:</b><p> Class that provides an instance of {@link SignatureGeneratorI}.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>13/09/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 13/09/2011.
 */
package es.gob.signaturereport.mfirma.generators;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.mfirma.SignatureManagerException;


/** 
 * <p>Class that provides an instance of {@link SignatureGeneratorI}.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 13/09/2011.
 */
public final class SignatureGeneratorFactory {

	/**
	 * Gets an instance of {@link SignatureGeneratorI} specifics .	
	 * @param signatureFormat	Signature format.
	 * @return	An instance of {@link SignatureGeneratorI}
	 * @throws SignatureManagerException	If the signature format is not valid.
	 */
	public static SignatureGeneratorI getSignatureGenerator(String signatureFormat) throws SignatureManagerException{
		if(signatureFormat==null){
			throw new SignatureManagerException(SignatureManagerException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.SM_021));
		}
		
		
		else if(signatureFormat.equals(SignatureGeneratorI.PDF_SIGNATURE_FORMAT) || signatureFormat.equals(SignatureGeneratorI.PADES_SIGNATURE_FORMAT)){
			return new PDFSignatureGenerator(signatureFormat);
		}
		else{
			throw new SignatureManagerException(SignatureManagerException.INVALID_INPUT_PARAMETERS, Language.getFormatMessage(LanguageKeys.SM_022, new Object[]{signatureFormat} ));
		}
	}

	/**
	 * Constructor method for the class SignatureGeneratorFactory.java. 
	 */
	private SignatureGeneratorFactory() {
	}
}
