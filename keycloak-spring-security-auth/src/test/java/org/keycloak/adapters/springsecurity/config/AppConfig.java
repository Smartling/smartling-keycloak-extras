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

package org.keycloak.adapters.springsecurity.config;

import org.keycloak.adapters.springsecurity.AdapterDeploymentContextBean;
import org.keycloak.adapters.springsecurity.KeycloakSecurityComponents;
import org.keycloak.adapters.springsecurity.authentication.DirectAccessGrantAuthenticationProvider;
import org.keycloak.adapters.springsecurity.service.DirectAccessGrantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Spring integration test application configuration.
 */
@Configuration
@ComponentScan(basePackageClasses = KeycloakSecurityComponents.class)
public class AppConfig {

    public static final String KNOWN_EMAIL = "srossillo@smartling.com";
    public static final String KNOWN_USERNAME = "srossillo";
    public static final String KNOWN_PASSWORD = "password";

    @Autowired
    protected DirectAccessGrantService directAccessGrantService;

    @Bean
    AdapterDeploymentContextBean adapterDeploymentContextBean() {
        return new AdapterDeploymentContextBean();
    }

    @Bean
    DirectAccessGrantAuthenticationProvider directAccessGrantAuthenticationProvider() {
        DirectAccessGrantAuthenticationProvider provider = new DirectAccessGrantAuthenticationProvider();
        provider.setAdapterDeploymentContextBean(adapterDeploymentContextBean());
        provider.setDirectAccessGrantService(directAccessGrantService);
        return provider;
    }

}
