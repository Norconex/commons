/* Copyright 2016-2019 Norconex Inc.
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
package com.norconex.commons.lang.url;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QueryStringTest {

    // This tests issue: https://github.com/Norconex/collector-http/issues/304
    @Test
    public void testKeepProtocolUpperCase() {
        QueryString qs = new QueryString(
                "http://site.com/page?NoEquals&WithEquals=EqualsValue");

        Assertions.assertTrue(qs.toString().contains("NoEquals"),
                "Argument without equal sign was not found.");
    }

    @Test
    public void testEmptyConstructor() {
        QueryString qs = new QueryString();
        Assertions.assertEquals("", qs.toString());
    }

}
