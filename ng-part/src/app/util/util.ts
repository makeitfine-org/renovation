/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */

export class Util {
  static getRandomInt(min: number, max: number) {
    min = Math.ceil(min)
    max = Math.floor(max)
    return Math.floor(Math.random() * (max - min + 1)) + min
  }
}
