/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.unb.cic.bionimbus.services.security.utilities;

/**Classe que faz a sessão assim que o usuário loga no sistema. Esta classe
 * utiliza o padrão singleton, que faz com que ela seja instanciada apenas 
 * umas vez durante toda a execução do programa, que é justamente no momento
 * do login. Na sessão, são mantidos os dados do usuário como nome e id para
 * que outros serviços possam utilizar.
 * 
 *
 * @author Heitor Henrique
 */
public class Session {
    
    private String nome;
    private String senha;
    private Integer id;
    private static Session INSTANCIA = null;
    
    
    private Session(){
    
    }
    
    /**Método que retorna a instância atual em execução no programa, e caso
     * seja nula cria uma nova.
     *
     * @return Instância atual da sessão de usuário.
     */
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
