import axios, {type AxiosInstance} from 'axios';
import axiosRetry from 'axios-retry';
import Cookies from "js-cookie";

export interface ApiError {
    status: number;
    timestamp: string;
    message: string;
    debugMessage: string;
}

const API_BASE:string = 'http://192.168.1.152/api/';

const axiosAPI: AxiosInstance = axios.create({
    baseURL : API_BASE
});

axiosRetry(axiosAPI, {
    retries: 2,
    retryCondition: (error) => {
        // Retry only if the error is 401 (Unauthorized)
        return axiosRetry.isNetworkError(error) || (error.response && error.response.status === 401);
    }
});

axiosAPI.interceptors.response.use(
    response => {
        return response;
    },
    error => {
        if (error.response && error.response.status === 401) {
            //TODO: Regenerate JWT Token
            return Promise.reject(error);
        }
        return Promise.reject(error);
    }
);

const apiRequest = (method: string, url: string, request: any, enableRetries: boolean = true) => {
    const headers: any = {
        authorization: Cookies.get('Authorization') || '',
    };

    //using the axios instance to perform the request that received from each http method
    let axiosInstance: AxiosInstance = axiosAPI;
    if (!enableRetries) {
        // If retries are disabled, use a fresh instance of axios
        axiosInstance = axios.create({
            baseURL: API_BASE
        });
    }

    return axiosInstance({
        method,
        url,
        data: request,
        headers
    }).then(res => {
        return Promise.resolve(res.data);
    })
    .catch(err => {
        return Promise.reject(err);
    });
};

export const get = (url: string, request: any, enableRetries: boolean = true): Promise<any> => apiRequest("get", url, request, enableRetries);

export const post = (url: string, request: any, enableRetries: boolean = true) => apiRequest("post", url, request, enableRetries);