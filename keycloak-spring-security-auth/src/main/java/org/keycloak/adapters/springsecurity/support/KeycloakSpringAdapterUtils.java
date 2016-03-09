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

package org.keycloak.adapters.springsecurity.support;

import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.keycloak.RSATokenVerifier;
import org.keycloak.adapters.AdapterUtils;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.OidcKeycloakAccount;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.KeycloakRole;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.keycloak.common.VerificationException;
import org.keycloak.jose.jws.JWSInput;
import org.keycloak.jose.jws.JWSInputException;
import org.keycloak.representations.AccessToken;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.IDToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Provides common utility methods for working with Spring Security and Keycloak
 *
 * @author <a href="mailto:srossillo@smartling.com">Scott Rossillo</a>
 */
public final class KeycloakSpringAdapterUtils {

    /**
     * Creates a {@link OidcKeycloakAccount} from the given {@link KeycloakDeployment} and {@link RefreshableKeycloakSecurityContext}.
     *
     * @param deployment the <code>KeycloakDeployment</code> requesting an account (required)
     * @param context the current <code>RefreshableKeycloakSecurityContext</code> (required)
     *
     * @return a <code>KeycloakAccount</code> for the given <code>deployment</code> and <code>context</code>
     */
    public static OidcKeycloakAccount createAccount(KeycloakDeployment deployment, RefreshableKeycloakSecurityContext context) {
        Assert.notNull(context);
        Set<String> roles = AdapterUtils.getRolesFromSecurityContext(context);
        KeycloakPrincipal<RefreshableKeycloakSecurityContext> principal = AdapterUtils.createPrincipal(deployment, context);
        return new SimpleKeycloakAccount(principal, roles, context);
    }

    /**
     * Creates a {@link GrantedAuthority} collection from the given {@link RefreshableKeycloakSecurityContext}.
     *
     * @param context the current <code>RefreshableKeycloakSecurityContext</code> (required)
     *
     * @return a {@link GrantedAuthority} collection if any; an empty list otherwise
     */
    public static Collection<?extends GrantedAuthority> createGrantedAuthorities(RefreshableKeycloakSecurityContext context) {
        return createGrantedAuthorities(context, null);
    }

    /**
     * Creates a {@link GrantedAuthority} collection from the given {@link KeycloakSecurityContext}.
     *
     * @param context the current <code>RefreshableKeycloakSecurityContext</code> (required)
     * @param mapper an optional {@link GrantedAuthoritiesMapper} to convert the
     * authorities loaded the given <code>context</code> which will be used in the
     * {@code Authentication} object
     *
     * @return a {@link GrantedAuthority} collection if any; an empty list otherwise
     */
    public static Collection<? extends GrantedAuthority> createGrantedAuthorities(RefreshableKeycloakSecurityContext context, GrantedAuthoritiesMapper mapper) {
        Assert.notNull(context, "RefreshableKeycloakSecurityContext cannot be null");
        List<KeycloakRole> grantedAuthorities = new ArrayList<>();

        for (String role : AdapterUtils.getRolesFromSecurityContext(context)) {
            grantedAuthorities.add(new KeycloakRole(role));
        }

        return mapper != null ? mapper.mapAuthorities(grantedAuthorities) : Collections.unmodifiableList(grantedAuthorities);
    }

    /**
     * Creates a new {@link RefreshableKeycloakSecurityContext} from the given {@link KeycloakDeployment} and {@link AccessTokenResponse}.
     *
     * @param deployment the <code>KeycloakDeployment</code> for which to create a <code>RefreshableKeycloakSecurityContext</code> (required)
     * @param accessTokenResponse the <code>AccessTokenResponse</code> from which to create a RefreshableKeycloakSecurityContext (required)
     *
     * @return a <code>RefreshableKeycloakSecurityContext</code> created from the given <code>accessTokenResponse</code>
     * @throws VerificationException if the given <code>AccessTokenResponse</code> contains an invalid {@link IDToken}
     */
    public static RefreshableKeycloakSecurityContext createKeycloakSecurityContext(KeycloakDeployment deployment, AccessTokenResponse accessTokenResponse) throws VerificationException {
        String tokenString = accessTokenResponse.getToken();
        String idTokenString = accessTokenResponse.getIdToken();
        AccessToken accessToken = RSATokenVerifier
                .verifyToken(tokenString, deployment.getRealmKey(), deployment.getRealmInfoUrl());
        IDToken idToken;

        try {
            JWSInput input = new JWSInput(idTokenString);
            idToken = input.readJsonContent(IDToken.class);
        } catch (JWSInputException e) {
            throw new VerificationException("Unable to verify ID token", e);
        }

        // FIXME: does it make sense to pass null for the token store?
        return new RefreshableKeycloakSecurityContext(deployment, null, tokenString, accessToken, idTokenString, idToken, accessTokenResponse.getRefreshToken());
    }

    private KeycloakSpringAdapterUtils() { }
}
