/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

import {Subject} from "rxjs"

export abstract class EventHandler<T = {}> {

  subject: Subject<T>

  constructor(subject: Subject<T>) {
    this.subject = subject

    this.subject.subscribe(data => {
      console.log(`hello from important ${ JSON.stringify(data) }`)
      this.handle(data)
    })
  }

  abstract handle(data: T): void
}
