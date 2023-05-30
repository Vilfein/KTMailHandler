fun main(args: Array<String>) {
    val sender = MailHandler("postmaster@vasekdoskar.cz","x9VVS2cC-V")

    println("Číst nebo napsat?\n 1 - číst \n 2 - napsat\n")
    var volba:Int = readln().toInt()

    if(volba == 1){
        sender.ReadMails()
    }
    else if(volba == 2){
        print("Komu:\n")
        var to = readln()
        print("Obsah:\n")
        var body = readln()

        sender.sendMail(to,"ahojda:)",body)
    }

}