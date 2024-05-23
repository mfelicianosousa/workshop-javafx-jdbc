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

	private Connection conn;

	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Seller entity) {
		
		String cmdSQL = "INSERT "
				.concat("INTO seller ")
				.concat("(Name, Email, BirthDate, BaseSalary, DepartmentId)")
				.concat("VALUES ") 
				.concat("(?, ?, ?, ?, ?)");

		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement( cmdSQL, Statement.RETURN_GENERATED_KEYS );
            st.setString(1, entity.getName());
            st.setString(2, entity.getEmail());
            st.setDate(3, new java.sql.Date(entity.getBirthDate().getTime()));
            st.setDouble(4, entity.getBaseSalary());
            st.setInt(5, entity.getDepartment().getId());
           
			int rowsAffected = st.executeUpdate();
			if (rowsAffected > 0) {
				rs = st.getGeneratedKeys();
				if (rs.next()) {
					int id = rs.getInt(1);
					entity.setId( id ); // update primary-key
				}
			} else {
				throw new DbException("Unexpected error. No rows affected!");
			}
			
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}

	@Override
	public void update(Seller entity) {

		String cmdUpdate = "UPDATE "
				.concat("seller ")
				.concat("SET ")
				.concat("Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? ")
				.concat("WHERE ") 
				.concat("Id = ? ");

		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement( cmdUpdate );
            st.setString(1, entity.getName());
            st.setString(2, entity.getEmail());
            st.setDate(3, new java.sql.Date(entity.getBirthDate().getTime()));
            st.setDouble(4, entity.getBaseSalary());
            st.setInt(5, entity.getDepartment().getId());
            // 
            st.setInt(6, entity.getId());
            
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}

	@Override
	public void deleteById(Integer id) {
		
		String cmdDelete = "DELETE FROM "
				.concat("seller ")
				.concat("WHERE ") 
				.concat("Id = ? ");

		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement( cmdDelete );
            st.setInt(1, id );
            
            int rows = st.executeUpdate();
            if (rows == 0) {
            	throw new DbException("Registro não encontrado! id = "+id);
            }
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Seller findById(Integer id) {
		String cmdSQL = "SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
				+ "ON seller.DepartmentId = department.Id " + "WHERE seller.Id = ?";

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(cmdSQL);
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {

				Department dep = instantiateDepartment(rs);

				Seller seller = instantiateSeller(rs, dep);
				return seller;
			}
			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}

	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller seller = new Seller();
		seller.setId(rs.getInt("id"));
		seller.setName(rs.getString("Name"));
		seller.setEmail(rs.getString("Email"));
		seller.setBaseSalary(rs.getDouble("BaseSalary"));
		seller.setBirthDate(rs.getDate("BirthDate"));
		seller.setDepartment(dep);
		return seller;
	}

	private Department instantiateDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();

		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));

		return dep;
	}

	@Override
	public List<Seller> findAll() {
		String cmdSQL = "SELECT "
				.concat("seller.*,department.Name as DepName ")
				.concat("FROM seller INNER JOIN department ")
				.concat("ON seller.DepartmentId = department.Id ") 
				.concat("ORDER BY Name");

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( cmdSQL );

			rs = st.executeQuery();

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while (rs.next()) {

				Department dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller seller = instantiateSeller(rs, dep);
				list.add(seller);
			}
			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		String cmdSQL = "SELECT seller.*,department.Name as DepName " + "FROM seller INNER JOIN department "
				+ "ON seller.DepartmentId = department.Id " + "WHERE DepartmentId = ? " + "ORDER BY Name";

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(cmdSQL);
			st.setInt(1, department.getId());
			rs = st.executeQuery();

			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();

			while (rs.next()) {

				Department dep = map.get(rs.getInt("DepartmentId"));
				if (dep == null) {
					dep = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				Seller seller = instantiateSeller(rs, dep);
				list.add(seller);
			}
			return list;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
	}

}
