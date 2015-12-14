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

package org.keycloak.adapters.springsecurity.userdetails.authentication;

import org.junit.Before;
import org.junit.Test;
import org.keycloak.adapters.KeycloakAccount;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.adapters.springsecurity.userdetails.token.KeycloakUserDetailsAuthenticationToken;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Keycloak user details authentication provider test cases.
 */
public class KeycloakUserDetailsAuthenticationProviderTest {

    private static final String KNOWN_USERNAME = "srossillo@smartling.com";
    private static final String UNKNOWN_USERNAME = "me@example.com";

    private KeycloakUserDetailsAuthenticationProvider provider;
    private UserDetailsService userDetailsService;
    private KeycloakAuthenticationToken token;
    private User user;
    
    @Mock
    private KeycloakAccount account;

    @Mock
    private Principal principal;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        Set<UserDetails> users = new HashSet<>();

        user = new User(KNOWN_USERNAME, "password", Arrays.asList(new SimpleGrantedAuthority("user")));
        users.add(user);

        userDetailsService = new InMemoryUserDetailsManager(Collections.unmodifiableCollection(users));

        provider = new KeycloakUserDetailsAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);

        when(principal.getName()).thenReturn(KNOWN_USERNAME);
        when(account.getPrincipal()).thenReturn(principal);

        token = new KeycloakAuthenticationToken(account);
    }

    @Test
    public void testAuthenticate() throws Exception {
        KeycloakUserDetailsAuthenticationToken authentication = (KeycloakUserDetailsAuthenticationToken) provider.authenticate(token);
        assertNotNull(authentication);
        assertEquals(user, authentication.getPrincipal());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testAuthenticateUserNotFound() throws Exception {
        when(principal.getName()).thenReturn(UNKNOWN_USERNAME);
        provider.authenticate(token);
    }

    @Test
    public void testResolveUsername() throws Exception {
        assertEquals(KNOWN_USERNAME, provider.resolveUsername(token));
    }

    @Test
    public void testSupports() throws Exception {
        assertTrue(provider.supports(KeycloakAuthenticationToken.class));
        assertTrue(provider.supports(KeycloakUserDetailsAuthenticationToken.class));
        assertFalse(provider.supports(UsernamePasswordAuthenticationToken.class));
    }
}
