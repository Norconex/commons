/* Copyright 2017-2020 Norconex Inc.
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
package com.norconex.commons.lang.net;

import org.junit.jupiter.api.Test;

import com.norconex.commons.lang.encrypt.EncryptionKey;
import com.norconex.commons.lang.encrypt.EncryptionKey.Source;
import com.norconex.commons.lang.xml.XML;

public class ProxySettingsTest {

    @Test
    public void testWriteRead() {
        ProxySettings ps = new ProxySettings();
        ps.setHost(new Host("myhost", 99));
        ps.getCredentials().setUsername("myusername");
        ps.getCredentials().setPassword("mypassword");
        ps.getCredentials().setPasswordKey(
                new EncryptionKey("keyvalue", Source.KEY, 256));
        ps.setRealm("realm");
        ps.setScheme("scheme");

        XML.assertWriteRead(ps, "proxySettings");
    }
}
