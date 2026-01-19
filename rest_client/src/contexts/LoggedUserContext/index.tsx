import {createContext} from 'react'
import type {LoggedUserContextType} from "./types.ts";

const LoggedUserContext = createContext({} as LoggedUserContextType);
export default LoggedUserContext;
