package spring.project.dto;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Component
@Entity
@Getter
@Setter	
public class Branch {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String bname;
	private String ifsc;
	@OneToMany
	private User user;
	@ManyToOne
	private Bank bank;
	@OneToOne
	private Address adrs;
	@OneToOne
	private Manager mng;
	
}