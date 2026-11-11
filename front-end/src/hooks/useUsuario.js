
import { useEffect, useState } from "react";
import api from "../service/api";
import { useLocation } from "react-router-dom";
import useMessageBox from '../hooks/useMessageBox';

export default function useUsuario() {
  const [usuario, setUsuario] = useState({
    nome: "",
    email: "",
    senha: "",
    role: "cliente"
  });

  function preencherUsuario(event) {
    const { name, value } = event.target;

    setUsuario((atual) => ({
      ...atual,
      [name]: value
    }));
  }

  const {exibirMessageBox} = useMessageBox();

  const fazerCadastro = () => {
    console.log(usuario)
    api.post("/cadastro", usuario)
    .then((resp) => {
      exibirMessageBox("/", true, "Cadastrado com sucesso!", "Prosseguir");
    })
    .catch((error) => {
      console.log(error)
    });
  }

  const fazerLogin = () => {
    api.post("/entrar", {
      "email": usuario.email.trim(),
      "senha": usuario.senha.trim()
    })
    .then(() => {
      exibirMessageBox("/", true, "Logado com sucesso!", "Prosseguir");
    })
    .catch((error) => {
      console.log(error)
    });
  }

  const [meusDados, setMeusDados] = useState({});
  const location = useLocation();

  useEffect(() => {
    if(location.pathname == "/perfil"){
      buscarMeusDados();
    }
  }, []);

  const buscarMeusDados = () => {
    api.get("/usuario-atual")
    .then((resp) => {
      console.log(resp.data)
      setMeusDados(resp.data);
    })
    .catch((error) => {
      console.log(error)
    });
  }

  const enviarFormularioCadastro = (e) => {
    e.preventDefault();
    fazerCadastro();
  }
  
  const enviarFormularioLogin = (e) => {
    e.preventDefault();
    fazerLogin();
  }

  return {
    usuario,
    preencherUsuario,
    enviarFormularioLogin, enviarFormularioCadastro,
    meusDados
  };
}