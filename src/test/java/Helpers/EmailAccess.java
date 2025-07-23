package Helpers;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import junit.framework.Assert;
import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import org.mortbay.jetty.servlet.AbstractSessionManager;

import javax.mail.*;
import javax.mail.internet.MimeMessage;

import static org.openqa.selenium.devtools.v137.webauthn.WebAuthn.getCredentials;

public class EmailAccess {

    private static final String APPLICATION_NAME = "NaukriSeleniumTest";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static Date yesterdaysDate() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
    private static DateFormat df2 = new SimpleDateFormat("yyyy/MM/dd");

    private static String user = "me";
    private static String query = "";
    private static String finalEmailLink = null;
    private static String mailContent = null;
    private static String messageFrom;
    private static String subject;
    private static Date receivedTime;
    private static Date sentTimeFormated;
    private static int mailRenderCount = 0;

    public static String getfinalEmailLink() {
        return finalEmailLink;
    }

    public static void setfinalEmailLink(String value) {
        finalEmailLink = value;
    }
    public static String getEmailQuery() {
        query = "IN:INBOX IS:UNREAD AFTER:"+df2.format(yesterdaysDate());
        return query;
    }

    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.MAIL_GOOGLE_COM);
    private static final String CREDENTIALS_FILE_PATH = System.getProperty("user.dir") + "/src/main/credentials.json";


    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static String ReadEmailWithSubject(String emailId, String mailSubject, Date sentTimeDate, String folderName) throws IOException, GeneralSecurityException, ParseException, InterruptedException, MessagingException {
        DateFormat df = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z");
        String sentTime = df.format(sentTimeDate);

        while (mailContent == null) {
            Hooks.forcedWait(10000);

            // Build a new authorized API client service.
            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            Gmail service = new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(APPLICATION_NAME)
                    .build();


            ListMessagesResponse response = service.users().messages().list(user).setQ(getEmailQuery()).execute();
            while (response.getMessages() != null) {
                for (int l = 0; l <= response.getMessages().size() - 1; l++) {
                    Message message = response.getMessages().get(l);

                    Message messageS = service.users().messages().get(user, message.getId()).execute();
                    //System.out.println("Message snippet: " + messageS.getPayload().getHeaders());
                    messageFrom = messageS.getPayload().getHeaders().get(0).getValue();
                    for (int i = 0; i < messageS.getPayload().getHeaders().size(); i++) {
                        if ((messageS.getPayload().getHeaders().get(i).getName().equals("Subject"))) {
                            subject = messageS.getPayload().getHeaders().get(i).getValue().trim();
                        }

                        if ((messageS.getPayload().getHeaders().get(i).getName().equals("Date"))) {
                            //DateFormat df1 = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z (z)");
                            //df1.setTimeZone(TimeZone.getTimeZone("GMT"));
                            receivedTime = df.parse(messageS.getPayload().getHeaders().get(i).getValue());
                        }
                    }

//********************************************* Start Reading EMAIL and Attachments*************************************************************//
                    if (messageFrom.equals(emailId)) {
                        if (subject.equals(mailSubject)) {
                            //DateFormat df3 = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss Z (z)");
                            //df3.setTimeZone(TimeZone.getTimeZone("GMT"));
                            sentTimeFormated = df.parse(sentTime);

                            if (receivedTime.compareTo(sentTimeFormated) > 0 || receivedTime.compareTo(sentTimeFormated) == 0) {
                                System.out.println("Message To: " + messageFrom);
                                System.out.println("Message Subject: " + subject);
                                System.out.println("Message Sent Time: " + (sentTimeFormated));
                                System.out.println("Message Recv Time: " + (receivedTime));
                                Message messageF = service.users().messages().get(user, messageS.getId()).setFormat("raw").execute();
                                //Message messageF = service.users().messages().get(user, (messages.get(0)).getThreadId()).setFormat("raw").execute();
                                Base64 base64Url = new Base64(true);
                                byte[] emailBytes = base64Url.decodeBase64(messageF.getRaw());

                                Properties props = new Properties();
                                Session session = Session.getDefaultInstance(props, null);

                                MimeMessage email = new MimeMessage(session, new ByteArrayInputStream(emailBytes));

                                Multipart multipart = (Multipart) email.getContent();
                                File mailExtractFolder = new File("target/" + "//mail_attachments//" + folderName);
                                mailExtractFolder.mkdirs();
                                for (int j = 0; j < multipart.getCount(); j++) {
                                    BodyPart bodyPart = multipart.getBodyPart(j);
                                    //System.out.println("Body: "+ j + bodyPart.getContent());
                                    if (bodyPart.getContent().toString().contains("com.sun.mail.util.BASE64DecoderStream") || bodyPart.getContent().toString().contains("SharedByteArrayInputStream")) {
                                        String filename = bodyPart.getFileName();
                                        //System.out.println("content type" + bodyPart.getContentType());
                                        String filePath = System.getProperty("user.dir") + "/target/mail_attachments/" + folderName;
                                        InputStream is = bodyPart.getInputStream();
                                        // -- EDIT -- SECURITY ISSUE --
                                        // do not do this in production code -- a malicious email can easily contain this filename: "../etc/passwd", or any other path: They can overwrite _ANY_ file on the system that this code has write access to!
                                        File file = new File(filePath + "\\" + filename);
                                        FileOutputStream fileOutFile = new FileOutputStream(file);
                                        byte[] buf = new byte[4096];
                                        int bytesRead;
                                        while ((bytesRead = is.read(buf)) != -1) {
                                            fileOutFile.write(buf, 0, bytesRead);
                                        }
                                        fileOutFile.close();
                                    } else {
                                        mailContent = bodyPart.getContent().toString();
                                        File file = new File(System.getProperty("user.dir") + "/target/mail_attachments/" + folderName + "\\" + "mail.html");
                                        FileWriter fWriter = new FileWriter(file);
                                        BufferedWriter writer = new BufferedWriter(fWriter);
                                        writer.write(mailContent);
                                        writer.newLine(); //this is not actually needed for html files - can make your code more readable though
                                        writer.close(); //make sure you close the writer object
                                    }
                                }

                            }
                        }
                        break;
                    }
                }
                if (response.getNextPageToken() != null && mailContent == null) {
                    String pageToken = response.getNextPageToken();
                    response = service.users().messages().list(user).setQ(getEmailQuery())
                            .setPageToken(pageToken).execute();
                } else {
                    break;
                }

            }
            mailRenderCount++;
            System.out.println("Unformatted sent time: " + sentTimeDate);
            System.out.println("mailRenderCount:" + mailRenderCount);
            System.out.println("Message Sent Time: " + (sentTimeFormated));
            if (mailRenderCount >= 10)
                break;
        }
        mailRenderCount = 0;
        Assert.assertNotNull("Mail not received in 5 minutes", mailContent);
        return mailContent;
    }

}
