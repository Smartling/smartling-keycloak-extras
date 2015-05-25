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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.AdapterUtils;
import org.keycloak.adapters.KeycloakAccount;
import org.keycloak.adapters.KeycloakDeployment;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.account.KeycloakRole;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.collections.Sets;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;

import java.util.Collection;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Keycloak Spring adapter utility method tests.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AdapterUtils.class)
public class KeycloakSpringAdapterUtilsTest {

    private static final String AUTHORITY_ADMIN = "admin";
    private static final String AUTHORITY_USER = "user";

    private static final Set<String> AUTHORITIES = Sets.newSet(AUTHORITY_USER, AUTHORITY_ADMIN);
    private static final Set<KeycloakRole> KEYCLOAK_ROLES = Sets.newSet(new KeycloakRole(AUTHORITY_USER), new KeycloakRole(AUTHORITY_ADMIN));

    private GrantedAuthoritiesMapper grantedAuthoritiesMapper;

    @Mock
    private KeycloakDeployment deployment;

    @Mock
    private RefreshableKeycloakSecurityContext context;

    @Mock
    private KeycloakPrincipal<RefreshableKeycloakSecurityContext> principal;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        SimpleAuthorityMapper roleMapper = new SimpleAuthorityMapper();
        roleMapper.setConvertToUpperCase(true);
        grantedAuthoritiesMapper = roleMapper;

        PowerMockito.mockStatic(AdapterUtils.class);
        when(AdapterUtils.getRolesFromSecurityContext(any(RefreshableKeycloakSecurityContext.class))).thenReturn(AUTHORITIES);
        when(AdapterUtils.createPrincipal(eq(deployment), eq(context))).thenReturn(principal);
    }

    @Test
    public void testCreateAccount() throws Exception {
        KeycloakAccount account = KeycloakSpringAdapterUtils.createAccount(deployment, context);
        assertNotNull(account);
        assertEquals(principal, account.getPrincipal());
        assertEquals(context, account.getKeycloakSecurityContext());
        assertEquals(AUTHORITIES, account.getRoles());
    }

    @Test
    public void testCreateGrantedAuthorities() throws Exception {
        Collection<? extends GrantedAuthority> authorities = KeycloakSpringAdapterUtils.createGrantedAuthorities(context);
        assertNotNull(authorities);
        assertTrue(authorities.containsAll(KEYCLOAK_ROLES));
    }

    @Test
    public void testCreateGrantedAuthoritiesWithMapper() throws Exception {
        Collection<? extends GrantedAuthority> authorities = KeycloakSpringAdapterUtils.createGrantedAuthorities(context, grantedAuthoritiesMapper);
        assertNotNull(authorities);
        assertEquals(Sets.newSet("ROLE_USER", "ROLE_ADMIN"), AuthorityUtils.authorityListToSet(authorities));
    }

}