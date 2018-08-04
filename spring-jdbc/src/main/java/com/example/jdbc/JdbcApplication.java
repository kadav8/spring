package com.example.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.sql.DataSource;

import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory;
import org.simpleflatmapper.jdbc.spring.ResultSetExtractorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.annotation.Id;
import org.springframework.data.jdbc.mapping.model.NamingStrategy;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SpringBootApplication
public class JdbcApplication {
	public static void main(String[] args) {
		SpringApplication.run(JdbcApplication.class, args);
	}
}

@Order(1)
@Component
class QueryCustomersAndOrdersCount implements ApplicationRunner {
		@Autowired JdbcTemplate jdbcTemplate;

		@Data
		@AllArgsConstructor
		public static class CustomerOrderReport {
				private Long customerId;
				private String name, email;
				private int orderCount;
		}

		@Override
		public void run(ApplicationArguments args) throws Exception {
				Collection<CustomerOrderReport> reports =
					jdbcTemplate.query("select c.*, (  select count(o.id) from orders o where o.customer_fk = c.id  ) as count from customers c ",
							(rs, rowNum) -> new CustomerOrderReport(rs.getLong("id"), rs.getString("name"), rs.getString("email"), rs.getInt("count")));
				reports.forEach(System.out::println);
				System.out.println("--------------");
		}
}

@Order(2)
@Component
class QueryCustomersAndOrders implements ApplicationRunner {
		@Autowired JdbcTemplate jdbcTemplate;

		@Data
		@AllArgsConstructor
		public static class Customer {
				private Long id;
				private String name, email;
				private Set<Order> orders = new HashSet<>();
		}

		@Data
		@AllArgsConstructor
		public static class Order {
				private Long id;
				private String sku;
		}

		@Override
		public void run(ApplicationArguments args) throws Exception {
				ResultSetExtractor<Collection<Customer>> rse = rs -> {
						Map<Long, Customer> customerMap = new HashMap<>();
						Customer currentCustomer = null;
						while (rs.next()) {
								long id = rs.getLong("cid");
								if (currentCustomer == null || currentCustomer.getId() != id) {
										currentCustomer = new Customer(rs.getLong("cid"), rs.getString("name"), rs.getString("email"), new HashSet<>());
								}
								String customerFk = rs.getString("customer_fk");
								if (customerFk != null) {
										String sku = rs.getString("sku");
										Long oid = rs.getLong("oid");
										Order order = new Order(oid, sku);
										currentCustomer.getOrders().add(order);
								}
								customerMap.put(currentCustomer.getId(), currentCustomer);
						}
						return customerMap.values();
				};

				Collection<Customer> customers = this.jdbcTemplate
					.query("select cust.id as cid, cust.*, ord.id as oid, ord.* "
							+ "from customers cust left join orders ord on cust.id = ord.customer_fk "
							+ "order by cid ", rse);
				customers.forEach(System.out::println);
				System.out.println("--------------");
		}
}

@Order(3)
@Component
class QueryCustomersAndOrdersSimpleFlatMapper implements ApplicationRunner {
		@Autowired JdbcTemplate jdbcTemplate;

		@Data
		@AllArgsConstructor
		public static class Customer {
				private Long id;
				private String name, email;
				private Set<Order> orders = new HashSet<>();
		}

		@Data
		@AllArgsConstructor
		public static class Order {
				private Long id;
				private String sku;
		}

		@Override
		public void run(ApplicationArguments args) throws Exception {
				ResultSetExtractorImpl<Customer> rse = JdbcTemplateMapperFactory
						.newInstance().addKeys("id").newResultSetExtractor(Customer.class);

				Collection<Customer> customers = this.jdbcTemplate
					.query("select c.id as id, c.name as name, c.email as email, o.id as orders_id, o.sku as orders_sku "
							+ "from customers c left outer join orders o on o.customer_fk = c.id  "
							+ "order by c.id  ", rse);

				customers = customers.stream()
					.map(c -> {
							boolean hasNullOrderValues = c.getOrders().stream().anyMatch(o -> o.getId() == null);
							if (hasNullOrderValues) {
									c.setOrders(new HashSet<>());
							}
							return c;
					})
					.collect(Collectors.toSet());

				customers.forEach(System.out::println);
				System.out.println("--------------");
		}
}

