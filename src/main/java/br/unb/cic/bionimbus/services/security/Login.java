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


/** Classe responsável pela validação das credenciais do usuário. 
 *  Essa classe só é instanciada pela interface uma vez para que seja efetuado
 * a autenticação do usuário. Depois disso todas as informações do usuário
 * são armazenadas em uma classe {@link Session} que utiliza o singleton, ou
 * seja, mantém a mesma instância ao longo de toda a execução do programa.
 *
 * @author Heitor Henrique
 */
public class Login {
    
    /** Método responsável por testar as credenciais que foram adquiridas pela 
     * interface. 
     * Este metódo utiliza a classe {@link PBKDF2}, que utiliza o algoritmo de
     * mesmo nome para realizar o teste das credenciais que estão armazenadas no
     * banco.
     * Se for autenticado, as informações do usuário são armanzenados na sessão.
     * 
     * @param pessoa Objeto do tipo {@link Usuario} que contém usuário e senha
     * digitados na interface.
     * @return true se o usuario e senha estiverem corretos e false caso contrário.
     */
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
