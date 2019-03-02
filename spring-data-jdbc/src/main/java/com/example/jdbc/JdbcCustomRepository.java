package com.example.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.example.jdbc.dto.Customer;

@Order(6)
@Component
public class JdbcCustomRepository implements ApplicationRunner {

	@Autowired
	MyCustomerRepository repository;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Customer c = new Customer(null, "Teszt Elek", "elek@gmail.com");
		Customer savedC = repository.insert(c);

		System.out.println("QUERY 6");
		System.out.println(repository.findAll());
		System.out.println(repository.findById(savedC.getId()));
		System.out.println("--------------");
	}
}

@Service
class MyCustomerRepository {

	private final JdbcTemplate jdbcTemplate;
	private final RowMapper<Customer> rowMapper;
	private final SimpleJdbcInsert simpleJdbcInsert;

	public MyCustomerRepository(JdbcTemplate jdbcTemplate) {
		this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("customers").usingGeneratedKeyColumns("id");
		this.jdbcTemplate = jdbcTemplate;
		this.rowMapper = new BeanPropertyRowMapper<>(Customer.class);
	}

	public Customer findById(long id) {
	    return jdbcTemplate.queryForObject("select * from customers where id=?", new Object[] { id }, rowMapper);
	}

	public List<Customer> findAll() {
	    return jdbcTemplate.query("select * from customers", rowMapper);
	}

	public Customer insert(Customer customer) {
		Map<String, Object> params = new HashMap<>();
		params.put("name", customer.getName());
		params.put("email", customer.getEmail());
		Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
		return findById(id);
	}
}