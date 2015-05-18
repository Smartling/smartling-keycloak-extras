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

package org.keycloak.adapters.springsecurity.authentication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.adapters.springsecurity.config.AppConfig;
import org.keycloak.adapters.springsecurity.token.DirectAccessGrantToken;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Direct access grant authentication provider tests.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class DirectAccessGrantAuthenticationProviderTest {

    @Autowired
    private DirectAccessGrantAuthenticationProvider directAccessGrantAuthenticationProvider;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAuthenticate() throws Exception {
        DirectAccessGrantToken token = new DirectAccessGrantToken(AppConfig.KNOWN_USERNAME, AppConfig.KNOWN_PASSWORD);
        Authentication authenication = directAccessGrantAuthenticationProvider.authenticate(token);
        assertNotNull(authenication);
        assertTrue(authenication.isAuthenticated());
    }

    @Test(expected = AuthenticationServiceException.class)
    public void testAuthenticateBadCredentials() throws Exception {
        directAccessGrantAuthenticationProvider.authenticate(new DirectAccessGrantToken("foo", "bar"));
    }

    @Test
    public void testSupports() throws Exception {
        assertTrue(directAccessGrantAuthenticationProvider.supports(DirectAccessGrantToken.class));
        assertTrue(directAccessGrantAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class));
        assertFalse(directAccessGrantAuthenticationProvider.supports(KeycloakAuthenticationToken.class));
    }

}
