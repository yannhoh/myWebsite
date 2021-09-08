package ch.mywebsite.yannhoh;

import ch.mywebsite.yannhoh.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class APITestHelper {
    TestRestTemplate restTemplate;

    public APITestHelper() {
        restTemplate = new TestRestTemplate();
    }

    public List<User> convertUserList(ResponseEntity<Object[]> responseEntity) {
        ObjectMapper mapper = new ObjectMapper();
        return Arrays.stream(Objects.requireNonNull(responseEntity.getBody()))
                .map(obj -> mapper.convertValue(obj, User.class))
                .collect(Collectors.toList());
    }

    public List<User> getAllUserList(String urlUser) {
        ResponseEntity<Object[]> responseEntity = restTemplate.getForEntity(urlUser,
                Object[].class);
        assertThat(responseEntity.getStatusCode()).isSameAs(HttpStatus.OK);
        return convertUserList(responseEntity);
    }

    public void deleteUser(Long id, String url) {
        User user = new User("", "", "");
            user.setId(id);
            restTemplate.exchange(url, HttpMethod.DELETE,
                    new HttpEntity<>(user),
                    String.class);
        }

    public void deleteTheseUsers(Long[] idArr, String url) {
        User user = new User("", "", "");
        for (Long id : idArr) {
            user.setId(id);
            restTemplate.exchange(url, HttpMethod.DELETE,
                    new HttpEntity<>(user),
                    String.class);
        }
    }
}
