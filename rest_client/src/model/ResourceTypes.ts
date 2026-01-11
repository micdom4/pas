import type {BaseType} from "./BaseType.ts";

export interface ResourceType extends BaseType {
    cpuNumber: number,
    ramGiB: number,
    storageGiB: number
}

export interface CreateResourceType {
    cpuNumber: number,
    ramGiB: number,
    storageGiB: number
}

export interface EditResourceType {
    cpuNumber: number,
    ramGiB: number,
    storageGiB: number
}