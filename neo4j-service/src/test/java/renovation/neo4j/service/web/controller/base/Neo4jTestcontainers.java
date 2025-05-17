/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.neo4j.service.web.controller.base;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.Neo4jContainer;


@TestConfiguration(proxyBeanMethods = false)
public class Neo4jTestcontainers {
    public static final String NEO4J_IMAGE = "neo4j:2025.04.0-community";

    @Bean
    @ServiceConnection(name = "neo4j")
    public Neo4jContainer neo4jContainer() {
        return new Neo4jContainer(NEO4J_IMAGE);
    }
}
