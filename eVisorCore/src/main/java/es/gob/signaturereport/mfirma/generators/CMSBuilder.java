// Copyright (C) 2017 MINHAP, Gobierno de España
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
 * <b>File:</b><p>es.gob.afirma.signature.cades.SignedDataBuilder.java.</p>
 * <b>Description:</b><p>Class that manages the generation of CMS elements as defined on RFC 3852.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>13/09/2011.</p>
 * @author Gobierno de España.
 * @version 1.3, 21/03/2017.
 */
package es.gob.signaturereport.mfirma.generators;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Object;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.BERSet;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObject;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.DERUTCTime;
import org.bouncycastle.asn1.cms.Attribute;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.cms.CMSAttributes;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.asn1.cms.SignedData;
import org.bouncycastle.asn1.cms.SignerIdentifier;
import org.bouncycastle.asn1.cms.SignerInfo;
import org.bouncycastle.asn1.ess.ESSCertID;
import org.bouncycastle.asn1.ess.ESSCertIDv2;
import org.bouncycastle.asn1.ess.SigningCertificate;
import org.bouncycastle.asn1.ess.SigningCertificateV2;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.IssuerSerial;
import org.bouncycastle.asn1.x509.TBSCertificateStructure;
import org.bouncycastle.asn1.x509.X509CertificateStructure;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.util.Store;
import org.ietf.jgss.Oid;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.mfirma.SignatureConstants;
import es.gob.signaturereport.mfirma.SignatureManagerException;
import es.gob.signaturereport.tools.CryptoUtilPdfBc;
import es.gob.signaturereport.tools.GenericUtilsCommons;
import es.gob.signaturereport.tools.ICryptoUtil;
import es.gob.signaturereport.tools.UtilsResources;

/**
 * <p>Class that manages the generation of CMS elements as defined on RFC 3852.</p>
  * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 29/05/2017.
 */
public final class CMSBuilder {

	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(CMSBuilder.class);

    /**
     * <p>Enumeration class that represents different types of SignerInfo objects.</p>
     * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
     * @version 1.0, 27/01/2012.
     */
	enum SignerInfoTypes {
		/**
		 * Enum that represents type used for counter-signatures (signatures in
		 * parallels).
		 */
		COUNTERSIGNATURE,
		/**
		 * Enum that represents type used for co-signatures (signatures in
		 * serial).
		 */
		COSIGNATURE
	};

    /**
     * Method that calculates the ContentInfo.
     * @param includeContent Parameter that indicates whether the document content is included in the signature (true) or is only referenced (false).
     * @param dataType Parameter that represents the type of the content to sign.
     * @param parameters Parameters related to the signature.
     * @return an object that represents the ContentInfo.
     * @throws SigningException If the method fails.
     */
	private ContentInfo createContentInto(boolean includeContent, Oid dataType,
			P7ContentSignerParameters parameters) throws SignatureManagerException {
		ContentInfo encInfo = null;
		ASN1ObjectIdentifier contentTypeOID = new ASN1ObjectIdentifier(
				dataType.toString());

		if (includeContent) {
			LOGGER.debug(Language.getMessage(LanguageKeys.CMSB_LOG003));
			OutputStream bOut = new ByteArrayOutputStream();
			byte[] content2 = parameters.getContent();
			CMSProcessable msg = new CMSProcessableByteArray(content2);
			try {
				msg.write(bOut);
				encInfo = new ContentInfo(contentTypeOID, new DEROctetString(
						((ByteArrayOutputStream) bOut).toByteArray()));
			} catch (IOException ex) {
				throw new SignatureManagerException(
						Language.getMessage(LanguageKeys.CMSB_LOG004),
						ex);
			} catch (CMSException e) {
				throw new SignatureManagerException(
						Language.getMessage(LanguageKeys.CMSB_LOG004), e);
			} finally {
				UtilsResources.safeCloseOutputStream(bOut);
			}

		} else {
			LOGGER.debug(Language.getMessage(LanguageKeys.CMSB_LOG005));
			encInfo = new ContentInfo(contentTypeOID, null);
		}
		return encInfo;
	}

