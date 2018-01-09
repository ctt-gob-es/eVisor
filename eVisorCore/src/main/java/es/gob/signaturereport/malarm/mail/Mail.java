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
 * <b>File:</b><p>es.gob.signaturereport.malarm.mail.Mail.java.</p>
 * <b>Description:</b><p> Class that represents a mail that will be sent.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>27/05/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 27/05/2011.
 */
package es.gob.signaturereport.malarm.mail;


/** 
 * <p>Class that represents a mail that will be sent.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 27/05/2011.
 */
public class Mail {
	
	/**
	 * Attribute that represents the email sender. 
	 */
	private String sender = null;
	
	/**
	 * Attribute that represents the list of mail receivers. 
	 */
	private String[] receivers = null;
	
	/**
	 * Attribute that represents the mail subject. 
	 */
	private String subject = null;
	
	/**
	 * Attribute that represents the mail message. 
	 */
	private String message = null;

	/**
	 * Constructor method for the class Mail.java. 
	 */
	public Mail() {
	}

	/**
	 * Constructor method for the class Mail.java.
	 * @param mailSender	Mail sender.
	 * @param mailReceivers	List of mail receivers.
	 * @param mailSubject	Mail subject.
	 * @param mailMessage 	Mail message.
	 */
	public Mail(String mailSender, String[ ] mailReceivers, String mailSubject, String mailMessage) {
		super();
		this.sender = mailSender;
		if(mailReceivers!=null){
			this.receivers = mailReceivers.clone();
		}
		this.subject = mailSubject;
		this.message = mailMessage;
	}

	
	/**
	 * Gets the value of the mail sender.
	 * @return the value of the mail sender.
	 */
	public String getSender() {
		return sender;
	}

	
	/**
	 * Sets the value of the mail sender.
	 * @param mailSender The value for the mail sender.
	 */
	public void setSender(String mailSender) {
		this.sender = mailSender;
	}

	
	/**
	 * Gets the value of the mail receivers.
	 * @return the value of the mail receivers.
	 */
	public String[ ] getReceivers() {
		return receivers;
	}

	
	/**
	 * Sets the value of the mail receivers.
	 * @param mailReceivers The value for the mail receivers.
	 */
	public void setReceivers(String[ ] mailReceivers) {
		if(mailReceivers!=null){
			this.receivers = mailReceivers.clone();
		}
	}

	
	/**
	 * Gets the value of the mail subject.
	 * @return the value of the mail subject.
	 */
	public String getSubject() {
		return subject;
	}

	
	/**
	 * Sets the value of the mail subject.
	 * @param mailSubject The value for the mail subject.
	 */
	public void setSubject(String mailSubject) {
		this.subject = mailSubject;
	}

	
	/**
	 * Gets the value of the mail message.
	 * @return the value of the mail message.
	 */
	public String getMessage() {
		return message;
	}

	
	/**
	 * Sets the value of the mail message.
	 * @param mailMessage The value for the mail message.
	 */
	public void setMessage(String mailMessage) {
		this.message = mailMessage;
	}
	
	
}
