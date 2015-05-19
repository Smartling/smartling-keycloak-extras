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

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.adapters.springsecurity.config.AppConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Keycloak direct access grant service tests.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class KeycloakDirectAccessGrantServiceTest {

    @Autowired
    private KeycloakDirectAccessGrantService service;

    @Ignore
    @Test
    public void testLogin() throws Exception {
        RefreshableKeycloakSecurityContext securityContext =  service.login(AppConfig.KNOWN_USERNAME, AppConfig.KNOWN_PASSWORD);
        assertNotNull(securityContext);
        assertTrue(securityContext.isActive());
        assertNotNull(securityContext.getTokenString());
        assertNotNull(securityContext.getIdToken());
        assertNotNull(securityContext.getRefreshToken());
    }

    @Test
    public void testTemplateConverters() throws Exception {
        // template converters must be Jackson 1, not Jackson 2
        boolean hasJacksonConverter = false;
        boolean hasJackson2Converter = false;

        for (HttpMessageConverter converter : service.template.getMessageConverters()) {
            if (converter instanceof MappingJacksonHttpMessageConverter) {
                hasJacksonConverter = true;
            } else if (converter instanceof MappingJackson2HttpMessageConverter) {
                hasJackson2Converter = true;
            }
        }

        assertTrue(hasJacksonConverter);
        assertFalse(hasJackson2Converter);
    }

}
