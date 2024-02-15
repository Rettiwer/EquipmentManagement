import axios, {type AxiosInstance} from "axios";
import axiosRetry from "axios-retry";

const API_BASE:string = 'http://192.168.1.152/api/';

interface ApiError {
    status: number;
    timestamp: string;
    message: string;
    debugMessage: string;
}

class Api {
    accessToken: string | null;
    refreshToken: string | null;

    axiosInstance: AxiosInstance;

    constructor(accessToken: string|null, refreshToken: string|null) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;

        this.axiosInstance = axios.create({
            baseURL: API_BASE
        });

        // axiosRetry(this.axiosInstance, {
        //     retries: 1,
        //     retryCondition: (error: any) => {
        //         // Retry only if the error is 401 (Unauthorized)
        //         return axiosRetry.isNetworkError(error) || (error.response && error.response.status === 401);
        //     }
        // });
        //
        // this.axiosInstance.interceptors.response.use(
        //     response => {
        //         return response;
        //     },
        //      error => {
        //         return Promise.reject(error);
        //     }
        // );
    }

    apiRequest(method: string, url: string, request: any, enableRetries: boolean = true): Promise<any> {
        let headers = {
            Authorization: this.accessToken !== null ? 'Bearer ' + this.accessToken : '',
            'Content-Type': 'application/json',
            Accept: 'application/json'
        };

        // let axiosInstance: AxiosInstance = this.axiosInstance;
        // if (!enableRetries) {
        //     // If retries are disabled, use a fresh instance of axios
        //     axiosInstance = axios.create({
        //         baseURL: API_BASE
        //     });
        // }

        return this.axiosInstance({
            method,
            url,
            data: request,
            headers
        }).then(res => {
            return Promise.resolve(res.data);
        })
            .catch(error => {
                if (!error.response) {
                    let networkError = <ApiError>{ status: -1, message: "Network error has occurred.", debugMessage: error.code };
                    return Promise.reject(networkError);
                }
                if (error.response.status >= 400 && error.response.status < 500) {
                    throw error.response.data as ApiError;
                }
                return Promise.reject(error);
            });
    }

    get(url: string, request: any, enableRetries: boolean = true): Promise<any> {
        return this.apiRequest("get", url, request, enableRetries);
    }

    post(url: string, request: any, enableRetries: boolean = true): Promise<any> {
        return this.apiRequest("post", url, request, enableRetries);
    }

    put(url: string, request: any, enableRetries: boolean = true): Promise<any> {
        return this.apiRequest("put", url, request, enableRetries);
    }

    delete(url: string): Promise<any> {
        return this.apiRequest("delete", url, null, false);
    }

}

export default Api;