fun main(args: Array<String>) {
    val sender = MailHandler("vasek@vasekdoskar.cz","Q5fVk_fWFf")

    var emaily = sender.GetEmails()

    for(m in emaily){
        print(m)
    }

}