    /**
     *<p> Builds a signedData object used in CAdES signatures. SignedData is defined in the
     * <a href="http://tools.ietf.org/html/rfc3852">rfc3852</a>:<br>
     * <code><pre>
     * id-signedData OBJECT IDENTIFIER ::= { iso(1) member-body(2)
         us(840) rsadsi(113549) pkcs(1) pkcs7(7) 2 }
    
      SignedData ::= SEQUENCE {
        version CMSVersion,
        digestAlgorithms DigestAlgorithmIdentifiers,
        encapContentInfo EncapsulatedContentInfo,
        certificates [0] IMPLICIT CertificateSet OPTIONAL,
        crls [1] IMPLICIT RevocationInfoChoices OPTIONAL,
        signerInfos SignerInfos }
    
    </pre></code></p>
     * @param parameters parameters used in the signature.
     * @param includeContent indicates whether the document content is included in the signature or is only referenced.
     * @param dataType type of content to sign.
     * @param extraParams optional parameters.
     * @param includeTimestamp Parameter that indicates if the signature will have a timestamp (true) or not (false).
     * @return a sinedData object.
     * @param signatureForm Parameter that represents the signature form.
     * The allowed values for PAdES signature are:
     * <ul>
     * <li>{@link es.gob.afirma.signature.SignatureFormatDetectorCommons#FORMAT_PADES_BES}</li>
     * </ul>
     * @param signaturePolicyID Parameter that represents the identifier of the signature policy used for generate the signature with signature
     * policies. The identifier must be defined on the properties file where to configure the validation and generation of signatures with
     * signature policies.
     * @param idClient Parameter that represents the client application identifier.
     * @throws SigningException in error case.
     */
	@SuppressWarnings("restriction")
	public byte[] generateSignedData(
			final P7ContentSignerParameters parameters,
			final boolean includeContent, final Oid dataType,
			Properties extraParams, boolean includeTimestamp,
			String signatureForm, String signaturePolicyID, String idClient)
			throws SignatureManagerException {
		LOGGER.debug(Language.getMessage(LanguageKeys.CMSB_LOG001));
		if (GenericUtilsCommons.checkNullValues(parameters, dataType)) {
			throw new IllegalArgumentException(
					Language.getMessage(LanguageKeys.CMSB_LOG002));
		}
		Properties optionalParams = extraParams;
		if (optionalParams == null) {
			optionalParams = new Properties();
		}

		try {
			// 1. VERSION
			// la version se mete en el constructor del signedData y es 1

			// 2. DIGESTALGORITM
			// buscamos que tipo de algoritmo de digest es y lo codificamos con
			// su OID

			final ASN1EncodableVector digestAlgs = new ASN1EncodableVector();

			String digestAlgorithm = SignatureConstants.SIGN_ALGORITHMS_SUPPORT_CADES
					.get(parameters.getSignatureAlgorithm());

			final sun.security.x509.AlgorithmId digestAlgorithmId = sun.security.x509.AlgorithmId
					.get(digestAlgorithm);

			AlgorithmIdentifier digAlgId = makeAlgId(digestAlgorithmId.getOID()
					.toString(), digestAlgorithmId.getEncodedParams());

			digestAlgs.add(digAlgId);

			// 3. CONTENTINFO
			// si se introduce el contenido o no

			ContentInfo encInfo = createContentInto(includeContent, dataType,
					parameters);

			// 4. CERTIFICADOS
			// obtenemos la lista de certificados e incluimos el certificado
			// firmante

			X509Certificate signerCertificate = (X509Certificate) parameters
					.getPrivateKey().getCertificate();
			
			ASN1Set certificates = createBerSetFromList(X509CertificateStructure
					.getInstance(ASN1Object.fromByteArray(signerCertificate
							.getEncoded())));

			ASN1Set certrevlist = null;

			// 5. SIGNERINFO
			// raiz de la secuencia de SignerInfo
			ASN1EncodableVector signerInfos = new ASN1EncodableVector();

			TBSCertificateStructure tbs = TBSCertificateStructure
					.getInstance(ASN1Object.fromByteArray(signerCertificate
							.getTBSCertificate()));
			IssuerAndSerialNumber encSid = new IssuerAndSerialNumber(
					X500Name.getInstance(tbs.getIssuer()), tbs
							.getSerialNumber().getValue());

			SignerIdentifier identifier = new SignerIdentifier(encSid);

			// AlgorithmIdentifier
			digAlgId = new AlgorithmIdentifier(new DERObjectIdentifier(
					digestAlgorithmId.getOID().toString()), new DERNull());

			// Atributos firmados
			ASN1Set signedAttr = generateSignedAttr(parameters,
					digestAlgorithmId, digAlgId, digestAlgorithm, dataType,
					optionalParams, signatureForm, signaturePolicyID,
					includeContent, idClient);

			// Generamos la firma
			ASN1OctetString sign2 = sign(parameters.getSignatureAlgorithm(),
					parameters.getPrivateKey(), signedAttr);

			// Atributos no firmados
			ASN1Set unsignedAttr = null;

			// Validamos el certificado firmante respecto a la fecha actual
//			UtilsSignatureOp.validateCertificate(signerCertificate, Calendar
//					.getInstance().getTime(), false, idClient, false);

			// digEncryptionAlgorithm
			AlgorithmIdentifier encAlgId = new DefaultSignatureAlgorithmIdentifierFinder()
					.find(parameters.getSignatureAlgorithm());

			SignerInfo signerInfo = new SignerInfo(identifier, digAlgId,
					signedAttr, encAlgId, sign2, unsignedAttr);

			signerInfos.add(signerInfo);

			// construimos el Signed Data y lo devolvemos
			return new ContentInfo(PKCSObjectIdentifiers.signedData,
					new SignedData(new DERSet(digestAlgs), encInfo,
							certificates, certrevlist, new DERSet(signerInfos)))
					.getDEREncoded();
		} catch (CertificateException e) {
			throw new SignatureManagerException(
					Language.getMessage(LanguageKeys.CMSB_LOG006), e);
		} catch (NoSuchAlgorithmException e) {
			throw new SignatureManagerException(
					Language.getMessage(LanguageKeys.CMSB_LOG007), e);
		} catch (IOException e) {
			throw new SignatureManagerException(
					Language.getMessage(LanguageKeys.CMSB_LOG008), e);
		}

	}
  		

