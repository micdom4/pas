import {loginApiInstance} from './api.config'
import type {AxiosResponse} from "axios";
import type {LoginDataType, RegisterDataType} from "../model/LoginTypes.ts";
import type {LoginResponse} from "../contexts/LoggedUserContext/types.ts";

export const loginApi = {

    login: async (loginData: LoginDataType):Promise<AxiosResponse<LoginResponse>> => {
        return await loginApiInstance.post(`/login`, loginData)
    },

    // refresh: async (id: string, token: {refreshToken: string}): Promise<AxiosResponse<{accessToken: string}>> => {
    //     return await loginApiInstance.post(`/${id}/refresh`, token)
    // },

    register: async (registerData: RegisterDataType): Promise<AxiosResponse<string>> => {
        return await loginApiInstance.post(`/register`, registerData)
    },

    logout: async (): Promise<AxiosResponse<undefined>> => {
        return await loginApiInstance.post('/logout')
    }

}
