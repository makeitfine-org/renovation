import {Injectable} from "@angular/core"
import {Subject} from "rxjs"

@Injectable({
  providedIn: "root"
})
export class ErrorService {
  private _error$ = new Subject<string>()

  get error$(): Subject<string> {
    return this._error$
  }

  set error$(value: Subject<string>) {
    this._error$ = value
  }

  handle(message: string) {
    this._error$.next(message)
  }

  clear() {
    this._error$.next("")
  }
}
