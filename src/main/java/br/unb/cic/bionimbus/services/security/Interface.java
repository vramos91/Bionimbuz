/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.unb.cic.bionimbus.services.security;

import br.unb.cic.bionimbus.client.shell.SimpleShell;
import br.unb.cic.bionimbus.services.security.utilities.Session;
import br.unb.cic.bionimbus.services.security.attribute.Atributo;
import br.unb.cic.bionimbus.services.security.attribute.AtributoArquivo;
import br.unb.cic.bionimbus.services.security.attribute.AtributoUsuario;
import br.unb.cic.bionimbus.services.security.entities.Arquivo;
import br.unb.cic.bionimbus.services.security.entities.Usuario;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author heitor
 */
public class Interface {
    
    public void iniciar() throws IOException {
        
        String nome;
        String senha;
        
        Scanner entrada = new Scanner(System.in);
        
        System.out.println("Digite o nome de usuário");
        nome = entrada.nextLine();
        System.out.println("Digite a senha");
        senha = entrada.nextLine();
        
        Usuario pessoa = new Usuario();
        pessoa.setNome(nome);
        pessoa.setSenha(senha);
        
        Login session = new Login();
        if (session.efetuaLogin(pessoa)){
            System.out.println("Login efetuado com sucesso");
            if (("admin".equals(nome)) && ("admin".equals(senha))){
                this.painelAdm();
            }
            else{
                 new SimpleShell().readEvalPrintLoop();
            }
        }
        else{
            System.out.println("O login falhou. Tente Novamente");
            this.iniciar();
        }  
    }
    
   
    
    public void cadastrarUsuario() {
        
        String nome;
        String senha;
        
        Scanner entrada = new Scanner(System.in);
        System.out.println("Digite o nome de usuário");
        nome = entrada.nextLine();
        System.out.println("Digite a senha");
        senha = entrada.nextLine();
        
        Usuario novo = new Usuario();
        novo.setNome(nome);
        novo.setSenha(senha);
        
        if (!novo.existeUsuario()){
            novo.cadastraUsuario(novo);
            System.out.println("\n **Cadastro Efetuado com Sucesso**\n");
        }
        else{
            System.out.println("\n**Usuario ja cadastrado**\n");
        }
        
    }
    
    public void insereAtributo(){
        
        String nome;
        String tipo;
        
        Scanner entrada = new Scanner(System.in);
        System.out.println("Digite o nome do atributo");
        nome = entrada.nextLine();
        
        Atributo novo = new AtributoUsuario();
        novo.setNome(nome);
        novo.cadastrarAtributo(novo);
    
    }
    
    public void novaRegra(){
    
        String regra;
        Scanner entrada = new Scanner(System.in);
        System.out.println("Digite a regra");
        regra = entrada.nextLine();
    
        PolicyManager novo = new PolicyManager();
        novo.criaRegra(regra);
    
    }
    
    public void insereAtributoNaPessoa(){
        
        List<Usuario> todos;
        List<AtributoUsuario> todosAtt;
        Scanner entrada = new Scanner(System.in);
        Integer opcao,opcao2;
        String valorAtt;
        
        
        System.out.println("Escolha um usuário:");
        Usuario all = new Usuario();
        todos = all.selectPessoas();
        
        for (Usuario todo : todos) {
            System.out.println(todo.getId() + ")" + todo.getNome());
        }
        
        try{
            opcao = new Integer(entrada.nextLine());
            all.setId(opcao);
        }
        catch (NumberFormatException erro){
            System.out.println("Digite uma opção válida");
        }
        
        System.out.println("Escolha um Atributo de Usário:");
        AtributoUsuario att = new AtributoUsuario();
        todosAtt = att.selectAtributos();
        
        for (AtributoUsuario todosAtt1 : todosAtt) {
            System.out.println(todosAtt1.getId() + ")" + todosAtt1.getNome());
        }
        
        try{
            opcao2 = new Integer(entrada.nextLine());
            att.setId(opcao2);
        }
        catch (NumberFormatException erro){
            System.out.println("Digite uma opção válida");
        }
        
        System.out.println("Digite um valor para o Atributo escolhido:");
        valorAtt = entrada.nextLine();
        att.setValor(valorAtt);
        
        att.cadastraAtributoUsuario(all,att);
    }
    
