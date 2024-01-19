/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.senac.mercearia.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import br.senac.mercearia.model.Funcionario;
import br.senac.mercearia.utils.Conexao;

/**
 *
 * @author Dymi
 */
public class FuncionarioDAO {
      private Connection connection = Conexao.getConexao();

    public void save(Funcionario funcionario) {
        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO cadastro_funcionario (IdFuncionario, Nome, Endereco, CEP, CPF, Telefone, Nascimento) VALUES (?,?,?,?,?,?,?)");
            ps.setInt(1,funcionario.getIdFuncionario());
            ps.setString(2, funcionario.getNome());
            ps.setString(3, funcionario.getEndereco());
            ps.setString(4, funcionario.getCEP());
            ps.setString(5, funcionario.getCPF());
            ps.setString(6, funcionario.getTelefone());
            ps.setString(7, funcionario.getNascimento());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Funcionario cadastrado com sucesso!");
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    public void update(Funcionario funcionario) {
    try {
        PreparedStatement ps = connection.prepareStatement("UPDATE cadastro_funcionario SET Nome=?, Endereco=?, CEP=?, CPF=?, Telefone=?, Nascimento=? WHERE idFuncionario=?");
        ps.setString(1, funcionario.getNome());
        ps.setString(2, funcionario.getEndereco());
        ps.setString(3, funcionario.getCEP());
        ps.setString(4, funcionario.getCPF());
        ps.setString(5, funcionario.getTelefone());
        ps.setString(6, funcionario.getNascimento());
        ps.setInt(7,funcionario.getIdFuncionario());
        ps.execute();
        JOptionPane.showMessageDialog(null, "Funcionario atualizado com sucesso!");
    } catch (SQLException ex) {
        Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    public void saveOrUpdate(Funcionario funcionario) {
        String sql;

        try {
            // Desativa o autocommit
            connection.setAutoCommit(false);

            if (funcionario.getIdFuncionario() == 0) {
                // Se o ID do funcionário for 0 ou nulo, significa que é um novo funcionário
                sql = "INSERT INTO cadastro_funcionario (Nome, Endereco, CEP, CPF, Telefone, Nascimento) VALUES (?, ?, ?, ?, ?, ?)";
            } else {
                // Se o ID do funcionário for diferente de 0, significa que é uma atualização ou uma nova inserção com um ID específico
                sql = "INSERT INTO cadastro_funcionario (idFuncionario, Nome, Endereco, CEP, CPF, Telefone, Nascimento) VALUES (?, ?, ?, ?, ?, ?, ?)";
            }

            try (PreparedStatement pst = connection.prepareStatement(sql)) {
                if (funcionario.getIdFuncionario() == 0) {
                    // Se for um novo funcionário, configura os parâmetros sem o ID
                    pst.setString(1, funcionario.getNome());
                    pst.setString(2, funcionario.getEndereco());
                    pst.setString(3, funcionario.getCEP());
                    pst.setString(4, funcionario.getCPF());
                    pst.setString(5, funcionario.getTelefone());
                    pst.setString(6, funcionario.getNascimento());
                } else {
                    // Se for uma atualização ou nova inserção com ID específico, configura os parâmetros com o ID
                    pst.setInt(1, funcionario.getIdFuncionario());
                    pst.setString(2, funcionario.getNome());
                    pst.setString(3, funcionario.getEndereco());
                    pst.setString(4, funcionario.getCEP());
                    pst.setString(5, funcionario.getCPF());
                    pst.setString(6, funcionario.getTelefone());
                    pst.setString(7, funcionario.getNascimento());
                }

                pst.executeUpdate();
            }

            // Confirma as alterações no banco de dados
            connection.commit();

            // Ativa o autocommit novamente
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                // Em caso de erro, desfaz as alterações no banco de dados
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                // Trate a exceção conforme necessário
            }
        }
    }




     /*public void  saveOrUpdate(Funcionario funcionario) {
        if (funcionario.getIdFuncionario() == 0) {
            save(funcionario);
        } else {
            update(funcionario);
        }
    }*/
    /*public void saveOrUpdate(Funcionario funcionario) {
        String sql;

        if (funcionario.getIdFuncionario() == 0) {
            // Se o ID do funcionário for 0, significa que é um novo funcionário
            sql = "INSERT INTO cadastro_funcionario (Nome, Endereco, CEP, CPF, Telefone, Nascimento) VALUES (?, ?, ?, ?, ?, ?)";
        } else {
            // Se o ID do funcionário for diferente de 0, significa que é uma atualização
            sql = "UPDATE cadastro_funcionario SET Nome=?, Endereco=?, CEP=?, CPF=?, Telefone=?, Nascimento=? WHERE idFuncionario=?";
        }

        try (Connection conexao = Conexao.getConexao();
             PreparedStatement pst = conexao.prepareStatement(sql)) {

            pst.setString(1, funcionario.getNome());
            pst.setString(2, funcionario.getEndereco());
            pst.setString(3, funcionario.getCEP());
            pst.setString(4, funcionario.getCPF());
            pst.setString(5, funcionario.getTelefone());
            pst.setString(6, funcionario.getNascimento());

            if (funcionario.getIdFuncionario() != 0) {
                pst.setInt(7, funcionario.getIdFuncionario());  // Configura o ID para a cláusula WHERE
            }

            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Trate a exceção conforme necessário
        }
    }
    */
    
    public List<Funcionario> listarFuncionarios() throws SQLException {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT * FROM cadastro_funcionario";

        try ( Connection conexao = Conexao.getConexao();  PreparedStatement pst = conexao.prepareStatement(sql);  ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setIdFuncionario(rs.getInt("idFuncionario"));
                funcionario.setNome(rs.getString("Nome"));
                funcionario.setEndereco(rs.getString("Endereco"));
                funcionario.setCEP(rs.getString("CEP"));
                funcionario.setCPF(rs.getString("CPF"));
                funcionario.setTelefone(rs.getString("Telefone"));
                funcionario.setNascimento(rs.getString("Nascimento"));

                funcionarios.add(funcionario);
            }
        }

        return funcionarios;
    }


    public void delete(Funcionario funcionario) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM cadastro_funcionario WHERE idFuncionario=?");
            ps.setInt(1, funcionario.getIdFuncionario());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Funcionario deletado com sucesso!");
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean existeFuncionario(int id) {
    String sql = "SELECT * FROM cadastro_funcionario WHERE idFuncionario=?";
    
    try (PreparedStatement pst = connection.prepareStatement(sql)) {
        pst.setInt(1, id);
        try (ResultSet rs = pst.executeQuery()) {
            return rs.next();
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Erro ao verificar a existência do funcionário: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        return false;
    }
}
 
    public void excluirFuncionario(int id) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM cadastro_funcionario WHERE IdFuncionario=?");
            ps.setInt(1, id);
            ps.execute();
            JOptionPane.showMessageDialog(null, "Funcionario excluído com sucesso!");
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, "Erro ao excluir o funcionário: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }


    
    public List<Funcionario> getAll() {
        List<Funcionario> funcionarios = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM cadastro_funcionario");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Funcionario funcionario = new Funcionario();
                funcionario.setIdFuncionario(rs.getInt("IdFuncionario"));
                funcionario.setNome(rs.getString("Nome"));
                funcionario.setEndereco(rs.getString("Endereco"));
                funcionario.setCEP(rs.getString("CEP"));
                funcionario.setCPF(rs.getString("CPF"));
                funcionario.setTelefone(rs.getString("Telefone"));
                funcionario.setNascimento(rs.getString("Nascimento"));
                funcionarios.add(funcionario);
            }
            return funcionarios;
        } catch (SQLException ex) {
            Logger.getLogger(FuncionarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}
    

