package org.n3r.sandbox.liyun;

import com.alibaba.fastjson.JSON;
import lombok.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

import static com.google.common.truth.Truth.assertThat;
import static org.apache.commons.codec.binary.Base64.encodeBase64URLSafeString;

public class YidongbangIntegrationPageDemo {
    @Data @AllArgsConstructor @NoArgsConstructor
    public static class YidongbangUser {
        private String userId;   // 用户ID
        private String userName; // 用户姓名
        private String mobile;   // 用户手机号码
        private String sex;      // 用户性别，男/女
    }

    static final String KEY_ALGORITHM = "AES"; // 口令算法
    static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding"; // 加密算法

    @SneakyThrows
    public static String generateAesKey() {
        val kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        kg.init(128);
        val secretKey = kg.generateKey();
        return encodeBase64URLSafeString(secretKey.getEncoded());
    }

    public static Key getAesKey(String key) {
        return new SecretKeySpec(Base64.decodeBase64(key), KEY_ALGORITHM);
    }

    @SneakyThrows
    public static String decrypt(String value, String key) {
        val cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, getAesKey(key));
        byte[] decrypted = cipher.doFinal(Base64.decodeBase64(value));
        return new String(decrypted, "UTF-8");
    }

    @SneakyThrows
    public static String encrypt(String value, String key) {
        val cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, getAesKey(key));
        byte[] encrypted = cipher.doFinal(value.getBytes("UTF-8"));
        return encodeBase64URLSafeString(encrypted);
    }

    public static void main(String[] args) {
        // 创建给权益页面所需要使用到的用户信息
        val user = new YidongbangUser("12345678910", "孙悟空", "13818182828", "男");
        // 生成JSON
        val json = JSON.toJSONString(user);
        // 5分钟后过期
        val expiredAt = DateTime.now().plusMinutes(5).toString("yyyy-MM-dd HH:mm:ss");

        // key需要提前生成好，并且通过安全渠道提供给对方。
        val key = generateAesKey();
        // eg. DX1M3qaCL3QeA7rJF2Io4Q

        // 生成加密令牌，附加在权益页面URL后面，比如: http://liyun.easy-hi.com/index.html?token=Xxx
        val token = encrypt(expiredAt + "^" + json, key);
        // eg.cTt6XGoZsJCoStiMDGIDjHe1SRywPIVYTyhI1p5Lg0OpkSOjJ7fOgslIvf50yYgquVt-_LSP-kRlrFqYEeFDPTP86GFQIRYvaIX9eWH6dWbPbQ0ZUs0TRZU89BNc2MAp

        val decrypt = decrypt(token, key);

        String expiredAt2Str = StringUtils.substringBefore(decrypt, "^");
        DateTime expiredAt2 = DateTime.parse(expiredAt2Str, DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));
        if (expiredAt2.isBeforeNow()) { // TOKEN 过期了
            // do expire process
        } else {
            String json2 = StringUtils.substringAfter(decrypt, "^");
            val usr = JSON.parseObject(json2, YidongbangUser.class);
            assertThat(user).isEqualTo(usr);
        }
    }
}
