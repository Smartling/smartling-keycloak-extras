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

package org.keycloak.adapters.springsecurity.token;

import org.keycloak.adapters.springsecurity.authentication.DirectAccessGrantAuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * An {@link org.springframework.security.core.Authentication} implementation that is designed for simple presentation
 * of a username and password to a {@link DirectAccessGrantAuthenticationProvider}.
 *
 * @author <a href="mailto:srossillo@smartling.com">Scott Rossillo</a>
 */
public class DirectAccessGrantToken extends UsernamePasswordAuthenticationToken {

    /**
     * Creates a new unauthenticated direct access grant token.
     *
     * @param principal the principal to authenticate
     * @param credentials the credentials for the given <code>principal</code>
     */
    public DirectAccessGrantToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    /**
     * Creates a new authenticated direct access grant token.
     *
     * @param principal the authenticated principal
     * @param credentials the credentials for the authenticated <code>principal</code>
     * @param authorities a list of authorities granted to the given <code>principals</code>
     */
    public DirectAccessGrantToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }

}
