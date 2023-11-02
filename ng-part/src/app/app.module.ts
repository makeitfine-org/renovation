/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {BrowserModule} from "@angular/platform-browser"
import {NgModule} from "@angular/core"
import {AppComponent} from "src/app/app.component"
import {TodoComponent} from "src/app/component/todo/todo.component"
import {HttpClientModule} from "@angular/common/http"
import {TodoFormComponent} from "src/app/component/todo-form/todo-form.component"
import {FormsModule} from "@angular/forms"
import {TodoTitleFilterPipe} from "src/app/pipe/todo-filter.pipe"
import {DatePipe} from "@angular/common"
import {AppRoutingModule} from "src/app/app-routing.module"
import {GlobalErrorComponent} from "src/app/component/global-error/global-error.component"
import {NavigationComponent} from "./component/navigation/navigation.component"
import {RouterLinkActive} from "@angular/router"
import {AboutPageComponent} from "./component/about-page/about-page.component"

@NgModule({
  declarations: [
    AppComponent,
    TodoComponent,
    TodoFormComponent,
    TodoTitleFilterPipe,
    GlobalErrorComponent,
    NavigationComponent,
    AboutPageComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    RouterLinkActive
  ],
  providers: [ DatePipe ],
  bootstrap: [ AppComponent ]
})
export class AppModule {
}
