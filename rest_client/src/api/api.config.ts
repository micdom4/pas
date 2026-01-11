import axios, {AxiosError} from 'axios'

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

const errorMessages = {
    serverError: "Internal Server Error!!!",
    unknownError: "Unknown Error"
}

api.interceptors.response.use(
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