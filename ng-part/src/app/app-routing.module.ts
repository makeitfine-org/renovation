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
  {path: "about", component: AboutPageComponent}
]

@NgModule({
  declarations: [],
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
