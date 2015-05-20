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

/**
 *
 * @author heitor
 */
public class AtributoUsuario extends Atributo {
    
    public void setId(String nome) {
        Database db = new Database();
        this.setId(db.selectIdAtributoUsr(nome));
    }
    
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
    
    public void cadastraAtributoUsuario (Usuario pessoa, AtributoUsuario atributo){
    
        Database novo = new Database();
        novo.insereAtributoUsuario(pessoa,atributo);
        
        pessoa.atualizaArquivosUsuario();
    
    }
    
    public void deletaAtributo(){
    
        Database db = new Database();
        
        db.deletarAtributoDeUsuario(this.getId());
        db.deletarAtributoUsr(this.getId());
    
    }
    public void alteraAtributoUsuario (Usuario pessoa) {
    
        Database novo = new Database();
        novo.alterarAtributoUsuario(pessoa,this);
        
        pessoa.atualizaArquivosUsuario();
     
    }
    
    /**
     *
     * @param att
     */
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
