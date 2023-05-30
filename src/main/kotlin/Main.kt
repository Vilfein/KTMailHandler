fun main(args: Array<String>) {
    val sender = MailHandler("vasek@vasekdoskar.cz","Q5fVk_fWFf")

    sender.sendMail("doskar.vaclav@seznam.cz","pokus","Ahoj Vilfeinku")
    var x = readln()
    print("čtu")

    var emaily = sender.GetEmails()

    for(m in emaily){
        print(m)
    }

}