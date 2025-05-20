package renovation.neo4j.service.web;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Route {
    public static final String STARTUP = "/startup";

    public static final String AUTHOR = "/author";

    public static final String BOOK = "/book";
}
