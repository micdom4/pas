import './App.css'
import RoutesComponent from "./routes";
import {BrowserRouter} from "react-router-dom";

function App() {

    return (
        <>
            <BrowserRouter>
                <RoutesComponent/>
            </BrowserRouter>
        </>
    )
}

export default App
