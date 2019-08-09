package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {

	private DepartmentDao departmentDao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll(){
		return departmentDao.findAll();
	}
	
	public void saveOrUpdate(Department dep) {
		if (dep.getId() == null) {
			departmentDao.insert(dep);
		
		}else {
			departmentDao.update(dep);
		}
	}
	
	public void remove(Department obj) {
		departmentDao.deleteById(obj.getId());
	}
}
