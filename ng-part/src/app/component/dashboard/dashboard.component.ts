import {Component, OnInit} from "@angular/core"

export interface User {
  name: string
  email: string
  title: string
  title2: string
  status: string
  role: string
}

@Component({
  selector: "app-dashboard",
  templateUrl: "./dashboard.component.html",
  styleUrls: [ "./dashboard.component.scss" ]
})
export class DashboardComponent implements OnInit {
  users: User[]

  ngOnInit(): void {
    const testUser = {
      name: "John Doe",
      email: "john@example.com",
      title: "Software Engineer",
      title2: "Web dev",
      status: "Active",
      role: "Owner",
    }

    this.users = Array(10).fill(testUser)
  }
}
