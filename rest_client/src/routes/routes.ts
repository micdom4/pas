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
import LoginPage from "../pages/login/LoginPage.tsx";
import RegisterPage from "../pages/login/RegisterPage.tsx";

export type RouteType = {
    Component: () => React.ReactElement,
    path: string
}

export const anonymousRoutes: RouteType[] = [
    {
        path: Paths.anonymous.home,
        Component: Home,
    },
    {
        path: Paths.anonymous.login,
        Component: LoginPage
    },
    {
        path: Paths.anonymous.register,
        Component: RegisterPage
    }
]

export const defaultRoutes: RouteType[] = [
    {
        path: Paths.default.home,
        Component: Home,
    },
]

export const clientRoutes: RouteType[] = [
    {
        path: Paths.client.detailedUser,
        Component: DetailedUser,
    },
    {
        path: Paths.client.listResources,
        Component: ListResources,
    },
    {
        path: Paths.client.createAllocations,
        Component: CreateAllocation,
    },
]

export const managerRoutes: RouteType[] = [
    {
        path: Paths.manager.detailedUser,
        Component: DetailedUser,
    },
    {
        path: Paths.manager.listResources,
        Component: ListResources,
    },
    {
        path: Paths.manager.createResource,
        Component: CreateResource,
    },
    {
        path: Paths.manager.listAllocations,
        Component: ListAllocations,
    },
]

export const administratorRoutes: RouteType[] = [
    {
        path: Paths.administrator.listUsers,
        Component: ListUsers,
    },
    {
        path: Paths.administrator.createUser,
        Component: CreateUser,
    },
    {
        path: Paths.administrator.detailedUser,
        Component: DetailedUser,
    },
    {
        path: Paths.administrator.listResources,
        Component: ListResources,
    },
    {
        path: Paths.administrator.createResource,
        Component: CreateResource,
    },
    {
        path: Paths.administrator.listAllocations,
        Component: ListAllocations,
    },
    {
        path: Paths.administrator.createAllocations,
        Component: CreateAllocation,
    },
]