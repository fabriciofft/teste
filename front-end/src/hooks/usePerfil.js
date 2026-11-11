
import { useEffect, useState } from "react";
import axios from "axios";
import useMessageBox from "./useMessageBox";

const api = axios.create({
  baseURL: "http://localhost:8080/api",
  withCredentials: true
});

export default function usePerfil(usuarioId) {

  const [perfil, setPerfil] = useState({
    usuario_Id: usuarioId || "",
    categoria: [],
    titulo: "",
    biografia: "",
    cidade: "",
    regiao: "",
    unidade: "",
    celular: "",
    raio: "",
    preco: "",
    latitude: "",
    longitude: ""
  });

  const camposObrigatorios = [
    "categoria",
    "titulo",
    "biografia",
    "cidade",
    "regiao",
    "unidade",
    "celular",
    "raio",
    "preco"
  ];

  const perfilValido = !!perfil &&  camposObrigatorios
    .filter((campo) => campo !== "categoria")
    .every((campo) => String(perfil[campo] ?? "").trim() !== "") &&
  (Array.isArray(perfil.categoria)
    ? perfil.categoria.length > 0
    : String(perfil.categoria ?? "").trim() !== "");

  function preencherPerfil(event) {
    const { name, value, checked, type } = event.target;

    if (name === "categoria" && type === "checkbox") {

      setPerfil((atual) => {
        const categoriasAtuais = Array.isArray(atual.categoria)
          ? atual.categoria
          : [];

        return {
          ...atual,
          categoria: checked
            ? [...new Set([...categoriasAtuais, value])]
            : categoriasAtuais.filter((categoria) => categoria !== value)
        };
      });

      return;
    }

    setPerfil((atual) => ({
      ...atual,
      [name]: value
    }));
  }

  const {exibirMessageBox} = useMessageBox();

  async function salvarPerfil(id) {
    const dados = {
      ...perfil,
      usuario_Id: id,
      categoria: Array.isArray(perfil.categoria)
        ? perfil.categoria.join(", ")
        : perfil.categoria,
      raio: Number(perfil.raio),
      preco: Number(perfil.preco),
      latitude: perfil.latitude === "" ? null : Number(perfil.latitude),
      longitude: perfil.longitude === "" ? null : Number(perfil.longitude)
    };

    await api.post("/perfil", dados)
    .then(() => {
      exibirMessageBox("/", true, "Perfil salvo sucesso!", "Prosseguir");
    })
    .catch((error) => {
      console.log(error);
    });
  }

   const buscarMeuPerfil = () => {
    api.get("/usuario-atual")
    .then((resp) => {
      setPerfil(resp.data.profile);
    })
    .catch((error) => {
      console.log(error)
    });
  }

  useEffect(() => {
    buscarMeuPerfil();
  }, []);

  const enviarFormularioSalvarPerfil = (e, id) => {
    e.preventDefault();
    salvarPerfil(id);
  }

  return {
    perfil, perfilValido,
    preencherPerfil,
    enviarFormularioSalvarPerfil
  };
}
