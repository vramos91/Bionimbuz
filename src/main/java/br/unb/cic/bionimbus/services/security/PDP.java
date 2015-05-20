/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.bionimbus.services.security;

import br.unb.cic.bionimbus.services.security.utilities.Database;
import br.unb.cic.bionimbus.services.security.utilities.Session;
import br.unb.cic.bionimbus.services.security.entities.Arquivo;
import br.unb.cic.bionimbus.services.security.entities.Usuario;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author heitor
 */
public class PDP {

    public void verificaArquivos(Usuario usr ) throws SQLException {

        List<PolicyManager> regras;
        HashSet<Arquivo> arquivosFinal = new HashSet<Arquivo>();
        HashSet<Arquivo> arquivosTemp = new HashSet<Arquivo>();
        HashSet<Arquivo> arquivosTemp2 = new HashSet<Arquivo>();
        List<Arquivo> arquivoProcessado = new ArrayList<Arquivo>();
        Integer flag = 0;
        Integer i;
        

        regras = usr.obterRegrasSQL();
        
        

        Database db = new Database();
        db.getConnection2();

        for (i = 0; i < regras.size() - 1; i++) {
            //System.out.println(regras.get(i).getId() + regras.get(i).getSQL());
            arquivosTemp = db.executaRegra(regras.get(i).getSQL());
            if (regras.get(i).getId() != regras.get(i + 1).getId()) {
                if (flag == 1) {
                    arquivosTemp.retainAll(arquivosTemp2);
                    arquivosFinal.addAll(arquivosTemp);
                    arquivosTemp = new HashSet<Arquivo>();
                    arquivosTemp2 = new HashSet<Arquivo>();
                    flag = 0;
                } else {
                    arquivosFinal.addAll(arquivosTemp);
                    arquivosTemp = new HashSet<Arquivo>();
                }

            } else {
                if (flag == 0) {
                    arquivosTemp2.addAll(arquivosTemp);
                    arquivosTemp = new HashSet<Arquivo>();
                    flag = 1;
                } else {
                    arquivosTemp2.retainAll(arquivosTemp);
                    arquivosTemp = new HashSet<Arquivo>();
                }
            }

        }

        if (flag == 1) {
            //System.out.println(regras.get(i).getId() + regras.get(i).getSQL());
            arquivosTemp = db.executaRegra(regras.get(i).getSQL());
            arquivosTemp.retainAll(arquivosTemp2);
            arquivosFinal.addAll(arquivosTemp);
        } else {
            if (i != 0) {
               // System.out.println(regras.get(i).getId() + regras.get(i).getSQL());
                arquivosFinal.addAll(db.executaRegra(regras.get(i).getSQL()));
            }
        }
        if (regras.size() == 1){
            arquivosFinal = db.executaRegra(regras.get(0).getSQL());
        }
        
        
        for (Arquivo arquivo : arquivosFinal) {

            Arquivo arq = new Arquivo(arquivo.getId());
            regras = null;
            regras = arq.obterRegrasSQL(db);

            //System.out.println(arquivo.getNome() + arquivo.getId());
            //System.out.println(regras.size());
            if (regras.size() > 0 ){
                if (this.verificaUsuario(regras,db,usr)){
                    arquivoProcessado.add(arquivo);
                }  
            }
            else{
                arquivoProcessado.add(arquivo);
            }
        }
        
       
        for (Arquivo arq: arquivoProcessado){
            db.insereArquivoPessoa(arq.getId(),usr.getId());
        }
        db.closeConnnection2();
    }

    public boolean verificaUsuario(List<PolicyManager> reg, Database db, Usuario usr) throws SQLException {

        String query;
        Integer regAtual;
        Integer flag = 0;
        Boolean existe;
        Boolean existeTemp = false;
        Integer i;
        

        for (i = 0; i < reg.size() - 1; i++) {
            query = reg.get(i).getSQL();
            query = query.replace(";", " ");
            query = query.concat("AND u.nome = '" + usr.getNome() + "';");
            
            
            existe = db.executaRegraArq(query);

            if (reg.get(i).getId() != reg.get(i + 1).getId()) {
                if (flag == 1) {
                    flag = 0;
                    existeTemp = existe && existeTemp;
                    if (existeTemp == true) {
                        return true;
                    }
                } else {
                    if (existe == true) {
                        return true;
                    }
                }
            } else {
                if (flag == 0) {
                    existeTemp = existe;
                    flag = 1;
                } else {
                    existeTemp = existe && existeTemp;
                }

            }
        }
        if (i != 0){
            query = reg.get(i).getSQL();
            query = query.replace(";", " ");
            query = query.concat("AND u.nome = '" + usr.getNome() + "';");
            existe = db.executaRegraArq(query);
        
            if (flag == 1) {
                existeTemp = existe && existeTemp;
                if (existeTemp == true) {
                    return true;
                }
            } else {
                if (existe == true) {
                    return true;
                }
            }

        }
        if (reg.size() == 1){
            query = reg.get(0).getSQL();
            query = query.replace(";", " ");
            query = query.concat("AND u.nome = '" + usr.getNome() + "';");
            existe = db.executaRegraArq(query);
            
            if (existe == true){
                return true;
            }
        }
        
        return false;
    }
    
    public List<Arquivo> mostraArquivos() {
        List<Arquivo> arquivos;
        
        Database db = new Database();
        Session sessao = Session.getInstance();
        arquivos = db.selectArquivosUsr(sessao.getId());
        
        return arquivos;
    }
    
    public void atualizaArquivosUsuarios(){
        List<Usuario> lista; 
        
        Database novo = new Database();
        novo.deletarTodosArqUsr();
    
        try{
            lista = novo.selectTodasPessoas();
            PDP autorizacao = new PDP();
            
            for (Usuario usr : lista){
                autorizacao.verificaArquivos(usr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
	}
    
    
    }
}
