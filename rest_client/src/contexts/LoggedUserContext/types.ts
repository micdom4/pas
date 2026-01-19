import type {Dispatch, SetStateAction} from 'react';

// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-expect-error
export enum RoleEnum {
    ADMIN = 'ADMIN',
    MANAGER = 'MANAGER',
    CLIENT = 'CLIENT',
}

export class LoggedUser {
    login: string | null;
    token: string | null;
    role: RoleEnum | null;

    constructor(login: string | null, token: string | null, role: RoleEnum | null) {
        this.login = login;
        this.token = token;
        this.role = role;
    }

    isAuthenticated() {
        return (!!this.login && !!this.token);
    }

    isAdmin() {
        return !!this.role && this.role.includes(RoleEnum.ADMIN)
    }

    isManager() {
        return !!this.role && this.role.includes(RoleEnum.MANAGER)
    }

    isClient() {
        return !!this.role && this.role.includes(RoleEnum.CLIENT)
    }

}

export const emptyUser = new LoggedUser(null, null, null)

export type LoggedUserContextType = {
    user: LoggedUser;
    setUser: Dispatch<SetStateAction<LoggedUser>>;
}

export type LoginResponse = {
    token: string
}
