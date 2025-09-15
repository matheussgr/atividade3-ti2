package dao;

import model.Servico;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class ServicoDAO extends DAO {

    public ServicoDAO() {
        super();
        conectar();
    }
    
	public void finalize() {
		close();
	}

    public boolean insert(Servico servico) {
        boolean status = false;
        try {
        	String sql = "INSERT INTO servico (nome, descricao, preco) VALUES ('" 
        	           + servico.getNome() + "', '" 
        	           + servico.getDescricao() + "', " 
        	           + servico.getPreco() + ")";
			PreparedStatement st = conexao.prepareStatement(sql);
        	st.executeUpdate();
			st.close();
            status = true;
        } catch (SQLException u) {
        	throw new RuntimeException(u);
        }
        return status;
    }

    
	public Servico get(int id) {
		Servico servico = null;
		
		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			String sql = "SELECT * FROM servico WHERE id="+id;
			ResultSet rs = st.executeQuery(sql);	
	        if (rs.next()) {
	            servico = new Servico(
	                rs.getInt("id"),
	                rs.getString("nome"),
	                rs.getString("descricao"),
	                (float)rs.getDouble("preco")
	            );
	        }
	        st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return servico;
	}    
    
	public List<Servico> get() {
	    return get("");
	}

	public List<Servico> getOrderByID() {
	    return get("id");		
	}

	public List<Servico> getOrderByDescricao() {
		return get("descricao");		
	}

	public List<Servico> getOrderByPreco() {
	    return get("preco");		
	}

	private List<Servico> get(String orderBy) {
	    List<Servico> servicos = new ArrayList<Servico>();
	    try {
	        Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
	        String sql = "SELECT * FROM servico" + ((orderBy.trim().length() == 0) ? "" : (" ORDER BY " + orderBy));
	        ResultSet rs = st.executeQuery(sql);
	        while (rs.next()) {
	            Servico s = new Servico(
	                rs.getInt("id"),
	                rs.getString("nome"),
	                rs.getString("descricao"),
	                (float)rs.getDouble("preco")
	            );
	            servicos.add(s);
	        }
	        st.close();
	    } catch (Exception e) {
	        System.err.println(e.getMessage());
	    }
	    return servicos;
	}
    
    
    public boolean update(Servico servico) {
        boolean status = false;
        try {
            String sql = "UPDATE servico SET nome = '" + servico.getNome() + "', descricao = '"
                       + servico.getDescricao() + "', preco = " + servico.getPreco()
                       + " WHERE id = " + servico.getID();
            PreparedStatement st = conexao.prepareStatement(sql);
            st.executeUpdate();
            st.close();
            status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
        }
        return status;
    }

    public boolean delete(int id) {
        boolean status = false;
        try {
            Statement st = conexao.createStatement();
            st.executeUpdate("DELETE FROM servico WHERE id = " + id);
            st.close();
            status = true;
		} catch (SQLException u) {  
			throw new RuntimeException(u);
		}
        return status;
    }
}


