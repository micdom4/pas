import {type RolesType, RoleTypes} from "../model/LoginTypes.ts";
import {RoleEnum} from "../contexts/LoggedUserContext/types.ts";

export const formatDate = (date: Date | null) => {
    if (!date) return '-';
    return new Date(date).toLocaleDateString('pl-PL') + ' ' + new Date(date).toLocaleTimeString('pl-PL',
        {hour: '2-digit', minute: '2-digit'});
};

export const convertRole = (rolesType: RolesType[]): RoleEnum => {
    const roles: RoleEnum[] = []

    for (const role of rolesType) {
        if (role.authority == RoleTypes.ROLE_ADMIN) {
            roles.push(RoleEnum.ADMIN)
        } else if (role.authority == RoleTypes.ROLE_MANAGER) {
            roles.push(RoleEnum.MANAGER)
        } else {
            roles.push(RoleEnum.CLIENT)
        }
    }

    if (roles.find((role) => role == RoleEnum.ADMIN)) {
        return RoleEnum.ADMIN
    } else if (roles.find((role) => role == RoleEnum.MANAGER)) {
        return RoleEnum.MANAGER
    } else {
        return RoleEnum.CLIENT
    }
}