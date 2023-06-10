/*
 *  Created under not commercial project "Renovation"
 *
 *   Copyright 2021-2022
 */

package renovation.backend.api.test.helper

import org.apache.commons.io.IOUtils
import java.nio.charset.Charset

enum class FileHelper(filePath: String) {
    WORK_CONTROLLER_GET_ALL_RESPONSE("backend/work_controller/get_all_response.json"),
    WORK_CONTROLLER_GET_TITLE_LIKE_RESPONSE("backend/work_controller/get_title_like_response.json"),
    WORK_CONTROLLER_GET_BY_ID_RESPONSE("backend/work_controller/get_by_id_response.json"),

    WORKER_CONTROLLER_GET_ALL_RESPONSE("backend/worker_controller/get_all_response.json"),

    WORKER_RESPONSE("frontend-info/worker_response.json"),
    GRAPHQL_RESPONSE("frontend-info/graphql_response.json");

    val fileContent: String = IOUtils.toString(
        FileHelper::class.java.getResourceAsStream("/$filePath"),
        Charset.defaultCharset()
    )
}
