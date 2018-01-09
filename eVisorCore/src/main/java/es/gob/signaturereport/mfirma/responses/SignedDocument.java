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
 * <b>File:</b><p>es.gob.signaturereport.mfirma.responses.SignedDocument.java.</p>
 * <b>Description:</b><p> Class that contains a signed document.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>11/04/2012.</p>
 * @author Spanish Government.
 * @version 1.0, 11/04/2012.
 */
package es.gob.signaturereport.mfirma.responses;


/** 
 * <p>Class that contains a signed document.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 11/04/2012.
 */
public class SignedDocument {
	
	/**
	 * Attribute that represents the document content. 
	 */
	private byte[] content = null;
	
	/**
	 * Attribute that represents the MimeType of the document. 
	 */
	private String mimeType = null;
	
	/**
	 * Attribute that represents the document extension. 
	 */
	private String extension = null;

	/**
	 * Constructor method for the class SignedDocument.java.
	 * @param content	The document content.
	 * @param mimeType	MimeType.	
	 * @param extension Extension.
	 */
	public SignedDocument(byte[ ] content, String mimeType, String extension) {
		super();
		this.content = content;
		this.mimeType = mimeType;
		this.extension = extension;
	}

	
	/**
	 * Gets the value of the document content.
	 * @return the value of the document content.
	 */
	public byte[ ] getContent() {
		return content;
	}

	
	/**
	 * Sets the value of the document content.
	 * @param content The value for the document content.
	 */
	public void setContent(byte[ ] content) {
		this.content = content;
	}

	
	/**
	 * Gets the value of the MimeType.
	 * @return the value of the MimeType.
	 */
	public String getMimeType() {
		return mimeType;
	}

	
	/**
	 * Sets the value of the MimeType.
	 * @param mimeType The value for the MimeType.
	 */
	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	
	/**
	 * Gets the value of the document extension.
	 * @return the value of the document extension.
	 */
	public String getExtension() {
		return extension;
	}

	
	/**
	 * Sets the value of the document extension.
	 * @param extension The value for the document extension.
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}
}
