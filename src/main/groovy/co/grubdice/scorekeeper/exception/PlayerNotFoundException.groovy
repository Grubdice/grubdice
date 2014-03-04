package co.grubdice.scorekeeper.exception

class PlayerNotFoundException extends NotFoundException{
    def textMessage = "Player not found"
    PlayerNotFoundException() {}

    PlayerNotFoundException(String playerName){
        textMessage = "Could not find player by name ${playerName}"
    }
}
