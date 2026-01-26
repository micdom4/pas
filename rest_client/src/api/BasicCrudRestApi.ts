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

    async create(item: C, etag?: string): Promise<AxiosResponse<T>> {
        const config = etag ? {
            headers: {
                'If-Match': etag
            }
        } : {};

        return await api.post(this.endpoint, item, config);
    }

    async update(id: string, item: E, etag?: string): Promise<AxiosResponse<T>> {
        const config = etag ? {
            headers: {
                'ETag': etag
            }
        } : {};

        return await api.put(`${this.endpoint}/${id}`, item, config);
    }

    async delete(id: string): Promise<void> {
        return await api.delete(`${this.endpoint}/${id}`);
    }
}