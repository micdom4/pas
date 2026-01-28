import './App.css'
import RoutesComponent from "./routes";
import {BrowserRouter} from "react-router-dom";
import {ToastProvider} from "./components/toasts/ToastProvider.tsx";
import {ModalProvider} from "./components/modals/ModalProvider.tsx";
import LoggedUserContextProvider from "./contexts/LoggedUserContext/LoggedUserContextProvider.tsx";


function App() {

    return (
        <>
            <LoggedUserContextProvider>
                <ToastProvider>
                    <ModalProvider>
                        <BrowserRouter>
                            <RoutesComponent/>
                        </BrowserRouter>
                    </ModalProvider>
                </ToastProvider>
            </LoggedUserContextProvider>
        </>
    )
}

export default App
