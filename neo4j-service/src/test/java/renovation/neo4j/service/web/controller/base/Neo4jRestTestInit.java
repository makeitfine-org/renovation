/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.neo4j.service.web.controller.base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import renovation.common.web.RestTestInit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@ContextConfiguration(classes = Neo4jTestcontainers.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class Neo4jRestTestInit extends RestTestInit {

    @Autowired
    private CypherScriptExecutor executor;

    public Neo4jRestTestInit(String basePath) {
        super(basePath);
    }

    @BeforeEach
    void fillNeo4j() throws IOException {
        String script = Files.readString(Path.of("src/test/resources/test.cypher"));
        executor.executeScript(script);
    }

    @AfterEach
    void clearNeo4j() {
        executor.executeScript("MATCH (n) DETACH DELETE n");
    }
}
