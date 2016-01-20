package org.n3r.sandbox.log;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.n3r.sandbox.regex.RegexSubstituter;

import java.util.List;

public class LogbackPatternMask extends PatternLayout {
    private List<RegexSubstituter> regexSubstituters;

    public void setRegexSubstituers(String regexSubstituers) {
        regexSubstituters = RegexSubstituter.parse(regexSubstituers);
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        String message = super.doLayout(event);

        return replaceMessage(message);
    }

    private String replaceMessage(String message) {
        for (RegexSubstituter regexSubstituter : regexSubstituters) {
            String newMsg = regexSubstituter.substitute(message);
            if (!newMsg.equals(message)) return newMsg;
        }

        return message;
    }
}

