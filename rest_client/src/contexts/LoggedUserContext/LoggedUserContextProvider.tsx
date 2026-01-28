import {type ReactNode, useEffect, useLayoutEffect, useState} from 'react'
import {emptyUser, LoggedUser, RoleEnum} from "./types.ts";
import LoggedUserContext from "./index.tsx";
import {accessTokenStorageName, userLoginStorageName, userRoleStorageName} from "./tokenStorageConfig.ts";

export default function LoggedUserContextProvider({children}: { children: ReactNode }) {

    const [loggedUser, setLoggedUser] = useState(emptyUser)
    const [isInitialized, setIsInitialized] = useState(false);

    useLayoutEffect(() => {
        const accessToken = sessionStorage.getItem(accessTokenStorageName);
        const login = sessionStorage.getItem(userLoginStorageName);
        const role = sessionStorage.getItem(userRoleStorageName) as RoleEnum;

        if (accessToken && login && role) {
            const restoredUser = new LoggedUser(login, accessToken, role);
            setLoggedUser(restoredUser);
        }
        setIsInitialized(true);
    }, []);

    useEffect(() => {
        if (loggedUser.isAuthenticated()) {
            sessionStorage.setItem(accessTokenStorageName, loggedUser.token || "");
            if (loggedUser.login) {
                sessionStorage.setItem(userLoginStorageName, loggedUser.login);
            }
            if (loggedUser.role) {
                sessionStorage.setItem(userRoleStorageName, loggedUser.role);
            }
        } else if (isInitialized) {
            sessionStorage.removeItem(accessTokenStorageName);
            sessionStorage.removeItem(userLoginStorageName);
            sessionStorage.removeItem(userRoleStorageName);
        }
    }, [loggedUser, isInitialized]);

    if (!isInitialized) {
        return null;
    }

    return (
        <LoggedUserContext value={{user: loggedUser, setUser: setLoggedUser}}>
            {children}
        </LoggedUserContext>
    )
}

