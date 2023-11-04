/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {NgModule} from "@angular/core"
import {RouterModule, Routes} from "@angular/router"
import {TodoComponent} from "src/app/component/todo/todo.component"
import {AboutPageComponent} from "src/app/component/about-page/about-page.component"
import {AppComponent} from "./app.component"

const routes: Routes = [
  //deprecated: to be rewritten and removed (for learning purposes)
  {path: "about", component: AboutPageComponent},
  {path: "todo", component: TodoComponent},

  {path: "", component: TodoComponent},
  {path: "about", component: AboutPageComponent},

  //todo: temp:
  {path: "dashboard", component: AppComponent},
  {path: "ui", component: AppComponent},
  {path: "tables", component: AppComponent},
  {path: "forms", component: AppComponent},
  {path: "cards", component: AppComponent},
  {path: "modal", component: AppComponent},
  {path: "blank", component: AppComponent},
]

@NgModule({
  declarations: [],
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {
}
