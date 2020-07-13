package application;

import java.util.Date;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		Department dp = new Department(1, "Computers");
		Seller seller = new Seller(null, "Gabriella", "gaby@gmail.com", new Date(), 12500.00, dp);
		sellerDao.insert(seller);
	}

}
