import {CrudApi} from "./BasicCrudRestApi.ts";
import axios, {type AxiosResponse} from "axios";
import type {AllocationType, CreateAllocationType} from "../model/AllocationTypes.ts";
import {api} from "./api.config.ts";

const allocations_path = "/allocations"

const allocationCrud = new CrudApi<AllocationType, CreateAllocationType, AllocationType>(allocations_path)

const errorMessages = {
    allocatedResource: "Resource is already allocated",
    itemsNotFound: "Resource or Client not found",
    notFound: "Allocation not found",
    invalidId: "Invalid ID of Allocation",
    userType: "User has to be a Client",
    inactiveClient: "Client has to be active",
    notActive: "Cannot delete not active allocation"
}

export const allocationApi = {
    getAll: async (): Promise<AxiosResponse<AllocationType[]>> => {
        return await allocationCrud.getAll()
    },

    getById: async (id: string): Promise<AxiosResponse<AllocationType>> => {
        return await allocationCrud.getById(id)
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

    getActiveForClient: async (id: string): Promise<AxiosResponse<AllocationType[]>> => {
        return await api.get(`${allocations_path}/active/client/${id}`)
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

    getPastForClient: async (id: string): Promise<AxiosResponse<AllocationType[]>> => {
        return await api.get(`${allocations_path}/past/client/${id}`)
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

    getActiveForResource: async (id: string): Promise<AxiosResponse<AllocationType[]>> => {
        return await api.get(`${allocations_path}/active/vm/${id}`)
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

    getPastForResource: async (id: string): Promise<AxiosResponse<AllocationType[]>> => {
        return await api.get(`${allocations_path}/past/vm/${id}`)
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

    create: async (allocation: CreateAllocationType, etag?: string): Promise<AxiosResponse<AllocationType>> => {
        return await allocationCrud.create(allocation, etag)
            .catch((error) => {
                if (axios.isAxiosError(error) && error.response) {
                    if (error.response.status === 400) {
                        throw new Error(errorMessages.inactiveClient)
                    }
                    if (error.response.status === 409) {
                        throw new Error(errorMessages.allocatedResource)
                    }
                    if (error.response.status === 404) {
                        throw new Error(errorMessages.itemsNotFound)
                    }
                }

                throw error;
            })
    },

    delete: async (id: string) => {
        return await allocationCrud.delete(id)
            .catch((error) => {
                if (axios.isAxiosError(error) && error.response) {
                    if (error.response.status === 404) {
                        throw new Error(errorMessages.notFound)
                    }
                    if (error.response.status === 422) {
                        throw new Error(errorMessages.invalidId)
                    }
                    if (error.response.status === 409) {
                        throw new Error(errorMessages.notActive)
                    }
                }

                throw error;
            })
    },

    finish: async (id: string) => {
        return await api.put(`${allocations_path}/${id}/finish`)
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

    prepareForCreate: async (clientId: string, vmId: string): Promise<string> => {
        const response = await api.get(`auth/change/${clientId}/${vmId}`)

        return response.headers['etag']
    }
}