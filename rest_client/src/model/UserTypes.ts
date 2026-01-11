import type {BaseType} from "./BaseType.ts";
import * as Yup from "yup";

// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-expect-error
export enum TypeOfUser {
    "CLIENT",
    "MANAGER",
    "ADMIN"
}

export const StringTypeOfUser = ["CLIENT", "MANAGER", "ADMIN"]

export const UserSchema = Yup.object().shape({
    login: Yup.string()
        .min(3)
        .matches(/^[A-Z][A-Z][a-z]{1,18}[0-9]{0,5}$/, 'login must have two capital letters at the beginning')
        .required(),
    name: Yup.string()
        .min(2, )
        .matches(/^[A-Z][a-z]{1,19}$/, "name must start with a capital letter")
        .required(),
    surname: Yup.string()
        .min(2, )
        .matches(/^[A-Z][a-z]{1,19}(-[A-Z][a-z]{1,19})?$/, "surname must start with a capital letter")
        .required(),
    type: Yup.string()
        .required(),
});

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