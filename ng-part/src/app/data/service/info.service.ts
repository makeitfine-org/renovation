import {Injectable} from "@angular/core"
import {HttpClient} from "@angular/common/http"
import {environment} from "../../../environments/environment"

@Injectable({
  providedIn: "root"
})
export class InfoService {
  constructor(private http: HttpClient) {
  }

  getAbout = () => this.http.get<object>(`${ environment.infoModuleUrl }/about`)

  getModule = () => this.http.get<string>(
    `${ environment.infoModuleUrl }/module`,
    {responseType: "text" as "json"}
  )
}
