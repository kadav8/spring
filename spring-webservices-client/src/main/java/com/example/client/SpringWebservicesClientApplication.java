package com.example.client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import services.wsdl.CreateMovieRequest;
import services.wsdl.CreateMovieResponse;
import services.wsdl.GetMovieRequest;
import services.wsdl.GetMovieResponse;
import services.wsdl.Movie;

@SpringBootApplication
public class SpringWebservicesClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWebservicesClientApplication.class, args);
	}

	@Bean
	CommandLineRunner lookup(WsClient client) {
		return args -> {
			Movie movie = new Movie();
			movie.setDirectorName("George Lucas");
			movie.setTitle("Star Wars");
			movie.setYear(1977);
			client.createMovie(movie);
			GetMovieResponse resp = client.getMovie("Star Wars");
			System.out.println(resp.getDirectorName());
		};
	}
}

class WsClient extends WebServiceGatewaySupport {

	public CreateMovieResponse createMovie(Movie movie) {
		CreateMovieRequest request = new CreateMovieRequest();
		request.setMovie(movie);
		CreateMovieResponse response = (CreateMovieResponse) getWebServiceTemplate()
				.marshalSendAndReceive("http://localhost:8080/ws/services.wsdl", request,
						new SoapActionCallback(
								"http://types.webservices.example.com/createMovieRequest"));
		return response;
	}

	public GetMovieResponse getMovie(String title) {
		GetMovieRequest request = new GetMovieRequest();
		request.setTitle(title);
		GetMovieResponse response = (GetMovieResponse) getWebServiceTemplate()
				.marshalSendAndReceive("http://localhost:8080/ws/services.wsdl", request,
						new SoapActionCallback(
								"http://types.webservices.example.com/getMovieRequest"));
		return response;
	}
}

@Configuration
class WsClientConfiguration {

	@Bean
	public Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("services.wsdl");
		return marshaller;
	}

	@Bean
	public WsClient countryClient(Jaxb2Marshaller marshaller) {
		WsClient client = new WsClient();
		client.setDefaultUri("http://localhost:8080/ws");
		client.setMarshaller(marshaller);
		client.setUnmarshaller(marshaller);
		return client;
	}
}