import axios, {type AxiosResponse} from "axios";
import type {CreateUserType, EditUserType, UserType} from "../model/UserTypes.ts";
import {CrudApi} from "./BasicCrudRestApi.ts";
import {api} from "./api.config.ts";

const user_api = "/users"

const userCrud = new CrudApi<UserType, CreateUserType, EditUserType>(user_api)

const errorMessages = {
    alreadyExists: "User with this login already exists",
    notFound: "User not found",
    invalidType: "Invalid User Type",
    invalidId: "Invalid ID of User",
}

export const userApi = {
    getAll: async (): Promise<AxiosResponse<UserType[]>> => {
        return await userCrud.getAll()
    },

    getById: async (id: string): Promise<AxiosResponse<UserType>> => {
        return await userCrud.getById(id)
            .catch((error) => {
                if (axios.isAxiosError(error) && error.response) {
                    if (error.response.status === 404) {
                        throw new Error(errorMessages.notFound)
                    }
                    if (error.response.status === 422) {
                        throw new Error(errorMessages.invalidId)
                    }
                }

                throw error;
            })
    },

    getByLogin: async (login: string): Promise<AxiosResponse<UserType>> => {
        return await api.get(`${user_api}/login/${login}`)
            .catch((error) => {
                if (axios.isAxiosError(error) && error.response) {
                    if (error.response.status === 404) {
                        throw new Error(errorMessages.notFound)
                    }
                }

                throw error;
            })
    },

    searchByLogin: async (login: string): Promise<AxiosResponse<UserType[]>> => {
        return await api.get(`${user_api}/search/${login}`)
    },

    create: async (user: CreateUserType): Promise<AxiosResponse<UserType>> => {
        return await userCrud.create(user)
            .catch((error) => {
                if (axios.isAxiosError(error) && error.response) {
                    if (error.response.status === 409) {
                        throw new Error(errorMessages.alreadyExists)
                    }
                    if (error.response.status === 422) {
                        throw new Error(errorMessages.invalidType)
                    }
                }

                throw error;
            })
    },

    edit: async (id: string, newData: EditUserType, etag?: string): Promise<AxiosResponse<UserType>> => {
        return await userCrud.update(id, newData, etag)
            .catch((error) => {
                if (axios.isAxiosError(error) && error.response) {
                    if (error.response.status === 404) {
                        throw new Error(errorMessages.notFound)
                    }
                    if (error.response.status === 422) {
                        throw new Error(errorMessages.invalidId)
                    }
                }

                throw error;
            })
    },

    deactivate: async (id: string, etag?: string) => {
        const config = etag ? {
            headers: {
                'If-Match': etag
            }
        } : {};

        return await api.put(`${user_api}/${id}/deactivate`, null, config)
            .catch((error) => {
                if (axios.isAxiosError(error) && error.response) {
                    if (error.response.status === 404) {
                        throw new Error(errorMessages.notFound)
                    }
                    if (error.response.status === 422) {
                        throw new Error(errorMessages.invalidId)
                    }
                }

                throw error;
            })
    },

    activate: async (id: string, etag?: string) => {
        const config = etag ? {
            headers: {
                'If-Match': etag
            }
        } : {};

        return await api.put(`${user_api}/${id}/activate`, null, config)
            .catch((error) => {
                if (axios.isAxiosError(error) && error.response) {
                    if (error.response.status === 404) {
                        throw new Error(errorMessages.notFound)
                    }
                    if (error.response.status === 422) {
                        throw new Error(errorMessages.invalidId)
                    }
                }

                throw error;
            })
    },

    prepareForChange: async (id: string): Promise<string> => {
        const response = await api.get(`auth/change/${id}`)

        return response.headers['etag']
    }
}