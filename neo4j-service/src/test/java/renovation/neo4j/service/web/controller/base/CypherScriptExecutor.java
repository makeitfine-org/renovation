/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.neo4j.service.web.controller.base;

import lombok.RequiredArgsConstructor;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CypherScriptExecutor {

    private final Driver driver;

    public void executeScript(String script) {

        String[] statements = script
                .lines()
                .filter(l->!l.startsWith("//"))
                .collect(Collectors.joining(System.getProperty("line.separator")))
                .split(";");

        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {
                for (String raw : statements) {
                    String trimmed = raw.trim();
                    if (!trimmed.isEmpty()) {
                        tx.run(trimmed);
                    }
                }
                return null;
            });
        }
    }
}