@Order(4)
@Component
class JdbcTemplateWriter implements ApplicationRunner {
		@Autowired JdbcTemplate jdbcTemplate;

		RowMapper<Customer> customerRowMapper = (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("name"), rs.getString("email"));

		@Data
		@AllArgsConstructor
		public static class Customer {
				private Long id;
				private String name, email;
		}

		public Customer insert(String name, String email) {
				GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

				jdbcTemplate.update(con -> {
						PreparedStatement preparedStatement = con.prepareStatement("insert into customers (name, email) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
						preparedStatement.setString(1, name);
						preparedStatement.setString(2, email);
						return preparedStatement;
				}, generatedKeyHolder);

				long idOfNewCustomer = generatedKeyHolder.getKey().longValue();
				return jdbcTemplate.queryForObject("select c.* from customers c where c.id = ? ", customerRowMapper, idOfNewCustomer);
		}

		@Override
		public void run(ApplicationArguments args) throws Exception {
				Stream.of("A", "B", "C").forEach(name -> insert(name, name + '@' + name + ".com"));
				this.jdbcTemplate.query("select * from customers order by id ", customerRowMapper).forEach(System.out::println);
				System.out.println("--------------");
		}
}

@Order(5)
@Component
class JdbcObjectWriter implements ApplicationRunner {

		private final SimpleJdbcInsert simpleJdbcInsert;
		private final CustomerMappingSqlQuery all, byId;

		@Data
		@AllArgsConstructor
		public static class Customer {
				private Long id;
				private String name, email;
		}

		JdbcObjectWriter(DataSource ds) {
				this.simpleJdbcInsert = new SimpleJdbcInsert(ds).withTableName("customers").usingGeneratedKeyColumns("id");
				this.all = new CustomerMappingSqlQuery(ds, "select * from customers");
				this.byId = new CustomerMappingSqlQuery(ds, "select * from customers where id = ?", new SqlParameter("id", Types.INTEGER));
		}

		private static class CustomerMappingSqlQuery extends MappingSqlQuery<Customer> {
				public CustomerMappingSqlQuery(DataSource ds, String sql, SqlParameter... params) {
						setDataSource(ds);
						setSql(sql);
						setParameters(params);
						afterPropertiesSet();
				}

				@Override
				protected Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
						return new Customer(rs.getLong("id"), rs.getString("name"), rs.getString("email"));
				}
		}

		public Customer insert(String name, String email) {
				Map<String, Object> params = new HashMap<>();
				params.put("name", name);
				params.put("email", email);
				Long id = this.simpleJdbcInsert.executeAndReturnKey(params).longValue();
				return this.byId.findObject(id);
		}

		@Override
		public void run(ApplicationArguments args) throws Exception {
				Stream.of("A", "B", "C").forEach(name -> insert(name, name + '@' + name + ".com"));
				this.all.execute().forEach(System.out::println);
				System.out.println("--------------");
		}
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Customer {
		@Id
		private Long id;
		private String name, email;
}


@Repository
interface CustomerRepository extends CrudRepository<Customer, Long> {
		@Query("select * from customers c where c.email = :email")
		Collection<Customer> findByEmail(@Param("email") String email);
}

@Order(6)
@Component
class SpringDataJdbc implements ApplicationRunner {
		@Autowired CustomerRepository customerRepository;

		@Override
		public void run(ApplicationArguments args) throws Exception {
				Stream.of("A", "B", "C").forEach(name -> customerRepository.save(new Customer(null, name, name + '@' + name + ".com")));
				customerRepository.findAll().forEach(System.out::println);
				customerRepository.save(new Customer(null, "foo", "bar"));
				customerRepository.findByEmail("bar").forEach(System.out::println);
		}
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
