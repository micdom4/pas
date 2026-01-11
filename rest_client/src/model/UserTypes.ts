// export class UserType {
//     id: string
//     login: string
//     name: string
//     surname: string
//     active: boolean
//
//     constructor(id: string, login: string, name: string, surname: string, active: boolean) {
//         this.id = id;
//         this.login = login;
//         this.name = name;
//         this.surname = surname;
//         this.active = active;
//     }
//
//     public toString(): string {
//         return `Person: ${this.id} ${this.login} ${this.name} ${this.surname} ${this.active}`;
//     }
// }

import type {BaseType} from "./BaseType.ts";

// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-expect-error
export enum TypeOfUser {
    "CLIENT",
    "MANAGER",
    "ADMIN"
}

export interface UserType extends BaseType {
    login: string
    name: string
    surname: string
    type: TypeOfUser
    active: boolean
}

export interface CreateUserType {
    login: string
    name: string
    surname: string
    type: TypeOfUser
}

export interface EditUserType {
    surname: string
}