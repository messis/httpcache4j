/*
 * Copyright (c) 2009. The Codehaus. All Rights Reserved.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.codehaus.httpcache4j.payload;

import org.codehaus.httpcache4j.MIMEType;
import org.codehaus.httpcache4j.util.IOUtils;

import java.io.Serializable;
import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayInputStream;
import java.util.Objects;

/**
 * @author <a href="mailto:erlend@codehaus.org">Erlend Hamnaberg</a>
* @version $Revision: $
*/
public final class ByteArrayPayload implements Payload, Serializable {
    private static final long serialVersionUID = -4845254892809632007L;
    private final byte[] bytes;
    private final MIMEType type;
    private final long length;

    public ByteArrayPayload(InputStream stream, MIMEType type) throws IOException {
        try(InputStream is = stream) {
            this.bytes = IOUtils.toByteArray(is);
        }
        length = bytes.length;
        this.type = type;
    }

    public ByteArrayPayload(byte[] bytes, MIMEType type) {
        this.bytes = Objects.requireNonNull(bytes, "Byte array may not be null");
        this.type = Objects.requireNonNull(type, "MIMEType may not be null");
        this.length = bytes.length;
    }

    public MIMEType getMimeType() {
        return type;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(bytes);
    }

    public long length() {
        return length;
    }

    public boolean isAvailable() {
        return bytes != null && bytes.length > 0;
    }

    @Override
    public void close() throws IOException {
    }
}
