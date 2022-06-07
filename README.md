# Smartling Keycloak Extras

Provides projects that may be useful when implementing Spring Security with [Keycloak].
These project may add auxiliary behavior or have yet to be merged into the Keycloak project.

## Modules

### Keycloak Spring Security Authentication

Provides an extension to Keycloak's Spring Security adapter that enables the authentication
via direct access grants.

### Keycloak Spring Security User Details

Provides an extension to Keycloak's Spring Security adapter that enables the authenticated
principal to be loaded from a Spring Security user detail service.

### Usage

[ ![Download](https://api.bintray.com/packages/smartling/release/keycloak-spring-security-auth/images/download.svg) ](https://bintray.com/smartling/release/keycloak-spring-security-auth/_latestVersion)

```
<dependency>
	<groupId>com.smartling.keycloak.extras</groupId>
	<artifactId>keycloak-spring-security-auth</artifactId>
	<version>${version}</version>
</dependency>

<dependency>
	<groupId>com.smartling.keycloak.extras</groupId>
	<artifactId>keycloak-spring-security-user-details</artifactId>
	<version>${version}</version>
</dependency>

```

## Using Smartling Keycloak Extras

Currently this artifact is deployed to artifactory /local-ext-snapshots.
You can deploy it with following command
```
export ARTIFACTORY_USER='foo'
export ARTIFACTORY_PASSWORD='bar'
mvn --settings settings.xml -Dhttps.protocols=TLSv1.2 clean deploy
```

[keycloak]: http://keycloak.org
