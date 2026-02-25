# MercadoX OAuth Service

## Overview

`mercado-x-oauth` is a Spring Boot microservice responsible for:

- Authentication
- Authorization
- JWT token generation
- Role-based access control

---

## Responsibilities

- /login endpoint
- /register endpoint
- JWT generation and validation
- UserDetailsService implementation
- Spring Security configuration
- Role management

---

## Security

- Stateless authentication (JWT)
- Role-based authorization (ROLE_USER, ROLE_ADMIN, etc.)
- Multi-tenant awareness (orgId support)

---

## Dependencies

- mercado-x-library-entity
- mercado-x-library-jpa
- mercado-x-context

---

## Architecture Role

Acts as the **identity provider** for the MercadoX ecosystem.