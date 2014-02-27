package co.grubdice.scorekeeper.exception

class NotFoundException extends RuntimeException{
    NotFoundException() {}

    NotFoundException(String s) { super(s) }
}
