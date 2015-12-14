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

package org.keycloak.adapters.springsecurity.userdetails.token;

import org.keycloak.adapters.KeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import java.util.Collection;

/**
 * Provides a {@link UserDetails} aware Keycloak authentication token.
 *
 * @author <a href="mailto:srossillo@smartling.com">Scott Rossillo</a>
 */
public class KeycloakUserDetailsAuthenticationToken extends KeycloakAuthenticationToken
{

    private UserDetails userDetails;

    public KeycloakUserDetailsAuthenticationToken(UserDetails userDetails, KeycloakAccount account,
            Collection<? extends GrantedAuthority> authorities) {
        super(account, authorities);
        Assert.notNull(userDetails, "UserDetails required");
        this.userDetails = userDetails;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }

}
