import type {BaseType} from "./BaseType.ts";
import * as Yup from 'yup';

export const ResourceSchema = Yup.object().shape({
    cpuNumber: Yup.number()
        .typeError('CPU quantity must be a number')
        .integer()
        .positive()
        .required(),
    ramGiB: Yup.number()
        .typeError('Value must be a number')
        .positive()
        .required(),
    storageGiB: Yup.number()
        .typeError('Value must be a number')
        .positive()
        .required(),
});

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