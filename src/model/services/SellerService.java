package model.services;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class SellerService {

	private SellerDao departmentDao = DaoFactory.createSellerDao();
	
	public List<Seller> findAll(){
		return departmentDao.findAll();
	}
	
	public void saveOrUpdate(Seller dep) {
		if (dep.getId() == null) {
			departmentDao.insert(dep);
		
		}else {
			departmentDao.update(dep);
		}
	}
	
	public void remove(Seller obj) {
		departmentDao.deleteById(obj.getId());
	}
}
