package utilitaire;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class GestionMail {
	
	/**
	 * @param mailFrom Nom de l'utilisateur emetteur
	 * @param mailTo adresse de l'utilisateur destinataire
	 * @param objet objet du mail
	 * @param text	texte du mail
	 * @return succesStatut
	 */
	public static boolean send(String mailFrom, String mailTo , String objet, String text){    		
		final String from = "clicknsleep@gmail.com";
		final String pwd = "teamBifle";
		
		Properties props = new Properties();
		props.put("mail.transport.protocal","smtp");
		props.put("mail.host","smtp.gmail.com");
		props.put("mail.port","587");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");

		Authenticator authentication = new Authenticator(){
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				PasswordAuthentication pwdAuth = new PasswordAuthentication(from, pwd);
				return pwdAuth;
			}
		};
		
		Session session = Session.getDefaultInstance(props, authentication);
		try{
			Transport transport = session.getTransport();
			MimeMessage message = new MimeMessage(session);

			message.setFrom(new InternetAddress(from));

			message.addRecipient(Message.RecipientType.TO,
					new InternetAddress(mailTo));

			message.setSubject(objet);

			message.setText(mailFrom+" vous envoit le message suivant : \n"+text);

			transport.connect();
			transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
			transport.close();
			System.out.println("Sent message successfully....");
			return true;
		}catch (MessagingException mex) {
			mex.printStackTrace();
		}
		return false;
	}

}
