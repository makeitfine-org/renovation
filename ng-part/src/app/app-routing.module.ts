/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {NgModule} from "@angular/core"
import {RouterModule, Routes} from "@angular/router"
import {TodoComponent} from "src/app/component/todo/todo.component"
import {AboutPageComponent} from "src/app/component/about-page/about-page.component"
import {DashboardComponent} from "src/app/component/dashboard/dashboard.component"
import {UiElementsComponent} from "./component/ui-elements/ui-elements.component"
import {TablesComponent} from "./component/tables/tables.component"
import {FormsComponent} from "./component/forms/forms.component"
import {CardsComponent} from "./component/cards/cards.component"
import {ModalComponent} from "./component/modal/modal.component"
import {BlankComponent} from "./component/blank/blank.component"
import {TodoFullComponent} from "./component/todo-full/todo-full.component"

const routes: Routes = [
  //deprecated: to be rewritten and removed (for learning purposes)
  {path: "about", component: AboutPageComponent},
  {path: "todo", component: TodoComponent},
  {path: "todofull", component: TodoFullComponent},

  {path: "", component: TodoComponent},
  {path: "about", component: AboutPageComponent},

  //todo: temp:
  {path: "dashboard", component: DashboardComponent},
  {path: "ui", component: UiElementsComponent},
  {path: "tables", component: TablesComponent},
  {path: "forms", component: FormsComponent},
  {path: "cards", component: CardsComponent},
  {path: "modal", component: ModalComponent},
  {path: "blank", component: BlankComponent},
]

@NgModule({
  declarations: [],
  imports: [ RouterModule.forRoot(routes) ],
  exports: [ RouterModule ]
})
export class AppRoutingModule {
}
