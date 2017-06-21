package pl.edu.agh.sius;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@SpringBootApplication
@EnableZuulProxy
public class EdgeApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdgeApplication.class, args);
	}


	@Bean
	public ZuulFallbackProvider steamFallbackProvider() {
		return new ZuulFallbackProvider() {
			@Override
			public String getRoute() {
				return "steam-service";
			}

			@Override
			public ClientHttpResponse fallbackResponse() {
				return getClientHttpResponse("fallback");
			}
		};
	}

	@Bean
	public ZuulFallbackProvider twitterFallbackProvider() {
		return new ZuulFallbackProvider() {
			@Override
			public String getRoute() {
				return "twitter-service";
			}

			@Override
			public ClientHttpResponse fallbackResponse() {
				return getClientHttpResponse("fallback");
			}
		};
	}


	private ClientHttpResponse getClientHttpResponse(String response) {
		return new ClientHttpResponse() {
            @Override
            public HttpStatus getStatusCode() throws IOException {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() throws IOException {
                return 200;
            }

            @Override
            public String getStatusText() throws IOException {
                return "OK";
            }

            @Override
            public void close() {

            }

            @Override
            public InputStream getBody() throws IOException {
                return new ByteArrayInputStream(response.getBytes());
            }

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                return headers;
            }
        };
	}
}
