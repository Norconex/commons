/* Copyright 2020 Norconex Inc.
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
package com.norconex.commons.lang.unit;

/**
 * Runtime exception when a {@link DataUnitParser} could not parse a string
 * value.
 * @author Pascal Essiembre
 * @since 2.0.0
 */
public class DataUnitParserException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     */
    public DataUnitParserException() {
        super();
    }

    /**
     * Constructor.
     * @param message exception message
     */
    public DataUnitParserException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * @param cause exception root cause
     */
    public DataUnitParserException(Throwable cause) {
        super(cause);
    }

    /**
     * Constructor.
     * @param message exception message
     * @param cause exception root cause
     */
    public DataUnitParserException(String message, Throwable cause) {
        super(message, cause);
    }

}
