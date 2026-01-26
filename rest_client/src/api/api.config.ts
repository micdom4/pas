import axios, {AxiosError, type InternalAxiosRequestConfig} from 'axios'
import {
    accessTokenStorageName,
    refreshTokenStorageName,
    userLoginStorageName
} from "../contexts/LoggedUserContext/tokenStorageConfig.ts";
import {loginApi} from "./LoginApi.ts";

export const API_URL = "http://localhost:8080"
export const TIMEOUT_IN_MS = 10000
export const DEFAULT_HEADERS = {
    'Accept': 'application/json',
    'Content-type': 'application/json',
}

export const api = axios.create({
    baseURL: API_URL,
    timeout: TIMEOUT_IN_MS,
    headers: DEFAULT_HEADERS,
})

const authorizationRequestInterceptor = (config: InternalAxiosRequestConfig) => {
    const accessToken = sessionStorage.getItem(accessTokenStorageName)

    if (accessToken && config.headers) {
        config.headers.Authorization = "Bearer " + accessToken
    }

    return config
}

const errorMessages = {
    authError: "Access Denied!",
    serverError: "Internal Server Error!!!",
    unknownError: "Unknown Error"
}

api.interceptors.request.use(
    (config) => {
        return authorizationRequestInterceptor(config)
    }
)

api.interceptors.response.use(
    (response) => {
        return response;
    },
    async (error: AxiosError) => {
        const originalRequest = error.config as InternalAxiosRequestConfig & { _retry?: boolean };

        console.log("Received error:", error, " with code: ", error.response?.status)
        if (error.response?.status === 403 && !originalRequest._retry) {
            originalRequest._retry = true;

            const refreshToken = sessionStorage.getItem(refreshTokenStorageName);
            const userId = sessionStorage.getItem(userLoginStorageName) || "unknown";

            if (refreshToken) {
                try {
                    const res = await loginApi.refresh(userId, { refreshToken: refreshToken });

                    if (res.data.accessToken) {
                        console.log("Token refreshed successfully");

                        sessionStorage.setItem(accessTokenStorageName, res.data.accessToken);

                        originalRequest.headers.Authorization = "Bearer " + res.data.accessToken;

                        return api(originalRequest);
                    }
                } catch (refreshError) {
                    console.error("Refresh token failed", refreshError);
                    sessionStorage.clear();
                    window.location.href = '/login';
                    return Promise.reject(refreshError);
                }
            }
            else {
                sessionStorage.clear();
                setTimeout(() => window.location.href = '/login', 1000);
                throw new Error(errorMessages.authError);
            }
        }

        if (!error.response) {
            console.error(error);
        } else {
            const status = error.response.status;
            if (status === 500) {
                console.error("Server Error 500");
            }
        }

        return Promise.reject(error);
    }
);

export const secureApiInstance = axios.create({
    baseURL: API_URL,
    timeout: TIMEOUT_IN_MS,
    headers: DEFAULT_HEADERS,
})

secureApiInstance.interceptors.request.use(
    (config) => {
        return authorizationRequestInterceptor(config)
    }
)


export const loginApiInstance = axios.create({
    baseURL: API_URL + "/auth",
    timeout: TIMEOUT_IN_MS,
    headers: DEFAULT_HEADERS
})

loginApiInstance.interceptors.response.use(
    (response) => {
        return response;
    },
    (error: AxiosError) => {
        if (!error.response) {
            console.error(error);
            throw new Error(errorMessages.unknownError)
        } else {
            const status = error.response.status;

            if (status === 500) {
                console.error(error);
                throw new Error(errorMessages.serverError)
            }
        }

        return Promise.reject(error);
    }
);