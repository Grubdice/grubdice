import ch.qos.logback.classic.Level
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.FileAppender

appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    }
}

appender("ROOT-LOG", FileAppender) {
    file = "grub-dice.log"
    append = true
    encoder(PatternLayoutEncoder) {
        pattern = "%-4relative [%thread] %-5level %logger{35} - %msg%n"
    }
}

logger("org.hibernate", Level.WARN)

root(Level.DEBUG, ["STDOUT", "ROOT-LOG"])