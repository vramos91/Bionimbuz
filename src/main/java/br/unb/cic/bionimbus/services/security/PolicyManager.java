/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.unb.cic.bionimbus.services.security;

import br.unb.cic.bionimbus.services.security.utilities.Database;
import br.unb.cic.bionimbus.services.security.attribute.Atributo;
import br.unb.cic.bionimbus.services.security.attribute.AtributoArquivo;
import br.unb.cic.bionimbus.services.security.attribute.AtributoUsuario;
import br.unb.cic.bionimbus.services.security.entities.Arquivo;
import br.unb.cic.bionimbus.services.security.entities.Usuario;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author heitor
 */
public class PolicyManager {

    private Integer id;
    private final String base = "SELECT u.nome, u.identificador FROM tabela1 AS u,tabela2 AS f, tabela3 AS a WHERE u.id1 = f.id2 AND a.id3 = f.id4 AND";
    private String sql;
    

    public void setSQL(String regra) {
        this.sql = regra;
    }

    public String getSQL() {
        return sql;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
  
    public List<PolicyManager> selectRegras() {

        List<PolicyManager> todasRegras = null;
        Database seleciona = new Database();
        try {
            todasRegras = seleciona.selectTodasRegras();
        } catch (SQLException erro) {
            System.out.println("Não foi possível realizar a operação");
        }
        return (todasRegras);

    }

    public void atribuiRegraUsr(Usuario usr, PolicyManager regra) {

        Database novo = new Database();
        novo.atribuirRegraUsr(usr, regra);
        
        usr.atualizaArquivosUsuario();
       
    }

    public void atribuiRegraArq(Arquivo usr, PolicyManager regra) {

        Database novo = new Database();
        novo.atribuirRegraArq(usr, regra);
        
        PDP autorizacao = new PDP();
        autorizacao.atualizaArquivosUsuarios();

    }
    
    public void deletaRegra(){
    
        Database db = new Database();
        
        db.deletarRegraDeUsuario(this.getId());
        db.deletarRegraDeArquivo(this.getId());
        db.deletarRegraSQL(this.getId());
        db.deletarRegra(this.getId());
        
        PDP autorizacao = new PDP();
        autorizacao.atualizaArquivosUsuarios();
    
    }
    
    public void removeRegraUsr(Usuario usr){
    
        Database novo = new Database();
        novo.removerRegraUsr(usr.getId(), this.getId());
        
        usr.atualizaArquivosUsuario();
        
    
    }
    
    public void removeRegraArq(Arquivo arq){
    
        Database novo = new Database();
        novo.removerRegraArq(arq.getId(), this.getId());
        
        PDP autorizacao = new PDP();
        autorizacao.atualizaArquivosUsuarios();
    
    }

    private void atualizaRegra(String tipo) {

        Integer opc;
        String identificador = "";
        String tab1 = "";
        String tab2 = "";
        String tab3 = "";
        String id1 = "";
        String id2 = "";
        String id3 = "";
        String id4 = "";
        
        if (tipo.equals("u")){
            opc = 1;
        }else {
            opc = 2;
        }
        switch (opc) {
            case 1:
                identificador = "idusuario";
                tab1 = "usuario";
                tab2 = "usuario_has_atributo";
                tab3 = "atributo_usuario";
                id1 = "idusuario";
                id2 = "usuario_idusuario";
                id3 = "idatributo";
                id4 = "atributo_idatributo";
                break;
            case 2:
                identificador = "idarquivo";
                tab1 = "arquivo";
                tab2 = "arquivo_has_atributo";
                tab3 = "atributo_arquivo";
                id1 = "idarquivo";
                id2 = "arquivo_idarquivo";
                id3 = "idatributo_arquivo";
                id4 = "idatributo";
                break;
        }
        sql = sql.replace("identificador", identificador);
        sql = sql.replace("tabela1", tab1);
        sql = sql.replace("tabela2", tab2);
        sql = sql.replace("tabela3", tab3);
        sql = sql.replace("id1", id1);
        sql = sql.replace("id2", id2);
        sql = sql.replace("id3", id3);
        sql = sql.replace("id4", id4);

    }

    public void criaRegra(String regra) {

        String[] result;

        Integer i;
        Integer num;
        Integer opc;
        boolean valido = true;
        Atributo atributo = null;

        result = regra.split("[ .]");
        Database novo = new Database();
        novo.novaRegra(regra);
        num = novo.ultRegra();
        this.setId(num);
  

        sql = base;
        for (i = 0; i < result.length; i++) {
            opc = i % 5;
            switch (opc) {
                case 0:
                    //System.out.println(result[i]);
                    atualizaRegra(result[i]);
                    if (result[i].equals("u")){
                        atributo = new AtributoUsuario();
                    }
                    else if(result[i].equals("a")){
                        atributo = new AtributoArquivo();
                    }
                    break;
                case 1:
                    //System.out.println(result[i]); 
                    atributo.setNome(result[i]);
                    valido = atributo.verificaAtributo();
                    sql = sql.concat(" a.nome = '" + result[i] + "'");
                    break;
                case 2:
                    //System.out.println(result[i]);
                    sql = sql.concat(" AND valor " + result[i] + " ");
                    break;
                case 3:
                    sql = sql.concat("'" + result[i] + "'");
                    // System.out.println(result[i]);
                    sql = sql.concat(";");
                   // System.out.println(sql);

                    if(valido){
                        novo.regraSQL(num, sql);
                    }
                    sql = base;
                    break;
                case 4:
                    
                    if (valido){
                        novo.updateRegra(result[i], novo.ultRegraSQL());
                    }
                    //System.out.println(result[i]);   
                    break;
            }
            if (!valido){
                System.out.println("\n**Digite um atributo valido para a regra**\n");
                this.setId(num);
                this.deletaRegra();
                break;
            }

        }

    }
}
