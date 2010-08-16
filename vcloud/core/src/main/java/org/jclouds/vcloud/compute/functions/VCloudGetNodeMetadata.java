/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */

package org.jclouds.vcloud.compute.functions;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.jclouds.compute.util.ComputeServiceUtils.parseTagFromName;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.NodeState;
import org.jclouds.compute.domain.internal.NodeMetadataImpl;
import org.jclouds.compute.reference.ComputeServiceConstants;
import org.jclouds.domain.Location;
import org.jclouds.logging.Logger;
import org.jclouds.vcloud.VCloudClient;
import org.jclouds.vcloud.compute.BaseVCloudComputeClient;
import org.jclouds.vcloud.compute.VCloudComputeClient;
import org.jclouds.vcloud.domain.VApp;
import org.jclouds.vcloud.domain.VAppStatus;

import com.google.common.collect.ImmutableMap;

/**
 * Configures the {@link VCloudComputeServiceContext}; requires {@link BaseVCloudComputeClient}
 * bound.
 * 
 * @author Adrian Cole
 */
@Singleton
public class VCloudGetNodeMetadata {

   @Resource
   @Named(ComputeServiceConstants.COMPUTE_LOGGER)
   public Logger logger = Logger.NULL;
   protected final VCloudClient client;
   protected final VCloudComputeClient computeClient;
   protected final Provider<Set<? extends Image>> images;
   protected final FindLocationForResource findLocationForResourceInVDC;
   protected final GetExtra getExtra;
   protected final Map<VAppStatus, NodeState> vAppStatusToNodeState;

   @Inject
   VCloudGetNodeMetadata(VCloudClient client, VCloudComputeClient computeClient,
            Map<VAppStatus, NodeState> vAppStatusToNodeState, GetExtra getExtra,
            FindLocationForResource findLocationForResourceInVDC, Provider<Set<? extends Image>> images) {
      this.client = checkNotNull(client, "client");
      this.images = checkNotNull(images, "images");
      this.getExtra = checkNotNull(getExtra, "getExtra");
      this.findLocationForResourceInVDC = checkNotNull(findLocationForResourceInVDC, "findLocationForResourceInVDC");
      this.computeClient = checkNotNull(computeClient, "computeClient");
      this.vAppStatusToNodeState = checkNotNull(vAppStatusToNodeState, "vAppStatusToNodeState");
   }

   public NodeMetadata execute(String in) {
      URI id = URI.create(in);
      VApp from = client.getVApp(id);
      if (from == null)
         return null;
      String tag = parseTagFromName(from.getName());
      Location location = findLocationForResourceInVDC.apply(from.getVDC());
      return new NodeMetadataImpl(in, from.getName(), in, location, from.getId(), ImmutableMap.<String, String> of(),
               tag, null, vAppStatusToNodeState.get(from.getStatus()), computeClient.getPublicAddresses(id),
               computeClient.getPrivateAddresses(id), getExtra.apply(from), null);
   }

}