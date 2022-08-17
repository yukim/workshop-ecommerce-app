# Cassandra eCommerce Application

This is a fork of ["Building an E-commerce Website"](https://github.com/datastaxdevs/workshop-ecommerce-app), to demonstrate
OpenTelemetry integration of Apache Cassandra.

## Modifications

- Removed Astra DB dependency to enable connecting to Apache Cassandra cluster
- Introduced custom `CqlSession` that injects OpenTelemetry `Context` to Cassandra Native Protocol custom payload
- Added `spring-boot:build-image` config to build the container image