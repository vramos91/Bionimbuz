/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.unb.cic.bionimbus.services.security.attribute;

import br.unb.cic.bionimbus.services.security.utilities.Database;
import br.unb.cic.bionimbus.services.security.entities.Usuario;
import java.sql.SQLException;
import java.util.List;

/**Classe que gerencia específicamente os atributos relacionados aos usuários.
 * Esta classe extende a classe {@link Atributo}, assim como qualquer classe
 * que queira trabalhar com atributos no sistema.
 * 
 *
 * @author Heitor Henrique
 */
public class AtributoUsuario extends Atributo {
    
    /**Método que seta o Id de um atributo cujo nome foi passado como 
     * parâmetro.
     * Útil quando se acaba de criar um novo atributo e se tem apenas o nome
     * dele e se deseja adicionar também o Id.
     *
     * @param nome String com o nome do atributo que se deseja adicionar o Id.
     */
    public void setId(String nome) {
        Database db = new Database();
        this.setId(db.selectIdAtributoUsr(nome));
    }
    
    /**Metodo que lista todos os atributos de usuário que estao atualmente
     * cadastrados.
     * 
     * Este metodo pesquisa no banco de dados todos os atibutos de usuário
     * que o administrador ja cadastrou, ou os que ja foram cadastrados
     * automaticamente.
     *
     * @return List-AtributoUsuario- Lista de todos atributos de usuario.
     */
    public List<AtributoUsuario> selectAtributos(){
    
        List<AtributoUsuario> todosAtributos = null;
        Database seleciona = new Database();
        try{
            todosAtributos = seleciona.selectTodosAtributos();
        }
        catch (SQLException erro){
            System.out.println("Não foi possível realizar a operação");
        }
        return (todosAtributos);
    
    } 
    
    /**Metodo que atribui um valor de um atributo a um determinado usuário.
     * Recebe como parâmetro um objeto {@link Usuario} e um objeto 
     * {@link AtributoUsuario} ambos com os devidos IDs setados e o atributo
     * contendo o valor também adicionado.
     *
     * @param pessoa Usuario que se deseja adicionar o atributo com o ID setado. 
     * @param atributo Atributo que se deseja associar ao usuario.
     */
    
    public void cadastraAtributoUsuario (Usuario pessoa, AtributoUsuario atributo){
    
        Database novo = new Database();
        novo.insereAtributoUsuario(pessoa,atributo);
        
        pessoa.atualizaArquivosUsuario();
    
    }
    
     /**Método que deleta um atributo do sistema. 
     * Basta instanciar um objeto desta classe, setar o ID do atributo que
     * se deseja excluir e chamar este método.
     *
     */
    
    public void deletaAtributo(){
    
        Database db = new Database();
        
        db.deletarAtributoDeUsuario(this.getId());
        db.deletarAtributoUsr(this.getId());
    
    }
    
     /**Método que altera o valor de um atributo já associado a algum usuário.
     * Basta passar como parâmtro o usuário que se deseja alterar,
     * que o atributo será alterado com os valores do atributo que chamou
     * este método.
     *
     * @param pessoa Arquivo cujo atributo deve ser alterado.
     */
    
    public void alteraAtributoUsuario (Usuario pessoa) {
    
        Database novo = new Database();
        novo.alterarAtributoUsuario(pessoa,this);
        
        pessoa.atualizaArquivosUsuario();
     
    }
    
  
    @Override
    public void cadastrarAtributo (Atributo att){
    
        Database novoatt = new Database();
        novoatt.insereAtributo(att);
    }
    
    @Override
    public boolean verificaAtributo(){
        
        Database db = new Database();
        return db.verificaAtributoUsr(this.getNome());
        
    }
    
    
}
