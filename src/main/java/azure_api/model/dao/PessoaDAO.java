package azure_api.model.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import azure_api.model.entity.PessoaEntity;

public class PessoaDAO {

	public int inserirPessoa(PessoaEntity pessoa) {
		int id = -1;
		String sql = "insert into pessoa (nome, cpf, rg, telefone, email) values (?,?,?,?,?)";

		try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement pst = conn.prepareStatement(sql);) {

			pst.setString(1, pessoa.getNome());
			pst.setString(2, pessoa.getCpf());
			pst.setString(3, pessoa.getRg());
			pst.setString(4, pessoa.getTelefone());
			pst.setString(5, pessoa.getEmail());
			pst.execute();

			String query = "select LAST_INSERT_ID()";
			try (PreparedStatement pst1 = conn.prepareStatement(query); ResultSet rs = pst1.executeQuery();) {

				if (rs.next()) {
					id = rs.getInt(1);
				}
			}
		} catch (SQLException | IOException e) {
			e.printStackTrace();
		}
		return id;
	}

	public int alterarPessoa(PessoaEntity pessoa, int id) throws SQLException, IOException {
		// int id = -1;
		String sql = "UPDATE pessoa SET nome = (?), cpf = (?), rg = (?), telefone = (?), email = (?) WHERE id = " + id;

		try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement pst = conn.prepareStatement(sql);) {

			pst.setString(1, pessoa.getNome());
			pst.setString(2, pessoa.getCpf());
			pst.setString(3, pessoa.getRg());
			pst.setString(4, pessoa.getTelefone());
			pst.setString(5, pessoa.getEmail());
			pst.execute();
		}
		return id;
	}

	public void selecionarPessoa(int id) throws SQLException, IOException {
		String sql = "SELECT nome, cpf, rg, telefone, email, azure_person_id from pessoa where id = " + id;
		Connection conn = ConnectionFactory.getConnection();

		try {

			PreparedStatement ste = conn.prepareStatement(sql);
			// ste.setString(1, lo.getCodigo());

			ResultSet res = ste.executeQuery();

			String msg = null;

			while (res.next())

				msg = "Nome: " + res.getString("nome") + "\nCpf: " + res.getString("cpf") + "\nRg:  "
						+ res.getString("rg") + "\nTelefone: " + res.getString("telefone") + "\nEmail: "
						+ res.getString("email");

			System.out.println(msg);

			ste.close();
			res.close();

		} catch (SQLException e) {
			System.out.println(e.toString());

		}
	}

	public int deletarPessoa(int id) throws SQLException, IOException {
		// int id = -1;
		String sql = "delete from pessoa where id = " + id;

		try (Connection conn = ConnectionFactory.getConnection(); PreparedStatement pst = conn.prepareStatement(sql);) {

			pst.execute();
		}
		return id;
	}
}