    /**
     * Generates a set of signed attributes used in CMS messages defined in <a href="http://tools.ietf.org/html/rfc3852">rfc3852</a>.
     * Definition:
     * <pre>
     SignedAttributes ::= SET SIZE (1..MAX) OF Attribute
    
      Attribute ::= SEQUENCE {
        attrType OBJECT IDENTIFIER,
        attrValues SET OF AttributeValue }
    
      AttributeValue ::= ANY
      </pre>
     * @param parameters parameters for signature process.
     * @param digestAlgorithmId digest algorithm identificator.
     * @param algId algorithm identificator.
     * @param digestAlgorithm digest algorithm name.
     * @param dataType data type oid identificator(Universal Object Identifiers) to sign.
     * @param extraParams optional parameters
     * @param signatureForm Parameter that represents the signature form.
     * The allowed values for PAdES signature are:
     * <ul>
     * <li>{@link es.gob.afirma.signature.SignatureFormatDetectorCommons#FORMAT_PADES_BES}</li>
     * </ul>
     * @param signaturePolicyID Parameter that represents the identifier of the signature policy used for generate the signature with signature
     * policies. The identifier must be defined on the properties file where to configure the validation and generation of signatures with
     * signature policies.
     * @param includeContent Parameter that indicates whether the document content is included in the signature (true) or is only referenced (false).
     * @param idClient Parameter that represents the client application identifier.
     * @return a SignerInfo object.
     * @throws SigningException throws in error case.
     */
	@SuppressWarnings("restriction")
	private ASN1Set generateSignedAttr(P7ContentSignerParameters parameters,
			sun.security.x509.AlgorithmId digestAlgorithmId,
			AlgorithmIdentifier algId, String digestAlgorithm, Oid dataType,
			Properties extraParams, String signatureForm,
			String signaturePolicyID, boolean includeContent, String idClient)
			throws SignatureManagerException {

		try {
			boolean isPadesSigner = extraParams
					.get(SignatureConstants.SIGN_FORMAT_PADES) == null ? false
					: true;
			X509Certificate cert = (X509Certificate) parameters.getPrivateKey()
					.getCertificate();

			// // ATRIBUTOS

			// authenticatedAttributes
			ASN1EncodableVector contexExpecific = new ASN1EncodableVector();

			// tipo de contenido
			contexExpecific.add(new Attribute(CMSAttributes.contentType,
					new DERSet(new DERObjectIdentifier(dataType.toString()))));

			// fecha de firma
			if (!isPadesSigner) {
				contexExpecific.add(new Attribute(CMSAttributes.signingTime,
						new DERSet(new DERUTCTime(Calendar.getInstance()
								.getTime()))));
			}

			// Digest del documento
			byte[] messageDigest = null;
			// Si el valor del digest viene externo lo incluimos directamente en
			// los atributos.
			if (parameters.getDigestValue() != null) {
				messageDigest = parameters.getDigestValue();
			} else { // si no lo calculamos a partir de los datos del documento
				// original.
				messageDigest = CryptoUtilPdfBc.digest(digestAlgorithm,
						parameters.getContent());
			}
			contexExpecific.add(new Attribute(CMSAttributes.messageDigest,
					new DERSet(new DEROctetString(messageDigest))));

			if (!digestAlgorithm.equals(ICryptoUtil.HASH_ALGORITHM_SHA1)) {

				// INICIO SIGNING CERTIFICATE-V2

				/**
				 * IssuerSerial ::= SEQUENCE { issuer GeneralNames, serialNumber
				 * CertificateSerialNumber
				 * 
				 */

				TBSCertificateStructure tbs = TBSCertificateStructure
						.getInstance(ASN1Object.fromByteArray(cert
								.getTBSCertificate()));
				GeneralName gn = new GeneralName(tbs.getIssuer());
				GeneralNames gns = new GeneralNames(gn);

				IssuerSerial isuerSerial = new IssuerSerial(gns,
						tbs.getSerialNumber());

				/**
				 * ESSCertIDv2 ::= SEQUENCE { hashAlgorithm AlgorithmIdentifier
				 * DEFAULT {algorithm id-sha256}, certHash Hash, issuerSerial
				 * IssuerSerial OPTIONAL }
				 * 
				 * Hash ::= OCTET STRING
				 */

				MessageDigest md = MessageDigest.getInstance(CryptoUtilPdfBc
						.getDigestAlgorithmName(digestAlgorithmId.getName()));
				byte[] certHash = md.digest(cert.getEncoded());
				ESSCertIDv2[] essCertIDv2 = { new ESSCertIDv2(algId, certHash,
						isuerSerial) };

				/**
				 * PolicyInformation ::= SEQUENCE { policyIdentifier
				 * CertPolicyId, policyQualifiers SEQUENCE SIZE (1..MAX) OF
				 * PolicyQualifierInfo OPTIONAL }
				 * 
				 * CertPolicyId ::= OBJECT IDENTIFIER
				 * 
				 * PolicyQualifierInfo ::= SEQUENCE { policyQualifierId
				 * PolicyQualifierId, qualifier ANY DEFINED BY policyQualifierId
				 * }
				 * 
				 */

				SigningCertificateV2 scv2 = new SigningCertificateV2(
						essCertIDv2); // Sin

				// Secuencia con singningCertificate
				contexExpecific.add(new Attribute(
						PKCSObjectIdentifiers.id_aa_signingCertificateV2,
						new DERSet(scv2)));

				// FIN SINGING CERTIFICATE-V2

			} else {

				// INICIO SINGNING CERTIFICATE

				/**
				 * IssuerSerial ::= SEQUENCE { issuer GeneralNames, serialNumber
				 * CertificateSerialNumber }
				 */

				TBSCertificateStructure tbs = TBSCertificateStructure
						.getInstance(ASN1Object.fromByteArray(cert
								.getTBSCertificate()));
				GeneralName gn = new GeneralName(tbs.getIssuer());
				GeneralNames gns = new GeneralNames(gn);

				IssuerSerial isuerSerial = new IssuerSerial(gns,
						tbs.getSerialNumber());

				/**
				 * ESSCertID ::= SEQUENCE { certHash Hash, issuerSerial
				 * IssuerSerial OPTIONAL }
				 * 
				 * Hash ::= OCTET STRING -- SHA1 hash of entire certificate
				 */
				// MessageDigest
				String digestAlgorithmName = CryptoUtilPdfBc
						.getDigestAlgorithmName(digestAlgorithmId.getName());
				MessageDigest md = MessageDigest
						.getInstance(digestAlgorithmName);
				byte[] certHash = md.digest(cert.getEncoded());
				ESSCertID essCertID = new ESSCertID(certHash, isuerSerial);

				/**
				 * PolicyInformation ::= SEQUENCE { policyIdentifier
				 * CertPolicyId, policyQualifiers SEQUENCE SIZE (1..MAX) OF
				 * PolicyQualifierInfo OPTIONAL }
				 * 
				 * CertPolicyId ::= OBJECT IDENTIFIER
				 * 
				 * PolicyQualifierInfo ::= SEQUENCE { policyQualifierId
				 * PolicyQualifierId, qualifier ANY DEFINED BY policyQualifierId
				 * }
				 * 
				 */

				SigningCertificate scv = new SigningCertificate(essCertID); // Sin
				// politica

				/**
				 * id-aa-signingCertificate OBJECT IDENTIFIER ::= { iso(1)
				 * member-body(2) us(840) rsadsi(113549) pkcs(1) pkcs9(9)
				 * smime(16) id-aa(2) 12 }
				 */
				// Secuencia con singningCertificate
				contexExpecific.add(new Attribute(
						PKCSObjectIdentifiers.id_aa_signingCertificate,
						new DERSet(scv)));
			}

			return getAttributeSet(new AttributeTable(contexExpecific));
		} catch (CertificateException e) {
			throw new SignatureManagerException(
					Language.getMessage(LanguageKeys.CMSB_LOG006), e);
		} catch (NoSuchAlgorithmException e) {
			throw new SignatureManagerException(
					Language.getMessage(LanguageKeys.CMSB_LOG007), e);
		} catch (IOException e) {
			throw new SignatureManagerException(
					Language.getMessage(LanguageKeys.CMSB_LOG008), e);
		}
	}

