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


/**Classe que representa o banco de dados utilizado na federação de nuvens.
 * Aqui se encontram todos os métodos para as execuções das mais diversas 
 * consultas ao banco.
 * 
 * Atualmente estamos utilizando o MySQL, e o banco de dados se chama seguranca,
 * e pode ser encontrado no arquivo banco.sql.
 * 
 * Basta importar o arquivo para o MySQL que o programa poderá funcionar
 * corretamente.
 * 
 * A idéia é migrar para o Cassandra o mais rápido o possível, para que tenhamos
 * uma solução distribuída.
 *
 * @author Heitor Henrique 
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
    
    /**Método que realiza a conexão com o banco de dados utilizand o JDBC.
     * Este método deve ser chamado sempre que se quiser fazer uma consulta
     * ao banco.
     *
     * @return CUma conexão ativa com o banco em um objeto do tipo 
     * {@link Connection}
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
		
        Connection con = null;
	con = DriverManager.getConnection("jdbc:mysql://localhost/seguranca?user=root&password=");		
        //System.out.println("Conectado");
        return con;
    }    
    
    /**Método que encerra uma conexão com o banco de dados. Deve ser chamado
     * sempre ao término de uma consulta.
     *
     * @param con Conexão que está aberta.
     */
    public void closeConnnection(Connection con) {
	try {
            con.close();
	} catch (SQLException e) {
            e.printStackTrace();
	}
    }
    
    /**Método que inicia uma conexão, porém não retorna a conexão, inicia na
     * própria variável de classe. Basicamente a mesma coisa do outro 
     * getConnection, porém estou utilizando este aqui em alguns casos,
     * para ganhar um pouco de desempenho e não ter que abrir e fechar uma 
     * conexão toda hora.
     *
     * @throws SQLException
     */
    public void getConnection2() throws SQLException {
		
        Connection con = null;
	conexao = DriverManager.getConnection("jdbc:mysql://localhost/seguranca?user=root&password=");		
       // System.out.println("Conectado");
    }    
    
    /**Método que encerra a conexão com o banco de dados, caso tenha sido
     * aberto com o método {@link Database#getConnection2()}.
     * 
     */
    public void closeConnnection2() {
	try {
            conexao.close();
           
	} catch (SQLException e) {
            e.printStackTrace();
	}
    }

    /**Método que cadastra uma pessoa no banco de dados. Atualmente os dados
     * que ele está armazenando são o usuário e a senha, lembrando que a senha
     * já está codificada com o PBKDF2.
     *
     * @param pessoa Objeto pessoa a ser armazenado no banco.
     * @return True se foi inserido corretamente e false caso contrário.
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
    
    /**Método utilizado no momento do login, dado um objeto pessoa contendo um
     * valor de usuário e senha, ele busca no banco estes valores e caso exista
     * retorna o objeto com o seu ID.
     *
     * @param pessoa Objeto contendo usuário e senha a serem procurados no banco.
     * @return Objeto que foi encontrado.
     * @throws SQLException
     */
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
    
    /**Método para cadastrar um atributo de usuário no banco. 
     *
     * @param att Atributo a ser cadastrado
     * @return True se foi cadastrado com sucesso e false caso contrário.
     */
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
    
    /**Método que retorna a lista de todas os usuários cadastrados no BioNimbuZ.
     * Os objetos retornados estão apenas com nome e ID setados.
     *
     * @return List-{@link Usuario}- Lista de todos usuários.
     * @throws SQLException
     */
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
    
    /**Método que retorna todos os atributos de usuário cadastrados no BioNimbuZ.
     * Os atributos estão com os vaores de nome e ID setados.
     *
     * @return List-{@link AtributoUsuario}- lista de atributos de usuário.
     * @throws SQLException
     */
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
    
    /**Método que associa um valor de um atributo a um determinado usuário.
     *
     * @param pessoa Usuário cujo valor de atributo será atribuído.
     * @param atributo Atributo a ser inserido no usuário.
     * @return True caso a associação foi feita com sucesso e false caso 
     * contrário.
     */
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
    
    /**Método para realizar o cadastro de arquivo no serviço de segurança.
     * 
     *
     * @param arq Arquivo a ser cadastrado no serviço de segurança.
     * @return True se foi cadastrado com sucesso e false caso contrário.
     */
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
    
    /**Método que retorna a lista de todos os arquivos atualmente cadastrados
     * no serviço de segurança.
     *
     * @return List-{@link Arquivo}- Lista de todos arquivos.
     * @throws SQLException
     */
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
    
    /**Método que retona a lista de todos atributos de arquivo.
     *
     * @return List-{@link AtributoArquivo}- lista de todos atributos de
     * arquivo.
     * @throws SQLException
     */
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
    
    /**Método que associa um valor de um atributo a um determinado arquivo.
     *
     * @param arq Arquivo que se quer associar um valor de atributo.
     * @param atributo Atributo a ser adicionado ao arquivo.
     * @return True se a associação der certo e false caso contrário.
     */
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
    
    /**Método para cadastrar um novo atributo de arquivo.
     *
     * @param att Atributo a ser cadastrado.
     * @return True se der certo e false caso contrário.
     */
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
    
    /**Método que adiciona uma nova regra no sistema.
     *
     * @param regra String contendo a regra que se deseja adicionar.
     * @return True se der certo e false caso contrário.
     */
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
    
    /**Método que retorna um inteiro contendo o ID da última regra que foi 
     * adicionada.
     *
     * @return Inteiro contendo o ID da ultima regra adicionada.
     */
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
    
    /**Método que insere uma regra no formato SQL no banco de dados.
     * Para isto, é necessário associá-la a alguma regra escrita em formato
     * não SQL já cadastrada. É exatamente este o intuito do primeiro parametro,
     * o ID de alguma regra.
     *
     * @param num ID de uma regra cadastrada (não SQL)
     * @param sql Regra em formato SQl.
     * @return true se der certo e false caso contrário
     */
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
    
    /**Método que retorna o ID da ultima regra SQl adicionada.
     *
     * @return ID da ultima regra SQL.
     */
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
    
    /**Método que adiciona o operador lógico utilizado entre duas regras. Na 
     * prática este método não faz muito mais sentido, porém deixarei ele aqui
     * para uso futuro.
     *
     * @param opLogic Operador entre duas regras.
     * @param num ID da ultima regra adicionada.
     * @return true de funcionou e false caso contrário.
     */
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
    
    /**Método que retorna a lista de todos as regras cadastradas no sistema.
     *
     * @return List-{@link PolicyManager- Lista de todas as regras.
     * @throws SQLException
     */
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
    
    /**Método para associar uma regra a um determinado usuário.
     *
     * @param pessoa usuário que se deseja associar a regra.
     * @param regra Regra a ser associada.
     * @return true se funcionar e false caso contrário.
     */
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
    
    /**Método para atrbuir uma regra a um determinado arquivo.
     *
     * @param arq Arquivo cuja regra deve ser associada.
     * @param regra Regra a ser associada ao arquivo.
     * @return true se der certo e false caso contrário.
     */
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
    
    /**Método que retorna a lista de todas as regras em formato SQL associadas
     * a um determinado usuário.
     *
     * @param id Id do usuário cujas regras se quer saber.
     * @return List-{@link PolicyManager}- Lista de regras SQL de um usuário.
     */
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
    
    /**Método que retorna a lista de regras em formato não SQL associadas a 
     * um determinado usuário.
     *
     * @param id ID do usuário que se deseja saber as regras.
     * @return List-{@link PolicyManager- Lista de todas regras em formato
     * não SQl associados a um usuário.
     */
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
    
    /**Método que executa uma regra em formato SQL e retorna um HashSet com
     * os arquivos resultantes desta consulta.
     * As regras que devem ser passadas para este método são exatamente aquelas 
     * que foram retornadas pelo método 
     * {@link Database#obterRegrasSQL(java.lang.Integer) }.
     *
     * @param regra Regra a ser executada.
     * @return HashSet-{@link Arquivo}- Arquivos retornados pela consulta.
     */
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
       
    /**Método que retorna a lista de todas regras em formato SQl que estão 
     * associadas um determinado arquivo.
     *
     * @param id ID do arquivo que se deseja saber as regras.
     * @return List-{@link PolicyManager}- Lista de regras associadas ao arquivo.
     */
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
    
    /**Método que retorna a lista de regras em formato não SQL associadas a 
     * um determinado arquivo.
     *
     * @param id ID do arquivo que se deseja saber as regras.
     * @return List-{@link PolicyManager- Lista de todas regras em formato
     * não SQl associados a um arquivo.
     */
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
    
    
    /**Método que executa uma regra em formato SQL e retorna verdadeiro se a 
     * consulta contém o usuário que se deseja e false caso contrário.
     * 
     * @param regra Regra a ser executada.
     * @return true e o usuário for encontrado e false caso contrário.
     */
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
    
    /**Método que insere na cache um arquivo que um determinado usuário possui
     * acesso.
     *
     * @param idarquivo ID do arquivo que a pessoa tem acesso.
     * @param idpessoa ID da pessoa que pode acessar o arquivo.
     * @return true se deu certo e false caso contrário.
     */
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
 
    /**Método que retorna o ID de um usuário cujo o nome foi passado como 
     * parametro.
     *
     * @param nome String com o nome do usuário que se quer saber o ID.
     * @return ID do usuário cujo noe foi passado ou -1 se não for encontrado.
     */
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
    
    /**Método que retorna o ID de um arquivo cujo nome foi passado como 
     * parametro.
     *
     * @param nome String contendo o nome do arquivo que se deseja saber o ID.
     * @return Inteiro com o ID do arquivo ou -1 se não for encontrado.
     */
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

    /**Método que retorna o ID de um determinado atributo de arquivo
     * cujo nome foi passado como parametro.
     *
     * @param nome String com o nome do atributo de arquivo que se quer saber 
     * o ID.
     * @return Inteiro com o id do atributo ou -1 se não for encontrado.
     */
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
    
     /**Método que retorna o ID de um determinado atributo de usuário
     * cujo nome foi passado como parametro.
     *
     * @param nome String com o nome do atributo de usuário que se quer saber 
     * o ID.
     * @return Inteiro com o id do atributo ou -1 se não for encontrado.
     */
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
    
    /**Método que exclui um determinado usuário do sistema.
     *
     * @param id Inteiro com o ID do usuário a ser excluido.
     * @return true se foi excluido com sucesso e false caso contrário.
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
    
    /**Método que exclui as dependencias de um usuário excluído, na tabela 
     * de regras associadas. Como o usuário foi excluído, as referencias a
     * ele na tabela regras_has_usuario são excluidas também.
     *
     * @param id Id do usuário que se deseja excluir as regras associadas.
     * @return true se deletou com sucesso false caso contrário.
     */
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
    
    /**Método que exclui as dependencias de um usuário excluído, na tabela 
     * de atributos associadas. Como o usuário foi excluído, as referencias a
     * ele na tabela usuario_has_atributo são excluidas também.
     *
     * @param id Id do usuário que se deseja excluir os atributos associados.
     * @return true se deletou com sucesso false caso contrário.
     */
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
    
    /**Método que exclui as dependencias de uma regra excluída, na tabela 
     * de regras associadas a um usuário. Como a regra foi excluída, 
     * as referencias a ela na tabela regras_has_usuario são excluidas também.
     *
     * @param id Id da regra que se deseja excluir os usuários associados.
     * @return true se deletou com sucesso false caso contrário.
     */
    
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
    
     /**Método que exclui as dependencias de uma regra excluída, na tabela 
     * de regras associadas a um arquivo. Como a regra foi excluída, 
     * as referencias a ele na tabela regras_has_arquivo são excluidas também.
     *
     * @param id Id da regra que se deseja excluir os arquivos associados.
     * @return true se deletou com sucesso false caso contrário.
     */
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
    
     /**Método que exclui as dependencias de uma regra excluída, na tabela 
     * de regras no formato SQL. Como a regra foi excluída, 
     * as referencias a ela na tabela regrasql são excluidas também.
     *
     * @param id Id da regra que se deseja excluir as regras sql associadas.
     * @return true se deletou com sucesso false caso contrário.
     */
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
    
    /**Método que exclui uma regra do BioNimbuZ.
     *
     * @param id Inteiro com o ID da regra a ser excluida.
     * @return true se excluiu corretamente e fase caso contrário.
     */
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
    
    /**Método que exclui as dependencias de um arquivo excluído, na tabela 
     * de regras associadas. Como o arquivo foi excluído, as referencias a
     * ele na tabela regras_has_arquivo são excluidas também.
     *
     * @param id Id do arquivo que se deseja excluir as regras associadas.
     * @return true se deletou com sucesso false caso contrário.
     */
    
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
    
    /**Método que exclui as dependencias de um arquivo excluído, na tabela 
     * de atributos associadas. Como o usuário foi excluído, as referencias a
     * ele na tabela arquivo_has_atributo são excluidas também.
     *
     * @param id Id do arquivo que se deseja excluir os atributos associados.
     * @return true se deletou com sucesso false caso contrário.
     */
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
    
    /**Método que exclui um arquivo do serviço de segurança da federação.
     *
     * @param id Id do arquivo a ser excluido.
     * @return true se excluiu corretamente e false caso contrário.
     */
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
    
    /**Método que exclui as dependencias de um usuário excluído, na tabela 
     * de atributos associadas a um usuário. Como o atributo foi excluído, 
     * as referencias a ele na tabela usuario_has_atributo são excluidas também.
     *
     * @param id Id do atributo que se deseja excluir os usuários associados.
     * @return true se deletou com sucesso false caso contrário.
     */
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
    
    /**Método que exclui um atributo de usuário.
     *
     * @param id Inteiro com o ID do atributo de usuário a ser excluido.
     * @return true se excluir corretamente e false caso contrário.
     */
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
    
     /**Método que exclui as dependencias de um arquivo excluído, na tabela 
     * de atributos associadas a um arquivo. Como o atributo foi excluído, 
     * as referencias a ele na tabela arquivo_has_atributo são excluidas também.
     *
     * @param id Id do atributo que se deseja excluir os arquivo associados.
     * @return true se deletou com sucesso false caso contrário.
     */
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
    
     /**Método que exclui um atributo de arquivo.
     *
     * @param id Inteiro com o ID do atributo de arquivo a ser excluido.
     * @return true se excluir corretamente e false caso contrário.
     */
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
    
    /**Método para a alteração de um valor de atributo de usuário.
     *
     * @param pessoa Usuário cujo atributo será alterado.
     * @param atributo Novo valor para o atributo que será modificado.
     * @return true se modificou corretamente e false caso contrário.
     */
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
    
    /**Método para a alteração de um valor de atributo de arquivo.
     *
     * @param arq Arquivo cujo atributo será alterado.
     * @param atributo Novo valor para o atributo que será modificado.
     * @return true se modificou corretamente e false caso contrário.
     */
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
    
    /**Método que verifica se um usuário ja está cadastrado no BioNimbuZ, pois
     * não pode haver dois usuários com o mesmo nome.
     *
     * @param nome String com o nome a ser verificado.
     * @return True se ja existe usuário com este nome e false caso contrário.
     */
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
    
    /**Método que verifica se determinado atributo de usuário que foi inserido 
     * em alguma regra já foi cadastrado. É necessário para saber se a regra
     * foi construida corretamente.
     *
     * @param nome Nome do atributo de usuário a ser verificado.
     * @return True se o atributo ja está cadastrado e false caso contrário.
     */
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
    
    /**Método que verifica se determinado atributo de arquivo que foi inserido 
     * em alguma regra já foi cadastrado. É necessário para saber se a regra
     * foi construida corretamente.
     *
     * @param nome Nome do atributo de arquivo a ser verificado.
     * @return True se o atributo ja está cadastrado e false caso contrário.
     */
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
    
    /**Método que desassocia uma regra de um determinado usuário.
     *
     * @param idusuario Inteiro com o ID do usuário.
     * @param idregra Inteiro com o ID da regra.
     * @return true se foi retirado com sucesso e false caso contrário.
     */
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
    
     /**Método que desassocia uma regra de um determinado arquivo.
     *
     * @param idarquivo Inteiro com o ID do arquivo.
     * @param idregra Inteiro com o ID da regra.
     * @return true se foi retirado com sucesso e false caso contrário.
     */
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
    
    /**Método que retorna a lista de arquivos que um determinado usuário tem
     * acesso, essa busca é feita na cache.
     *
     * @param idusuario Inteiro com o ID do usuário que se deseja buscar os
     * arquivos.
     * @return List-{@link Arquivo}- Lista de arquivos que o usuário tem acesso.
     */
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
    
    /**Método que deleta todas as referencias de um determinado usuário na
     * tabela arquivo_has_usuario (cache). 
     *
     * @param id Id do usuário que se deseja excluir as entradas na tabela
     * arquivo_has_usuario.
     * @return true se deletou corretamente false caso contrário.
     */
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
    
    /**Método que deleta todas as referencias de um determinado arquivo na
     * tabela arquivo_has_usuario (cache). 
     *
     * @param id Id do arquivo que se deseja excluir as entradas na tabela
     * arquivo_has_usuario.
     * @return true se deletou corretamente false caso contrário.
     */
    
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
    
    /**Método que retorna um nome de usuário cujo ID foi passado como parâmetro.
     *
     * @param id Inteiro com o ID do usuário que se deseja saber o nome.
     * @return String contendo o nome do usuário.
     */
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
    
    /**Método que exclui todos os registros da tabela arquivo_has_usuario, ou
     * seja, ele limpa toda a cache.
     *
     * @return true se excluiu corretamente e false caso contrário.
     */
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

    


