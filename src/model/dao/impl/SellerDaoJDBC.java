package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
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
	
	public SellerDaoJDBC( Connection conn ) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"INSERT INTO seller " +
					"(Name, Email, BirthDate, BaseSalary, DepartmentId)" +
					"VALUES" +
					"(?, ?, ?, ? , ?)", Statement.RETURN_GENERATED_KEYS
					);
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new Date( obj.getBirthDate().getTime()) );
			st.setDouble(4, obj.getSalary());
			st.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if( rowsAffected > 0 ) {
				ResultSet rs = st.getGeneratedKeys();
				if( rs.next() ) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpeceted Error: No rows affected!");
			}
		}
		catch(SQLException e) {
			throw new DbException( e.getMessage() );
		}
		finally {
			DB.closeStatement(st);
			DB.closeConnection();
		}
			
	}

	@Override
	public void update(Seller obj) {
		
		
	}

	@Override
	public void deleteById(Integer id) {
		
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {	
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as DepName " +
					"FROM seller INNER JOIN department " +
					"ON seller.DepartmentId = department.Id " +
					"WHERE seller.Id = ? "
					);
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			if( rs.next() ) {
				Department dep = new Department(rs.getInt("Id"), rs.getString("DepName"));
				Seller seller = new Seller(rs.getInt("Id"), rs.getString("Name"), 
										   rs.getString("Email"), rs.getDate("BirthDate"), 
										   rs.getDouble("BaseSalary"), dep);
				return seller;
			}
			
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
	
	@Override
	public List<Seller> findByDepartment(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Seller> sellers = new ArrayList<>();
		
		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as depName " +
					"FROM seller INNER JOIN department " +
					"ON seller.departmentId = department.Id " +
					"WHERE department.Id = ? " +
					"ORDER BY Name"
					);
			st.setInt(1, id);
			rs = st.executeQuery();
			
			Map<Integer, Department> map = new HashMap<>();
			Department dep = null;
			
			while( rs.next() ) {
				dep =  map.get(rs.getInt("DepartmentId"));
				if( dep == null ) {
					dep = new Department(rs.getInt("Id"), rs.getString("DepName"));
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				sellers.add( new Seller(rs.getInt("Id"), rs.getString("Name"), rs.getString("Email"), 
								   		rs.getDate("BirthDate"), rs.getDouble("BaseSalary"), dep) 
							);
			}
			return sellers;
			
		}
		catch (SQLException e) {
			throw new DbException(e.getLocalizedMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Seller> sellers = new ArrayList<>();
		
		try {
			st = conn.prepareStatement(
					"SELECT seller.*, department.Name as depName " +
					"FROM seller INNER JOIN department " +
					"ON seller.departmentId = department.Id " +
					"ORDER BY Id"
					);
			rs = st.executeQuery();
			
			Map<Integer, Department> map = new HashMap<>();
			Department dep = null;
			
			while( rs.next() ) {
				dep =  map.get(rs.getInt("DepartmentId"));
				if( dep == null ) {
					dep = new Department(rs.getInt("Id"), rs.getString("DepName"));
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				sellers.add( new Seller(rs.getInt("Id"), rs.getString("Name"), rs.getString("Email"), 
								   		rs.getDate("BirthDate"), rs.getDouble("BaseSalary"), dep) 
							);
			}
			return sellers;
			
		}
		catch (SQLException e) {
			throw new DbException(e.getLocalizedMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}
}
