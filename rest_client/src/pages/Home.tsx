import LoggedUserContext from "../contexts/LoggedUserContext";
import {use} from "react";

export default function Home() {
    const {user} = use(LoggedUserContext)

    return <>
        <h1>Welcome in polVirt{user.isAuthenticated() && `, ${user.login}`}!</h1>
    </>
}