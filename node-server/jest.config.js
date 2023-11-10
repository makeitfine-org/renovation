/** @type {import('ts-jest').JestConfigWithTsJest} */
module.exports = {
  preset: 'ts-jest',
  testEnvironment: 'node',
  "moduleNameMapper": {
    "@main/(.*)": "<rootDir>/src/main/$1",
    "@test/(.*)": "<rootDir>/test/main/$1"
  },
}
