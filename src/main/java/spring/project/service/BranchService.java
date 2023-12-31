
package spring.project.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import spring.project.Config.ResponseStructure;
import spring.project.Exception.BankNotFound;
import spring.project.Exception.BranchNotFound;
import spring.project.dao.BankDao;
import spring.project.dao.BranchDao;
import spring.project.dao.ManagerDao;
import spring.project.dao.UserDao;
import spring.project.dto.Bank;
import spring.project.dto.Branch;
import spring.project.dto.Manager;
import spring.project.dto.User;

@Service
public class BranchService {
	@Autowired
	BranchDao bndao;
	
	@Autowired
	BankDao bdao;
	
	@Autowired
	ManagerDao mdao;
	
	@Autowired
	UserDao udao;
	
	public ResponseEntity<ResponseStructure<Branch>> saveBranch(Branch b, int bId) 
	{
		ResponseStructure<Branch> rs = new ResponseStructure<>();
		Bank exBank =bdao.findBank(bId);
		Branch savedBranch = bndao.saveBranch(b);
		exBank.getListbranch().add(savedBranch);
		savedBranch.setBank(exBank);
		bndao.updateBranch(savedBranch, savedBranch.getId());
		
		rs.setData(savedBranch);
		rs.setMsg("Branch Has Been Saved");
		rs.setStatus(HttpStatus.CREATED.value());
		return new ResponseEntity<ResponseStructure<Branch>>(rs,HttpStatus.CREATED );
	}
	
	public ResponseEntity<ResponseStructure<Branch>> findBranch(int id) 
	{
		ResponseStructure<Branch> rs = new ResponseStructure<>();
		if (bndao.findBranch(id)!=null) {
		rs.setData(bndao.findBranch(id));
		rs.setMsg("Branch Has Been Found");
		rs.setStatus(HttpStatus.FOUND.value());
		return new ResponseEntity<ResponseStructure<Branch>>(rs,HttpStatus.FOUND );
		}
		throw new BranchNotFound("No Branch Present With the given id");
	}
	
	public ResponseEntity<ResponseStructure<Branch>> updateBranch(Branch b , int id) 
	{
		ResponseStructure<Branch> rs = new ResponseStructure<>();
		if (bndao.findBranch(id)!=null) {
		rs.setData(bndao.updateBranch(b,id));
		rs.setMsg("Branch Has Been Updated");
		rs.setStatus(HttpStatus.OK.value());
		return new ResponseEntity<ResponseStructure<Branch>>(rs,HttpStatus.OK );
		}
		throw new BranchNotFound("No Branch Present With the given id");
		
	}
	
	public ResponseEntity<ResponseStructure<Branch>> deleteBranch( int id) 
	{
		ResponseStructure<Branch> rs = new ResponseStructure<>();
		if (bndao.findBranch(id)!=null) {
			Branch b = bndao.findBranch(id);
			b.setBank(null);
			bndao.updateBranch(b, id);
			rs.setData(bndao.deleteBranch(id));
			rs.setMsg("Branch Has Been Deleted");
			rs.setStatus(HttpStatus.OK.value());
			return new ResponseEntity<ResponseStructure<Branch>>(rs,HttpStatus.OK );
		}
		throw new BranchNotFound("No Branch Present With the given id");
		
	}
	
	public ResponseEntity<ResponseStructure<List<Branch>>>findAllBranch() {
		ResponseStructure<List<Branch>> rs = new ResponseStructure<>();
		if (!bndao.getAllList().isEmpty()) {
			rs.setData(bndao.getAllList());
			rs.setMsg("Branchs are Found");
			rs.setStatus(HttpStatus.FOUND.value());
			return new ResponseEntity<ResponseStructure<List<Branch>>>(rs,HttpStatus.FOUND );
		}
		throw new BranchNotFound("No Branch Present With the given id");
	}
	
	public ResponseEntity<ResponseStructure<Branch>>  assignBank(int bId,int bhId) 
	{
		ResponseStructure<Branch> rs = new ResponseStructure<>();
		if (bndao.findBranch(bhId)!=null) {
			if (bdao.findBank(bId)!=null) {
				Branch bh = bndao.findBranch(bhId);
				bh.setBank(bdao.findBank(bId));
				rs.setData(bndao.updateBranch(bh,bhId));
				rs.setStatus(HttpStatus.OK.value());
				rs.setMsg("Bank Has Been Assigned");
				return new ResponseEntity<ResponseStructure<Branch>>(rs,HttpStatus.OK ); 
			}
			throw new BankNotFound("No Bank Present With the given id");
		}
		throw new BranchNotFound("No Branch Present With the given id");
	}
	
	public ResponseEntity<ResponseStructure<User>> changeBranch(String mn,String mp,int uid,int bid){
		ResponseStructure<User> rs = new ResponseStructure<>();
		Manager m = mdao.loginManager(mn, mp);
		User u = udao.findUser(uid);
		Branch b = bndao.findBranch(bid);
		if (m!=null) {
			if (u!=null) {
				if (b!=null) {
					if (u.getBranch().getId()!=bid) {
						Branch exB = u.getBranch();
						exB.getListuser().remove(u);
						bndao.updateBranch(exB, exB.getId());
						u.setBranch(b);
						b.getListuser().add(u);
						bndao.updateBranch(b, bid);
						rs.setData(udao.updateUser(u, uid));
						rs.setMsg("Your Branch Has Been changed ");
						rs.setStatus(HttpStatus.OK.value());
						return new ResponseEntity<ResponseStructure<User>>(rs,HttpStatus.OK);
					}
					return null;
				}return null;
			}return null;
		}
		return null;
	}
}
