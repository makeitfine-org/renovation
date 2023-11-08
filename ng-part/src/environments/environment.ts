/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

// This file can be replaced during build by using the `fileReplacements` array.
// `ng build` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const baseUrl = "http://192.168.0.113:9190"

export const environment = {
  production: false,
  v1ApiTodoUrl: baseUrl + "/api/v1/info/todo",
  infoModuleUrl: baseUrl,
  websocketServerUrl: "ws://localhost:6759",
  nodeServerUrl: "ws://localhost:3000"
}

/*
 * For easier debugging in development mode, you can import the following file
 * to ignore zone related error stack frames such as `zone.run`, `zoneDelegate.invokeTask`.
 *
 * This import should be commented out in production mode because it will have a negative impact
 * on performance if an error is thrown.
 */
// import 'zone.js/plugins/zone-error';  // Included with Angular CLI.
