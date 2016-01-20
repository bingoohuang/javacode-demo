package org.n3r.sandbox.codec;


import org.junit.Assert;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.nio.ByteBuffer;

public class TwoDimensionIntArrayToBase64 {
    @Test
    public void test() {
        int[][] clicks = new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        ByteBuffer clicksBuffer = ByteBuffer.allocate(3 * 3 * 4);

        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                clicksBuffer.putInt(clicks[i][j]);
            }
        }

        byte[] array = clicksBuffer.array();
        String base64Binary = DatatypeConverter.printBase64Binary(array);
        System.out.println(base64Binary);

        byte[] bytes = DatatypeConverter.parseBase64Binary(base64Binary);

        int[][] clicks1 = new int[3][];


        ByteBuffer wrap = ByteBuffer.wrap(bytes);
        for (int i = 0; i < 3; ++i) {
            clicks1[i] = new int[3];
            for (int j = 0; j < 3; ++j) {
                clicks1[i][j] = wrap.getInt();
            }
        }

        Assert.assertArrayEquals(clicks, clicks1);
    }
}
