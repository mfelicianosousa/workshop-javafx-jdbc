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
import model.dao.DepartmentDao;
import model.entities.Department;
import model.entities.Seller;

public class DepartmentDaoJDBC implements DepartmentDao {

	private Connection conn;

	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department entity) {
		
		String cmdInsert = "INSERT INTO "
				.concat("Department ")
				.concat("(Name)")
				.concat("VALUES ") 
				.concat("( ? )");

		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement( cmdInsert, Statement.RETURN_GENERATED_KEYS );
            st.setString(1, entity.getName());
           
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
	public void update(Department entity) {

		PreparedStatement st = null;
		
		String cmdUpdate = "UPDATE "
				.concat("Department ")
				.concat("SET ")
				.concat("Name = ? ")
				.concat("WHERE ") 
				.concat("Id = ? ");

		try {
			st = conn.prepareStatement( cmdUpdate );
            st.setString(1, entity.getName());
            // 
            st.setInt(2, entity.getId());
            
			st.executeUpdate();
			
		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
		}

	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement st = null;
		
		String cmdDelete = "DELETE FROM "
				.concat("Department ")
				.concat("WHERE ") 
				.concat("Id = ? ");

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
	public Department findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		String cmdSelect = "SELECT "
				.concat("Id, Name ")
				.concat("FROM ")
				.concat("Department ")
				.concat("WHERE ")
				.concat("Id = ?");

		try {
			st = conn.prepareStatement( cmdSelect );
			st.setInt(1, id);
			rs = st.executeQuery();
			if (rs.next()) {
				Department entity = new Department();
			    entity.setId(rs.getInt("id"));
			    entity.setName(rs.getString("Name"));
				return entity;
			}
			return null;

		} catch (SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}

	}

	@Override
	public List<Department> findAll() {
		String cmdSelect = "SELECT "
				.concat("Id, Name ")
				.concat("FROM Department ")
	    		.concat("ORDER BY Name");

		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement( cmdSelect );

			rs = st.executeQuery();

			List<Department> list = new ArrayList<>();
	
			while (rs.next()) {

				Department department = new Department();
				department.setId(rs.getInt("id"));
				department.setName(rs.getString("Name"));
				
				list.add(department);
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
