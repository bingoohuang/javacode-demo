package org.n3r.sandbox.jaxb;

import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;

import java.io.IOException;
import java.io.Writer;

public class OnDemandCDataEscapeHandler implements CharacterEscapeHandler {
    public static final CharacterEscapeHandler instance = new OnDemandCDataEscapeHandler();

    @Override
    public void escape(char[] buf, int start, int length, boolean isAttVal, Writer out) throws IOException {
        int limit = start + length;

        boolean hasXmlSpecialCharacters = false;
        for (int i = start; i < limit; ++i) {
            char c = buf[i];
            if (c == '&' || c == '<' || c == '>' || c == '\r' || c == '"' && isAttVal) {
                hasXmlSpecialCharacters = true;
                break;
            }
        }

        if (hasXmlSpecialCharacters) {
            out.write("<![CDATA[");
            out.write(buf, start, length);
            out.write("]]>");
        } else {
            out.write(buf, start, length);
        }
    }
}
