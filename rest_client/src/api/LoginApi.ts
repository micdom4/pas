import {api, loginApiInstance} from './api.config'
import type {AxiosResponse} from "axios";
import type {ChangePasswordType, LoginDataType, LoginResponse, RegisterDataType} from "../model/LoginTypes.ts";


export const loginApi = {

    login: async (loginData: LoginDataType):Promise<AxiosResponse<LoginResponse>> => {
        return await loginApiInstance.post(`/login`, loginData)
    },

    refresh: async ( accessToken: string): Promise<AxiosResponse<{token: string, refreshToken: string}>> => {
        return await loginApiInstance.post(`/refresh`, { accessToken })
    },

    register: async (registerData: RegisterDataType): Promise<AxiosResponse<string>> => {
        return await loginApiInstance.post(`/register`, registerData)
    },

    logout: async (): Promise<AxiosResponse<undefined>> => {
        return await api.post('/auth/logout')
    },

    changePassword: async (data: ChangePasswordType): Promise<AxiosResponse<string>> => {
        return await api.post('auth/reset', data)
    }
}
