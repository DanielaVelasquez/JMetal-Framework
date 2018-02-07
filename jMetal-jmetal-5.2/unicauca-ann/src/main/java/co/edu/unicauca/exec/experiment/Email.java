
package co.edu.unicauca.exec.experiment;

import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class Email {


    public static void enviar(String destinatario, String asunto, String cuerpo)
    {
        String de = "bit.software.company@gmail.com";  //Para la dirección nomcuenta@gmail.com
        String clave = "unicaucacdu123";
        Properties prop = System.getProperties();
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", 587);

         Session session = Session.getInstance
            (prop,
                    new javax.mail.Authenticator() 
                    {
                        protected PasswordAuthentication getPasswordAuthentication() 
                        {
                            return new PasswordAuthentication(de, clave);
                        }
            });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(de));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setText("SOY LO MAXIMO");
            Transport.send(message);
        }
        catch (MessagingException me) {
            me.printStackTrace();   //Si se produce un error
        }
    }
    public static void main(String[] args)
    {
        Email.enviar("angiedanielav@unicauca.edu.co", "HOLA", "SIIII");
        
    }
    
}
