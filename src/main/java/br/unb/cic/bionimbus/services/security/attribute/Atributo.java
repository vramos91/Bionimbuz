/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.unb.cic.bionimbus.services.security.attribute;

/**Classe abstrata que contém os métodos de gerenciamento dos atributos do
 * sistema. Atributos são características que são associadas aos objetos 
 * do sistema, e podem ser agrupadas em grupos maiores.
 * 
 * As classes que extendem atualmente são {@link AtributoArquivo} 
 * e {@link AtributoUsuario}.  Quaisquer outros tipos de atributos que possam
 * vir a ser adicionados no futuro devem também extender esta classe. Todo
 * atributo possui um nome, valor, tipo e id. 
 * 
 *
 * @author Heitor Henrique
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
    
    /**Método abstrato para o cadastro de atributos. Tanto atributos de usuário
     * como atributos de arquivo, ou quaisquer outros atributos futuros, devem
     * implementar este método para que os atributos sejam cadastrados 
     * em suas devidas tabelas corretamente.
     *
     * @param att Atributo que será cadastrado no banco. 
     */
    public abstract void cadastrarAtributo (Atributo att);
    
       

    /**Método que verifica se um determinado atributo existe, ou seja, se ele
     * já foi previamente cadastrado.
     * 
     * Cada classe deve implementar a sua própria verificação de atributo, 
     * pois eles são cadastrados em tabelas diferentes no banco de dados.
     *
     * @return
     */
    public abstract boolean verificaAtributo();
    
}
