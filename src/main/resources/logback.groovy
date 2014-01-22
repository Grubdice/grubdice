import ch.qos.logback.classic.Level
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy

appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    }
}

appender("ROOT-LOG", RollingFileAppender) {
    file = System.getProperty("catalina.base") + "/logs/grub-dice.log"
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "logFile.%d{yyyy-MM-dd}.log"
        maxHistory = 30
    }
    encoder(PatternLayoutEncoder) {
        pattern = "%-4relative [%thread] %-5level %logger{35} - %msg%n"
    }
}

logger("org.hibernate", Level.WARN)

if(System.getProperty("spring.profiles.active")?.equalsIgnoreCase("prod")){
    root(Level.INFO, ["STDOUT", "ROOT-LOG"])
} else {
    root(Level.DEBUG, ["STDOUT"])
}

