*** This is not a prod-friendly app, just mock services and made-up Payment Providers & Delivery Providers  ***
*** Only for Educational purposes ***

# webshop

Mock webshop with Spring Boot, RabbitMQ and PostgresSQL and React Vite as Webshop UI and React Vite as Admin UI

## backend

Spring Boot web app

Postgres is used as a database and RabbitMQ as a message broker

- CQRS (Command Query Responsibility Segregation)
    - GraphQL is used for Queries and REST for Commands
- Outbox Transactional pattern
- DDD (Domain Driven Design)
    - Application
    - Domain
    - Infrastructure
- Event Sourcing (ish)
    - Outbox table can be used for Replays
- Design for Observability
    - Loki
    - Zipkin
    - Grafana
- Design for Resilience
- Design for Security

### Management/UI

- Codecentric admin server (:8081)
- Zipkin (:9411)
- RabbitMQ management UI (:15672)
- Graphiql (/graphiql)
- Swagger (/swagger-ui/index.html)
- Admin View
- View

### What this project does

- enhance distributed architectures
- utilize 24/7 mindshare
- transform sticky supply-chains
- brand strategic schemas
- architect virtual vortals