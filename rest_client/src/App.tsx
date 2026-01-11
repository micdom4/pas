import './App.css'
import RoutesComponent from "./routes";
import {BrowserRouter} from "react-router-dom";
import {ToastProvider} from "./components/toasts/ToastProvider.tsx";


function App() {

    return (
        <>
            <ToastProvider>
                <BrowserRouter>
                    <RoutesComponent/>
                </BrowserRouter>
            </ToastProvider>
        </>
    )
}

export default App
