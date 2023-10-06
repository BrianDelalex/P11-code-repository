import { useState } from "react";
import { useNavigate } from "react-router";

import { register } from "../Components/APIs/User";

import "./Register.css";

const Register = () => {
    const [credentials, setCredentials] = useState({login: "", password: "", passwordConfirm: ""});
    const [error, setError] = useState({displayed: false, msg: ""});
    const navigate = useNavigate();

    const onSubmit = async (e) => {
        setError({displayed: false, msg: ""});
        e.preventDefault();
        if (credentials.login === "" ||
            credentials.password === "" ||
            credentials.passwordConfirm == "") {
            setError({displayed: true, msg: "Please enter a login, a password and confirm your password."});
            return;
        }
        if (credentials.password !== credentials.passwordConfirm) {
            setError({displayed: true, msg: "Password and password confirmation should be the same."});
            return;
        }
        const data = await register(credentials);
        if (data.statusCode == 200) {
            console.log(data);
            navigate('/login');
        } else {
            setError({displayed: true, msg: "An error occured please retry."});
        }
    }

    return (
        <div className="window">
            <div className="errorContainer">{error.displayed ? error.msg : null}</div>
            <form className="inputContainer" onSubmit={(e) => onSubmit(e)}>
                <input
                    type="text"
                    onChange={e => setCredentials(state => ({...state, login: e.target.value}))} 
                    placeholder="Login"
                />
                <input 
                    type="password" 
                    onChange={e => setCredentials(state => ({...state, password: e.target.value}))}
                    placeholder="Password"
                />
                <input 
                    type="password" 
                    onChange={e => setCredentials(state => ({...state, passwordConfirm: e.target.value}))}
                    placeholder="Confirm password"
                />
                <button type="submit" >Register</button>
            </form>
        </div>
    );
}

export default Register;