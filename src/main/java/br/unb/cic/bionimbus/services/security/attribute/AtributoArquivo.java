/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.unb.cic.bionimbus.services.security.attribute;

import br.unb.cic.bionimbus.services.security.utilities.Database;
import br.unb.cic.bionimbus.services.security.PDP;
import br.unb.cic.bionimbus.services.security.entities.Arquivo;
import java.sql.SQLException;
import java.util.List;

/**Classe que gerencia específicamente os atributos relacionados aos arquivos.
 * Esta classe extende a classe {@link Atributo}, assim como qualquer classe
 * que queira trabalhar com atributos no sistema.
 * 
 *
 * @author Heitor Henrique
 */
public class AtributoArquivo extends Atributo {
    
    /**Método que retorna o Id de um atributo cujo nome foi passado como 
     * parâmetro.
     * Útil quando se acaba de criar um novo atributo e se tem apenas o nome
     * dele e se deseja saber o Id.
     *
     * @param nome String com o nome do atributo que se deseja saber o Id.
     * @return Inteiro que representa o Id daquele atributo.
     */
    public Integer getId(String nome) {
        Database db = new Database();
        this.setId(db.selectIdAtributoArq(nome));
        return this.getId();
    }
    
    /**Metodo que lista todos os atributos de arquivos que estao atualmente
     * cadastrados.
     * 
     * Este metodo pesquisa no banco de dados todos os atibutos de arquivo
     * que o administrador ja cadastrou, ou os que ja foram cadastrados
     * automaticamente.
     *
     * @return List-AtributoArquivo- Lista de todos atributos de arquivo.
     */
    public List<AtributoArquivo> selectAtributos(){
    
        List<AtributoArquivo> todosAtributos = null;
        Database seleciona = new Database();
        try{
            todosAtributos = seleciona.selectTodosAtributosArq();
        }
        catch (SQLException erro){
            System.out.println("Não foi possível realizar a operação");
        }
        return (todosAtributos);
    
    }
    
    /**Metodo que atribui um valor de um atributo a um determinado arquivo.
     * Recebe como parâmetro um objeto {@link Arquivo} e um objeto 
     * {@link AtributoArquivo} ambos com os devidos IDs setados e o atributo
     * contendo o valor também adicionado.
     *
     * @param arq Arquivo que se deseja adicionar o atributo com o ID setado. 
     * @param atributo Atributo que se deseja associar ao arquivo.
     */
    public void cadastraAtributoNoArquivo (Arquivo arq, AtributoArquivo atributo){
    
        Database novo = new Database();
        novo.insereAtributoNoArquivo(arq,atributo);
        
        PDP autorizacao = new PDP();
        autorizacao.atualizaArquivosUsuarios();
        
    
    }

    
    @Override
    public void cadastrarAtributo (Atributo att){
    
        Database novoatt = new Database();
        novoatt.insereAtributoDeArquivo(att);
    
    }

    /**Método que deleta um atributo do sistema. 
     * Basta instanciar um objeto desta classe, setar o ID do atributo que
     * se deseja excluir e chamar este método.
     *
     */
    public void deletaAtributo(){
    
        Database db = new Database();
        
        db.deletarAtributoDeArquivo(this.getId());
        db.deletarAtributoArq(this.getId());
    
    }
    
    /**Método que altera o valor de um atributo já associado a algum arquivo.
     * Basta passar como parâmetro o arquivo que se deseja alterar ,
     * que o atributo será alterado com os valores do atributo que chamou 
     * este método.
     *
     * @param arq Arquivo cujo atributo deve ser alterado.
     */
    public void alteraAtributoNoArquivo (Arquivo arq){
    
        Database novo = new Database();
        novo.alterarAtributoNoArquivo(arq,this);
        
        PDP autorizacao = new PDP();
        autorizacao.atualizaArquivosUsuarios();
    
    }
    
    @Override
    public boolean verificaAtributo(){
        
        Database db = new Database();
        return db.verificaAtributoArq(this.getNome());
    }
    
    
   
}
