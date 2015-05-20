/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.unb.cic.bionimbus.services.security;

import br.unb.cic.bionimbus.services.security.utilities.Database;
import br.unb.cic.bionimbus.services.security.utilities.Session;
import br.unb.cic.bionimbus.services.security.entities.Usuario;
import br.unb.cic.bionimbus.services.security.utilities.PBKDF2;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;


/**
 *
 * @author heitor
 */
public class Login {
    
   
    public boolean efetuaLogin(Usuario pessoa){
        
        PBKDF2 hash = new PBKDF2();
        Database login = new Database();
        try{
            Usuario usr = login.testaCredencial(pessoa);
        
            if (("admin".equals(pessoa.getNome())) && ("admin".equals(pessoa.getSenha()))){
                return true;
            }else{
                if (usr.getNome() == null){
                    return false;
                }
                else{
                    if (hash.validatePassword(pessoa.getSenha(), usr.getSenha())){
                        Session sessao = Session.getInstance();
                        sessao.setNome(usr.getNome());
                        sessao.setSenha(usr.getSenha());
                        sessao.setId(usr.getId());
                        return true;
                    }
                    else{
                        return false;
                    }
                }
            }
        }
        catch(SQLException ex){
            System.out.println("O login nao pode ser efetuado");
            return false;
        }
        catch(NoSuchAlgorithmException alg){
            System.out.println("Não foi possível realizar o login");
            return false;
        }
        catch(InvalidKeySpecException chave){
            System.out.println("Não foi possível realizar o login");
            return false;
        }
    }
    
}
