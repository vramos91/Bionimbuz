/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package br.unb.cic.bionimbus.services.security.utilities;
import br.unb.cic.bionimbus.services.security.PolicyManager;
import br.unb.cic.bionimbus.services.security.attribute.Atributo;
import br.unb.cic.bionimbus.services.security.attribute.AtributoArquivo;
import br.unb.cic.bionimbus.services.security.attribute.AtributoUsuario;
import br.unb.cic.bionimbus.services.security.entities.Arquivo;
import br.unb.cic.bionimbus.services.security.entities.Usuario;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/**
 *
 * @author heitor
 */
public class Database {
    
    Connection conexao = null;
    
    private boolean retorno;
    private final String INSERT_PESSOA = "INSERT INTO usuario (nome,senha) VALUES (?,?)";
    private final String INSERT_ARQUIVO = "INSERT INTO arquivo (nome) VALUES (?)";
    private final String INSERT_ATRIBUTO_USR = "INSERT INTO atributo_usuario (nome,tipo) VALUES (?,'usuario')";
    private final String INSERT_ATRIBUTO_ARQ = "INSERT INTO atributo_arquivo (nome,tipo) VALUES (?,'arquivo')";
    private final String SELECT_PESSOA = "SELECT * FROM usuario WHERE nome=?";
    private final String SELECT_TODAS_PESSOAS = "SELECT idusuario,nome FROM usuario";
    private final String SELECT_TODOS_ARQ = "SELECT * FROM arquivo";
    private final String SELECT_ATT_USR = "SELECT idatributo,nome FROM atributo_usuario";
    private final String INSERT_ATT_USR = "INSERT INTO usuario_has_atributo (usuario_idusuario,atributo_idatributo,valor) VALUES (?,?,?)";
    private final String SELECT_ATT_ARQ = "SELECT idatributo_arquivo,nome FROM atributo_arquivo";
    private final String INSERT_ATT_ARQ = "INSERT INTO arquivo_has_atributo (arquivo_idarquivo,idatributo,valor) VALUES (?,?,?)";
    private final String INSERT_REGRA = "INSERT INTO regras (regra) VALUES (?)";
    private final String SELECT_ULTREGRA = "SELECT MAX(idregra) FROM regras";
    private final String INSERT_REGRA_SQL = "INSERT INTO regrasql (descricao,tipo,regra_idregra) VALUES (?,?,?)";
    private final String SELECT_ULTREGRASQL = "SELECT MAX(idsql) FROM regrasql";
    private final String UPDATE_REGRA_SQL = "UPDATE regrasql SET tipo = ? WHERE idsql=?";
    private final String SELECT_TODAS_REGRAS = "SELECT idregra,regra FROM regras";
    private final String INSERT_REGRA_USR = "INSERT INTO regras_has_usuario (regras_idregra,usuario_idusuario) VALUES (?,?)";
    private final String INSERT_REGRA_ARQ = "INSERT INTO regras_has_arquivo (regras_idregra,arquivo_idarquivo) VALUES (?,?)";
    private final String SELECT_REGRASQL_USR = "SELECT descricao, regra_idregra FROM regrasql,regras,regras_has_usuario WHERE idregra = regra_idregra AND idregra=regras_idregra AND usuario_idusuario = ?";
    private final String SELECT_REGRAS_USR = "SELECT idregra,regra FROM regras,regras_has_usuario,usuario WHERE idregra=regras_idregra AND usuario_idusuario = idusuario AND usuario_idusuario = ?";
    private final String SELECT_REGRASQL_ARQ = "SELECT descricao, regra_idregra FROM regrasql,regras,regras_has_arquivo WHERE idregra = regra_idregra AND idregra=regras_idregra AND arquivo_idarquivo = ?";
    private final String SELECT_REGRAS_ARQ = "SELECT idregra,regra FROM regras,regras_has_arquivo,arquivo WHERE idregra=regras_idregra AND idarquivo = arquivo_idarquivo AND arquivo_idarquivo = ?";
    private final String SELECT_ID_USR = "SELECT idusuario FROM usuario WHERE nome=?";
    private final String SELECT_ID_ARQ = "SELECT idarquivo FROM arquivo WHERE nome=?";
    private final String SELECT_ID_ATT_USR = "SELECT idatributo FROM atributo_usuario WHERE nome=?";
    private final String SELECT_ID_ATT_ARQ =  "SELECT idatributo_arquivo FROM atributo_arquivo WHERE nome=?";
    private final String DELETE_USUARIO =  "DELETE FROM usuario WHERE idusuario=?";
    private final String DELETE_REGRA_USR =  "DELETE FROM regras_has_usuario WHERE usuario_idusuario=?";
    private final String DELETE_ATT_USR =  "DELETE FROM usuario_has_atributo WHERE usuario_idusuario=?";
    private final String DELETE_REGRA_DE_USR =  "DELETE FROM regras_has_usuario WHERE regras_idregra=?";
    private final String DELETE_REGRA_DE_ARQ =  "DELETE FROM regras_has_arquivo WHERE regras_idregra=?";
    private final String DELETE_REGRA_SQL =  "DELETE FROM regrasql WHERE regra_idregra=?";
    private final String DELETE_REGRA =  "DELETE FROM regras WHERE idregra=?";
    private final String DELETE_REGRA_ARQ =  "DELETE FROM regras_has_arquivo WHERE arquivo_idarquivo=?"; 
    private final String DELETE_ATT_ARQ =  "DELETE FROM arquivo_has_atributo WHERE arquivo_idarquivo=?";
    private final String DELETE_ARQUIVO =  "DELETE FROM arquivo WHERE idarquivo=?";
    private final String DELETE_ATT_DE_USR =  "DELETE FROM usuario_has_atributo WHERE atributo_idatributo=?";
    private final String DELETE_ATRIBUTO_USR =  "DELETE FROM atributo_usuario WHERE idatributo=?";
    private final String DELETE_ATT_DE_ARQ =  "DELETE FROM arquivo_has_atributo WHERE idatributo=?";
    private final String DELETE_ATRIBUTO_ARQ =  "DELETE FROM atributo_arquivo WHERE idatributo_arquivo=?";
    private final String UPDATE_ATT_USR = "UPDATE usuario_has_atributo SET valor = ? WHERE atributo_idatributo=? AND usuario_idusuario=?";
    private final String UPDATE_ATT_ARQ = "UPDATE arquivo_has_atributo SET valor = ? WHERE idatributo=? AND arquivo_idarquivo=?";
    private final String SELECT_USR = "SELECT nome FROM usuario WHERE nome=?";
    private final String SELECT_ATT_1USR = "SELECT nome FROM atributo_usuario WHERE nome=?";
    private final String SELECT_ATT_1ARQ = "SELECT nome FROM atributo_arquivo WHERE nome=?";
    private final String DELETE_1REGRA_USR =  "DELETE FROM regras_has_usuario WHERE usuario_idusuario=? AND regras_idregra=?";
    private final String DELETE_1REGRA_ARQ =  "DELETE FROM regras_has_arquivo WHERE arquivo_idarquivo=? AND regras_idregra=?";
    private final String INSERT_ARQ_USR = "INSERT IGNORE INTO arquivo_has_usuario (arquivo_idarquivo, usuario_idusuario) VALUES (?,?)";
    private final String SELECT_ARQ_USR = "SELECT idarquivo,nome FROM arquivo,arquivo_has_usuario WHERE idarquivo=arquivo_idarquivo AND usuario_idusuario = ?";
    private final String DELETE_ARQ_USR =  "DELETE FROM arquivo_has_usuario WHERE usuario_idusuario=?";
    private final String DELETE_USR_ARQ =  "DELETE FROM arquivo_has_usuario WHERE arquivo_idarquivo=?";
    private final String SELECT_NOME_USR = "SELECT nome FROM usuario WHERE idusuario=?";
    private final String DELETE_ALL_ARQ_USR =  "DELETE FROM arquivo_has_usuario";
    
    
    public Connection getConnection() throws SQLException {
		
        Connection con = null;
	con = DriverManager.getConnection("jdbc:mysql://localhost/seguranca?user=root&password=");		
        //System.out.println("Conectado");
        return con;
    }    
    
