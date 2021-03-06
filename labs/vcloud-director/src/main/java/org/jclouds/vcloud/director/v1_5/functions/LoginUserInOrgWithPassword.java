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
package org.jclouds.vcloud.director.v1_5.functions;

import java.net.URI;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jclouds.domain.Credentials;
import org.jclouds.vcloud.director.v1_5.annotations.Login;
import org.jclouds.vcloud.director.v1_5.domain.SessionWithToken;
import org.jclouds.vcloud.director.v1_5.login.SessionClient;

import com.google.common.base.Function;
import com.google.common.base.Supplier;

@Singleton
public class LoginUserInOrgWithPassword implements Function<Credentials, SessionWithToken> {
   private final SessionClient client;
   private final Supplier<URI> loginUrl;

   @Inject
   public LoginUserInOrgWithPassword(SessionClient client, @Login Supplier<URI> loginUrl) {
      this.client = client;
      this.loginUrl = loginUrl;
   }

   @Override
   public SessionWithToken apply(Credentials input) {
      String user = input.identity.substring(0, input.identity.lastIndexOf('@'));
      String org = input.identity.substring(input.identity.lastIndexOf('@') + 1);
      String password = input.credential;

      return client.loginUserInOrgWithPassword(loginUrl.get(), user, org, password);
   }

   @Override
   public String toString() {
      return "loginUserInOrgWithPassword()";
   }
}