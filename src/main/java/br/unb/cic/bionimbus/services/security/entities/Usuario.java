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


/**Classe que serve de abstração para o trabalho com usuários dentro do sistema.
 * Esta classe contém os métodos de gerenciamento de usuários, bem como suas
 * características.
 * 
 *
 * @author Heitor Henrique
 */
public class Usuario {

    private String nome;
    private String senha;
    private Integer id;

    /**Construtor simples que apenas coloca os valores como nulos. 
     *
     */
    public Usuario() {
        this.id = null;
        this.nome = null;
        this.senha = null;
    }

    /**Construtor que já inicia um usuário com o seu valor de ID.
     *
     * @param id ID que o usuário deve receber
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

    /**Método que cadastra um usuário na federação. Este cadastro contém apenas
     * as informações simples como nome e senha, para que mais informações sejam
     * adicionadas este método pode ser reescrito, ou então um novo método
     * pode ser adicionado. 
     * A senha é cadastrada no banco utilizando o PBKDF2.
     * Algumas configurações iniciais são realizadas, como atributos cadastrados
     * automaticamente, através do método {@link Usuario#configuraInicial()}.
     *
     * @param pessoa Objeto {@link Usuario} contendo os valores de nome e senha
     * para cadastro.
     */
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

    /**Método que retorna todos os usuários cadastrados no sistema.
     *
     * @return List-{@link Usuario}- Lista de pessoas cadastradas no sistema.
     */
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

    /**Método que retorna as regras no formato SQl referentes a um determinado
     * usuário da federação.
     *
     * @return List-{@link PolicyManager}- lista de regras em SQL referentes a
     * um determinado usuário.
     */
    public List<PolicyManager> obterRegrasSQL() {

        List<PolicyManager> regras;
        Database db = new Database();

        regras = db.obterRegrasSQL(this.getId());

        return regras;
    }

    /**Método que retorna as regras no formato natural (não SQl) referentes a 
     * um determinado usuário da federação.
     *
     * @return List-{@link PolicyManager}- lista de regras em formato natural 
     * (não SQL) referentes a um determinado usuário.
     */
    public List<PolicyManager> obterRegras() {

        List<PolicyManager> regras;
        Database db = new Database();

        regras = db.obterRegrasUsr(this.getId());

        return regras;
    }

    /**Método que realiza a primeira configuração de um novo usuário criado.
     * O atributo permissão é setado como norma, padrão para todos os novos 
     * usuários e a regra a.proprietário = 'nomeDoUsuário' é adicionada,
     * para que ele possa ver todos os arquivos na qual ele é proprietário.
     *
     * @throws SQLException
     */
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

    /**Método para exclusão de um usuário do sistema. Todas as dependencias
     * deste usuário, como seus atributos, também são excluídos.
     *
     */
    public void deletaUsuario() {

        Database db = new Database();

        db.deletarRegraUsuario(this.getId());
        db.deletarAtributoUsuario(this.getId());
        db.deletarArquivoUsuario(this.getId());
        db.deletarUsuario(this.getId());

    }

    /**Método para verificação da existência de um usuário. Como não pode
     * haver dois usuários cadastrados com o mesmo nome, este método verifica
     * se já existe algum com o mesmo nome.
     *
     * @return True se já existir um usuário com este nome e falso caso 
     * contrário.
     */
    public boolean existeUsuario() {

        Boolean existe;

        Database db = new Database();

        existe = db.verificaUsuario(this.getNome());

        return existe;

    }
    
    /**Método que atualiza a cache de arquivos que um usuário pode vizualizar.
     * Este método deve ser chamado sempre que a cache fique inconsistente,
     * ou seja, uma nova regra seja adicionada para um usuário, ou então um
     * valor de atributo seja modificado ou excluido.
     *
     */
    public void atualizaArquivosUsuario(){
        
        Database novo = new Database();
        novo.deletarArquivoUsuario(this.getId());
    
        this.setNome(this.getId());
        PDP autorizacao = new PDP();
        try{
           autorizacao.verificaArquivos(this);
        
        } catch (SQLException e) {
            /*e.printStackTrace();*/
	}
    
    
    }

}
