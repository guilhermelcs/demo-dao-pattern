package application;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Seller;

public class Program {

	public static void main(String[] args) {
		
		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		List<Seller> sellers = sellerDao.findAll();
		
		for( Seller seller : sellers ) {
			System.out.println(seller);
		}
	}

}
