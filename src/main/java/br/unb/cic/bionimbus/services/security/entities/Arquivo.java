/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.unb.cic.bionimbus.services.security.entities;

import br.unb.cic.bionimbus.services.security.PDP;
import br.unb.cic.bionimbus.services.security.PolicyManager;
import br.unb.cic.bionimbus.services.security.attribute.AtributoArquivo;
import br.unb.cic.bionimbus.services.security.utilities.Database;
import br.unb.cic.bionimbus.services.security.utilities.Session;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

/**Classe que serve como abstração para o trabalho com os arquivos do sistema.
 * Esses arquivos não necessariamente são os arquivos do tipo File do java,
 * mas sim, uma abstração para que se torne mais fácil o trabalho de definição 
 * de atributos e regras sobre os arquivos.
 *
 * @author Heitor Henrique
 */
public class Arquivo {
    
    private String nome;
    private Integer id;
    
    /**Construtor padrão que apenas define o valores como nulos.
     */
    public Arquivo(){
        this.id = null;
        this.nome = null;
    }
    
    /** Construtor que já define um Id para o arquivo a ser criado.
     *
     * @param id Id do arquivo que será criado.
     */
    public Arquivo(Integer id){
        this.id = id;
    }
    
    /** Construtor que define um arquivo do sistema em função de um arquivo
     * real passado como argumento.
     *
     * @param arq Arquivo que se deseja adicionar ao sistema.
     */
    public Arquivo(File arq){
        this.cadastraArquivo(arq);
    }
    
    public void setNome(String nome){
        
        this.nome = nome;
    }
    public String getNome(){
        return nome;
    }
     public void setId(int id){
        this.id = id;
    }
    public int getId(){
        return id;
    }
    public Integer getId(String nome) {
        Database db = new Database();
        this.setId(db.selectIdArq(nome));
        return id;
    }
    
    /**Método de configuração dos arquivos recém criados. Todo
     * arquivo que é criado ele passa por uma configuração inicial que consiste
     * na inserção de alguns atributos automaticamente, como por exemplo o tipo
     * aberto ou fechado, seu tamanho e proprietário.
     *
     **/
    private void configura(File arq){
    
        this.getId(this.getNome());
        
        AtributoArquivo atributo = new AtributoArquivo();
        atributo.getId("tipo");
        atributo.setValor("fechado");
        atributo.cadastraAtributoNoArquivo(this, atributo);
        
        atributo.getId("tamanho");
        atributo.setValor(Long.toString(arq.length()));
        atributo.cadastraAtributoNoArquivo(this, atributo);
        
        atributo.getId("proprietario");
        Session sessao = Session.getInstance();
        atributo.setValor(sessao.getNome());
        atributo.cadastraAtributoNoArquivo(this, atributo);
        
    
    }
    
    /**Método para o cadastro de arquivos no sistema.
     * Todo arquivo na federação deve passar por este cadastro, para que seja
     * reconhecido como um arquivo válido. Assim que é cadastrado ele também
     * é configurado com alguns atributos automaticamente.
     *
    **/
    private void cadastraArquivo(File arq){
    
        Arquivo novoArquivo = new Arquivo();
        novoArquivo.setNome(arq.getName());
        
        Database inserir = new Database();
        inserir.insereArquivo(novoArquivo);
        
        novoArquivo.configura(arq);
        
        PDP autorizacao = new PDP();
        autorizacao.atualizaArquivosUsuarios();
    
    }
    
    /**Método que seleciona todos os arquivos que pertencem atualmente à 
     * federação.
     *
     * @return List-{@link Arquivo}- Lista de todos arquivos da federação.
     */
    public List<Arquivo> selectArquivos(){
        
        List<Arquivo> todosArquivos = null;
        Database seleciona = new Database();
        try{
            todosArquivos = seleciona.selectTodosArquivos();
        }
        catch (SQLException erro){
            System.out.println("Não foi possível realizar a operação");
        }
        return (todosArquivos);
    }
    
    /**Método que retorna todos as regras em formato SQL que atualmente estão
     * associadas a um determinado arquivo.
     *
     * @param db Conexão já aberta de um banco de dados.
     * @return List-{@link PolicyManager}- Lista de todas regras de determinado
     * arquivo.
     */
    public List<PolicyManager> obterRegrasSQL(Database db){
    
        List<PolicyManager> regras;
        
        
        regras = db.obterRegrasSQLArq(this.getId());

        return regras;
    }
    
    /**Método que exclui o cadastro de um arquivo do sistema. Este método
     * deve ser chamado sempre que um arquivo deixa de fazer parte da federação,
     * pois só assim o serviço de segurança saberá da exclusão do arquivo.
     *
     */
    public void deletaArquivo(){
    
        Database db = new Database();
        
        db.deletarRegraArquivo(this.getId());
        db.deletarAtributoArquivo(this.getId());
        db.deletarUsuarioArquivo(this.getId());
        db.deletarArquivo(this.getId());
    
    }
    
    /**Método que retorna a lista de regras, em linguagem natural (não SQL),
     * que estão associados a um arquivo.
     *
     * @return List-{@link PolicyManager}- Lista de todas regras de um arquivo 
     * em linguagem natural (não SQL).
     */
    public List<PolicyManager> obterRegras(){
    
        List<PolicyManager> regras;
        Database db = new Database();
        
        regras = db.obterRegrasArq(this.getId());

        return regras;
    }
    
    
    @Override
    public int hashCode(){
        
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.nome);
        buffer.append(this.id);
        return buffer.toString().hashCode();
        
    }
    @Override
    public boolean equals(Object object){
        
        if (object == null) return false;
        if (object == this) return true;
        if (this.getClass() != object.getClass())return false;
        Arquivo arq = (Arquivo)object;
        if(this.hashCode()== arq.hashCode())return true;
        return false;
        
    }   
}
