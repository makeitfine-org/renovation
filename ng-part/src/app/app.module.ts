import {BrowserModule} from '@angular/platform-browser'
import {NgModule} from '@angular/core'
import {AppComponent} from './app.component'
import {TodosComponent} from './todos/todos.component'
import {HttpClientModule} from '@angular/common/http'
import {TodosFormComponent} from './todo-form/todos-form.component'
import {FormsModule} from '@angular/forms'
import {TodosFilterPipe} from './shared/todos-filter.pipe'
import {DatePipe} from "@angular/common"
import {AppRoutingModule} from './app-routing.module'

@NgModule({
  declarations: [
    AppComponent,
    TodosComponent,
    TodosFormComponent,
    TodosFilterPipe
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    AppRoutingModule
  ],
  providers: [ DatePipe ],
  bootstrap: [ AppComponent ]
})
export class AppModule {
}
