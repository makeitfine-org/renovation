/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {enableProdMode} from "@angular/core"
import {platformBrowserDynamic} from "@angular/platform-browser-dynamic"

import {AppModule} from "./app/app.module"
import {environment} from "./environments/environment"

if (environment.production) {
  enableProdMode()
}

platformBrowserDynamic().bootstrapModule(AppModule)
  .catch(err => console.error(err))
