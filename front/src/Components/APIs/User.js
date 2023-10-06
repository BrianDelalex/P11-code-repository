import BodyReader from '../BodyReader';


const base_url = import.meta.env.VITE_USER_SERVICE_URL;

export const login = async (credentials) => {
    const encodedPwd = btoa(credentials.password);
    const url = base_url + "/user/login" + "?username=" + credentials.login + "&password=" + encodedPwd;

    return await fetch(url, {method: 'POST'}).then(
        async (data) => {
            const body = await BodyReader(data.body);
            return {statusCode: data.status, body};
        },
        (err) => {
            console.log(err);
            return {statusCode: undefined, body: undefined};
        }
    );
}

export const register = async (credentials) => {
    const encodedPwd = btoa(credentials.password);
    const url = base_url + "/user/register" + "?username=" + credentials.login + "&password=" + encodedPwd;

    return await fetch(url, {method: 'POST'}).then(
        async (data) => {
            const body = await BodyReader(data.body);
            return {statusCode: data.status, body};
        },
        (err) => {
            console.log(err);
            return {statusCode: undefined, body: undefined};
        }
    );
}