package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class Program {

	public static void main(String[] args) {
		
		DepartmentDao depDao = DaoFactory.createDepartmentDao();
		List<Department> deps = depDao.findAll();
		
		for( Department dep : deps ) {
			System.out.println(dep);
		}
	}

}
