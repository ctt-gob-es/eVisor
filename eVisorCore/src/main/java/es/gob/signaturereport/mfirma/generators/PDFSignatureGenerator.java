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
 * <b>File:</b><p>es.gob.signaturereport.mfirma.generators.PDFSignatureGenerator.java.</p>
 * <b>Description:</b><p> Class that represents a PDF signature generator.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>29/09/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 29/09/2011.
 */
package es.gob.signaturereport.mfirma.generators;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Properties;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.ietf.jgss.GSSException;
import org.ietf.jgss.Oid;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.PdfDate;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfPKCS7;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfSignature;
import com.lowagie.text.pdf.PdfSignatureAppearance;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfString;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.mfirma.SignatureConstants;
import es.gob.signaturereport.mfirma.SignatureManagerException;
import es.gob.signaturereport.persistence.utils.NumberConstants;
import es.gob.signaturereport.properties.SignatureReportPropertiesI;
import es.gob.signaturereport.properties.StaticSignatureReportProperties;
import es.gob.signaturereport.tools.CryptoUtilCommons;
import es.gob.signaturereport.tools.GenericUtilsCommons;
import es.gob.signaturereport.tools.PDFUtils;
import es.gob.signaturereport.tools.UtilsResources;


/** 
 * <p>Class that represents a PDF signature generator.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 29/05/2017.
 */
public class PDFSignatureGenerator implements SignatureGeneratorI{
	
	/**
     * Constant attribute that represents implicit signature mode.
     */
    public static final String SIGN_MODE_IMPLICIT = "implicit mode";
    
	/**
     * Constant attribute that identifies PAdES-BES signature format.
     */
    private static String FORMAT_PADES_BES = "PAdES-BES";
	
	/**
	 * Constant attribute that represents the number 8192.
	 */
	private static final int MMMMMMMMCXCII = 8192;
	
	/**
     * Attribute that represents space reserved for content data.
     */
    private static final int CONTENT_RESERVED_SIZE = 8000;
	
	/**
	 * Constant attribute that represents the string to identify the reason used to generate new PDF signatures.
	 */
	private static final String SIGN_REASON = "Firma electrónica @Firma5";

	/**
	 * Constant attribute that represents the string to identify the contact used to generate new PDF signatures.
	 */
	private static final String SIGN_CONTACT = "soporte.afirma5@map.es";
	
	/**
	 * Constant attribute that represents the number 8.0.
	 */
	private static final double VIII = 8.0;
	
	/**
	 * Attribute that represents the signature format. 
	 */
	private String signatureFormat = null;
	
	/**
	 * Attribute that represents the property that contains the signature algorithm.
	 */
	private static final String ALGORITHM_KEY = "signaturereport.global.algorithm";
	