    /**
     * Performs sign of signed attributes.
     * @param signatureAlgorithm signature algorithm
     * @param keyEntry private key.
     * @param signedAttributes signed attributes of SignedInfo.
     * @return signature in ASN1OctetString format.
     * @throws SigningException in error case.
     */
    private ASN1OctetString sign(String signatureAlgorithm, PrivateKeyEntry keyEntry, ASN1Set signedAttributes) throws SignatureManagerException {

	Signature sig = null;
	try {
	    sig = Signature.getInstance(signatureAlgorithm);
	} catch (Exception e) {
	    throw new SignatureManagerException(Language.getFormatMessage(LanguageKeys.CMSB_LOG011, new Object[ ] { signatureAlgorithm }), e);
	}

	byte[ ] tmp = null;

	try {
	    tmp = signedAttributes.getEncoded(ASN1Encodable.DER);
	} catch (IOException ex) {
	    LOGGER.error(ex);
	    throw new SignatureManagerException(Language.getMessage(LanguageKeys.CMSB_LOG012), ex);
	}

	// Indicar clave privada para la firma
	try {
	    sig.initSign(keyEntry.getPrivateKey());
	} catch (InvalidKeyException e) {
	    throw new SignatureManagerException(Language.getFormatMessage(LanguageKeys.CMSB_LOG013, new Object[ ] { signatureAlgorithm }), e);
	}

	// Actualizamos la configuracion de firma
	try {
	    sig.update(tmp);
	} catch (SignatureException e) {
	    throw new SignatureManagerException(Language.getMessage(LanguageKeys.CMSB_LOG014), e);
	}

	// firmamos.
	byte[ ] realSig = null;
	try {
	    realSig = sig.sign();
	} catch (SignatureException e) {
	    throw new SignatureManagerException(Language.getMessage(LanguageKeys.CMSB_LOG015), e);
	}

	ASN1OctetString encDigest = new DEROctetString(realSig);

	return encDigest;

    }

   

    

