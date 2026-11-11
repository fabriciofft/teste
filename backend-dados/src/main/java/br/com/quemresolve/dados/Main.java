package br.com.quemresolve.dados;

import br.com.quemresolve.dados.configuracao.Configuracao;
import br.com.quemresolve.dados.banco.DB;
import br.com.quemresolve.dados.banco.InicializadorBanco;
import br.com.quemresolve.dados.servidor.RoteadorRequisicoes;
import br.com.quemresolve.dados.servidor.ServidorTcp;

public class Main {
  public static void main(String[] args) throws Exception {

    Configuracao configuracao = Configuracao.carregar();
    DB dados = new DB(configuracao);

    new InicializadorBanco(configuracao, dados).inicializar();
    new ServidorTcp(configuracao, new RoteadorRequisicoes(dados)).iniciar();
  }
}
