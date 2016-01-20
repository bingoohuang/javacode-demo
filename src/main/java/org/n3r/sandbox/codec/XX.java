package org.n3r.sandbox.codec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.HexDump;

import java.io.UnsupportedEncodingException;

public class XX {
    public static void main(String[] args) throws UnsupportedEncodingException {
        byte[] bytes = Base64.decodeBase64("DD7GSnaNgkWD3H+K5kAHjEmtVrldSpf1Jqo0d6/ls4iNVro0USWYhBtYd0Sm9ewlIltVp34O81g=");
        System.out.println(Hex.encodeHexString(bytes));
    }
}
