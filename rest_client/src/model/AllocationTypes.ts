import type {BaseType} from "./BaseType.ts";
import type {ClientType} from "./UserTypes.ts";
import type {ResourceType} from "./ResourceTypes.ts";

export interface AllocationType extends BaseType {
    client: ClientType,
    vm: ResourceType,
    startTime: Date,
    endTime: Date | null
}

export interface CreateAllocationType {
    clientId: string,
    resourceId: string
}