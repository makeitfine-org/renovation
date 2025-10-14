<!-- 198fcd15-19bf-4681-9eef-896231c8bb5c f1f7b711-0dbc-4088-8955-c2b4c6172568 -->
# System Design Documentation for Renovation Project

## Overview

Create a detailed system design document analyzing the Renovation microservices architecture, technologies used, and deployment infrastructure.

## Approach

### 1. Document Structure

Create a comprehensive markdown document (`SYSTEM_DESIGN.md`) in the project root with the following sections:

- **Executive Summary**: High-level overview of the Renovation system
- **System Architecture**: Overall architecture and design principles
- **Module Analysis**: Detailed description of each module (backend, info, gateway, frontends, common, api-test)
- **Technology Stack**: Comprehensive list of technologies and their purposes
- **Data Architecture**: Database schemas and data flow
- **Security Architecture**: OAuth2/Keycloak integration, secrets management
- **Deployment Architecture**: Docker Compose, Kubernetes/Helm, Istio
- **Infrastructure Components**: Vault, External Secrets, monitoring stack
- **Integration Patterns**: How modules communicate

### 2. Diagrams to Create (using Mermaid)

**High-Level Architecture Diagram**

- Shows all microservices and their relationships
- External dependencies (databases, cache, auth)
- Client interactions

**Component Diagram**

- Detailed view of each module's internal structure
- Key components and their responsibilities

**Deployment Architecture**

- Kubernetes namespace organization
- Helm chart structure
- Service mesh (Istio) configuration

**Data Flow Diagram**

- Request/response flows
- Authentication/authorization flow
- Inter-service communication

**Infrastructure Diagram**

- CI/CD pipeline
- Secrets management (Vault + External Secrets)
- Monitoring stack (Prometheus, Grafana, Jaeger, Kiali, Loki)

### 3. Module Descriptions

**Backend Module**

- Purpose: Main REST API service for renovation work management
- Tech: Spring Boot, Kotlin, PostgreSQL, Redis, Liquibase, OAuth2
- Features: CRUD operations, session management, metrics

**Info Module**

- Purpose: GraphQL API for information/details management
- Tech: Spring Boot, Kotlin, MongoDB, Netflix DGS GraphQL, OAuth2
- Features: GraphQL queries/mutations, MongoDB persistence

**Gateway Module**

- Purpose: API Gateway and routing layer
- Tech: Spring Cloud Function, OAuth2, certificate-based auth
- Features: Request routing, authentication, social login support

**Frontend Module**

- Purpose: Main Vue.js UI (embedded in backend)
- Tech: Vue 3, Bootstrap, Axios, Vuex, Vue Router

**Frontend-Info Module**

- Purpose: Dedicated Vue.js UI for GraphQL info service
- Tech: Vue 3, Bootstrap, Axios, Vuex, Keycloak-js

**Common Module**

- Purpose: Shared utilities and security components
- Features: JWT utils, REST clients, JSON utilities, access token management

**API-Test Module**

- Purpose: Integration and E2E testing
- Tech: JUnit 5, REST Assured, Testcontainers

### 4. Key Findings to Document

- Microservices architecture with polyglot persistence (PostgreSQL + MongoDB)
- OAuth2/OIDC with Keycloak for authentication
- Redis for session management and caching
- Istio service mesh for traffic management
- Vault + External Secrets for secrets management
- Comprehensive monitoring with Prometheus, Grafana, Jaeger
- Multi-environment support (Docker Compose, Minikube, Kubernetes)
- Gradle multi-module build with Kotlin DSL

### To-dos

- [x] Create SYSTEM_DESIGN.md with executive summary and project overview
- [x] Add high-level architecture diagram showing all microservices, databases, and external systems
- [x] Document each module (backend, info, gateway, frontends, common, api-test) with purposes and technologies
- [x] Create detailed component diagram showing internal structure of each module
- [x] Document data architecture including PostgreSQL, MongoDB, Redis usage and schemas
- [x] Create security architecture diagram showing OAuth2/Keycloak flow and secrets management
- [x] Create deployment architecture diagram for Kubernetes with Helm charts and namespaces
- [x] Create data flow diagram showing request/response flows and inter-service communication
- [x] Create infrastructure diagram showing monitoring stack, CI/CD, and operational components
- [x] Create comprehensive technology stack summary table with all frameworks, libraries, and tools

