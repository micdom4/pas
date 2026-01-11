import type {BaseType} from "./BaseType.ts";

export interface ResourceType extends BaseType {
    cpu: number,
    ram: number,
    storage: number
}

export interface CreateResourceType {
    cpu: number,
    ram: number,
    storage: number
}

export interface EditResourceType {
    cpu: number,
    ram: number,
    storage: number
}