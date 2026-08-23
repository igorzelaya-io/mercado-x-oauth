# Graph Report - .  (2026-08-21)

## Corpus Check
- cluster-only mode — file stats not available

## Summary
- 153 nodes · 257 edges · 13 communities (12 shown, 1 thin omitted)
- Extraction: 98% EXTRACTED · 2% INFERRED · 0% AMBIGUOUS · INFERRED: 4 edges (avg confidence: 0.8)
- Token cost: 0 input · 0 output

## Graph Freshness
- Built from commit: `1767fd6c`
- Run `git rev-parse HEAD` and compare to check if the graph is stale.
- Run `graphify update .` after code changes (no API cost).

## Community Hubs (Navigation)
- AuthController.java
- UserDetailsMapper.java
- JwtSigningConfig.java
- RegistrationService
- MercadoXControllerTest.java
- OAuthTenantValidatorService.java
- AuthManagerConfig.java
- MercadoXSecurityFilterConfig.java
- AuthService.java
- RegistrationController.java
- mvnw
- MercadoXOauthApplicationTests.java
- mercado-x-oauth

## God Nodes (most connected - your core abstractions)
1. `RegistrationService` - 12 edges
2. `AuthService` - 10 edges
3. `AuthController` - 9 edges
4. `JwtSigner` - 8 edges
5. `AuthManagerConfig` - 7 edges
6. `MercadoXSecurityFilterConfig` - 7 edges
7. `RegistrationController` - 7 edges
8. `UserDetailsMapper` - 7 edges
9. `CustomUserDetailsService` - 7 edges
10. `JwtSigningProperties` - 6 edges

## Surprising Connections (you probably didn't know these)
- `RegistrationController` --references--> `AuthService`  [EXTRACTED]
  src/main/java/hn/shadowcore/mercadox/oauth/controller/RegistrationController.java → src/main/java/hn/shadowcore/mercadox/oauth/service/AuthService.java
- `RegistrationController` --references--> `RegistrationService`  [EXTRACTED]
  src/main/java/hn/shadowcore/mercadox/oauth/controller/RegistrationController.java → src/main/java/hn/shadowcore/mercadox/oauth/service/RegistrationService.java
- `AuthController` --references--> `JwtSigner`  [EXTRACTED]
  src/main/java/hn/shadowcore/mercadox/oauth/controller/AuthController.java → src/main/java/hn/shadowcore/mercadox/oauth/security/JwtSigner.java
- `CustomUserDetailsService` --references--> `UserDetailsMapper`  [EXTRACTED]
  src/main/java/hn/shadowcore/mercadox/oauth/service/CustomUserDetailsService.java → src/main/java/hn/shadowcore/mercadox/oauth/mapper/UserDetailsMapper.java

## Import Cycles
- None detected.

## Communities (13 total, 1 thin omitted)

### Community 0 - "AuthController.java"
Cohesion: 0.20
Nodes (13): AuthRequestDto, AuthController, AuthenticationManager, PostMapping, RegistrationUseCase, RequestMapping, RequiredArgsConstructor, Response (+5 more)

### Community 1 - "UserDetailsMapper.java"
Cohesion: 0.18
Nodes (13): GrantedAuthority, Mapper, Mapping, Role, User, UserDetailsMapper, CustomUserDetailsService, Override (+5 more)

### Community 2 - "JwtSigningConfig.java"
Cohesion: 0.20
Nodes (11): ConfigurationProperties, EnableConfigurationProperties, Bean, Configuration, RSAPrivateKey, JwtSigningConfig, Resource, JwtSigningProperties (+3 more)

### Community 3 - "RegistrationService"
Cohesion: 0.28
Nodes (9): KafkaTemplate, RedisTemplate, RegistrationUseCase, Override, RequiredArgsConstructor, Service, User, UserRepository (+1 more)

### Community 4 - "MercadoXControllerTest.java"
Cohesion: 0.27
Nodes (9): ActiveProfiles, AutoConfigureMockMvc, EnableCaching, Retention, SpringBootApplication, MercadoXOauthApplication, SpringBootTest, MercadoXControllerTest (+1 more)

### Community 5 - "OAuthTenantValidatorService.java"
Cohesion: 0.26
Nodes (9): AnonymousTenantValidator, Configuration, Import, OAuthSharedConfiguration, OrganizationRepository, Override, RequiredArgsConstructor, Service (+1 more)

### Community 6 - "AuthManagerConfig.java"
Cohesion: 0.33
Nodes (7): AuthenticationConfiguration, AuthManagerConfig, AuthenticationManager, Bean, Configuration, Import, PasswordEncoder

### Community 7 - "MercadoXSecurityFilterConfig.java"
Cohesion: 0.33
Nodes (9): EnableWebSecurity, HttpSecurity, JwtVerifier, SecurityFilterChain, AnonymousTenantValidator, Bean, Configuration, RequiredArgsConstructor (+1 more)

### Community 8 - "AuthService.java"
Cohesion: 0.33
Nodes (9): RoleRepository, AuthService, OrganizationRepository, PasswordEncoder, RegisterRequestDto, RequiredArgsConstructor, Service, User (+1 more)

### Community 9 - "RegistrationController.java"
Cohesion: 0.33
Nodes (9): PostMapping, RegisterRequestDto, RequestMapping, RequiredArgsConstructor, Response, ResponseEntity, RestController, User (+1 more)

### Community 10 - "mvnw"
Cohesion: 0.33
Nodes (6): mvnw script, clean(), die(), exec_maven(), set_java_home(), verbose()

### Community 11 - "MercadoXOauthApplicationTests.java"
Cohesion: 0.60
Nodes (3): SpringBootTest, MercadoXOauthApplicationTests, Test

## Knowledge Gaps
- **1 isolated node(s):** `mercado-x-oauth`
  These have ≤1 connection - possible missing edges or undocumented components.
- **1 thin communities (<3 nodes) omitted from report** — run `graphify query` to explore isolated nodes.

## Suggested Questions
_Questions this graph is uniquely positioned to answer:_

- **Why does `JwtSigner` connect `AuthController.java` to `JwtSigningConfig.java`?**
  _High betweenness centrality (0.151) - this node is a cross-community bridge._
- **Why does `AuthService` connect `AuthService.java` to `RegistrationController.java`?**
  _High betweenness centrality (0.131) - this node is a cross-community bridge._
- **Why does `RegistrationService` connect `RegistrationService` to `RegistrationController.java`?**
  _High betweenness centrality (0.073) - this node is a cross-community bridge._
- **What connects `mercado-x-oauth` to the rest of the system?**
  _1 weakly-connected nodes found - possible documentation gaps or missing edges._