import './App.css'
import RoutesComponent from "./routes";
import {BrowserRouter} from "react-router-dom";
import {ToastProvider} from "./components/toasts/ToastProvider.tsx";
import {ModalProvider} from "./components/modals/ModalProvider.tsx";


function App() {

    return (
        <>
            <ToastProvider>
                <ModalProvider>
                    <BrowserRouter>
                        <RoutesComponent/>
                    </BrowserRouter>
                </ModalProvider>
            </ToastProvider>
        </>
    )
}

export default App
