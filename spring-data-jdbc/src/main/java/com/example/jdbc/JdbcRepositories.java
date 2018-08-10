package com.example.jdbc;

import java.util.Collection;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.jdbc.mapping.model.NamingStrategy;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

@Order(7)
@Component
public class JdbcRepositories implements ApplicationRunner {

	@Autowired
	CustomerRepository customerRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Stream.of("A", "B", "C")
				.forEach(name -> customerRepository.save(new Customer(null, name, name + '@' + name + ".com")));
		customerRepository.findAll().forEach(System.out::println);

		customerRepository.save(new Customer(null, "foo", "bar"));

		customerRepository.findByEmail("bar").forEach(System.out::println);
	}
}

interface CustomerRepository extends CrudRepository<Customer, Long> {
	@Query("select * from customers c where c.email = :email")
	Collection<Customer> findByEmail(String email);
}

@Configuration
@EnableJdbcRepositories
class SpringDataJdbcConfiguration {
	@Bean
	NamingStrategy namingStrategy() {
		return new NamingStrategy() {
			@Override
			public String getTableName(Class<?> type) {
				return type.getSimpleName().toLowerCase() + "s";
			}
		};
	}
}
