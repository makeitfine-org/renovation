/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import "module-alias/register" //for alias to be working
import app from "./http-app"

const PORT: number = (process.env.PORT || 3000) as number

app.listen(PORT, () => console.log(`http server running on port: ${ PORT }`))
