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

package org.keycloak.adapters.springsecurity.service;

import org.keycloak.VerificationException;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;

/**
 * Provides an OAuth2 resource owner password credentials grant for clients
 * secured by Keycloak.
 *
 * <p>
 * The resource owner password credentials grant type is suitable in
 * cases where the resource owner has a trust relationship with the
 * client, such as the device operating system or a highly privileged.
 * </p>
 *
 * @author <a href="mailto:srossillo@smartling.com">Scott Rossillo</a>
 */
public interface DirectAccessGrantService {

    /**
     * Creates a login session for the Keycloak authenticated username and password.
     *
     * @param username to username to authenticate
     * @param password the password for the given <code>username</code>
     * @return an authenticated <code>RefreshableKeycloakSecurityContext</code> if login succeeds
     * @throws VerificationException if the <code>username</code> and <code>password</code> combination
     * are incorrect
     */
    RefreshableKeycloakSecurityContext login(String username, String password) throws VerificationException;
}
