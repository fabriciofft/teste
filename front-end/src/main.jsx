import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import App from "./App.jsx";
import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Home from "./pages/Home/index.jsx";
import Cadastro from "./pages/Cadastro/index.jsx";
import Login from "./pages/Login/index.jsx";
import Perfil from "./pages/Perfil/index.jsx";
import { MessageBoxProvider } from "./contexts/MesageBoxContext.jsx";

const router = createBrowserRouter([
  {
    path: "/",
    element: <App/>,
    children:[
      {
        path: "/",
        element: <Home/>
      },
      {
        path: "/cadastro",
        element: <Cadastro/>
      },
      {
        path: "/login",
        element: <Login/>
      },
      {
        path: "/perfil",
        element: <Perfil/>
      }
    ]
  }
]);


createRoot(document.getElementById("root")).render(
    <MessageBoxProvider>
      <RouterProvider router={router}>
        <App />
      </RouterProvider>
    </MessageBoxProvider>
);
