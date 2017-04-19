/*
 * Copyright (c) 2008, The Codehaus. All Rights Reserved.
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
 *
 */

package org.codehaus.httpcache4j;

import org.codehaus.httpcache4j.payload.InputStreamPayload;
import org.codehaus.httpcache4j.payload.Payload;
import org.codehaus.httpcache4j.payload.StringPayload;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

/** @author <a href="mailto:hamnis@codehaus.org">Erlend Hamnaberg</a> */
public class HTTPResponseTest {
    @Test
    public void noHeaders() {
        Headers headers = new Headers();
        HTTPResponse response = new HTTPResponse(Status.OK, headers);
        assertEquals(0, response.getHeaders().getAllow().size());
        assertFalse(response.getHeaders().getETag().isPresent());
        assertFalse(response.getHeaders().getLastModified().isPresent());
        assertFalse(response.hasPayload());
    }

    @Test
    public void eTagHeader() {
        Headers headers = new Headers().add(new Header("ETag", "\"abba\""));
        HTTPResponse response = new HTTPResponse(Status.OK, headers);
        assertEquals(0, response.getHeaders().getAllow().size());
        assertTrue(response.getHeaders().getETag().isPresent());
        assertEquals(response.getHeaders().getETag().get().format(), "\"abba\"");
        assertFalse(response.getHeaders().getLastModified().isPresent());
        assertFalse(response.hasPayload());
    }

    @Test
    public void allowHeader() {
        Headers headers = new Headers().add(HeaderConstants.ALLOW, "GET, POST, OPTIONS");
        HTTPResponse response = new HTTPResponse(Status.OK, headers);
        assertEquals(3, response.getHeaders().getAllow().size());
    }

    @Test
    public void responseShouldHaveCachedValueSet() {
        Headers headers = new Headers().add(CacheHeaderBuilder.getBuilder().createHITXCacheHeader());
        HTTPResponse response = new HTTPResponse(Status.OK, headers);
        assertTrue(response.isCached());
    }

    @Test
    public void transformShouldGiveUseSomethingUseful() {
        HTTPResponse response = new HTTPResponse(Optional.of(new StringPayload("Hello", MIMEType.valueOf("text/plain"))), Status.OK, new Headers());
        Optional<String> result = response.transform(payload -> {
            assertEquals(MIMEType.valueOf("text/plain"), payload.getMimeType());
            return Optional.of(payload.string());
        });

        assertTrue(result.isPresent());
        assertEquals("Hello", result.get());
    }

    @Test
    public void transformWithouthavingtoDealWithException() {
        HTTPResponse response = new HTTPResponse(Optional.of(new StringPayload("Hello", MIMEType.valueOf("text/plain"))), Status.OK, new Headers());
        Optional<String> result = response.transform(payload -> {
            assertEquals(MIMEType.valueOf("text/plain"), payload.getMimeType());
            return Optional.of(payload.string());
        });

        assertTrue(result.isPresent());
        assertEquals("Hello", result.get());
    }

    @Test
    public void nullCheckOnPayloadInputStreamClose(){
        String exampleResponseText = "text";
        Payload payload = new InputStreamPayload(new ByteArrayInputStream(exampleResponseText.getBytes(StandardCharsets.UTF_8)), MIMEType.valueOf("text/plain"));
        HTTPResponse response = new HTTPResponse(Optional.of(payload), Status.OK, new Headers());
        Optional<String>  expectedResponse = response.transform(input -> Optional.of(input.string()));
        try {
            response.consume();
        } catch (Exception e){
            fail("Failed to consume response " + e);
        }

        assertEquals(exampleResponseText, expectedResponse.orElse("failed"));

    }
}
