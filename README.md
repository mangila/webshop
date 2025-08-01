![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Flutter](https://img.shields.io/badge/Flutter-%2302569B.svg?style=for-the-badge&logo=Flutter&logoColor=white)
![Vite](https://img.shields.io/badge/vite-%23646CFF.svg?style=for-the-badge&logo=vite&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Grafana](https://img.shields.io/badge/grafana-%23F46800.svg?style=for-the-badge&logo=grafana&logoColor=white)

# Webshop

***This is NOT a production-ready application, just mock services with made-up Payment Providers & Delivery Providers***

***Educational purposes only***

A mock webshop application built with Spring Boot, PostgreSQL, React Vite for Admin Web UI, and Flutter for Webshop
UI

## Project Overview

This project demonstrates a modern e-commerce application architecture with the following features:

- Modular backend with domain-driven design
- CQRS pattern with GraphQL for queries and REST for commands
- Event-driven architecture with a Transactional Outbox pattern
- Observability and monitoring tools integration
- React-based admin web-dashboard
- Flutter-based Webshop UI

## Architecture

### Backend Architecture

The backend follows several architectural patterns:

#### CQRS (Command Query Responsibility Segregation)

- **GraphQL** is used for Queries
- **REST** is used for Commands

#### Transactional Outbox

- Message Relay with a Polling Publisher pattern
- Ensures reliable message delivery across services

#### DDD (Domain Driven Design)

The application is structured according to DDD principles:

- **Application Layer**: Contains application services, controllers, and DTOs
- **Domain Layer**: Contains domain entities, value objects, and domain services
- **Infrastructure Layer**: Contains repository implementations, external service integrations

#### Event Sourcing

- Outbox table can be used for event replays
- Enables rebuilding state from event history

#### Observability

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

### Frontend Modules

- **admin-dashboard**: React-based admin interface built with TypeScript and Vite

## Technology Stack

### Backend

- **Java 21**
- **Spring Boot 3.5.3**
- **Spring Data JPA**
- **Spring GraphQL**
- **PostgreSQL**
- **Caffeine** (for caching)
- **Micrometer** (for metrics)
- **Zipkin** (for distributed tracing)

### Frontend

- **React**
- **TypeScript**
- **Vite**

## Setup and Running

### Prerequisites

- Java 21
- Docker and Docker Compose
- Node.js and npm/yarn

### Running the Infrastructure

```bash
cd infrastructure
docker-compose up -d
```

This will start:

- PostgreSQL database on port 5432
- Zipkin on port 9411

### Running the Backend

```bash
cd platform
./mvnw spring-boot:run -pl app
```

### Running the Admin Dashboard

```bash
cd ui/admin-dashboard
npm install
npm run dev
```

## API Documentation

- **GraphQL**: Available at `/graphiql` for interactive queries
- **Swagger UI**: Available at `/swagger-ui/index.html` for REST API documentation

## Management and Monitoring

- **Zipkin UI**: Available at port 9411

## Database Configuration

The PostgreSQL database is configured with:

- Database name: `webshop`
- Username: `my_user`
- Password: `secret`

## Contributing

This project is for educational purposes. Feel free to fork and extend it.

## License

This project is open-source and available under the [MIT License](LICENSE).
