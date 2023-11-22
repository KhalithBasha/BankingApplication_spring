package spring.project.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import spring.project.dto.Address;

public interface AddressRepo extends JpaRepository<Address,Integer>{

}
