/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.unb.cic.bionimbus.services.security.attribute;

/**
 *
 * @author heitor
 */
public abstract class Atributo {
    
    private String nome;
    private String valor;
    private String tipo;
    private Integer id;
    
    public String getNome(){
        return nome;
    }
    public String getValor(){
        return valor;
    }
     public String getTipo(){
        return tipo;
    }
    public void setNome(String nome1){
        nome = nome1; 
    }
    public void setValor(String valor1){
        valor = valor1;
    }
    public void setTipo(String tipo1){
        tipo = tipo1; 
    }
    public Integer getId() {
        return id;
    }
   
    public void setId(Integer id) {
        this.id = id;
    }
    
    public abstract void cadastrarAtributo (Atributo att);
    
       

    /**
     *
     * @return
     */
    public abstract boolean verificaAtributo();
    
}
