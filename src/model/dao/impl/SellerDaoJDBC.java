package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection con;

	public SellerDaoJDBC(Connection connection) {
		this.con = connection;
	}

	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement(
					"INSERT INTO seller "
					+ "(Name, Email, BirthDate, BaseSalary, DepartmentId) "
					+ "VALUES "
					+ "(?, ?, ?, ?, ?)"
					, Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			if (rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			
			}else {
				throw new DbException("Erro inesperado, nenhuma linha foi afetada!");
			}
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		
		}finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void update(Seller obj) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement(
					"UPDATE seller "
					+"SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? "
					+"WHERE Id = ?");
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartment().getId());
			st.setInt(6, obj.getId());
			
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		
		}finally {
			DB.closeStatement(st);
		}



	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		try {
			st = con.prepareStatement("DELETE FROM seller WHERE Id = ?");
			st.setInt(1, id);
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		
		}finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public Seller findByid(Integer id) {
		PreparedStatement pStatement = null;
		ResultSet rSet = null;
		try {
			pStatement = con.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE seller.Id = ?");

			pStatement.setInt(1, id);
			rSet = pStatement.executeQuery();
			if (rSet.next()) {
				Department department = instantiateDepartment(rSet);
				Seller obj = instantiateSeller(rSet, department);
				return obj;
			}
			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeStatement(pStatement);
			DB.closeResultSet(rSet);
		}
	}

	private Seller instantiateSeller(ResultSet rSet, Department department) throws SQLException {
		Seller obj = new Seller();
		obj.setId(rSet.getInt("Id"));
		obj.setName(rSet.getString("Name"));
		obj.setEmail(rSet.getString("Email"));
		obj.setBaseSalary(rSet.getDouble("BaseSalary"));
		obj.setBirthDate(rSet.getDate("BirthDate"));
		obj.setDepartment(department);
		return obj;
	}

	private Department instantiateDepartment(ResultSet rSet) throws SQLException {
		Department dep = new Department();
		dep.setId(rSet.getInt("DepartmentId"));
		dep.setName(rSet.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement pStatement = null;
		ResultSet rSet = null;
		try {
			pStatement = con.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "ORDER BY Name");

			rSet = pStatement.executeQuery();

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			while (rSet.next()) {

				Department depart = map.get(rSet.getInt("DepartmentId"));

				if (depart == null) {
					depart = instantiateDepartment(rSet);
					map.put(rSet.getInt("DepartmentId"), depart);
				}

				Seller obj = instantiateSeller(rSet, depart);
				list.add(obj);
			}
			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeStatement(pStatement);
			DB.closeResultSet(rSet);
		}

	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement pStatement = null;
		ResultSet rSet = null;
		try {
			pStatement = con.prepareStatement(
					"SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
							+ "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? " + "ORDER BY Name");

			pStatement.setInt(1, department.getId());
			rSet = pStatement.executeQuery();

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			while (rSet.next()) {

				Department depart = map.get(rSet.getInt("DepartmentId"));

				if (depart == null) {
					depart = instantiateDepartment(rSet);
					map.put(rSet.getInt("DepartmentId"), depart);
				}

				Seller obj = instantiateSeller(rSet, department);
				list.add(obj);
			}
			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());

		} finally {
			DB.closeStatement(pStatement);
			DB.closeResultSet(rSet);
		}
	}

}
