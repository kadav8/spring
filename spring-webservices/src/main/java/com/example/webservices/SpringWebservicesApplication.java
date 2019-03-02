package com.example.webservices;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import com.example.webservices.types.CreateMovieRequest;
import com.example.webservices.types.CreateMovieResponse;
import com.example.webservices.types.GetMovieRequest;
import com.example.webservices.types.GetMovieResponse;
import com.example.webservices.types.Movie;

@SpringBootApplication
public class SpringWebservicesApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringWebservicesApplication.class, args);
	}
}

@Endpoint
class WebServiceEndpoint {
	private static final Logger log = LoggerFactory.getLogger(WebServiceEndpoint.class);
    public static final String NAMESPACE_URI = "http://types.webservices.example.com";

    Map<String, Movie> map = new HashMap<>();

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "createMovieRequest")
    @ResponsePayload
    public CreateMovieResponse createMovie(@RequestPayload CreateMovieRequest createMovieRequest) {
        CreateMovieResponse response = new CreateMovieResponse();
        try {
            log.info("Movie creating called");
            Movie m = createMovieRequest.getMovie();
            map.put(m.getTitle(), m);
            response.setErrorCode(0);
        } catch(Exception e) {
            response.setResult("Movie creating failed: " + e.getMessage());
            response.setErrorCode(1);
            log.error(response.getResult(), e);
        }
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getMovieRequest")
    @ResponsePayload
    public GetMovieResponse getMovie(@RequestPayload GetMovieRequest getMovieRequest) {
    	GetMovieResponse response = new GetMovieResponse();
        try {
        	log.info("Get movie called");
        	if(map.containsKey(getMovieRequest.getTitle())) {
        		Movie movie = map.get(getMovieRequest.getTitle());
        		response.setTitle(movie.getTitle());
        		response.setDirectorName(movie.getDirectorName());
        		response.setYear(movie.getYear());
        		response.setErrorCode(0);
        	}
        } catch(Exception e) {
            response.setResult("Get movie failed: " + e.getMessage());
            response.setErrorCode(1);
            log.error(response.getResult(), e);
        }
        return response;
    }
}

@EnableWs
@Configuration
class WebServiceConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<MessageDispatcherServlet>(servlet, "/ws/*");
    }

    @Bean(name = "Services")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema schema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("ServicesPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace(WebServiceEndpoint.NAMESPACE_URI);
        wsdl11Definition.setSchema(schema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema schema() {
        return new SimpleXsdSchema(new ClassPathResource("xsd/types.xsd"));
    }
}