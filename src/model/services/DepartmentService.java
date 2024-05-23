package model.services;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
	
	private DepartmentDao dao = DaoFactory.createDepartmentDao();
	
	
	/* Moock 
	public List<Department> findAll(){
		List<Department> list = new ArrayList<>();
		list.add(new Department(1, "Book"));
		list.add(new Department(2, "Computador"));
		list.add(new Department(3, "Electronics"));
		return list;
	}
    */
	
	public List<Department> findAll(){
       return dao.findAll();
	}
	
	public void saveOrUpdate(Department entity) {
		if (entity.getId() == null) {
			
			dao.insert(entity);
			
		} else {
		
			dao.update(entity);
		}
	}
	
	public void remove(Department entity) {
		
		dao.deleteById( entity.getId() );
		
	}
}
