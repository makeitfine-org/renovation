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
import {FormsModule, ReactiveFormsModule} from "@angular/forms"
import {TodoTitleFilterPipe} from "src/app/pipe/todo-filter.pipe"
import {DatePipe, NgOptimizedImage} from "@angular/common"
import {AppRoutingModule} from "src/app/app-routing.module"
import {GlobalErrorComponent} from "src/app/component/global-error/global-error.component"
import {NavigationComponent} from "src/app/component/navigation/navigation.component"
import {RouterLinkActive} from "@angular/router"
import {AboutPageComponent} from "src/app/component/about-page/about-page.component"
import {HeaderComponent} from "src/app/component/header/header.component"
import {SidebarComponent} from "src/app/component/sidebar/sidebar.component"
import {DashboardComponent} from "src/app/component/dashboard/dashboard.component"
import {UiElementsComponent} from "./component/ui-elements/ui-elements.component"
import {TablesComponent} from "./component/tables/tables.component"
import {FormsComponent} from "./component/forms/forms.component"
import {CardsComponent} from "./component/cards/cards.component"
import {ModalComponent} from "./component/modal/modal.component"
import {BlankComponent} from "./component/blank/blank.component"

@NgModule({
  declarations: [
    AppComponent,
    TodoComponent,
    TodoFormComponent,
    TodoTitleFilterPipe,
    GlobalErrorComponent,
    NavigationComponent,
    AboutPageComponent,
    HeaderComponent,
    SidebarComponent,
    DashboardComponent,
    UiElementsComponent,
    TablesComponent,
    FormsComponent,
    CardsComponent,
    ModalComponent,
    BlankComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule,
    RouterLinkActive,
    NgOptimizedImage,
    ReactiveFormsModule
  ],
  providers: [ DatePipe ],
  bootstrap: [ AppComponent ]
})
export class AppModule {
}
