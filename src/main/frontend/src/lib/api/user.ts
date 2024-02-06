export type User = {
    id: string
    firstname: string,
    lastname: string
}

export const getUserById = async (id: string, token: string) => {
    try {
        const res = await fetch('http://127.0.0.1/api/users/' + id, {
            method: "GET",
            headers: {
                "Content-Type": "application/json",
                "Authorization": token
            }
        });

        if (res.status === 200) {
            return (await res.json()) as User;
        } else {
        }
    } catch (error) {
        console.log("Login failed: ", error)
    }
}