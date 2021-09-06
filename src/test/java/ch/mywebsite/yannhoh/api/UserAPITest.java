package ch.mywebsite.yannhoh.api;

import ch.mywebsite.yannhoh.Role;
import ch.mywebsite.yannhoh.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserAPITest {

    private String urlUser;
    private ObjectMapper mapper;
    private User firstUser, secondUser, thirdUser, usernametakenUser, emailtakenUser;

    @LocalServerPort
    private int port = 8080;

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        urlUser = "http://localhost:" + port + "/user";
        firstUser = new User("richi", "123**ho?", "hee@ho.com");
        usernametakenUser = new User("richi", "123**ho?", "haa@ho.com");
        emailtakenUser = new User("richardine", "123**ho?", "hee@ho.com");
        secondUser = new User("richardine", "456**ha?", "haa@ho.com");
        thirdUser = new User("richardinho", "789**hi?", "hii@ho.com");
    }

    @Test
    public void addAndGetAllUserTest() {
        //Add first user
        ResponseEntity<Object> response = this.restTemplate.postForEntity(urlUser, firstUser, Object.class);
        assertThat(response.getStatusCode()).isSameAs(HttpStatus.OK);

        //Try to add a user with a username which is already taken
        response = this.restTemplate.postForEntity(urlUser, usernametakenUser, Object.class);
        assertThat(response.getStatusCode()).isSameAs(HttpStatus.NOT_ACCEPTABLE);
        assertThat(Objects.requireNonNull(response.getBody()).toString()).contains("Username is already taken");

        //Try to add a user with an email address which is already taken
        response = this.restTemplate.postForEntity(urlUser, emailtakenUser, Object.class);
        assertThat(response.getStatusCode()).isSameAs(HttpStatus.NOT_ACCEPTABLE);
        assertThat(Objects.requireNonNull(response.getBody()).toString()).contains("E-mail address is already taken");

        //Add a second legit User
        response = this.restTemplate.postForEntity(urlUser, secondUser, Object.class);
        assertThat(response.getStatusCode()).isSameAs(HttpStatus.OK);

        //Get all saved users
        ResponseEntity<Object[]> responseGetAllUsers = this.restTemplate.getForEntity(urlUser,
                Object[].class);
        List<User> userList = Arrays.stream(Objects.requireNonNull(responseGetAllUsers.getBody()))
                .map(obj -> mapper.convertValue(obj, User.class))
                .collect(Collectors.toList());

        assertThat(responseGetAllUsers.getStatusCode()).isSameAs(HttpStatus.OK);
        assertThat(userList.size()).isEqualTo(2);
        assertThat(userList.get(0).getId()).isEqualTo(1L);
        assertThat(userList.get(0).getUsername()).isEqualTo("richi");
        assertThat(userList.get(0).getEmail()).isEqualTo("hee@ho.com");
        assertThat(userList.get(0).getPassword()).isEqualTo("123**ho?");
        assertThat(userList.get(0).getRole()).isSameAs(Role.USER);

        assertThat(userList.get(1).getId()).isEqualTo(2L);
        assertThat(userList.get(1).getUsername()).isEqualTo("richardine");
        assertThat(userList.get(1).getEmail()).isEqualTo("haa@ho.com");
        assertThat(userList.get(1).getPassword()).isEqualTo("456**ha?");
        assertThat(userList.get(1).getRole()).isSameAs(Role.USER);
    }

    @Test
    public void deleteUserTest() {
        this.restTemplate.postForEntity(urlUser, firstUser, Object.class);
        this.restTemplate.postForEntity(urlUser, secondUser, Object.class);

        //Create a user with an ID outside the DB because DELETE needs only the ID
        User userWithID = firstUser;
        userWithID.setId(1L);

        //Delete existing ser
        ResponseEntity<String> exchange = restTemplate.exchange(urlUser, HttpMethod.DELETE,
                new HttpEntity<User>(userWithID),
                String.class);

        assertThat(exchange.getStatusCode()).isSameAs(HttpStatus.OK);

        ResponseEntity<Object[]> responseGetAllUsers = this.restTemplate.getForEntity(urlUser,
                Object[].class);
        List<User> userList = Arrays.stream(Objects.requireNonNull(responseGetAllUsers.getBody()))
                .map(obj -> mapper.convertValue(obj, User.class))
                .collect(Collectors.toList());

        assertThat(userList.size()).isEqualTo(1);

        //Try to delete non-existing user
        exchange = restTemplate.exchange(urlUser, HttpMethod.DELETE,
                new HttpEntity<User>(userWithID),
                String.class);
        assertThat(exchange.getStatusCode()).isSameAs(HttpStatus.NOT_FOUND);

    }
}