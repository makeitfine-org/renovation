/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.api.test.frontend.info

import org.apache.commons.io.IOUtils
import java.nio.charset.Charset

enum class FrontendInfoFileHelper(filePath: String) {
    WORKER_RESPONSE("frontend-info/worker_response.json"),
    GRAPHQL_RESPONSE("frontend-info/graphql_response.json");

    val fileContent: String = IOUtils.toString(
        FrontendInfoFileHelper::class.java.getResourceAsStream("/$filePath"),
        Charset.defaultCharset()
    )
}
