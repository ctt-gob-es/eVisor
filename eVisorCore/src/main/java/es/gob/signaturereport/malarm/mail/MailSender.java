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
 * <b>File:</b><p>es.gob.signaturereport.malarm.mail.MailSender.java.</p>
 * <b>Description:</b><p>Class that allows send a mail.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * <b>Date:</b><p>27/05/2011.</p>
 * @author Spanish Government.
 * @version 1.0, 27/05/2011.
 */
package es.gob.signaturereport.malarm.mail;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import es.gob.signaturereport.language.Language;
import es.gob.signaturereport.language.LanguageKeys;

/** 
 * <p>Class that allows send a mail.</p>
 * <b>Project:</b><p>Horizontal platform to generation signature reports in legible format.</p>
 * @version 1.0, 27/05/2011.
 */
public class MailSender extends Thread {
	
	/**
	 * Attribute that represents the object that manages the log of the class.
	 */
	private static final Logger LOGGER = Logger.getLogger(MailSender.class);
	
	/**
	 * Attribute that represents the key used to identifies the 'SMTP' host. 
	 */
	private static final String SMTP_HOST = "mail.smtp.host";
	
	/**
	 * Attribute that represents the key used to identifies the 'SMTP' port. 
	 */
	private static final String SMTP_PORT ="mail.smtp.port";
	
	/**
	 * Attribute that represents the key used to indicates if the 'SMTP' server needs authentication. 
	 */
	private static final String SMTP_AUTH = "mail.smtp.auth";
	
	/**
	 * Attribute that represents the 'SMTP' protocol. 
	 */
	private static final String SMTP_PROTOCOL = "smtp";
	/**
	 * Attribute that represents a mail. 
	 */
	private Mail mail = null;

	/**
	 * Attribute that represents the configuration of mail server. 
	 */
	private SMTPMailServer serverConf = null;

	/**
	 * Constructor method for the class MailSender.java.
	 * @param mailData	Mail to send.
	 * @param conf	Configuration of mail server. 
	 */
	public MailSender(Mail mailData, SMTPMailServer conf) {
		this.mail = mailData;
		this.serverConf = conf;
	}

	/**
	 * {@inheritDoc}
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		super.run();
		send();
	}

	/**
	 * Send the mail.
	 */
	private void send(){
		Properties props = new Properties();
    	props.put(SMTP_HOST, this.serverConf.getHost());
    	props.put(SMTP_PORT,this.serverConf.getPort());
    	props.put(SMTP_AUTH,String.valueOf(this.serverConf.getUser()!=null));
    	
    	Session session = Session.getInstance(props);
    	
    	Message msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(this.mail.getSender()));
			InternetAddress[] address = new InternetAddress[this.mail.getReceivers().length];
    		
    		// Se construye el array de destinatarios del mensaje
    		for(int i=0; i < this.mail.getReceivers().length; i++){
    			address[i] = new InternetAddress(this.mail.getReceivers()[i]);
    		}
    		msg.setRecipients(Message.RecipientType.TO, address);
    		msg.setSubject(this.mail.getSubject());
    		msg.setSentDate(new Date());
    		msg.setText(this.mail.getMessage());
    		// Se intenta la conexi�n con el servidor de correo
    		Transport transport = session.getTransport(SMTP_PROTOCOL);
    		transport.connect(this.serverConf.getHost(),this.serverConf.getUser(),this.serverConf.getPassword());
    		
    		// Se efectña el env�o el mensaje
    		transport.sendMessage(msg,msg.getAllRecipients());
    		transport.close();
		} catch (AddressException e) {
			LOGGER.error(Language.getMessage(LanguageKeys.AL_009),e);
		} catch (MessagingException e) {
			LOGGER.error(Language.getMessage(LanguageKeys.AL_010),e);
		}
    	
	}

	
}
