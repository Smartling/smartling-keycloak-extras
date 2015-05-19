# Smartling Keycloak Extras

Provides projects that may be useful when implementing Spring Security with [Keycloak].
These project may add auxiliary behavior or have yet to be merged into the Keycloak project.

## Modules

### Keycloak Spring Security Authentication

Provides an extension to Keycloak's Spring Security adapter that enables the authenticated
principal to be loaded from a Spring Security user detail service.

#### Usage

[ ![Download](https://api.bintray.com/packages/smartling/release/keycloak-spring-security-auth/images/download.svg) ](https://bintray.com/smartling/release/keycloak-spring-security-auth/_latestVersion)

```
<dependency>
	<groupId>com.smartling.keycloak.extras</groupId>
	<artifactId>keycloak-spring-security-auth</artifactId>
	<version>0.3.0-SNAPSHOT</version>
</dependency>
```

## Using Smartling Keycloak Extras

Until the extracts are included in JCenter, add the Smrartling OSS [release repository][repo]
to your build:

```
<repositories>
	<repository>
		<id>smartling-oss-release</id>
		<url>https://dl.bintray.com/smartling/release</url>
		<releases><enabled>true</enabled></releases>
		<snapshots><enabled>false</enabled></snapshots>
	</repository>
</repositories>
```

[keycloak]: http://keycloak.org
[repo]: https://dl.bintray.com/smartling/release
