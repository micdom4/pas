import type {AxiosResponse} from "axios";
import {api} from "./api.config.ts";
import type {BaseType} from "../model/BaseType.ts";

export class CrudApi<T extends BaseType, C, E> {
    private readonly endpoint: string;

    constructor(endpoint: string) {
        this.endpoint = endpoint;
    }

    async getAll(): Promise<AxiosResponse<T[]>> {
        return await api.get(this.endpoint);
    }

    async getById(id: string): Promise<AxiosResponse<T>> {
        return await api.get(`${this.endpoint}/${id}`);
    }

    async create(item: C): Promise<AxiosResponse<T>> {
        return await api.post(this.endpoint, item);
    }

    async update(id: string, item: E): Promise<AxiosResponse<T>> {
        return await api.put(`${this.endpoint}/${id}`, item);
    }

    async delete(id: string): Promise<void> {
        return await api.delete(`${this.endpoint}/${id}`);
    }
}