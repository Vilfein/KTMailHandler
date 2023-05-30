

import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage
import kotlin.collections.ArrayList

class MailHandler(var myAccountEmail:String,var password:String) {
    private  val properties = Properties()

    init{
        properties["mail.store.protocol"] = "imaps"
        properties["mail.imaps.host"] = "imap.forpsi.com"
        properties["mail.imaps.port"] =  "993"
        properties["mail.imaps.ssl.enable"] = "true"
        properties["mail.imaps.ssl.trust"] = "*"

        properties["mail.smtp.auth"] = "true"
        // properties.put("mail.smtp.starttls.enable","true");//pro google?
        properties["mail.smtp.host"] = "smtp.forpsi.com"
        properties["mail.smtp.port"] = "587"

    }

    //var myAccountEmail = "postmaster@vasekdoskar.cz"
    //val password = "x9VVS2cC-V"
//kotlin

    fun sendMail(recepient: String, subject:String,body:String) {
        println("odesílám zprávu")
        //val properties = Properties()
        properties["mail.smtp.auth"] = "true"
        // properties.put("mail.smtp.starttls.enable","true");//pro google?
        properties["mail.smtp.host"] = "smtp.forpsi.com"
        properties["mail.smtp.port"] = "587"

        val session: Session = Session.getInstance(properties,  object: Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(myAccountEmail,password)
            }
        })
        val message: Message? = prepareMessage(session, myAccountEmail, recepient, msg_to_send(body,subject))
        Transport.send(message)
        println("MSG odeslána")
    }

    fun ReceiverSettings(host:String,port:Int){
        properties["mail.store.protocol"] = "imaps"
        properties["mail.imaps.host"] = host//"imap.forpsi.com"
        properties["mail.imaps.port"] =  port.toString()//"993"
        properties["mail.imaps.ssl.enable"] = "true"
        properties["mail.imaps.ssl.trust"] = "*"
    }

    fun SenderSettings(host:String,port:Int){
        properties["mail.smtp.auth"] = "true"
        // properties.put("mail.smtp.starttls.enable","true");//pro google?
        properties["mail.smtp.host"] = host//"smtp.forpsi.com"
        properties["mail.smtp.port"] = port.toString()//"587"
    }

    private fun prepareMessage(session: Session, myAccountEmail: String, recepient: String, msg:msg_to_send): Message? {
        try {
            val message: Message = MimeMessage(session)
            message.setFrom(InternetAddress(myAccountEmail))
            message.setRecipient(Message.RecipientType.TO, InternetAddress(recepient))
            message.setSubject(msg.subject)
            message.setText(msg.body)
            return message
        } catch (ex: Exception) {
            println(ex.message)
        }
        return null

    }


    fun GetEmails():ArrayList<msg_to_read>{
        // Nastavení vlastností pro připojení k emailovému serveru
        val mails = ArrayList<msg_to_read>()
        val properties = Properties()
        properties["mail.store.protocol"] = "imaps"
        properties["mail.imaps.host"] = "imap.forpsi.com"
        properties["mail.imaps.port"] = "993"
        properties["mail.imaps.ssl.enable"] = "true"
        properties["mail.imaps.ssl.trust"] = "*"
        try {
            // Připojení k emailovému serveru
            val session: Session = Session.getDefaultInstance(properties)
            val store: Store = session.getStore("imaps")
            store.connect(myAccountEmail, password)

            // Získání složky INBOX
            val folder: Folder = store.getFolder("INBOX")
            folder.open(Folder.READ_WRITE)

            // Získání pole zpráv
            val messages: Array<Message> = folder.messages

            // Procházení zpráv
            for (message in messages) {
                var obsah = ""
                val content = message.content
                if (content is String) obsah = content else if (content is Multipart) {
                    val multipart: Multipart = content
                    val partCount: Int = multipart.count
                    for (i in 0 until partCount) {
                        val part: BodyPart = multipart.getBodyPart(i)
                        if (part.isMimeType("text/plain")) {
                            obsah = "Content: " + part.getContent()
                        }
                    }
                }
                var from = message.from.first().toString()
                from = if(from.contains("utf-8")) from.substring(from.lastIndexOf('<'),from.length) else from
                var subj = message.subject.toString()

                mails.add(msg_to_read(from,subj,obsah))

            }

            // Uzavření spojení
            folder.close(false)
            store.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mails
    }
     internal class msg_to_send(var body:String, var subject:String){

    }
     class msg_to_read(var from:String, var subject:String, var body:String){

         override fun toString(): String {
             return "from: $from\nsubject: $subject\nbody: $body"
         }
    }

}