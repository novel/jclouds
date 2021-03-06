/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 *(Link.builder().regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless(Link.builder().required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.vcloud.director.v1_5.features;

import org.jclouds.vcloud.director.v1_5.internal.BaseVCloudDirectorClientLiveTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests behavior of {@link NetworkClient}
 * 
 * @author danikov
 */
@Test(groups = { "live", "user", "upload" }, singleThreaded = true, testName = "UploadClientLiveTest")
public class UploadClientLiveTest extends BaseVCloudDirectorClientLiveTest {
   
   public static final String UPLOAD = "upload";
 
   /*
    * Convenience reference to API client.
    */
   protected UploadClient uploadClient;
 
   @Override
   @BeforeClass(alwaysRun = true)
   public void setupRequiredClients() {
      uploadClient = context.getApi().getUploadClient();
   }

   @Test(testName = "PUT ???", enabled = false)
   public void testUpload() {
   }
}
