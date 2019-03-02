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
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import com.example.jdbc.dto.Customer;
import com.example.jdbc.dto.Reservation;

@SpringBootApplication
public class JdbcApplication {
	public static void main(String[] args) {
		SpringApplication.run(JdbcApplication.class, args);
	}
}

@Order(1)
@Component
class QueryCustomersAndReservationsCount implements ApplicationRunner {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public static class CustomerReservationReport {
		private Long customerId;
		private String name, email;
		private int reservationCount;

		public CustomerReservationReport(Long customerId, String name, String email, int reservationCount) {
			this.customerId = customerId;
			this.name = name;
			this.email = email;
			this.reservationCount = reservationCount;
		}

		@Override
		public String toString() {
			return "CustomerOrderReport [customerId=" + customerId + ", name=" + name
					+ ", email=" + email + ", reservationCount=" + reservationCount + "]";
		}
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Collection<CustomerReservationReport> reports =
				jdbcTemplate.query(
				"select c.*, (  select count(o.id) from reservations o where o.customers = c.id  ) as count from customers c ",
				(rs, rowNum) -> new CustomerReservationReport(rs.getLong("id"), rs.getString("name"), rs.getString("email"), rs.getInt("count")));

		System.out.println("--------------");
		System.out.println("QUERY 1");
		reports.forEach(System.out::println);
		System.out.println("--------------");
	}
}

@Order(2)
@Component
class QueryCustomersAndReservations implements ApplicationRunner {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		ResultSetExtractor<Collection<Customer>> rsExtractor = rs -> {
			Map<Long, Customer> customerMap = new HashMap<>();
			Customer currentCustomer = null;
			while (rs.next()) {
				long id = rs.getLong("cid");
				if (currentCustomer == null || currentCustomer.getId() != id) {
					currentCustomer = new Customer(rs.getLong("cid"), rs.getString("name"), rs.getString("email"), new HashSet<>());
				}
				String customerFk = rs.getString("customers");
				if (customerFk != null) {
					String sku = rs.getString("sku");
					Long oid = rs.getLong("oid");
					Reservation res = new Reservation(oid, sku);
					currentCustomer.getReservations().add(res);
				}
				customerMap.put(currentCustomer.getId(), currentCustomer);
			}
			return customerMap.values();
		};

		Collection<Customer> customers = this.jdbcTemplate.query(
				"select cust.id as cid, cust.*, ord.id as oid, ord.* "
						+ "from customers cust left join reservations ord on cust.id = ord.customers " + "order by cid ",
				rsExtractor);

		System.out.println("QUERY 2");
		customers.forEach(System.out::println);
		System.out.println("--------------");
	}
}

@Order(3)
@Component
class QueryCustomersAndReservationsSimpleFlatMapper implements ApplicationRunner {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		ResultSetExtractorImpl<Customer> rse = JdbcTemplateMapperFactory.newInstance()
				.addKeys("id")
				.newResultSetExtractor(Customer.class);

		Collection<Customer> customers =
				this.jdbcTemplate
				.query(
					"select c.id as id, c.name as name, c.email as email, r.id as reservations_id, r.sku as reservations_sku "
							+ "from customers c left outer join reservations r on r.customers = c.id  " + "order by c.id  ", rse)
				.stream()
				.map(c -> {
					boolean hasNullOrderValues = c.getReservations().stream().anyMatch(r -> r.getId() == null);
					if (hasNullOrderValues) {
						c.setReservations(new HashSet<>());
					}
					return c;
				})
				.collect(Collectors.toSet());

		System.out.println("QUERY 3");
		customers.forEach(System.out::println);
		System.out.println("--------------");
	}
}

@Order(4)
@Component
class JdbcTemplateWriter implements ApplicationRunner {

	@Autowired
	JdbcTemplate jdbcTemplate;

	RowMapper<Customer> customerRowMapper = (rs, rowNum) -> new Customer(rs.getLong("id"), rs.getString("name"), rs.getString("email"));

	public Customer insert(String name, String email) {
		GeneratedKeyHolder generatedKeyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(con -> {
			PreparedStatement preparedStatement = con.prepareStatement(
					"insert into customers (name, email) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, name);
			preparedStatement.setString(2, email);
			return preparedStatement;
		}, generatedKeyHolder);

		long idOfNewCustomer = generatedKeyHolder.getKey().longValue();
		return jdbcTemplate.queryForObject("select c.* from customers c where c.id = ? ", customerRowMapper, idOfNewCustomer);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		System.out.println("QUERY 4");
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
		System.out.println("QUERY 5");
		Stream.of("A", "B", "C").forEach(name -> insert(name, name + '@' + name + ".com"));
		this.all.execute().forEach(System.out::println);
		System.out.println("--------------");
	}
}