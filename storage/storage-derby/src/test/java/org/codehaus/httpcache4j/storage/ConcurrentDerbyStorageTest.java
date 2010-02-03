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

package org.codehaus.httpcache4j.storage;

import org.codehaus.httpcache4j.cache.ConcurrentCacheStorageAbstractTest;
import org.codehaus.httpcache4j.cache.CacheStorage;
import org.codehaus.httpcache4j.util.TestUtil;
import org.apache.commons.io.FileUtils;

import java.io.File;

/**
 * @author <a href="mailto:erlend@escenic.com">Erlend Hamnaberg</a>
 * @version $Revision: $
 */
public class ConcurrentDerbyStorageTest extends ConcurrentCacheStorageAbstractTest {
    private File testFile;

    @Override
    protected CacheStorage createCacheStorage() {
        testFile = TestUtil.getTestFile("target/storage");
        return new DerbyCacheStorage(testFile, true);
    }

    @Override
    public void tearDown() {
        super.tearDown();
        FileUtils.deleteQuietly(testFile);
    }
}