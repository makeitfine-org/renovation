terraform {
    required_providers {
        docker = {
            source  = "kreuzwerker/docker"
            version = "~> 3.0"
        }
    }
}

provider "docker" {}

resource "docker_network" "spring_net" {
    name = "spring_network"
}

resource "docker_image" "postgres" {
    name = "postgres:16.1"
}

resource "docker_container" "postgres" {
    name  = "postgres-db"
    image = docker_image.postgres.name
    env = [
        "POSTGRES_DB=postgres",
        "POSTGRES_USER=postgres",
        "POSTGRES_PASSWORD=postgres1"
    ]
    ports {
        internal = 5432
        external = 5532
    }
    networks_advanced {
        name = docker_network.spring_net.name
    }
}

resource "docker_image" "springboot_app" {
    name = "springboot-app:latest"
    build {
        context    = "${path.module}"
        dockerfile = "${path.module}/Dockerfile"
    }
}

resource "docker_container" "springboot" {
    name  = "springboot-app"
    image = docker_image.springboot_app.name
    depends_on = [docker_container.postgres]
    env = [
        "POSTGRES_DB_URL=jdbc:postgresql://postgres-db:5432/postgres?currentSchema=renovation-batch",
        "POSTGRES_USERNAME=postgres",
        "POSTGRES_PASSWORD=postgres1"
    ]
    ports {
        internal = 8080
        external = 8280
    }
    networks_advanced {
        name = docker_network.spring_net.name
    }
}
