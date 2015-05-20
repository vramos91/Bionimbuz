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
public class AtributoSistema extends Atributo {
    
    @Override
    public boolean verificaAtributo(){
    
        return true;
    }
    
    /**
     *
     * @param att
     */
    @Override
    public void cadastrarAtributo(Atributo att){
    
    }
}
