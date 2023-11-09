/*
 * Created under not commercial project "Renovation"
 *
 * Copyright 2021-2023
 */
import cloneDeep from "lodash.clonedeep"

const phrases: Phrase[] = [ {
  id: 1, title: "hi", text: "Hello everyone!"
}, {
  id: 2, title: "place", text: "I'm here!"
}, {
  id: 3, title: "question", text: "Who there?"
}, {
  id: 4, title: "bed words", text: "Damn!"
}, {
  id: 5, title: "quite", text: "I'm off"
} ]

export interface Phrase {
  id: number
  title: string
  text: string
}

/**
 * Array of default phrases
 */
export const defaultPhrases = (): Phrase[] => cloneDeep(phrases)
