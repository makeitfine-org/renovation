/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

export const baseUrl = "http://192.168.0.113:9190"

export const environment = {
  production: true,
  v1ApiTodoUrl: baseUrl + "/api/v1/info/todo",
  infoModuleUrl: baseUrl,
  websocketServerUrl: `ws://192.168.0.113:6759`,
  nodeServerUrl: "ws://192.168.0.113:3000"
}
