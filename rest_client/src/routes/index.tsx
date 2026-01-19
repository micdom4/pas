import {Navigate, Route, Routes} from 'react-router-dom'
import {anonymousRoutes, defaultRoutes} from "./routes.ts";
import DefaultLayout from "../components/layout/DefaultLayout.tsx";
import {Paths} from "./paths.ts";
import LoggedUserContext from "../contexts/LoggedUserContext";
import {use} from "react";

export default function RoutesComponent() {
    const {user} = use(LoggedUserContext)

    return <>
        <Routes>
            {user.isAuthenticated() ? defaultRoutes.map(({path, Component}) => (
                <Route key={path} path={path} element={
                    <DefaultLayout>
                        <Component/>
                    </DefaultLayout>
                }
                />
            )) : anonymousRoutes.map(({path, Component}) => (
                <Route key={path} path={path} element={
                    <DefaultLayout>
                        <Component/>
                    </DefaultLayout>
                }
                />
            ))
            }
            <Route path="*" element={<Navigate to={Paths.default.home}/>}/>
        </Routes>
    </>
}