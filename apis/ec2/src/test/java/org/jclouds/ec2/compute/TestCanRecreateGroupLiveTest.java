/**
 * Licensed to jclouds, Inc. (jclouds) under one or more
 * contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  jclouds licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jclouds.ec2.compute;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.jclouds.compute.BaseVersionedServiceLiveTest;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.ComputeServiceContextFactory;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.Template;
import org.jclouds.compute.predicates.NodePredicates;
import org.jclouds.logging.log4j.config.Log4JLoggingModule;
import org.jclouds.sshj.config.SshjSshClientModule;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;

import com.google.common.base.Throwables;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Module;

/**
 * 
 * @author Adrian Cole
 */
@Test(groups = "live", testName="TestCanRecreateGroupLiveTest")
public class TestCanRecreateGroupLiveTest extends BaseVersionedServiceLiveTest {
   public TestCanRecreateGroupLiveTest() {
      provider = "ec2";
   }

   private ComputeServiceContext context;

   @BeforeGroups(groups = { "live" })
   public void setupClient() throws FileNotFoundException, IOException {
      setupCredentials();
      Properties overrides = setupProperties();
      context = new ComputeServiceContextFactory().createContext(provider,
            ImmutableSet.<Module> of(new Log4JLoggingModule(), new SshjSshClientModule()), overrides);
   }

   public void testCanRecreateGroup() throws Exception {

      String tag = PREFIX + "recreate";
      context.getComputeService().destroyNodesMatching(NodePredicates.inGroup(tag));

      try {
         Template template = context.getComputeService().templateBuilder().build();
         context.getComputeService().createNodesInGroup(tag, 1, template);
         context.getComputeService().destroyNodesMatching(NodePredicates.inGroup(tag));
         context.getComputeService().createNodesInGroup(tag, 1, template);
      } catch (RunNodesException e) {
         System.err.println(e.getNodeErrors().keySet());
         Throwables.propagate(e);
      } finally {
         context.getComputeService().destroyNodesMatching(NodePredicates.inGroup(tag));
      }
   }

   public static final String PREFIX = System.getProperty("user.name") + "ec2";

}
