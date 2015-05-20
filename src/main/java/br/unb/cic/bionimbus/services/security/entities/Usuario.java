/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.bionimbus.services.security.entities;

import br.unb.cic.bionimbus.services.security.utilities.Database;
import br.unb.cic.bionimbus.services.security.PDP;
import br.unb.cic.bionimbus.services.security.PolicyManager;
import br.unb.cic.bionimbus.services.security.attribute.AtributoUsuario;
import br.unb.cic.bionimbus.services.security.utilities.PBKDF2;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.List;


/**
 *
 * @author heitor
 */
public class Usuario {

    private String nome;
    private String senha;
    private Integer id;

    public Usuario() {
        this.id = null;
        this.nome = null;
        this.senha = null;
    }

    /**
     *
     * @param id
     */
    public Usuario(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setNome(String nome1) {
        nome = nome1;
    }
    public void setNome(Integer id){
        Database db = new Database();
        this.nome = db.selectNomePessoa(id);
    }

    public void setSenha(String senha1) {
        senha = senha1;
    }

    public Integer getId() {
        return id;
    }

    public void setId(String nome) {
        Database db = new Database();
        this.setId(db.selectIdPessoa(nome));
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void cadastraUsuario(Usuario pessoa) {

        Database cadastra = new Database();
        PBKDF2 hash = new PBKDF2();
        try {
            pessoa.setSenha(hash.createHash(pessoa.getSenha()));
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Não foi possível cadastrar o usuário");
        } catch (InvalidKeySpecException ex) {
            System.out.println("Não foi possível cadastrar o usuário");
        }
       
        try {
            cadastra.insertPessoa(pessoa);
            pessoa.configuraInicial();
            
        } catch (SQLException ex) {
            System.out.println("Usuario não pode ser cadastrado");
        }

    }

    public List<Usuario> selectPessoas() {

        List<Usuario> todasPessoas = null;
        Database seleciona = new Database();
        try {
            todasPessoas = seleciona.selectTodasPessoas();
        } catch (SQLException erro) {
            System.out.println("Não foi possível realizar a operação");
        }
        return (todasPessoas);
    }

    public List<PolicyManager> obterRegrasSQL() {

        List<PolicyManager> regras;
        Database db = new Database();

        regras = db.obterRegrasSQL(this.getId());

        return regras;
    }

    public List<PolicyManager> obterRegras() {

        List<PolicyManager> regras;
        Database db = new Database();

        regras = db.obterRegrasUsr(this.getId());

        return regras;
    }

    public void configuraInicial() throws SQLException {

        this.setId(this.getNome());

        AtributoUsuario atributo = new AtributoUsuario();
        atributo.setId("permissao");
        atributo.setValor("normal");
        atributo.cadastraAtributoUsuario(this, atributo);

        PolicyManager regra = new PolicyManager();
        regra.setId(1);
        regra.atribuiRegraUsr(this, regra);

        regra.criaRegra("a.proprietario = " + this.getNome());
        regra.atribuiRegraUsr(this, regra);

        PDP autorizacao = new PDP();
        autorizacao.verificaArquivos(this);

    }

    public void deletaUsuario() {

        Database db = new Database();

        db.deletarRegraUsuario(this.getId());
        db.deletarAtributoUsuario(this.getId());
        db.deletarArquivoUsuario(this.getId());
        db.deletarUsuario(this.getId());

    }

    public boolean existeUsuario() {

        Boolean existe;

        Database db = new Database();

        existe = db.verificaUsuario(this.getNome());

        return existe;

    }
    
    public void atualizaArquivosUsuario(){
        
        Database novo = new Database();
        novo.deletarArquivoUsr(this.getId());
    
        this.setNome(this.getId());
        PDP autorizacao = new PDP();
        try{
           autorizacao.verificaArquivos(this);
        
        } catch (SQLException e) {
            /*e.printStackTrace();*/
	}
    
    
    }

}
