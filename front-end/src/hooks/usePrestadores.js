import { useEffect, useState } from "react";
import api from "../service/api";

const usePrestadores = () => {

  const [prestadoress, setPrestadores] = useState([]);

  const listarPrestadores = () => {
    api.get("/providers")
    .then((resp) => {
      setPrestadores(resp.data.providers);
    })
    .catch((error) => {
      console.log(error);
    })
  }

  useEffect(() => {
    listarPrestadores();
  }, []);

  const prestadores = [
    {
      id: 1,
      nome: "Ricardo Almeida",
      categoria: "Eletricista",
      descricao: "Instalações e reparos elétricos",
      sobre:
        "Instalação de tomadas, troca de disjuntores e manutenção elétrica residencial.",
      cidade: "Paulínia",
      regiao: "Centro e bairros de Paulínia",
      nota: 4.9,
      avaliacoes: 84,
      preco: 90,
      unidade: "visita",
      icone: "💡",
      cor: "#EEE5D2",
    },
    {
      id: 2,
      nome: "Mariana Costa",
      categoria: "Limpeza",
      descricao: "Limpeza residencial e pós-obra",
      sobre:
        "Limpeza de casas, apartamentos e ambientes após reformas. O orçamento varia conforme o imóvel.",
      cidade: "Paulínia",
      regiao: "Paulínia e Betel",
      nota: 4.9,
      avaliacoes: 62,
      preco: 160,
      unidade: "diária",
      icone: "🧹",
      cor: "#F1E6DD",
    },
    {
      id: 3,
      nome: "Paulo Henrique",
      categoria: "Encanador",
      descricao: "Reparos hidráulicos e instalações",
      sobre:
        "Reparos de vazamentos, instalação de torneiras e manutenção hidráulica.",
      cidade: "Paulínia",
      regiao: "Paulínia e Campinas",
      nota: 4.8,
      avaliacoes: 47,
      preco: 100,
      unidade: "visita",
      icone: "🔧",
      cor: "#EAE3DC",
    },
    {
      id: 4,
      nome: "Lucas Ferreira",
      categoria: "Pintor",
      descricao: "Pintura residencial e acabamento",
      sobre:
        "Pintura de paredes, preparação de superfícies e acabamentos. Materiais são combinados no orçamento.",
      cidade: "Paulínia",
      regiao: "Paulínia e região",
      nota: 4.7,
      avaliacoes: 39,
      preco: 25,
      unidade: "m²",
      icone: "🎨",
      cor: "#EFE0D9",
    },
    {
      id: 5,
      nome: "André Santos",
      categoria: "Jardinagem",
      descricao: "Cuidados com jardins e poda",
      sobre:
        "Manutenção de jardins, poda e cuidados com plantas. O valor depende do espaço e do serviço.",
      cidade: "Campinas",
      regiao: "Campinas e Barão Geraldo",
      nota: 4.9,
      avaliacoes: 53,
      preco: 120,
      unidade: "serviço",
      icone: "🌿",
      cor: "#EBE4D5",
    },
    {
      id: 6,
      nome: "Fernando Lima",
      categoria: "Montador",
      descricao: "Montagem e ajustes de móveis",
      sobre:
        "Montagem de móveis e ajustes de portas e gavetas. Envie os detalhes do móvel para solicitar orçamento.",
      cidade: "Campinas",
      regiao: "Campinas e região",
      nota: 4.8,
      avaliacoes: 71,
      preco: 80,
      unidade: "serviço",
      icone: "🪛",
      cor: "#EAE3DA",
    },
  ];

  return{prestadores};
}

export default usePrestadores;