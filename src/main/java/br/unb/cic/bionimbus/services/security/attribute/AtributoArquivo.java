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

/**
 *
 * @author heitor
 */
public class AtributoArquivo extends Atributo {
    
    public Integer getId(String nome) {
        Database db = new Database();
        this.setId(db.selectIdAtributoArq(nome));
        return this.getId();
    }
    
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
    
    public void cadastraAtributoNoArquivo (Arquivo arq, AtributoArquivo atributo){
    
        Database novo = new Database();
        novo.insereAtributoNoArquivo(arq,atributo);
        
        PDP autorizacao = new PDP();
        autorizacao.atualizaArquivosUsuarios();
        
    
    }

    /**
     *
     * @param att
     */
    @Override
    public void cadastrarAtributo (Atributo att){
    
        Database novoatt = new Database();
        novoatt.insereAtributoDeArquivo(att);
    
    }
    public void deletaAtributo(){
    
        Database db = new Database();
        
        db.deletarAtributoDeArquivo(this.getId());
        db.deletarAtributoArq(this.getId());
    
    }
    
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
