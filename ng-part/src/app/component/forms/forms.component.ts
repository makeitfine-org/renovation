import {Component} from "@angular/core"
import {FormControl, FormGroup, Validators} from "@angular/forms"

interface User {
  username: string;
  email: string;
  password: string;
  passwordConfirmation: string;
}

@Component({
  selector: "app-forms",
  templateUrl: "./forms.component.html",
  styleUrls: [ "./forms.component.scss" ]
})
export class FormsComponent {
  userForm = new FormGroup({
    username: new FormControl<string>("", [
      Validators.required,
    ]),
    email: new FormControl<string>("", [
      Validators.required,
    ]),
    password: new FormControl<string>("", [
      Validators.required,
    ]),
    passwordConfirmation: new FormControl<string>("", [
      Validators.required,
    ]),
  })

  submit() {
    const user = {
      username: this.userForm.value.username,
      password: this.userForm.value.password,
      email: this.userForm.value.email,
      passwordConfirmation: this.userForm.value.passwordConfirmation
    } as User

    console.log("User: " + JSON.stringify(user))
  }
}
