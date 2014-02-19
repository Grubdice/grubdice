import ch.qos.logback.classic.Level
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.classic.PatternLayout
import com.logentries.logback.LogentriesAppender

appender("STDOUT", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    }
}

appender("LE", LogentriesAppender) {
  token = "307aa4af-9c1d-494d-96d2-88181a5e233b"
  ssl = "False"
  facility = "USER"
  layout(PatternLayout) {
    pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
  }
}

appender("ROOT-LOG", RollingFileAppender) {
    file = System.getProperty("catalina.base") + "/logs/grub-dice.log"
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = System.getProperty("catalina.base") + "/logs/grub-dice.%d{yyyy-MM-dd}.log"
        maxHistory = 30
    }
    encoder(PatternLayoutEncoder) {
        pattern = "%-4relative [%thread] %-5level %logger{35} - %msg%n"
    }
}

logger("org.hibernate", Level.WARN)

if(System.getProperty("spring.profiles.active")?.equalsIgnoreCase("prod")){
    root(Level.INFO, ["STDOUT", "ROOT-LOG", "LE"])
} else {
    root(Level.DEBUG, ["STDOUT"])
}

