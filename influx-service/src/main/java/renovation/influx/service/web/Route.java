/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2025
 */

package renovation.influx.service.web;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Route {

    public static final String STARTUP = "/startup";

    public static final String TEMPERATURE = "/temperature";
}
