import {Component, OnInit} from "@angular/core"
import {InfoService} from "../../data/service/info.service"
import {catchError, retry, tap} from "rxjs/operators"
import {HttpErrorResponse} from "@angular/common/http"
import {throwError} from "rxjs"
import {ErrorService} from "../../data/service/error.service"

export type About = {
  name: string,
  description: string,
} | null

@Component({
  selector: "app-about-page",
  templateUrl: "./about-page.component.html",
  styleUrls: [ "./about-page.component.scss" ]
})
export class AboutPageComponent implements OnInit {

  about: About = null
  module: string = ""
  string = ""

  constructor(
    private infoService: InfoService,
    private errorService: ErrorService,
  ) {
  }

  ngOnInit() {
    this.infoService.getAbout()
      .pipe(
        retry(2),
        tap(about => {
          this.about = about as About
        }),
        catchError(this.errorHandler.bind(this))
      ).subscribe(() => console.debug("get about"))

    this.infoService.getModule()
      .pipe(
        retry(2),
        tap(module => {
          this.module = module
        }),
        catchError(this.errorHandler.bind(this))
      )
      .subscribe(() => console.debug("get module"))
  }

  private errorHandler(error: HttpErrorResponse) {
    this.errorService.handle(error.message)
    return throwError(() => error.message) //todo: deprecated `throwError`
  }
}
