import * as yup from 'yup'
import * as Yup from 'yup'
import type {CreateUserType} from "./UserTypes.ts";

export const loginDataSchema = yup.object({
    login: Yup.string()
        .min(3)
        .matches(/^[A-Z][A-Z][a-z]{1,18}[0-9]{0,5}$/, 'login must have two capital letters at the beginning')
        .required(),
    password: yup.string().required(),
});

export type LoginDataType = yup.InferType<typeof loginDataSchema>;

export const registerDataSchema = yup.object({
    login: Yup.string()
        .min(3)
        .matches(/^[A-Z][A-Z][a-z]{1,18}[0-9]{0,5}$/, 'login must have two capital letters at the beginning')
        .required(),
    name: Yup.string()
        .min(2,)
        .matches(/^[A-Z][a-z]{1,19}$/, "name must start with a capital letter")
        .required(),
    password: Yup.string().required(),
    confirmPassword: Yup.string().required().oneOf([yup.ref('password'), ''], 'Passwords must match'),
    surname: Yup.string()
        .min(2,)
        .matches(/^[A-Z][a-z]{1,19}(-[A-Z][a-z]{1,19})?$/, "surname must start with a capital letter")
        .required(),
    type: Yup.string().required()
})

export type RegisterFormType = yup.InferType<typeof registerDataSchema>;

export type RegisterDataType = CreateUserType

// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-expect-error
export enum RoleTypes {
    "ROLE_CLIENT" = "ROLE_CLIENT",
    "ROLE_MANAGER" = "ROLE_MANAGER",
    "ROLE_ADMIN" = "ROLE_ADMIN"
}

export type RolesType = {
    authority: RoleTypes
}

export type LoginResponse = {
    token: string
    refreshToken: string
    roles: RolesType[]
}

export const changePasswordSchema = yup.object({
    oldPassword: Yup.string().required(),
    newPassword: Yup.string().required(),
    confirmNewPassword: Yup.string().required().oneOf([yup.ref('newPassword'), ''], 'Passwords must match'),
})

export type ChangePasswordType = {
    oldPassword: string,
    newPassword: string,
}