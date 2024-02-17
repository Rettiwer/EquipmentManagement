import axios, {type AxiosInstance} from "axios";
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

    }

    apiRequest(method: string, url: string, request: any= {}, queryParams: any = {}): Promise<any> {
        let headers = {
            Authorization: this.accessToken !== null ? 'Bearer ' + this.accessToken : '',
            'Content-Type': 'application/json',
            Accept: 'application/json'
        };

        let queryString = Object.keys(queryParams)
            .map(key => encodeURIComponent(key) + '=' + encodeURIComponent(queryParams[key]))
            .join('&');

        if (queryString) {
            url += (url.indexOf('?') === -1 ? '?' : '&') + queryString;
        }

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

    get(url: string, request: any= {}, queryParams: any = {}): Promise<any> {
        return this.apiRequest("get", url, request, queryParams);
    }

    post(url: string, request: any= {}): Promise<any> {
        return this.apiRequest("post", url, request);
    }

    put(url: string, request: any= {}): Promise<any> {
        return this.apiRequest("put", url, request);
    }

    delete(url: string): Promise<any> {
        return this.apiRequest("delete", url, null, false);
    }

}

export default Api;