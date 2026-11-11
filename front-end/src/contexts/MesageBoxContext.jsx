import { createContext, useState } from "react";

export const MessageBoxContext = createContext();

export const MessageBoxProvider = ({children}) => {

  const [visibilidadeMessageBox, setVisibilidadeMessageBox] = useState(false);

  const tornarVisivelMessageBox = () => {setVisibilidadeMessageBox(true);}
  const esconderMessageBox = () => {setVisibilidadeMessageBox(false);}

  return(
    <MessageBoxContext.Provider value={{visibilidadeMessageBox, tornarVisivelMessageBox, esconderMessageBox}}>
      {children}
    </MessageBoxContext.Provider>
  )
}