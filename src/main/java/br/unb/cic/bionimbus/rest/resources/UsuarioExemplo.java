/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.bionimbus.rest.resources;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
class UsuarioExemplo {
    
    private final String name;
    
    public UsuarioExemplo(String name){
        this.name = name;
    }
}
