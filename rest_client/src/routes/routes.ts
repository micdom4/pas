import {Paths} from "./paths.ts";
import * as React from "react";
import Home from "../pages/Home.tsx";
import ListUsers from "../pages/users/ListUsers.tsx";
import ListResources from "../pages/resources/ListResources.tsx";
import CreateUser from "../pages/users/CreateUser.tsx";
import CreateResource from "../pages/resources/CreateResource.tsx";
import ListAllocations from "../pages/allocations/ListAllocations.tsx";
import CreateAllocation from "../pages/allocations/CreateAllocation.tsx";
import DetailedUser from "../pages/users/DetailedUser.tsx";

export type RouteType = {
    Component: () => React.ReactElement,
    path: string
}

export const defaultRoutes: RouteType[] = [
    {
        path: Paths.default.home,
        Component: Home,
    },
    {
        path: Paths.default.listUsers,
        Component: ListUsers,
    },
    {
        path: Paths.default.createUser,
        Component: CreateUser,
    },
    {
        path: Paths.default.detailedUser,
        Component: DetailedUser,
    },
    {
        path: Paths.default.listResources,
        Component: ListResources,
    },
    {
        path: Paths.default.createResource,
        Component: CreateResource,
    },
    {
        path: Paths.default.listAllocations,
        Component: ListAllocations,
    },
    {
        path: Paths.default.createAllocations,
        Component: CreateAllocation,
    },
]