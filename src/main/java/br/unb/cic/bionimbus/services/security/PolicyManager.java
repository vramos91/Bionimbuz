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

/** Classe que gerencia a criação e manutenção de todas as políticas do sistema.
 * Ela contém os métodos utilizados para a criação de todas as políticas, 
 * seguindo os padrões que serão melhores descritos nos métodos apropriados.
 * Bom lembrar que apenas o administrador deve ter acesso a esta classe,
 * pois não queremos que usuários modifiquem as regras.
 *
 * @author Heitor Henrique
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
    
    /**Métodos que retorna todas as regras atualmente criadas no sistema. 
     * Para isto, é feita uma pesquisa no banco de dados que retorna todas
     * as regras constantes na tabela 'regras' presente no banco de dados.
     *
     * @return List-PolicyManager- Lista de todas as regras que foram criadas
     * no sistema.
     */
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

    /**Método que atribui uma determinada regra a um determinado usuário.
     * Recebendo como parâmetros dois objetos, um do tipo {@link Usuario} e 
     * outro do tipo {@link PolicyManager}, cada um com seus devidos Ids setados,
     * eles são inseridos no banco de dados.
     *
     * @param usr Objeto {@link Usuario} com o Id do usuário já setado.
     * @param regra Objeto {@link PolicyManager} com o Id da regra setada.
     */
    public void atribuiRegraUsr(Usuario usr, PolicyManager regra) {

        Database novo = new Database();
        novo.atribuirRegraUsr(usr, regra);
        
        usr.atualizaArquivosUsuario();
       
    }

    /**Método que atribui uma determinada regra a um determinado arquivo.
     * Recebendo como parâmetros dois objetos, um do tipo {@link Arquivo} e 
     * outro do tipo {@link PolicyManager}, cada um com seus devidos Ids setados,
     * eles são inseridos no banco de dados.
     * 
     * @param arq Objeto {@link Arquivo} com o Id setado.
     * @param regra Objeto {@link PolicyManager} com Id da regra setado.
     */
    public void atribuiRegraArq(Arquivo arq, PolicyManager regra) {

        Database novo = new Database();
        novo.atribuirRegraArq(arq, regra);
        
        PDP autorizacao = new PDP();
        autorizacao.atualizaArquivosUsuarios();

    }
    
    /**Método responsável pela exclusão de regras do sistema.
     * Para deletar uma regra basta instanciar um objeto desta classe,
     * setar o Id para o qual se deseja excluir a regra e chamar este método.
     * Todas as dependencias dessa regra também serão excluidas, ou seja,
     * se a regra está vinculada a algum usuário ou arquivo, esse vínculo será
     * excluido.
     *
     */
    public void deletaRegra(){
    
        Database db = new Database();
        
        db.deletarRegraDeUsuario(this.getId());
        db.deletarRegraDeArquivo(this.getId());
        db.deletarRegraSQL(this.getId());
        db.deletarRegra(this.getId());
        
        PDP autorizacao = new PDP();
        autorizacao.atualizaArquivosUsuarios();
    
    }
    
    /**Método responsável por retirar a associação de uma regra a um determinado
     * usuário. 
     * Para realizar a operação basta instanciar um objeto desta classe,
     * setar com o id da regra que deseja retirar, e chamar este método,
     * passando como parâmetro um objeto do tipo {@link Usuario} com o Id
     * setado.
     *
     * @param usr Objeto {@link Usuario} com o Id setado.
     */
    public void removeRegraUsr(Usuario usr){
    
        Database novo = new Database();
        novo.removerRegraUsr(usr.getId(), this.getId());
        
        usr.atualizaArquivosUsuario();
        
    
    }
    /**Método responsável por retirar a associação de uma regra a um determinado
     * arquivo. 
     * Para realizar a operação basta instanciar um objeto desta classe,
     * setar com o id da regra que deseja retirar, e chamar este método,
     * passando como parâmetro um objeto do tipo {@link Arquivo} com o Id
     * setado.
     *
     * @param arq Objeto {@link Arquivo} com o Id setado.
     */
    public void removeRegraArq(Arquivo arq){
    
        Database novo = new Database();
        novo.removerRegraArq(arq.getId(), this.getId());
        
        PDP autorizacao = new PDP();
        autorizacao.atualizaArquivosUsuarios();
    
    }
    /** Método privado que é chamado pelo método criaRegra() para identificar
     * os parametros corretos da regra a substituí-los.
     * Este método que diferencia uma regra criada para um arquivo ou para um
     * usuário, substituindo os nomes das tabelas nos lugares corretos da 
     * consulta SQL.
     *
     **/

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

    /**Método responsável pela criação da regra de acesso no sistema.
     * Ele recebe como parâmetro a regra que foi escrita pelo administrador
     * e realiza algumas operações a fim de deixá-la em formato SQL para que 
     * possa ser feita uma consulta ao banco de forma correta. Caso a regra
     * possua operadores lógicos AND ou OR ela é subdividade em mais de uma
     * regra para que cada uma seja processada de forma separada. 
     * Uma verificação também é feita na regra para que seja garantido que 
     * o atributo presente na regra realmente exista.
     * 
     * Ex: u.cargo = gerente AND u.permissao = normal 
     * 
     * Esta regra nos diz que somente usuários que tenham cargo de gerente E
     * permissao normal podem acessar os arquivos que forem associados a ela.
     * 
     * a.tipo = aberto AND a.extensao = java
     * 
     * Esta regra nos diz que somente arquivos do tipo aberto E extensão java
     * podem ser acessados pelo usuário associado a ela.
     *
     * @param regra Regra que foi escrita pelo administrador pela interface.
     */
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
