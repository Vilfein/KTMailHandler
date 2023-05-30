fun main(args: Array<String>) {
    val sender = MailHandler("postmaster@vasekdoskar.cz","x9VVS2cC-V")

    var emaily = sender.GetEmails()

    for(m in emaily){
        print(m)
    }

}