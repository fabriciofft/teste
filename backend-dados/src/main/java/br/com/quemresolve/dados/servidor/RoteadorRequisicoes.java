package br.com.quemresolve.dados.servidor;

import br.com.quemresolve.dados.banco.DB;
import br.com.quemresolve.dados.repository.*;
import static br.com.quemresolve.dados.utilitarios.ArgumentosJson.*;

import com.google.gson.*;
import java.sql.*;

// Recebe {"op": "...", "data": {...}} e chama o repositório correspondente
public class RoteadorRequisicoes {
  private final UsuarioRepository usuarios;
  private final SessaoRepository sessoes;
  private final PerfilRepository perfis;
  private final PrestadorRepository prestadores;
  private final AvaliacaoRepository avaliacoes;
  private final ContatoRepository contatos;

  public RoteadorRequisicoes(DB dados) {
    usuarios = new UsuarioRepository(dados);
    sessoes = new SessaoRepository(dados);
    perfis = new PerfilRepository(dados);
    prestadores = new PrestadorRepository(dados);
    avaliacoes = new AvaliacaoRepository(dados);
    contatos = new ContatoRepository(dados);
  }

  public JsonObject executar(JsonObject r) throws SQLException {
    System.out.println("OPERAÇÃO RECEBIDA: " + r.get("op").getAsString());

    JsonObject d = r.has("data") && r.get("data").isJsonObject()
          ? r.getAsJsonObject("data")
          : new JsonObject();

    return switch (r.get("op").getAsString()) {
      case "USER_EMAIL" -> usuarios.buscarUsuarioPorEmail(texto(d, "email"));
      case "USER_SESSION" -> sessoes.buscarSessaoUsuario(texto(d, "token"), numero(d, "now"));
      case "INSERT_USER" -> usuarios.inserirUsuario(d);
      case "SAVE_SESSION" -> sessoes.salvarSessao(d);
      case "DELETE_SESSION" -> sessoes.excluirSessao(texto(d, "token"));
      case "PROFILE" -> perfis.buscarPerfil(texto(d, "userId"));
      case "PROVIDERS" -> prestadores.listarPrestadores();
      case "UPSERT_PROFILE" -> {
        System.out.println("Executando UPSERT_PROFILE");
        yield perfis.gravarPerfil(d);
      }
      case "REVIEWS" -> avaliacoes.listarAvaliacoes(d);
      case "PHONE" -> perfis.buscarTelefone(texto(d, "usuario_id"));
      case "CONTACT" -> contatos.contatarPrestador(d);
      case "HAS_CONTACT" -> contatos.possuiContato(d);
      case "UPSERT_REVIEW" -> avaliacoes.gravarAvaliacao(d);
      default -> throw new SQLException("Operação desconhecida");
    };
  }
}
