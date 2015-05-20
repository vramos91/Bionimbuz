/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.unb.cic.bionimbus.services.security.utilities;

/**
 *
 * @author heitor
 */
public class Session {
    
    private String nome;
    private String senha;
    private Integer id;
    private static Session INSTANCIA = null;
    
    private Session(){
    
    }
    
    public static synchronized Session getInstance(){
    
        if (INSTANCIA == null){
            INSTANCIA = new Session();
        }
        return INSTANCIA;
    }
    
    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    
    
    
    
    
}