    public void atribuiRegraUsr(){
        
        List<Usuario> todos;
        List<PolicyManager> todasRegras;
        Scanner entrada = new Scanner(System.in);
        Integer opcao;
        Integer opcao2;
    
        System.out.println("Escolha um usuário:");
        Usuario all = new Usuario();
        todos = all.selectPessoas();
        
        for (Usuario todo : todos) {
            System.out.println(todo.getId() + ")" + todo.getNome());
        }
        
        try{
            opcao = new Integer(entrada.nextLine());
            all.setId(opcao);
        }
        catch (NumberFormatException erro){
            System.out.println("Digite uma opção válida");
        }
    
        System.out.println("Escolha uma Regra:");
        PolicyManager regras = new PolicyManager();
        todasRegras = regras.selectRegras();
        
        for (PolicyManager  regra: todasRegras) {
            System.out.println(regra.getId() + ")" + regra.getSQL());
        }
        try{
            opcao2 = new Integer(entrada.nextLine());
            regras.setId(opcao2);
        }
        catch (NumberFormatException erro){
            System.out.println("Digite uma opção válida");
        }
        
        regras.atribuiRegraUsr(all,regras);
    
    
    
    }
    
    public void atribuiRegraArq(){
    
        Scanner entrada = new Scanner(System.in);
        List<Arquivo> listaArq;
        List<PolicyManager> listaRegras;
        int opcao;
        int opcao2;
        
        System.out.println("Escolha um arquivo:");
        Arquivo arq = new Arquivo();
        listaArq = arq.selectArquivos();
            
        for (Arquivo listaArq1 : listaArq) {
            System.out.println(listaArq1.getId() + ")" + listaArq1.getNome());
        }
        
        try{
            opcao = new Integer(entrada.nextLine());
            arq.setId(opcao);
        }
        catch (NumberFormatException erro){
            System.out.println("Digite uma opção válida");
        }
        
        System.out.println("Escolha uma Regra:");
        PolicyManager regras = new PolicyManager();
        listaRegras = regras.selectRegras();
        
        for (PolicyManager  regra: listaRegras) {
            System.out.println(regra.getId() + ")" + regra.getSQL());
        }
        try{
            opcao2 = new Integer(entrada.nextLine());
            regras.setId(opcao2);
        }
        catch (NumberFormatException erro){
            System.out.println("Digite uma opção válida");
        }
        
        regras.atribuiRegraArq(arq,regras);
    
    
    }
    
    public void verTodosArq(){
        
        List<Arquivo> arquivos;
        
        PDP autorizacao = new PDP();
        Session sessao = Session.getInstance();
        
        arquivos = autorizacao.mostraArquivos();
        
        for (Arquivo arq: arquivos){
            System.out.println(arq.getNome());
        }
    }
    
    public void deletaUsuario(){
        
        List<Usuario> todos;
        Scanner entrada = new Scanner(System.in);
        Integer opcao;
        
        
        System.out.println("Escolha um usuário:");
        Usuario usuarios = new Usuario();
        todos = usuarios.selectPessoas();
        
        for (Usuario todo : todos) {
            System.out.println(todo.getId() + ")" + todo.getNome());
        }
        
        try{
            opcao = new Integer(entrada.nextLine());
            usuarios.setId(opcao);
        }
        catch (NumberFormatException erro){
            System.out.println("Digite uma opção válida");
        }
        
        usuarios.deletaUsuario();
    
    
    }
    public void deletaRegra(){
        
        List<PolicyManager> todasRegras;
        Scanner entrada = new Scanner(System.in);
        Integer opcao;
    
        System.out.println("Escolha uma Regra:");
        PolicyManager regras = new PolicyManager();
        todasRegras = regras.selectRegras();
        
        for (PolicyManager  regra: todasRegras) {
            System.out.println(regra.getId() + ")" + regra.getSQL());
        }
        try{
            opcao = new Integer(entrada.nextLine());
            regras.setId(opcao);
        }
        catch (NumberFormatException erro){
            System.out.println("Digite uma opção válida");
        }
        
        regras.deletaRegra();
    
    }
    
    public void deletaAttUsuario(){
        
        List<AtributoUsuario> todosAtt;
        Scanner entrada = new Scanner(System.in);
        Integer opcao;
        
        System.out.println("Escolha um Atributo de Usário:");
        AtributoUsuario att = new AtributoUsuario();
        todosAtt = att.selectAtributos();
        
        for (AtributoUsuario todosAtt1 : todosAtt) {
            System.out.println(todosAtt1.getId() + ")" + todosAtt1.getNome());
        }
        
        try{
            opcao = new Integer(entrada.nextLine());
            att.setId(opcao);
        }
        catch (NumberFormatException erro){
            System.out.println("Digite uma opção válida");
        }
        
        att.deletaAtributo();
    
    
    }
    
