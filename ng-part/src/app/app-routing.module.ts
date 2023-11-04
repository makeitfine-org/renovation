/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {NgModule} from "@angular/core"
import {RouterModule, Routes} from "@angular/router"
import {TodoComponent} from "src/app/component/todo/todo.component"
import {AboutPageComponent} from "src/app/component/about-page/about-page.component"

const routes: Routes = [
  {path: "", component: TodoComponent},
  {path: "todo", component: TodoComponent},
  {path: "about", component: AboutPageComponent},

  //todo: temp:
  {path: "dashboard", component: AboutPageComponent},
  {path: "ui", component: AboutPageComponent},
  {path: "tables", component: AboutPageComponent},
  {path: "forms", component: AboutPageComponent},
  {path: "cards", component: AboutPageComponent},
  {path: "modal", component: AboutPageComponent},
  {path: "blank", component: AboutPageComponent},
]

@NgModule({
  declarations: [],
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {
}
