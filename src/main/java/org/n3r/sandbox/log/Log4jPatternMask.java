package org.n3r.sandbox.log;

import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.n3r.sandbox.regex.RegexSubstituter;

import java.util.List;

public class Log4jPatternMask extends PatternLayout {
    private List<RegexSubstituter> regexSubstituters;

    public void setRegexSubstituers(String regexSubstituers) {
        regexSubstituters = RegexSubstituter.parse(regexSubstituers);
    }

    @Override
    public String format(LoggingEvent event) {
        if (!(event.getMessage() instanceof String))
            return super.format(event);

        String message = replaceMessage(event.getRenderedMessage());

        Throwable throwable = event.getThrowableInformation() != null ?
                event.getThrowableInformation().getThrowable() : null;

        LoggingEvent maskedEvent = new LoggingEvent(
                event.fqnOfCategoryClass,
                Logger.getLogger(event.getLoggerName()),
                event.timeStamp,
                event.getLevel(),
                message,
                throwable);

        return super.format(maskedEvent);
    }

    private String replaceMessage(String message) {
        for (RegexSubstituter regexSubstituter : regexSubstituters) {
            String newMsg = regexSubstituter.substitute(message);
            if (!newMsg.equals(message)) return newMsg;
        }

        return message;
    }
}