    public void closeConnnection(Connection con) {
	try {
            con.close();
	} catch (SQLException e) {
            e.printStackTrace();
	}
    }
    
    public void getConnection2() throws SQLException {
		
        Connection con = null;
	conexao = DriverManager.getConnection("jdbc:mysql://localhost/seguranca?user=root&password=");		
       // System.out.println("Conectado");
    }    
    
    public void closeConnnection2() {
	try {
            conexao.close();
           
	} catch (SQLException e) {
            e.printStackTrace();
	}
    }

    /**
     *
     * @param pessoa
     * @return
     */
    public boolean insertPessoa(Usuario pessoa) {
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(INSERT_PESSOA);

            prepared.setString(1, pessoa.getNome());
            prepared.setString(2, pessoa.getSenha());
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;

    }
    
    public Usuario testaCredencial(Usuario pessoa) throws SQLException {
        Connection con = null;
        Usuario usr = new Usuario();
	try {
            con = getConnection();

            PreparedStatement prepared = con.prepareStatement(SELECT_PESSOA);
            prepared.setString(1, pessoa.getNome());
          
            ResultSet resultSet = prepared.executeQuery();
            
            while (resultSet.next()) {
                
                usr.setId(resultSet.getInt("idusuario"));
                usr.setNome(resultSet.getString("nome"));
                usr.setSenha(resultSet.getString("senha"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnnection(con);
        }
	return usr;
    }
    
    public boolean insereAtributo(Atributo att){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(INSERT_ATRIBUTO_USR);

            prepared.setString(1, att.getNome());
            
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public List<Usuario> selectTodasPessoas() throws SQLException {
        Connection con = null;
	List<Usuario> listPessoa = new ArrayList<Usuario>();
	try {
            con = getConnection();

            PreparedStatement prepared = con.prepareStatement(SELECT_TODAS_PESSOAS);
            ResultSet resultSet = prepared.executeQuery();
           
            while (resultSet.next()) {
                Usuario pessoaTmp = new Usuario();
                pessoaTmp.setId(resultSet.getInt("idusuario"));
                pessoaTmp.setNome(resultSet.getString("nome"));
                
                listPessoa.add(pessoaTmp);
            }

            } catch (SQLException e) {
                    e.printStackTrace();
            } finally {
                    closeConnnection(con);
            }
	return listPessoa;
    }
    
    public List<AtributoUsuario> selectTodosAtributos() throws SQLException {
        Connection con = null;
	List<AtributoUsuario>  listAtributoUsuario = new ArrayList<AtributoUsuario>();
	try {
            con = getConnection();

            PreparedStatement prepared = con.prepareStatement(SELECT_ATT_USR);
            ResultSet resultSet = prepared.executeQuery();
           
            while (resultSet.next()) {
                AtributoUsuario pessoaTmp = new AtributoUsuario();
                pessoaTmp.setId(resultSet.getInt("idatributo"));
                pessoaTmp.setNome(resultSet.getString("nome"));
                
                listAtributoUsuario.add(pessoaTmp);
            }

            } catch (SQLException e) {
                    e.printStackTrace();
            } finally {
                    closeConnnection(con);
            }
	return listAtributoUsuario;
    }
    
    public boolean insereAtributoUsuario(Usuario pessoa,AtributoUsuario atributo) {
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(INSERT_ATT_USR);

            prepared.setInt(1, pessoa.getId());
            prepared.setInt(2, atributo.getId());
            prepared.setString(3, atributo.getValor());
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean insereArquivo (Arquivo arq){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(INSERT_ARQUIVO);

            prepared.setString(1, arq.getNome());
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    
    public List<Arquivo> selectTodosArquivos() throws SQLException {
        Connection con = null;
	List<Arquivo> listArquivo = new ArrayList<Arquivo>();
	try {
            con = getConnection();

            PreparedStatement prepared = con.prepareStatement(SELECT_TODOS_ARQ);
            ResultSet resultSet = prepared.executeQuery();
           
            while (resultSet.next()) {
                Arquivo arquivoTmp = new Arquivo();
                arquivoTmp.setId(resultSet.getInt("idarquivo"));
                arquivoTmp.setNome(resultSet.getString("nome"));
                
                listArquivo.add(arquivoTmp);
            }

            } catch (SQLException e) {
                    e.printStackTrace();
            } finally {
                    closeConnnection(con);
            }
	return listArquivo;
    }
    
    public List<AtributoArquivo> selectTodosAtributosArq() throws SQLException {
        Connection con = null;
	List<AtributoArquivo>  listAtributoArq = new ArrayList<AtributoArquivo>();
	try {
            con = getConnection();

            PreparedStatement prepared = con.prepareStatement(SELECT_ATT_ARQ);
            ResultSet resultSet = prepared.executeQuery();
           
            while (resultSet.next()) {
                AtributoArquivo arquivoTmp = new AtributoArquivo();
                arquivoTmp.setId(resultSet.getInt("idatributo_arquivo"));
                arquivoTmp.setNome(resultSet.getString("nome"));
                
                listAtributoArq.add(arquivoTmp);
            }

            } catch (SQLException e) {
                    e.printStackTrace();
            } finally {
                    closeConnnection(con);
            }
	return listAtributoArq;
    }
    
    public boolean insereAtributoNoArquivo(Arquivo arq,AtributoArquivo atributo) {
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(INSERT_ATT_ARQ);

            prepared.setInt(1, arq.getId());
            prepared.setInt(2, atributo.getId());
            prepared.setString(3, atributo.getValor());
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;

    }
    
    public boolean insereAtributoDeArquivo(Atributo att){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(INSERT_ATRIBUTO_ARQ);

            prepared.setString(1, att.getNome());
            
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean novaRegra(String regra) {
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(INSERT_REGRA);

            prepared.setString(1, regra);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;

    }
    
    public Integer ultRegra() {
        Integer num = -1;
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(SELECT_ULTREGRA);
            ResultSet resultSet = prepared.executeQuery();
            
            while (resultSet.next()) {
                num = resultSet.getInt("MAX(idregra)");

            }
	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return num;

    }
    
    public boolean regraSQL(Integer num, String sql) {
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(INSERT_REGRA_SQL);

            prepared.setString(1, sql);
            prepared.setString(2, "");
            prepared.setInt(3,num);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;

    }
    
    public Integer ultRegraSQL() {
        Integer num = -1;
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(SELECT_ULTREGRASQL);
            ResultSet resultSet = prepared.executeQuery();
            
            while (resultSet.next()) {
                num = resultSet.getInt("MAX(idsql)");

            }
	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return num;

    }
    
    public boolean updateRegra(String opLogic,Integer num) {
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(UPDATE_REGRA_SQL);

            prepared.setString(1, opLogic);
            prepared.setInt(2, num);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;

    }
    
    public List<PolicyManager> selectTodasRegras() throws SQLException {
        Connection con = null;
	List<PolicyManager> listRegras = new ArrayList<PolicyManager>();
	try {
            con = getConnection();

            PreparedStatement prepared = con.prepareStatement(SELECT_TODAS_REGRAS);
            ResultSet resultSet = prepared.executeQuery();
           
            while (resultSet.next()) {
                PolicyManager regraTmp = new PolicyManager();
                regraTmp.setId(resultSet.getInt("idregra"));
                regraTmp.setSQL(resultSet.getString("regra"));
                
                listRegras.add(regraTmp);
            }

            } catch (SQLException e) {
                    e.printStackTrace();
            } finally {
                    closeConnnection(con);
            }
	return listRegras;
    }
    
    public boolean atribuirRegraUsr(Usuario pessoa,PolicyManager regra) {
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(INSERT_REGRA_USR);

            prepared.setInt(1, regra.getId());
            prepared.setInt(2, pessoa.getId());
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean atribuirRegraArq(Arquivo arq,PolicyManager regra) {
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(INSERT_REGRA_ARQ);

            prepared.setInt(1, regra.getId());
            prepared.setInt(2, arq.getId());
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public List<PolicyManager> obterRegrasSQL(Integer id){
      
	List<PolicyManager> regras = new ArrayList<PolicyManager>(); 
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(SELECT_REGRASQL_USR);
            prepared.setInt(1, id);
            ResultSet resultSet = prepared.executeQuery();
           
            while (resultSet.next()) {
                PolicyManager regraTmp = new PolicyManager();
                regraTmp.setId(resultSet.getInt("regra_idregra"));
                regraTmp.setSQL(resultSet.getString("descricao"));
                
                regras.add(regraTmp);
               
            }

            } catch (SQLException e) {
                    e.printStackTrace();
            } finally {
            closeConnnection(con);
	}
        
        return regras;
       
    }
    
    public List<PolicyManager> obterRegrasUsr(Integer id){
      
	List<PolicyManager> regras = new ArrayList<PolicyManager>(); 
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(SELECT_REGRAS_USR);
            prepared.setInt(1, id);
            ResultSet resultSet = prepared.executeQuery();
           
            while (resultSet.next()) {
                PolicyManager regraTmp = new PolicyManager();
                regraTmp.setId(resultSet.getInt("idregra"));
                regraTmp.setSQL(resultSet.getString("regra"));
                
                regras.add(regraTmp);
               
            }

            } catch (SQLException e) {
                    e.printStackTrace();
            } finally {
            closeConnnection(con);
	}
        
        return regras;
       
    }
    
    public HashSet<Arquivo> executaRegra(String regra){

     HashSet<Arquivo> arquivo = new HashSet<Arquivo>();
     Connection con = null;
     try {

         PreparedStatement prepared = conexao.prepareStatement(regra);
         ResultSet resultSet = prepared.executeQuery();

         while (resultSet.next()) {
             Arquivo arquivoTmp = new Arquivo();
             arquivoTmp.setId(resultSet.getInt("idarquivo"));
             arquivoTmp.setNome(resultSet.getString("nome"));

             arquivo.add(arquivoTmp);
         }

         } catch (SQLException e) {
                 e.printStackTrace();
         } 

     return arquivo;
       
    }
       
    public List<PolicyManager> obterRegrasSQLArq(Integer id){

     List<PolicyManager> regras = new ArrayList<PolicyManager>(); 
     Connection con = null;
     try {
        
         PreparedStatement prepared = conexao.prepareStatement(SELECT_REGRASQL_ARQ);
         prepared.setInt(1, id);
         ResultSet resultSet = prepared.executeQuery();

         while (resultSet.next()) {
             PolicyManager regraTmp = new PolicyManager();
             regraTmp.setId(resultSet.getInt("regra_idregra"));
             regraTmp.setSQL(resultSet.getString("descricao"));

             regras.add(regraTmp);

         }

         } catch (SQLException e) {
                 e.printStackTrace();
         } 

     return regras;

    }
    
    public List<PolicyManager> obterRegrasArq(Integer id){
      
	List<PolicyManager> regras = new ArrayList<PolicyManager>(); 
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(SELECT_REGRAS_ARQ);
            prepared.setInt(1, id);
            ResultSet resultSet = prepared.executeQuery();
           
            while (resultSet.next()) {
                PolicyManager regraTmp = new PolicyManager();
                regraTmp.setId(resultSet.getInt("idregra"));
                regraTmp.setSQL(resultSet.getString("regra"));
                
                regras.add(regraTmp);
               
            }

            } catch (SQLException e) {
                    e.printStackTrace();
            } finally {
            closeConnnection(con);
	}
        
        return regras;
       
    }
    
    public Boolean executaRegraArq(String regra){
      
	List<String> arquivo = new ArrayList<String>();
        Connection con = null;
	try {
           

            PreparedStatement prepared = conexao.prepareStatement(regra);
            ResultSet resultSet = prepared.executeQuery();
            
            while (resultSet.next()) {
                arquivo.add(resultSet.getString("nome"));
            }

            } catch (SQLException e) {
                    e.printStackTrace();
            } 
        
        if (arquivo.size()> 0 ){
            return true;
        }
        else{
            return false;
        }
       
    }
    
    public boolean insereArquivoPessoa(Integer idarquivo,Integer idpessoa) {
        
	try {
            PreparedStatement prepared = conexao.prepareStatement(INSERT_ARQ_USR);
            prepared.setInt(1, idarquivo);
            prepared.setInt(2, idpessoa);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} 
	return retorno;
    }
 
    public Integer selectIdPessoa(String nome) {
        Integer num = -1;
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(SELECT_ID_USR);
            prepared.setString(1, nome);
            ResultSet resultSet = prepared.executeQuery();
            
            while (resultSet.next()) {
                num = resultSet.getInt("idusuario");

            }
	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return num;

    }
    
    public Integer selectIdArq(String nome) {
        Integer num = -1;
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(SELECT_ID_ARQ);
            prepared.setString(1, nome);
            ResultSet resultSet = prepared.executeQuery();
            
            while (resultSet.next()) {
                num = resultSet.getInt("idarquivo");

            }
	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return num;

    }
    public Integer selectIdAtributoArq(String nome) {
        Integer num = -1;
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(SELECT_ID_ATT_ARQ);
            prepared.setString(1, nome);
            ResultSet resultSet = prepared.executeQuery();
            
            while (resultSet.next()) {
                num = resultSet.getInt("idatributo_arquivo");

            }
	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return num;

    }
    public Integer selectIdAtributoUsr(String nome) {
        Integer num = -1;
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(SELECT_ID_ATT_USR);
            prepared.setString(1, nome);
            ResultSet resultSet = prepared.executeQuery();
            
            while (resultSet.next()) {
                num = resultSet.getInt("idatributo");

            }
	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return num;

    }
    
    /**
     *
     * @param id
     * @return
     */
    public boolean deletarUsuario (Integer id){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(DELETE_USUARIO);
            prepared.setInt(1, id);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean deletarRegraUsuario (Integer id){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(DELETE_REGRA_USR);
            prepared.setInt(1, id);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean deletarAtributoUsuario (Integer id){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(DELETE_ATT_USR);
            prepared.setInt(1, id);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean deletarRegraDeUsuario (Integer id){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(DELETE_REGRA_DE_USR);
            prepared.setInt(1, id);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean deletarRegraDeArquivo (Integer id){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(DELETE_REGRA_DE_ARQ);
            prepared.setInt(1, id);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean deletarRegraSQL (Integer id){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(DELETE_REGRA_SQL);
            prepared.setInt(1, id);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean deletarRegra (Integer id){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(DELETE_REGRA);
            prepared.setInt(1, id);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean deletarRegraArquivo (Integer id){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(DELETE_REGRA_ARQ);
            prepared.setInt(1, id);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean deletarAtributoArquivo (Integer id){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(DELETE_ATT_ARQ);
            prepared.setInt(1, id);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean deletarArquivo (Integer id){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(DELETE_ARQUIVO);
            prepared.setInt(1, id);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean deletarAtributoDeUsuario (Integer id){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(DELETE_ATT_DE_USR);
            prepared.setInt(1, id);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean deletarAtributoUsr (Integer id){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(DELETE_ATRIBUTO_USR);
            prepared.setInt(1, id);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean deletarAtributoDeArquivo (Integer id){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(DELETE_ATT_DE_ARQ);
            prepared.setInt(1, id);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean deletarAtributoArq (Integer id){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(DELETE_ATRIBUTO_ARQ);
            prepared.setInt(1, id);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean alterarAtributoUsuario(Usuario pessoa,AtributoUsuario atributo) {
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(UPDATE_ATT_USR);

            prepared.setString(1, atributo.getValor());
            prepared.setInt(2, atributo.getId());
            prepared.setInt(3, pessoa.getId());
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean alterarAtributoNoArquivo(Arquivo arq,AtributoArquivo atributo) {
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(UPDATE_ATT_ARQ);

            prepared.setString(1, atributo.getValor());
            prepared.setInt(2, atributo.getId());
            prepared.setInt(3, arq.getId());
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;

    }
    
    public Boolean verificaUsuario(String nome){
      
	List<String> nomes = new ArrayList<String>();
        Connection con = null;
	try {
            con = getConnection();
            
            PreparedStatement prepared = con.prepareStatement(SELECT_USR);
            prepared.setString(1, nome);
            ResultSet resultSet = prepared.executeQuery();
             
            
            while (resultSet.next()) {
                nomes.add(resultSet.getString("nome"));
            }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeConnnection(con);
            }
        
        if (nomes.size()> 0 ){ 
            return true;
        }
        else{
            return false;
        }
       
    }
    
    public Boolean verificaAtributoUsr(String nome){
      
	List<String> nomes = new ArrayList<String>();
        Connection con = null;
	try {
            con = getConnection();
            
            PreparedStatement prepared = con.prepareStatement(SELECT_ATT_1USR);
            prepared.setString(1, nome);
            ResultSet resultSet = prepared.executeQuery();
             
            
            while (resultSet.next()) {
                nomes.add(resultSet.getString("nome"));
            }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeConnnection(con);
            }
        
        if (nomes.size()> 0 ){ 
            return true;
        }
        else{
            return false;
        }
       
    }
    
    public Boolean verificaAtributoArq(String nome){
      
	List<String> nomes = new ArrayList<String>();
        Connection con = null;
	try {
            con = getConnection();
            
            PreparedStatement prepared = con.prepareStatement(SELECT_ATT_1ARQ);
            prepared.setString(1, nome);
            ResultSet resultSet = prepared.executeQuery();
             
            
            while (resultSet.next()) {
                nomes.add(resultSet.getString("nome"));
            }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                closeConnnection(con);
            }
        
        if (nomes.size()> 0 ){ 
            return true;
        }
        else{
            return false;
        }
       
    }
    
    public boolean removerRegraUsr (Integer idusuario,Integer idregra){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(DELETE_1REGRA_USR);
            prepared.setInt(1, idusuario);
            prepared.setInt(2, idregra);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean removerRegraArq (Integer idarquivo,Integer idregra){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(DELETE_1REGRA_ARQ);
            prepared.setInt(1, idarquivo);
            prepared.setInt(2, idregra);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public List<Arquivo> selectArquivosUsr(Integer idusuario) {
        Connection con = null;
	List<Arquivo> listArquivo = new ArrayList<Arquivo>();
	try {
            con = getConnection();

            PreparedStatement prepared = con.prepareStatement(SELECT_ARQ_USR);
            prepared.setInt(1, idusuario);
            ResultSet resultSet = prepared.executeQuery();
           
            while (resultSet.next()) {
                Arquivo arquivoTmp = new Arquivo();
                arquivoTmp.setId(resultSet.getInt("idarquivo"));
                arquivoTmp.setNome(resultSet.getString("nome"));
                
                listArquivo.add(arquivoTmp);
            }

            } catch (SQLException e) {
                    e.printStackTrace();
            } finally {
                    closeConnnection(con);
            }
	return listArquivo;
    }
    
    public boolean deletarArquivoUsuario (Integer id){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(DELETE_ARQ_USR);
            prepared.setInt(1, id);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean deletarUsuarioArquivo (Integer id){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(DELETE_USR_ARQ);
            prepared.setInt(1, id);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public boolean deletarArquivoUsr (Integer id){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(DELETE_ARQ_USR);
            prepared.setInt(1, id);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
    
    public String selectNomePessoa(Integer id) {
        String nome = null;
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(SELECT_NOME_USR);
            prepared.setInt(1, id);
            ResultSet resultSet = prepared.executeQuery();
            
            while (resultSet.next()) {
                nome = resultSet.getString("nome");

            }
	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return nome;

    }
    
    public boolean deletarTodosArqUsr (){
    
        Connection con = null;
	try {
            con = getConnection();
            PreparedStatement prepared = con.prepareStatement(DELETE_ALL_ARQ_USR);
            retorno = prepared.execute();

	} catch (SQLException e) {
            e.printStackTrace();
	} finally {
            closeConnnection(con);
	}
	return retorno;
    }
}

    


