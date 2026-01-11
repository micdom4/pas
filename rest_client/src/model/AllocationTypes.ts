import type {BaseType} from "./BaseType.ts";

export interface AllocationType extends BaseType {
    clientId: string,
    resourceId: string
}

export interface CreateAllocationType {
    clientId: string,
    resourceId: string
}