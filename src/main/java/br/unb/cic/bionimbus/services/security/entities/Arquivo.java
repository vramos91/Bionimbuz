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
import java.io.File;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author heitor
 */
public class Arquivo {
    
    private String nome;
    private Integer id;
    
    public Arquivo(){
        this.id = null;
        this.nome = null;
    }
    
    /**
     *
     * @param id
     */
    public Arquivo(Integer id){
        this.id = id;
    }
    
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
    
    private void configura(){
    
        this.getId(this.getNome());
        
        AtributoArquivo atributo = new AtributoArquivo();
        atributo.getId("tipo");
        atributo.setValor("aberto");
        atributo.cadastraAtributoNoArquivo(this, atributo);
    
    }
    private void cadastraArquivo(File arq){
    
        Arquivo novoArquivo = new Arquivo();
        novoArquivo.setNome(arq.getName());
        
        Database inserir = new Database();
        inserir.insereArquivo(novoArquivo);
        
        novoArquivo.configura();
        
        PDP autorizacao = new PDP();
        autorizacao.atualizaArquivosUsuarios();
    
    }
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
    public List<PolicyManager> obterRegrasSQL(Database db){
    
        List<PolicyManager> regras;
        
        
        regras = db.obterRegrasSQLArq(this.getId());

        return regras;
    }
    
    public void deletaArquivo(){
    
        Database db = new Database();
        
        db.deletarRegraArquivo(this.getId());
        db.deletarAtributoArquivo(this.getId());
        db.deletarUsuarioArquivo(this.getId());
        db.deletarArquivo(this.getId());
    
    }
    
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
