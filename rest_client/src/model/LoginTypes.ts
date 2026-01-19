import * as yup from 'yup'
import * as Yup from "yup";
import type {TypeOfUser} from "./UserTypes.ts";

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
        .min(2, )
        .matches(/^[A-Z][a-z]{1,19}$/, "name must start with a capital letter")
        .required(),
    password: yup.string().required(),
    confirmPassword: yup.string().required().oneOf([yup.ref('password'), ''], 'Passwords must match'),
    surname: Yup.string()
        .min(2, )
        .matches(/^[A-Z][a-z]{1,19}(-[A-Z][a-z]{1,19})?$/, "surname must start with a capital letter")
        .required(),
})

export type RegisterFormType = yup.InferType<typeof registerDataSchema>;

export type RegisterDataType = {
    login: string,
    name: string,
    password: string,
    surname: string,
    type: TypeOfUser
}