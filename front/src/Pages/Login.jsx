import { useState } from "react";
import { login } from "../Components/APIs/User";

import "./Login.css";
import { useNavigate } from "react-router";

const Login = () => {
    const [credentials, setCredentials] = useState({login: "", password: ""});
    const [error, setError] = useState({displayed: false, msg: ""});
    const navigate = useNavigate();

    const onSubmit = async (e) => {
        setError({displayed: false, msg: ""});
        e.preventDefault();
        if (credentials.login === "" || credentials.password === "") {
            setError({displayed: true, msg: "Please enter a login and password."});
            return;
        }
        const data = await login(credentials);
        if (data.statusCode == 200) {
            console.log(data);
            navigate('/home');
        } else if (data.statusCode == 401) {
            setError({displayed: true, msg: "The pair username/password is uncorrect."});
            return;
        } else {
            setError({displayed: true, msg: "An error occured please retry."});
        }
    }

    return (
        <div className="window">
            <div className="errorContainer">{error.displayed ? error.msg : null}</div>
            <form className="inputContainerLogin" onSubmit={(e) => onSubmit(e)}>
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
                <button type="submit" >Login</button>
                <button className="btnRegister" onClick={() => navigate('/register')}>Register</button>
            </form>
        </div>
    );
}

export default Login;