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

export interface ClientType extends UserType {
    type: TypeOfUser.CLIENT
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