	@Inject
	private static SignatureReportPropertiesI signatureReportProperties;
	/**
	 * Attribute that represents the signature format for PDF files.
	 */
	private static final String ALGORITHM_VALUE = StaticSignatureReportProperties.getProperty(ALGORITHM_KEY);
	
	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(PDFSignatureGenerator.class);

	
	/**
	 * {@inheritDoc}
	 * @see es.gob.signaturereport.mfirma.generators.SignatureGeneratorI#generateSignature(byte[], java.security.PrivateKey, java.security.cert.X509Certificate)
	 */
	public byte[ ] generateSignature(byte[ ] document, PrivateKey key, X509Certificate cert) throws SignatureManagerException {
		if(signatureFormat==null){
			throw new SignatureManagerException(SignatureManagerException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.SM_021));
		}else if(signatureFormat.equals(PDF_SIGNATURE_FORMAT)){
			return getPDFSignature(document, key, cert);
		}else if(signatureFormat.equals(PADES_SIGNATURE_FORMAT)){
			return getPADESSignature(document, key, cert);
		}else{
			throw new SignatureManagerException(SignatureManagerException.INVALID_INPUT_PARAMETERS,Language.getFormatMessage(LanguageKeys.SM_022, new Object[]{signatureFormat}));
		}
	}


	/**
	 * Constructor method for the class PDFSignatureGenerator.java.
	 * @param format 
	 */
	public PDFSignatureGenerator(String format) {
		super();
		this.signatureFormat = format;
	}

	
	/**
	 * Create a PDF signature into the supplied PDF document. 
	 * @param document	PDF document.
	 * @param key		Private key.
	 * @param cert		Certificate.
	 * @return	PDF that include a electronic signature.
	 * @throws SignatureManagerException	If an error occurs.
	 */
	private byte[] getPDFSignature(byte[ ] document, PrivateKey key, X509Certificate cert) throws SignatureManagerException{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		if(!PDFUtils.isPDFFile(document)){
			throw new SignatureManagerException(SignatureManagerException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.SM_030));
		}
		PdfReader pdfReader = null;
		PdfStamper pdfStamper = null;
		try {
			pdfReader  = new PdfReader(document);
			pdfStamper = PdfStamper.createSignature(pdfReader,baos,'\0',null,true);
		} catch (IOException e) {
			String msg = Language.getMessage(LanguageKeys.SM_030);
			LOGGER.error(msg,e);
			throw new SignatureManagerException(SignatureManagerException.INVALID_INPUT_PARAMETERS, msg, e);
		} catch (DocumentException e) {
			String msg = Language.getMessage(LanguageKeys.SM_030);
			LOGGER.error(msg,e);
			throw new SignatureManagerException(SignatureManagerException.INVALID_INPUT_PARAMETERS, msg, e);
		}
	
		ByteArrayOutputStream dataStream = null;
		InputStream inp = null;
		try {
			
			PdfSignatureAppearance pdfSignatureApparence = pdfStamper.getSignatureAppearance();
			Certificate[ ] certs = new Certificate[]{cert};
			pdfSignatureApparence.setCrypto(null, certs , null, PdfSignatureAppearance.SELF_SIGNED);
	        pdfSignatureApparence.setReason(SIGN_REASON);
	        pdfSignatureApparence.setContact(SIGN_CONTACT);
	        int keysize = ((RSAPublicKey) cert.getPublicKey()).getModulus().bitLength();
			int signatureSize = (int) Math.ceil(keysize / VIII);
			pdfSignatureApparence.setExternalDigest(new byte[signatureSize], null, "RSA");
			pdfSignatureApparence.preClose();
			PdfPKCS7 sig = pdfSignatureApparence.getSigStandard().getSigner();
			byte[ ] buf = new byte[MMMMMMMMCXCII];
			int n;
			dataStream = new ByteArrayOutputStream();
			inp = pdfSignatureApparence.getRangeStream();
			while ((n = inp.read(buf)) > 0) {
				dataStream.write(buf, 0, n);
			}
			byte[] toBeSigned = dataStream.toByteArray();
			Signature signature = Signature.getInstance(ALGORITHM_VALUE);
			signature.initSign(key);
			signature.update(toBeSigned);
			byte[] dataSignature = signature.sign();
			sig.setExternalDigest(dataSignature, null, "RSA");

			PdfDictionary dic = new PdfDictionary();
			dic.put(PdfName.CONTENTS, new PdfString(sig.getEncodedPKCS1()).setHexWriting(true));
			pdfSignatureApparence.close(dic);
			
			return baos.toByteArray();
		} catch (Exception e) {
			String msg = Language.getMessage(LanguageKeys.SM_031);
			LOGGER.error(msg,e);
			throw new SignatureManagerException(SignatureManagerException.UNKNOWN_ERROR, msg, e);
		}finally{
			UtilsResources.safeCloseOutputStream(baos);
			UtilsResources.safeCloseOutputStream(dataStream);
			UtilsResources.safeCloseInputStream(inp);
		}
	}
	
	/**
	 * Create a PAdES signature into the supplied PDF document. 
	 * @param document	PDF document.
	 * @param key		Private key.
	 * @param cert		Certificate.
	 * @return	PDF that include a advanced electronic signature.
	 * @throws SignatureManagerException	If an error occurs.
	 */
	private byte[] getPADESSignature(byte[ ] document, PrivateKey key, X509Certificate cert) throws SignatureManagerException{
		if(!PDFUtils.isPDFFile(document)){
			throw new SignatureManagerException(SignatureManagerException.INVALID_INPUT_PARAMETERS, Language.getMessage(LanguageKeys.SM_030));
		}
		java.security.KeyStore.PrivateKeyEntry keyEntry = new KeyStore.PrivateKeyEntry(key,new Certificate[]{cert});
		
		try {
			return sign(document, ALGORITHM_VALUE, keyEntry, false, FORMAT_PADES_BES, null, null);
		} catch (SignatureManagerException e) {
			String msg = Language.getMessage(LanguageKeys.SM_032);
			LOGGER.error(msg,e);
			throw new SignatureManagerException(SignatureManagerException.UNKNOWN_ERROR, msg, e);
		}
	}
	
	/**
	 * @param data Parameter that represents the data to sign.
	 * @param algorithm Parameter that represents the signature algorithm.
	 * @param signatureFormat Parameter that represents the signing mode.
	 * The allowed values for PAdES (Baseline or not) signature are:
     * <ul>
     * <li>{@link es.gob.afirma.signature.SignatureConstants#SIGN_MODE_IMPLICIT}</li>
     * </ul>
	 * @param privateKey Parameter that represents the private key of the signing certificate.
	 * @param includeTimestamp Parameter that indicates if the signature will include a timestamp (true) or not (false).
	 * @param signatureForm Parameter that represents the signature form.
	 * The allowed values for PAdES signature are:
     * <ul>
     * <li>{@link es.gob.signaturereport.mfirma.generators.PDFSignatureGenerator#FORMAT_PADES_BES}</li>
     * </ul>
     * The allowed values for PAdES Baseline signature are:
     * <ul>
     * <li>{@link ISignatureFormatDetector#FORMAT_PADES_B_LEVEL}</li>
     * </ul> 	
	 * @param signaturePolicyID Parameter that represents the identifier of the signature policy used for generate the signature with signature
	 * @param idClient Parameter that represents the client application identifier.
	 * @return an object that represents the generated signature.
	 * @throws SigningException
	 */
	private byte[] sign(byte[] data, String algorithm, PrivateKeyEntry privateKey, boolean includeTimestamp, String signatureForm,
			String signaturePolicyID, String idClient) throws SignatureManagerException {

		LOGGER.debug(Language.getMessage(LanguageKeys.PS_LOG006));
		
		// Comprobación parámetros de entrada
		checkSignInputParams(data, algorithm, privateKey);
		
		Properties externalParams = new Properties();
				
		LOGGER.debug(Language.getFormatMessage(LanguageKeys.PS_LOG004,
				new Object[] { algorithm }));

		ByteArrayOutputStream bytesResult = new ByteArrayOutputStream();

		try {

			Certificate[] certificateChain = privateKey.getCertificateChain();
			// Leemos el PDF original
			PdfReader reader = new PdfReader(data);
			
			// Creamos el contenido de la firma
			PdfStamper stp = PdfStamper.createSignature(reader, bytesResult,
					'\0', null, true);

			// Iniciamos el proceso de generación de la firma PAdES
			PdfSignatureAppearance signatureAppearance = stp
					.getSignatureAppearance();

			// Establecemos como fecha de creación de la firma la fecha actual
			signatureAppearance.setSignDate(new GregorianCalendar());

			// Asociamos la cadena de certificación
			signatureAppearance.setCrypto(null, certificateChain, null,
					PdfName.ADOBE_PPKLITE);

			// Establecemos el valor de la clave /SubFilter. Para firmas
			// PAdES-BES y PAdES-EPES el valor es 'ETSI.CAdES.detached'. Para
			// firmas PAdES-Basic el valor es 'adbe.pkcs7.detached'
			PdfSignature signDictionary = new PdfSignature(
					PdfName.ADOBE_PPKLITE, defineSubFilterValue());

			signDictionary.setDate(new PdfDate(signatureAppearance
					.getSignDate()));
			String signatureDictionaryName = PdfPKCS7.getSubjectFields(
					(X509Certificate) certificateChain[0]).getField("CN");
			signDictionary.setName(signatureDictionaryName);
			
			// En eVisor no permitimos que se añadan firmas "approval" en el futuro
			signatureAppearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);

			// Comentamos inclusión entrada 'Cert' para firmas PADES
			// signDictionary.setCert(((X509Certificate)certificateChain[0]).getEncoded());
			signatureAppearance.setCryptoDictionary(signDictionary);
			// Reservamos espacio para el contenido de la clave /Contents
			// 2229 <- CamerFirma Demo SHA-1 (1024)
			// 5123 <- Firma Profesional Demo SHA-1 (1024)
			// 5031 <- DNIe SHA-2 (2048)
			int csize = CONTENT_RESERVED_SIZE;
			HashMap<PdfName, Integer> exc = new HashMap<PdfName, Integer>();
			exc.put(PdfName.CONTENTS, Integer.valueOf(csize * 2 + 2));
			signatureAppearance.preClose(exc);

			/********************************** 
			 * Creación del objeto SignedData *
			 **********************************/
			byte[] signedData = null;
			P7ContentSignerParameters csp = new P7ContentSignerParameters(data,
					algorithm, privateKey);
			Oid dataType = new Oid(PKCSObjectIdentifiers.data.getId());
			// cálculo del digest del documento pdf original
			String digestAlgorithm = getDigestAlgorithmName(algorithm);

			byte[] messageDigest = digest(digestAlgorithm,
					GenericUtilsCommons
							.getDataFromInputStream(signatureAppearance
									.getRangeStream()));
			csp.setDigestValue(messageDigest);

			// incluimos en los parámetros opcionales el formato de la firma
			// pades para crear signedData (específico para PAdES)
			externalParams.put(SignatureConstants.SIGN_FORMAT_PADES, true);
			CMSBuilder cmsBuilder = new CMSBuilder();
			signedData = cmsBuilder.generateSignedData(csp, false, dataType,
					externalParams, includeTimestamp, signatureForm,
					signaturePolicyID, idClient);
			// signedData = cmsBuilder.generateSignedData(csp, false, dataType,
			// externalParams, includeTimestamp, signatureForm,
			// signaturePolicyID, isCalledByFacade);
			/***********************************/
			byte[] outc = new byte[csize];
			PdfDictionary dic2 = new PdfDictionary();
			System.arraycopy(signedData, 0, outc, 0, signedData.length);
			dic2.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));
			signatureAppearance.close(dic2);

			
		} catch (DocumentException e) {
			LOGGER.error(e);
			throw new SignatureManagerException(e);
		} catch (IOException e) {
			LOGGER.error(e);
			throw new SignatureManagerException(e);
		} catch (GSSException e) {
			LOGGER.error(e);
			throw new SignatureManagerException(e);
		} finally {
			// Cerramos recursos
			UtilsResources.safeCloseOutputStream(bytesResult);
			LOGGER.info(Language.getMessage(LanguageKeys.PBS_LOG002));
		}
		byte[] result = bytesResult.toByteArray();
		GenericUtilsCommons.printResult(result, LOGGER);
		return result;
	}
	
	/**
     * Method that checks the input parameters for {@link #sign(byte[], String, String, PrivateKeyEntry, Properties, boolean, String, String)}.
     * @param data Parameter that represents the data to sign.
     * @param algorithm Parameter that represents the signature algorithm.
     * @param privateKey Parameter that represents the private key of the signing certificate.
     */
    private void checkSignInputParams(byte[ ] data, String algorithm, PrivateKeyEntry privateKey) {
	if (GenericUtilsCommons.checkNullValues(data, privateKey) || !GenericUtilsCommons.assertStringValue(algorithm)) {
	    final String errorMsg = Language.getMessage(LanguageKeys.PS_LOG001);
	    LOGGER.error(errorMsg);
	    throw new IllegalArgumentException(errorMsg);
	}
	if (!SignatureConstants.SIGN_ALGORITHMS_SUPPORT_CADES.containsKey(algorithm)) {
	    final String errorMsg = Language.getFormatMessage(LanguageKeys.PS_LOG002, new Object[ ] { algorithm });
	    LOGGER.error(errorMsg);
	    throw new IllegalArgumentException(errorMsg);
	}
    }
	
	/**
     * Method that defines the value of <code>/SubFilter</code> key for a PAdES signature.
     * @return The value of <code>/SubFilter</code> key for a PAdES signature. If the signature form is PAdES-Basic, <code>/SubFilter</code> key has
     * <code>"adbe.pkcs7.detached"</code> value. For PAdES-BES and PAdES-EPES <code>/SubFilter</code> key has <code>"ETSI.CAdES.detached"</code> value.
     */
	private PdfName defineSubFilterValue() {
		// Establecemos el valor de la clave /SubFilter. Para firmas
		// PAdES-BES y PAdES-EPES el valor es 'ETSI.CAdES.detached'
		PdfName subFilterValue = new PdfName("ETSI.CAdES.detached");

		return subFilterValue;
	}
    
    /**
	 * Method that gets the name of a digest algorithm by name or alias.
	 * @param pseudoName Parameter that represents the name or alias of the digest algorithm.
	 * @return the name of the digest algorithm.
	 */
	private static String getDigestAlgorithmName(final String pseudoName) {
		final String upperPseudoName = pseudoName.toUpperCase();
		if (upperPseudoName.equals("SHA") || upperPseudoName.startsWith("SHA1") || upperPseudoName.startsWith("SHA-1")) {
			return CryptoUtilCommons.HASH_ALGORITHM_SHA1;
		} else if (upperPseudoName.startsWith("SHA256") || upperPseudoName.startsWith("SHA-256")) {
			return CryptoUtilCommons.HASH_ALGORITHM_SHA256;
		} else if (upperPseudoName.startsWith("SHA384") || upperPseudoName.startsWith("SHA-384")) {
			return CryptoUtilCommons.HASH_ALGORITHM_SHA384;
		}
		return getDigestAlgorithmNameAux(pseudoName);
	}
	
	/**
	 * Method that gets the name of a digest algorithm by name or alias.
	 * @param pseudoName Parameter that represents the name or alias of the digest algorithm.
	 * @return the name of the digest algorithm.
	 */
	private static String getDigestAlgorithmNameAux(final String pseudoName) {
		final String upperPseudoName = pseudoName.toUpperCase();
		if (upperPseudoName.startsWith("SHA512") || upperPseudoName.startsWith("SHA-512")) {
			return CryptoUtilCommons.HASH_ALGORITHM_SHA512;
		} else if (upperPseudoName.startsWith("RIPEMD160") || upperPseudoName.startsWith("RIPEMD-160")) {
			return CryptoUtilCommons.HASH_ALGORITHM_SHA512;
		} else {
			throw new IllegalArgumentException(Language.getFormatMessage(LanguageKeys.CU_LOG002, new Object[ ] { pseudoName }));
		}
	}
    
    /**
	 * Method that obtains the hash computation from a array bytes.
	 * @param algorithm Parameter that represents the algorithm used in the hash computation.
	 * @param data Parameter that represents the source data.
	 * @return the hash value.
	 * @throws SigningException If a MessageDigestSpi implementation for the specified algorithm is not available
	 * from the specified Provider object.
	 */
	private static byte[ ] digest(final String algorithm, final byte[ ] data) throws SignatureManagerException {
		if (GenericUtilsCommons.assertStringValue(algorithm) && GenericUtilsCommons.assertArrayValid(data)) {
			if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
				Security.addProvider(new BouncyCastleProvider());
			}
			final Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
			try {
				final MessageDigest messageDigest = MessageDigest.getInstance(algorithm, provider);
				final ByteArrayInputStream bais = new ByteArrayInputStream(data);
				final byte[ ] tmp = new byte[NumberConstants.INT_1024];
				int length = 0;
				while ((length = bais.read(tmp, 0, tmp.length)) >= 0) {
					messageDigest.update(tmp, 0, length);
				}
				return messageDigest.digest();
			} catch (NoSuchAlgorithmException e) {
				throw new SignatureManagerException(Language.getFormatMessage(LanguageKeys.CU_LOG001, new Object[ ] { algorithm }), e);
			}
		}
		return null;
	}
		
}
