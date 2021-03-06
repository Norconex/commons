/* Copyright 2018-2019 Norconex Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.norconex.commons.lang.encrypt;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;


public class EncryptionUtilTest {

    @Test
    public void testEncrypt() {
        EncryptionKey key = new EncryptionKey("this is my secret key.");
        String text = "please encrypt this text.";
        String encryptedText = EncryptionUtil.encrypt(text, key);
        String decryptedText = EncryptionUtil.decrypt(encryptedText, key);
        Assertions.assertEquals(text, decryptedText);
    }

    @Test
    public void testEncryptTwice() {
        EncryptionKey key = new EncryptionKey("this is my secret key.");
        String text = "please encrypt this text.";
        String encryptedText1 = EncryptionUtil.encrypt(text, key);
        String encryptedText2 = EncryptionUtil.encrypt(text, key);
        Assertions.assertNotEquals(encryptedText1, encryptedText2);
    }

    @Test
    public void testDecryptLegacy() {
        EncryptionKey key = new EncryptionKey("This is an encryption key");
        String expectedClearText = "Please encrypt this text";
        String encryptedText = "aeEFKa0uXMUHT4UyeFtuHjm37NQw3vEaxY03EkkD2qM=";
        String actualClearText = EncryptionUtil.decrypt(encryptedText, key);
        Assertions.assertEquals(expectedClearText, actualClearText);
    }

    @Test
    public void testAes256bitEncryptionKey() throws NoSuchAlgorithmException {

        // NOTE: this test should be true on Java 8 u162+ or on Java 9, or on
        // any Java where JCE Unlimited Strength has been applied
        Assumptions.assumeTrue(Cipher.getMaxAllowedKeyLength("AES") >= 256);

        // Create round-trip encryption key
        EncryptionKey key = new EncryptionKey("This as an encryption key", 256);
        String text = "please encrypt this text";
        String encryptedText = EncryptionUtil.encrypt(text, key);
        String decryptedText = EncryptionUtil.decrypt(encryptedText, key);

        Assertions.assertEquals(text, decryptedText);
    }
}
