package ch.mywebsite.yannhoh;

import ch.mywebsite.yannhoh.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class HttpRequestTest {

    ObjectMapper mapper = new ObjectMapper();

    @LocalServerPort
    private int port = 8080;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void addUserTest() {
        User user = new User("richi", "123", "he@ho.com");
//        RequestEntity<User> request = this.restTemplate.postForObject(user, )
    }

    @Test
    public void getAllUsersTest() {
        ResponseEntity<Object[]> response = this.restTemplate.getForEntity("http://localhost:" + port + "/user",
                Object[].class);

        List<User> userList = Arrays.stream(Objects.requireNonNull(response.getBody()))
                .map(obj -> mapper.convertValue(obj, User.class))
                .collect(Collectors.toList());

        assertThat(response.getStatusCode()).isSameAs(HttpStatus.OK);
        assertThat(userList.get(0).getId()).isEqualTo(1L);
        assertThat(userList.get(0).getUsername()).isEqualTo("richi");
        assertThat(userList.get(0).getEmail()).isEqualTo("he@ho.com");
        assertThat(userList.get(0).getPassword()).isEqualTo("123");
        assertThat(userList.get(0).getRole()).isSameAs(Role.USER);

    }
}