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


package es.gob.signaturereport.tools;

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.X509ObjectIdentifiers;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;
import es.gob.signaturereport.mfirma.SignatureManagerException;
import es.gob.signaturereport.persistence.utils.NumberConstants;

/**
 * <b>File:</b><p>es.gob.signaturereport.tools.CryptoUtil.java.</p>
 * <b>Description:</b><p> Utility class contains encryption and hash functions for digital signature.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 29/05/2017.
 */
public final class CryptoUtilPdfBc {

	/**
	 * Constructor method for the class CryptoUtil.java.
	 */
	private CryptoUtilPdfBc() {
	}

	/**
	 * Method that obtains the hash computation from a array bytes.
	 * @param algorithm Parameter that represents the algorithm used in the hash computation.
	 * @param data Parameter that represents the source data.
	 * @return the hash value.
	 * @throws SigningException If a MessageDigestSpi implementation for the specified algorithm is not available
	 * from the specified Provider object.
	 */
	public static byte[ ] digest(String algorithm, byte[ ] data) throws SignatureManagerException {
		if (GenericUtilsCommons.assertStringValue(algorithm) && GenericUtilsCommons.assertArrayValid(data)) {
			if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
				Security.addProvider(new BouncyCastleProvider());
			}
			Provider provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME);
			try {
				MessageDigest messageDigest = MessageDigest.getInstance(algorithm, provider);
				ByteArrayInputStream bais = new ByteArrayInputStream(data);
				byte[ ] tmp = new byte[NumberConstants.INT_1024];
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

	/**
	 * Method that translates a {@link AlgorithmIdentifier} object to a digest algorithm string.
	 * @param algorithmIdentifier Parameter that represents the OID of the digest algorithm.
	 * @return the digest algorithm name.
	 */
	public static String translateAlgorithmIdentifier(AlgorithmIdentifier algorithmIdentifier) {
		if (algorithmIdentifier == null) {
			return null;
		}
		ASN1ObjectIdentifier algId = algorithmIdentifier.getAlgorithm();
		if (OIWObjectIdentifiers.idSHA1.equals(algId)) {
			return CryptoUtilCommons.HASH_ALGORITHM_SHA1;
		} else if (NISTObjectIdentifiers.id_sha256.equals(algId)) {
			return CryptoUtilCommons.HASH_ALGORITHM_SHA256;
		} else if (NISTObjectIdentifiers.id_sha384.equals(algId)) {
			return CryptoUtilCommons.HASH_ALGORITHM_SHA384;
		} else if (NISTObjectIdentifiers.id_sha512.equals(algId)) {
			return CryptoUtilCommons.HASH_ALGORITHM_SHA512;
		} else if (X509ObjectIdentifiers.ripemd160.equals(algId)) {
			return CryptoUtilCommons.HASH_ALGORITHM_RIPEMD160;
		} else {
			return null;
		}
	}

	/**
	 * Method that obtains the OID of a hash algorithm from the name.
	 * @param hashAlgorithm Parameter that represents the name of the hash algorithm.
	 * @return the OID value.
	 */
	public static AlgorithmIdentifier getAlgorithmIdentifierByName(String hashAlgorithm) {
		if (hashAlgorithm == null) {
			return null;
		}
		if (hashAlgorithm.equals(CryptoUtilCommons.HASH_ALGORITHM_SHA1)) {
			return new AlgorithmIdentifier(OIWObjectIdentifiers.idSHA1);
		} else if (hashAlgorithm.equals(CryptoUtilCommons.HASH_ALGORITHM_SHA256)) {
			return new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha256);
		} else if (hashAlgorithm.equals(CryptoUtilCommons.HASH_ALGORITHM_SHA384)) {
			return new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha384);
		} else if (hashAlgorithm.equals(CryptoUtilCommons.HASH_ALGORITHM_SHA512)) {
			return new AlgorithmIdentifier(NISTObjectIdentifiers.id_sha512);
		} else if (hashAlgorithm.equals(CryptoUtilCommons.HASH_ALGORITHM_RIPEMD160)) {
			return new AlgorithmIdentifier(X509ObjectIdentifiers.ripemd160);
		} else {
			return null;
		}
	}

	/**
	 * Method that gets the name of a digest algorithm by name or alias.
	 * @param pseudoName Parameter that represents the name or alias of the digest algorithm.
	 * @return the name of the digest algorithm.
	 */
	public static String getDigestAlgorithmName(String pseudoName) {
		String upperPseudoName = pseudoName.toUpperCase();
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
	private static String getDigestAlgorithmNameAux(String pseudoName) {
		String upperPseudoName = pseudoName.toUpperCase();
		if (upperPseudoName.startsWith("SHA512") || upperPseudoName.startsWith("SHA-512")) {
			return CryptoUtilCommons.HASH_ALGORITHM_SHA512;
		} else if (upperPseudoName.startsWith("RIPEMD160") || upperPseudoName.startsWith("RIPEMD-160")) {
			return CryptoUtilCommons.HASH_ALGORITHM_SHA512;
		} else {
			throw new IllegalArgumentException(Language.getFormatMessage(LanguageKeys.CU_LOG002, new Object[ ] { pseudoName }));
		}
	}
}
