import {CrudApi} from "./BasicCrudRestApi.ts";
import type {CreateResourceType, EditResourceType, ResourceType} from "../model/ResourceTypes.ts";
import axios, {type AxiosResponse} from "axios";

const resources_api = "/resources"

const resourceCrud = new CrudApi<ResourceType, CreateResourceType, EditResourceType>(resources_api)

const errorMessages = {
    stillAllocated: "Resource is still allocated",
    notFound: "Resource not found",
    invalidId: "Invalid ID of Resource",
    invalidData: "Resource data validation error"
}

export const resourceApi = {
    getAll: async (): Promise<AxiosResponse<ResourceType[]>> => {
        return await resourceCrud.getAll()
    },

    getById: async (id: string): Promise<AxiosResponse<ResourceType>> => {
        return await resourceCrud.getById(id)
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

    create: async (resource: CreateResourceType): Promise<AxiosResponse<ResourceType>> => {
        return await resourceCrud.create(resource)
            .catch((error) => {
                if (axios.isAxiosError(error) && error.response) {
                    if (error.response.status === 400) {
                        throw new Error(errorMessages.invalidData)
                    }
                }

                throw error;
            })
    },

    edit: async (id: string, data: EditResourceType): Promise<AxiosResponse<ResourceType>> => {
        return await resourceCrud.update(id, data)
            .catch((error) => {
                if (axios.isAxiosError(error) && error.response) {
                    if (error.response.status === 400) {
                        throw new Error(errorMessages.invalidData)
                    }
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

    delete: async (id: string) => {
        return await resourceCrud.delete(id)
            .catch((error) => {
                if (axios.isAxiosError(error) && error.response) {
                    if (error.response.status === 404) {
                        throw new Error(errorMessages.notFound)
                    }
                    if (error.response.status === 422) {
                        throw new Error(errorMessages.invalidId)
                    }
                    if (error.response.status === 409) {
                        throw new Error(errorMessages.stillAllocated)
                    }
                }

                throw error;
            })
    }
}