import Cookies from "js-cookie";

export type AuthenticationRequest = {
    email: string,
    password: string,
}

export type AuthenticationResponse = {
    access_token: string,
    refresh_token: string
}

export const login = async (request: AuthenticationRequest) => {
    try {
        const res = await fetch('http://127.0.0.1/api/auth/authenticate', {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(request)
        });

        if (res.status === 200) {
            const response = (await res.json()) as AuthenticationResponse;

            Cookies.set('Authorization', `Bearer ${response.access_token}`, { expires: 1 });
            Cookies.set('refreshToken',  response.refresh_token, { expires: 7 });

            window.location.replace('/items');
        } else {
        }
    } catch (error) {
        console.log("Login failed: ", error)
    }
}