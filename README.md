![Kubernetes](https://img.shields.io/badge/kubernetes-%23326ce5.svg?style=for-the-badge&logo=kubernetes&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Vite](https://img.shields.io/badge/vite-%23646CFF.svg?style=for-the-badge&logo=vite&logoColor=white)
![React](https://img.shields.io/badge/react-%2320232a.svg?style=for-the-badge&logo=react&logoColor=%2361DAFB)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Grafana](https://img.shields.io/badge/grafana-%23F46800.svg?style=for-the-badge&logo=grafana&logoColor=white)

# Webshop

***This is NOT a production-ready application, just mock services with made-up Payment Providers & Delivery Providers***

***Educational purposes only***

A mock webshop application built with Spring Boot, Postgres, and React Vite for Admin Web UI

## Project Overview

This project demonstrates a modern e-commerce application architecture with the following features:

- Modular backend with domain-driven design
- CQRS pattern with GraphQL for queries and REST for commands
- Event-driven architecture with a Transactional Outbox pattern
- Observability and monitoring tools integration
- React-based admin web-dashboard

## Architecture

### Backend Architecture

The backend follows several architectural patterns:

#### CQRS (Command Query Responsibility Segregation)

- **GraphQL** is used for Queries
- **REST** is used for Commands

#### Transactional Outbox & Inbox

- Message Relay with a Polling Publisher pattern
- Ensures reliable message delivery across services

#### DDD (Domain Driven Design)

The application is structured according to DDD principles:

- **Application Layer**: Contains application services, controllers, and DTOs
- **Domain Layer**: Contains domain entities, value objects, and domain services
- **Infrastructure Layer**: Contains repository implementations, external service integrations

#### Event Sourcing

- Inbox table can be used for event replays
- Enables rebuilding state from event history

#### Observability

Note: The README mentions Grafana, Loki, and Prometheus for observability, but these are not currently configured in the
docker-compose file, yet!

- **Loki**: Log aggregation
- **Zipkin**: Distributed tracing
- **Prometheus**: Metrics monitoring
- **Grafana**: Visualization

#### Resilience & Security

- Audit and logging
- Input validation
- Error handling

## Project Structure

The project is organized into multiple modules:

### Backend Modules

- **app**: Main application module that integrates all other modules
- **shared**: Common utilities and shared components
- **product**: Product domain module
- **inventory**: Inventory management module
- **price**: Pricing logic module
- **identity**: User authentication and authorization module
- **outbox**: Implementation of the Transactional Outbox pattern
- **inbox**: Implementation of the Transactional Inbox pattern
- **category**: Product category management module
- **config**: Configuration and shared settings module

### Frontend Modules

- **admin-dashboard**: React-based admin interface built with TypeScript and Vite

Note: The project structure currently only includes the admin-dashboard frontend module.

### Infrastructure Modules

- **compose.yaml**: Docker Compose file for running the infrastructure
- **authorization**: Spring Authorization Server
- **gateway**: Spring API Gateway

## Contributing

This project is for educational purposes. Feel free to fork and extend it.

## License

This project is open-source and available under the [MIT License](LICENSE).