	/**
	 * Converts a {@link SignerInformation} store to a set of {@link SignerInfo}
	 * .
	 * 
	 * @param signerInfos
	 *            store with a collection of SignatureInformation objects.
	 * @return a set of {@link SignerInfo} objects.
	 */
	ASN1Set convertToASN1Set(SignerInformationStore signerInfos) {
		ASN1EncodableVector result = new ASN1EncodableVector();
		for (Object signerInformation : signerInfos.getSigners()) {
			result.add(((SignerInformation) signerInformation)
					.toASN1Structure());
		}
		return new DERSet(result);
	}

	/**
	 * Converts a certificate store to a set of
	 * {@link org.bouncycastle.asn1.x509.X509CertificateStructure
	 * X509CertificateStructure}.
	 * 
	 * @param store
	 *            store with a collection of {@link X509CertificateHolder}
	 * @return a ASN1Set with a collection of
	 *         certificates(X509CertificateStructure objects).
	 */
	ASN1Set convertCertStoreToASN1Set(Store store) {
		ASN1EncodableVector asn1Vector = new ASN1EncodableVector();
		for (Object element : store.getMatches(null)) {
			if (element instanceof X509CertificateHolder) {
				asn1Vector.add(((X509CertificateHolder) element)
						.toASN1Structure());
			}
		}
		return new DERSet(asn1Vector);
	}

