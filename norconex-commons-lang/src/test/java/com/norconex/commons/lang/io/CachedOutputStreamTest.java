/* Copyright 2014-2017 Norconex Inc.
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
package com.norconex.commons.lang.io;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Pascal Essiembre
 */
public class CachedOutputStreamTest {

    @Test
    public void testContentMatchMemCache() throws IOException {
        String content = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        CachedStreamFactory factory = new CachedStreamFactory(200, 100);
        CachedOutputStream cache = factory.newOuputStream();
        InputStream is = null;
        
        try {
            cache.write(content.getBytes());
            is = cache.getInputStream();
            Assert.assertEquals(content, readCacheToString(is));
        }  finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(cache);
        }
    }

    @Test
    public void testContentMatchFileCache() throws IOException {
        String content = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        CachedStreamFactory factory = new CachedStreamFactory(200, 10);
        CachedOutputStream cache = factory.newOuputStream();

        InputStream is = null;
        try {
            cache.write(content.getBytes());
            is = cache.getInputStream();
            Assert.assertEquals(content, readCacheToString(is));
        }  finally {
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(cache);
        }
    }
    
    private String readCacheToString(InputStream is) throws IOException {
        long i;
        StringBuilder b = new StringBuilder();
        while ((i=is.read()) != -1) {
            b.append((char) i);
        }
        return b.toString();
    }
}