    public void deletaAttArquivo(){
        
        Scanner entrada = new Scanner(System.in);
        List<AtributoArquivo> listaAtt;
        int opcao;
    
        System.out.println("Escolha um atributo de Arquivo:");
        AtributoArquivo att = new AtributoArquivo();
        listaAtt = att.selectAtributos();
        
        for(AtributoArquivo arq1 : listaAtt){
            System.out.println(arq1.getId() + ")" + arq1.getNome());
        }
        
        try{
            opcao = new Integer(entrada.nextLine());
            att.setId(opcao);
        }
        catch (NumberFormatException erro){
            System.out.println("Digite uma opção válida");
        }
        
        att.deletaAtributo();
    
    }
    
    public void alteraAttUsr() {
        
        List<Usuario> todos;
        List<AtributoUsuario> todosAtt;
        Scanner entrada = new Scanner(System.in);
        Integer opcao,opcao2;
        String valorAtt;
                
        System.out.println("Escolha um usuário:");
        Usuario usuarios = new Usuario();
        todos = usuarios.selectPessoas();
        
        for (Usuario todo : todos) {
            System.out.println(todo.getId() + ")" + todo.getNome());
        }
        
        try{
            opcao = new Integer(entrada.nextLine());
            usuarios.setId(opcao);
        }
        catch (NumberFormatException erro){
            System.out.println("Digite uma opção válida");
        }
        
        System.out.println("Escolha um Atributo de Usário:");
        AtributoUsuario att = new AtributoUsuario();
        todosAtt = att.selectAtributos();
        
        for (AtributoUsuario todosAtt1 : todosAtt) {
            System.out.println(todosAtt1.getId() + ")" + todosAtt1.getNome());
        }
        
        try{
            opcao2 = new Integer(entrada.nextLine());
            att.setId(opcao2);
        }
        catch (NumberFormatException erro){
            System.out.println("Digite uma opção válida");
        }
        
        System.out.println("Digite o novo valor para o Atributo escolhido:");
        valorAtt = entrada.nextLine();
        att.setValor(valorAtt);
        
        att.alteraAtributoUsuario(usuarios);
        
    }
    
    public void alteraAttArq(){
    
        Scanner entrada = new Scanner(System.in);
        List<Arquivo> listaArq;
        List<AtributoArquivo> listaAtt;
        int opcao;
        int opcao2;
        String valorAtt;
        
        System.out.println("Escolha um arquivo:");
        Arquivo arq = new Arquivo();
        listaArq = arq.selectArquivos();
            
        for (Arquivo listaArq1 : listaArq) {
            System.out.println(listaArq1.getId() + ")" + listaArq1.getNome());
        }
        
        try{
            opcao = new Integer(entrada.nextLine());
            arq.setId(opcao);
        }
        catch (NumberFormatException erro){
            System.out.println("Digite uma opção válida");
        }
        
        System.out.println("Escolha um atributo de Arquivo:");
        AtributoArquivo att = new AtributoArquivo();
        listaAtt = att.selectAtributos();
        
        for(AtributoArquivo arq1 : listaAtt){
            System.out.println(arq1.getId() + ")" + arq1.getNome());
        }
        
        try{
            opcao2 = new Integer(entrada.nextLine());
            att.setId(opcao2);
        }
        catch (NumberFormatException erro){
            System.out.println("Digite uma opção válida");
        }
        
        System.out.println("Digite um valor para o Atributo escolhido:");
        valorAtt = entrada.nextLine();
        att.setValor(valorAtt);

        att.alteraAtributoNoArquivo(arq);
        
        
    }
    
    public void insereAtributoDeArquivo(){
        
        String nome;
        
        Scanner entrada = new Scanner(System.in);
        System.out.println("Digite o nome do atributo");
        nome = entrada.nextLine();
        
        AtributoArquivo novo = new AtributoArquivo();
        novo.setNome(nome);
        novo.cadastrarAtributo(novo);
    
    }
    