    /**
     * Adds a new element to a ASN1Set list.
     * @param set list of ASN1Encodable elements.
     * @param element ASN1Encodable object to add.
     * @return a new ASN1Set with element included.
     */
	ASN1Set addElementToASN1Set(ASN1Set set, ASN1Encodable element) {
		ASN1Encodable[] arrayTmp = set.toArray();
		ASN1Encodable[] newArray = new ASN1Encodable[arrayTmp.length + 1];
		System.arraycopy(arrayTmp, 0, newArray, 0, arrayTmp.length);
		newArray[newArray.length - 1] = element;
		return new DERSet(newArray);
	}

    /**
     * Obtains the OID identifier associated to digest algorithm given.
     * @param digestAlg digest algorithm.
     * @return the OID identifier associated to digest algorithm.
     * @throws SigningException if algorithm given is wrong.
     */
	@SuppressWarnings("restriction")
	AlgorithmIdentifier makeDigestAlgorithmId(String digestAlg)
			throws SignatureManagerException {
		try {
			sun.security.x509.AlgorithmId digestAlgorithmId = sun.security.x509.AlgorithmId
					.get(digestAlg);
			return makeAlgId(digestAlgorithmId.getOID().toString(),
					digestAlgorithmId.getEncodedParams());
		} catch (NoSuchAlgorithmException e) {
			throw new SignatureManagerException(
					Language.getMessage(LanguageKeys.CMSB_LOG007), e);
		} catch (IOException e) {
			throw new SignatureManagerException(
					Language.getMessage(LanguageKeys.CMSB_LOG007), e);
		}
	}

    /**
     * Method that obtains the identifier from the OID of an algorithm.
     * @param oid Parameter that represents the OID of the algorithm.
     * @param params Set of elements to identify the algorithm.
     * @return the found identifier.
     * @throws IOException If the method fails.
     */
	private AlgorithmIdentifier makeAlgId(String oid, byte[] params)
			throws IOException {
		if (params != null) {
			return new AlgorithmIdentifier(new DERObjectIdentifier(oid),
					makeObj(params));
		}
		return new AlgorithmIdentifier(new DERObjectIdentifier(oid),
				new DERNull());
	}

    /**
     * Method that generates an ASN.1 object from the bytes array.
     * @param encoding Parameter that represents the ASN.1 object.
     * @return an object that represents the ASN.1 element.
     * @throws IOException If the method fails.
     */
	@SuppressWarnings("resource")
	private DERObject makeObj(byte[] encoding) throws IOException {
		if (encoding == null) {
			LOGGER.warn(Language.getMessage(LanguageKeys.CMSB_LOG017));
			return null;
		}
		return new ASN1InputStream(new ByteArrayInputStream(encoding))
				.readObject();
	}

    /**
     * Method that obtains a {@link ASN1Set} from a table of attributes.
     * @param attr Parameter that represents the table of attributes.
     * @return the generated element.
     */
	private ASN1Set getAttributeSet(AttributeTable attr) {
		if (attr != null) {
			return new DERSet(attr.toASN1EncodableVector());
		}
		LOGGER.warn(Language.getMessage(LanguageKeys.CMSB_LOG018));
		return null;
	}

    /**
     * Method that generates a {@link ASN1Set} from a DER element.
     * @param derObject Parameter that represents the DER element.
     * @return the generated element.
     */
	private ASN1Set createBerSetFromList(DEREncodable derObject) {
		ASN1EncodableVector v = new ASN1EncodableVector();
		v.add(derObject);
		return new BERSet(v);
	}

       
        
}
