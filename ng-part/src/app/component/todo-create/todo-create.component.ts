import {Component} from "@angular/core"
import {ModalService} from "src/app/data/service/modal.service"
import {TodoCrudService} from "src/app/data/service/todo-crud.service"
import {FormControl, FormGroup, Validators} from "@angular/forms"
import {Util} from "src/app/util/util"
import {Constant} from "src/app/data/model/constant.modal"
import {tap} from "rxjs"
import {TodoFullComponent} from "../todo-full/todo-full.component"

@Component({
  selector: "app-todo-create",
  templateUrl: "./todo-create.component.html",
  styleUrls: [ "./todo-create.component.scss" ]
})
export class TodoCreateComponent {
  constructor(
    private todoCrudService: TodoCrudService,
    private modalService: ModalService,
    private todoFullComponent: TodoFullComponent //todo: should be rewrite with changing todoService to save todos
  ) {
  }

  form = new FormGroup({
    title: new FormControl<string>("", [
      Validators.required,
      Validators.minLength(2)
    ])
  })

  get title() {
    return this.form.controls.title as FormControl
  }

  submit() {
    this.todoCrudService.createTodo({
      id: Util.getRandomInt(0, 1000_000),
      title: this.form.value.title as string,
      completed: false,

      // description?: boolean,
      // category?: string,
      image: Constant.DEFAULT_TODO_IMG,
      // price?: number,
      // rating?: Rating,

      date: new Date()
    })
      .pipe(
        tap(() => this.todoFullComponent.ngOnInit())
      )
      .subscribe(() => {
        this.modalService.close()
      })
  }
}
