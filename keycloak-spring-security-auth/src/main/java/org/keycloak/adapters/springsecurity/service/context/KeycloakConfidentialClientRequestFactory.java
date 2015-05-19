/*
 * Copyright 2015 Smartling, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.keycloak.adapters.springsecurity.service.context;

import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClients;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.springsecurity.AdapterDeploymentContextBean;
import org.keycloak.util.BasicAuthHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;

/**
 * {@link ClientHttpRequestFactory} implementation for creating requests to a
 * Keycloak server. This factory automatically authenticates confidential Keycloak
 * clients to the Keycloak server.
 *
 * @author <a href="mailto:srossillo@smartling.com">Scott Rossillo</a>
 */
@Component
public class KeycloakConfidentialClientRequestFactory extends HttpComponentsClientHttpRequestFactory implements
        ClientHttpRequestFactory {

    @Autowired
    private AdapterDeploymentContextBean adapterDeploymentContextBean;

    /**
     * Creates a new Keycloak confidential client request factory.
     */
    public KeycloakConfidentialClientRequestFactory() {
        super(HttpClients.custom().disableCookieManagement().build());
    }

    @Override
    protected void postProcessHttpRequest(HttpUriRequest request) {
        request.setHeader(HttpHeaders.AUTHORIZATION, this.createBasicAuthorizationHeader());
    }

    /**
     * Returns an HTTP Basic authentication header for authenticating a confidential client
     * to the Keycloak server.
     *
     * @return an HTTP Basic authentication header for the current client
     */
    protected String createBasicAuthorizationHeader() {
        KeycloakDeployment deployment = adapterDeploymentContextBean.getDeployment();
        String user = deployment.getResourceName();
        String pass = deployment.getResourceCredentials().get("secret");

        if (deployment.isPublicClient()) {
            throw new IllegalStateException("Public clients are not supported");
        }

        return BasicAuthHelper.createHeader(user, pass);
    }
}
