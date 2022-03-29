/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2022
 */

package renovation.backend.api.test

import org.apache.commons.io.IOUtils
import java.nio.charset.Charset

enum class FileHelper(filePath: String) {
    WORK_CONTROLLER_GET_ALL_RESPONSE("work_controller/get_all_response.json");

    val fileContent: String = IOUtils.toString(
        FileHelper::class.java.getResourceAsStream("/$filePath"),
        Charset.defaultCharset()
    )
}