    public void cadastrarAtributoArquivo(){
            
        Scanner entrada = new Scanner(System.in);
        List<Arquivo> listaArq;
        List<AtributoArquivo> listaAtt;
        int opcao;
        int opcao2;
        String valorAtt;
        
        System.out.println("Escolha um arquivo:");
        Arquivo arq = new Arquivo();
        listaArq = arq.selectArquivos();
            
        for (Arquivo listaArq1 : listaArq) {
            System.out.println(listaArq1.getId() + ")" + listaArq1.getNome());
        }
        
        try{
            opcao = new Integer(entrada.nextLine());
            arq.setId(opcao);
        }
        catch (NumberFormatException erro){
            System.out.println("Digite uma opção válida");
        }
        
        System.out.println("Escolha um atributo de Arquivo:");
        AtributoArquivo att = new AtributoArquivo();
        listaAtt = att.selectAtributos();
        
        for(AtributoArquivo arq1 : listaAtt){
            System.out.println(arq1.getId() + ")" + arq1.getNome());
        }
        
        try{
            opcao2 = new Integer(entrada.nextLine());
            att.setId(opcao2);
        }
        catch (NumberFormatException erro){
            System.out.println("Digite uma opção válida");
        }
        
        System.out.println("Digite um valor para o Atributo escolhido:");
        valorAtt = entrada.nextLine();
        att.setValor(valorAtt);

        att.cadastraAtributoNoArquivo(arq,att);
        
    }
    
    public void retirarRegraUsr(){
        
        List<Usuario> todos;
        List<PolicyManager> regras;
        PolicyManager regra = new PolicyManager();
        Scanner entrada = new Scanner(System.in);
        Integer opcao;
    
        System.out.println("Escolha um usuário:");
        Usuario usuario = new Usuario();
        todos = usuario.selectPessoas();
        
        for (Usuario todo : todos) {
            System.out.println(todo.getId() + ")" + todo.getNome());
        }
        
        try{
            opcao = new Integer(entrada.nextLine());
            usuario.setId(opcao);
        }
        catch (NumberFormatException erro){
            System.out.println("Digite uma opção válida");
        }
        
        regras = usuario.obterRegras();
        
        for (PolicyManager todas : regras) {
            System.out.println(todas.getId() + ")" + todas.getSQL());
        }
        try{
            opcao = new Integer(entrada.nextLine());
            regra.setId(opcao);
        }
        catch (NumberFormatException erro){
            System.out.println("Digite uma opção válida");
        }
        
        regra.removeRegraUsr(usuario);
        
    }
    
    public void retirarRegraArq(){
        
        Scanner entrada = new Scanner(System.in);
        PolicyManager regra = new PolicyManager();
        List<Arquivo> listaArq;
        List<PolicyManager> regras;
        int opcao;
        
        System.out.println("Escolha um arquivo:");
        Arquivo arq = new Arquivo();
        listaArq = arq.selectArquivos();
            
        for (Arquivo listaArq1 : listaArq) {
            System.out.println(listaArq1.getId() + ")" + listaArq1.getNome());
        }
        
        try{
            opcao = new Integer(entrada.nextLine());
            arq.setId(opcao);
        }
        catch (NumberFormatException erro){
            System.out.println("Digite uma opção válida");
        }
        
        regras = arq.obterRegras();
        for (PolicyManager todas : regras) {
            System.out.println(todas.getId() + ")" + todas.getSQL());
        }
        try{
            opcao = new Integer(entrada.nextLine());
            regra.setId(opcao);
        }
        catch (NumberFormatException erro){
            System.out.println("Digite uma opção válida");
        }
        
        regra.removeRegraArq(arq);
        
    }
    
