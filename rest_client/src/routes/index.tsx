import {Navigate, Route, Routes} from 'react-router-dom'
import {defaultRoutes} from "./routes.ts";
import DefaultLayout from "../components/layout/DefaultLayout.tsx";
import {Paths} from "./paths.ts";

export default function RoutesComponent() {
    return <>
        <Routes>
            {defaultRoutes.map(({path, Component}) => (
                <Route key={path} path={path} element={
                    <DefaultLayout>
                        <Component />
                    </DefaultLayout>
                }
                />
            ))}
            <Route path="*" element={<Navigate to={Paths.default.home} />}/>
        </Routes>
    </>
}