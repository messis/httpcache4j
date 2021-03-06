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

import org.codehaus.httpcache4j.uri.QueryParam;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:hamnis@codehaus.org">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
public class FormDataPayloadTest {
    static final String newline = "%0D%0A";

    @Test
    public void testSimpleFormPayload() {
        List<QueryParam> parameters = new ArrayList<>();
        parameters.add(new QueryParam("foo", "bar"));
        parameters.add(new QueryParam("bar", "foo"));
        FormDataPayload payload = new FormDataPayload(parameters);
        Assert.assertEquals("foo=bar&bar=foo",payload.getValue());
    }

    @Test
    public void testEscapedList() {
        List<QueryParam> parameters = new ArrayList<>();
        parameters.add(new QueryParam("selected song", "hey jude"));
        parameters.add(new QueryParam("bar", "foo"));
        FormDataPayload payload = new FormDataPayload(parameters);
        Assert.assertEquals("selected+song=hey+jude&bar=foo",payload.getValue());
    }

    @Test
    public void testEscapedWithNewLines() {
        List<QueryParam> parameters = new ArrayList<>();
        parameters.add(new QueryParam("lyrics", "Hello!\r\nIs there anybody out there?\r\nIs there anyone at home"));
        parameters.add(new QueryParam("bar", "foo"));
        FormDataPayload payload = new FormDataPayload(parameters);
        Assert.assertEquals("lyrics=Hello%21"+ newline + "Is+there+anybody+out+there%3F"+ newline +"Is+there+anyone+at+home&bar=foo", payload.getValue());
    }
}