    public void painelAdm(){
    
        Scanner entrada = new Scanner(System.in);
        boolean continua = true;
        
        while (continua){
            
            System.out.println("\n*****Painel do Adm*****\n");
            System.out.println("Escolha uma opção:\n");
            System.out.println("1) Gerenciar usuarios\n");
            System.out.println("2) Gerenciar atributos e politicas\n");
            System.out.println("3) Gerenciar arquivos\n");
            System.out.println("4) Sair");
            
            try{
                Integer opcao = new Integer(entrada.nextLine());
                
                switch(opcao){
                case 1:
                    this.painelUsr();
                    break;
                case 2:
                    this.painelPoliticas();
                    break;
                case 3:
                    this.painelArquivos();
                    break;
                case 4:
                    continua = false;
                    break;
                }
                
            }
            catch (NumberFormatException nfe) {
                System.out.println("Digite um número válido ");
            }   
        }
    }
    
    
    public void painelUsr(){
        
        Scanner entrada = new Scanner(System.in);
        boolean continua = true;
        
        while (continua){
            
            System.out.println("\n*****Painel do Adm*****\n");
            System.out.println("Escolha uma opção:\n");
            System.out.println("1)Cadastrar Usuario");
            System.out.println("2)Definir Atributo de um Usuario");
            System.out.println("3)Associar regra a um usuario");
            System.out.println("4)Retirar regra de um usuario");
            System.out.println("5)Deletar um usuário");
            System.out.println("6)Alterar valor de atributo de usuario");
            System.out.println("7)Sair");

            try{
                Integer opcao = new Integer(entrada.nextLine());

                switch(opcao){
                case 1:
                    this.cadastrarUsuario();
                    break;
                case 2:
                    this.insereAtributoNaPessoa();
                    break;
                case 3:
                    this.atribuiRegraUsr();
                    break;
                case 4:
                    this.retirarRegraUsr();
                    break;
                case 5:
                    this.deletaUsuario();
                    break;
                case 6:
                    this.alteraAttUsr();
                    break;
                case 7:
                    continua = false;
                    break;
                }
            }
            catch (NumberFormatException nfe) {
                System.out.println("Digite um número válido ");
            }
        
        }
    }
    
    public void painelPoliticas(){
    
        Scanner entrada = new Scanner(System.in);
        boolean continua = true;
        
        while (continua){
            
            System.out.println("\n*****Painel do Adm*****\n");
            System.out.println("Escolha uma opção:\n");
            System.out.println("1)Criar Atributo de Usuario");
            System.out.println("2)Criar Atributo de Arquivo");
            System.out.println("3)Criar Nova Regra de Acesso");
            System.out.println("4)Deletar uma Regra");
            System.out.println("5)Deletar Atributo de Usuario");
            System.out.println("6)Deletar Atributo de Arquivo");
            System.out.println("7) Sair");
            
            try{
                Integer opcao = new Integer(entrada.nextLine());
                
                switch(opcao){
                case 1:
                    this.insereAtributo();
                    break;
                case 2:
                    this.insereAtributoDeArquivo();
                    break;
                case 3:
                    this.novaRegra();
                    break;
                case 4:
                    this.deletaRegra();
                    break;
                case 5:
                    this.deletaAttUsuario();
                    break;
                case 6:
                    this.deletaAttArquivo();
                    break;
                case 7:
                    continua = false;
                    break;
                }
                
            }
            catch (NumberFormatException nfe) {
                System.out.println("Digite um número válido ");
            }
            
        }
    
    }
    
    public void painelArquivos(){
    
        Scanner entrada = new Scanner(System.in);
        boolean continua = true;
        
        while (continua){
            
            System.out.println("\n*****Painel do Adm*****\n");
            System.out.println("Escolha uma opção:\n");
            System.out.println("1)Definir Atributo de um Arquivo");
            System.out.println("2)Atribuir regra a um arquivo");
            System.out.println("3)Alterar valor de atributo de arquivo");
            System.out.println("4)Remover regra de um arquivo");
            System.out.println("5) Sair");
            
            try{
                Integer opcao = new Integer(entrada.nextLine());
                
                switch(opcao){
                case 1:
                    this.cadastrarAtributoArquivo();
                    break;
                case 2:
                    this.atribuiRegraArq();
                    break;
                case 3:
                    this.alteraAttArq();
                    break;
                case 4:
                    this.retirarRegraArq();
                    break;
                case 5:
                    continua = false;
                    break;
                }
                
            }
            catch (NumberFormatException nfe) {
                System.out.println("Digite um número válido ");
            }
            
        }
    
    }
    
    public void telaInicial() {
        
        Scanner entrada = new Scanner(System.in);
        
        System.out.println("\n*****Bem Vindo ao BioNimbuZ*****\n");
        System.out.println("Escolha uma opção:\n");
        System.out.println("1)Ver Arquivos");
        
        try{
            Integer opcao = new Integer(entrada.nextLine());
            
            switch(opcao){
            case 1:
                this.verTodosArq();
                break;
            }
        }
        catch (NumberFormatException nfe) {
            System.out.println("Digite um número válido ");
        }
       
    }
    
}
