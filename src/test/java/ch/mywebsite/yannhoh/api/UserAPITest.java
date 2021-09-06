package ch.mywebsite.yannhoh.api;

import ch.mywebsite.yannhoh.Role;
import ch.mywebsite.yannhoh.TestHelper;
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

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class UserAPITest {

    private String urlUser;
    private ObjectMapper mapper;
    private User firstUser, secondUser, thirdUser, usernametakenUser, emailtakenUser;
    private TestHelper testHelper;

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
        testHelper = new TestHelper();
        testHelper.deleteUser(1L, urlUser);
        testHelper.deleteUser(2L, urlUser);
        testHelper.deleteUser(3L, urlUser);
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

        //Check all saved users
        List<User> userList = testHelper.getAllUserList(urlUser);
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

        Long id = testHelper.getAllUserList(urlUser).get(0).getId();
        userWithID.setId(id);

        //Delete existing user
        ResponseEntity<String> exchange = restTemplate.exchange(urlUser, HttpMethod.DELETE,
                new HttpEntity<User>(userWithID),
                String.class);

        assertThat(exchange.getStatusCode()).isSameAs(HttpStatus.OK);

        List<User> userList = testHelper.getAllUserList(urlUser);
        assertThat(userList.size()).isEqualTo(1);

        //Try to delete non-existing user
        exchange = restTemplate.exchange(urlUser, HttpMethod.DELETE,
                new HttpEntity<User>(userWithID),
                String.class);
        assertThat(exchange.getStatusCode()).isSameAs(HttpStatus.NOT_FOUND);
        assertThat(Objects.requireNonNull(exchange.getBody())).contains("\"message\":\"A user with this id does not exist\"");

    }

    @Test
    public void updateUserTest() {
        this.restTemplate.postForEntity(urlUser, firstUser, Object.class);

        //Create a user with an ID outside the DB because PUT goes by the ID
        User updatedUser = thirdUser;
        Long id = testHelper.getAllUserList(urlUser).get(0).getId();
        updatedUser.setId(id);
        updatedUser.setRole(Role.ADMIN);

        //Update existing user
        ResponseEntity<String> exchange = restTemplate.exchange(urlUser, HttpMethod.PUT,
                new HttpEntity<User>(updatedUser),
                String.class);
        assertThat(exchange.getStatusCode()).isSameAs(HttpStatus.OK);

        List<User> userList = testHelper.getAllUserList(urlUser);
        assertThat(userList.size()).isEqualTo(1);
        assertThat(userList.get(0).getId()).isEqualTo(id);
        assertThat(userList.get(0).getUsername()).isEqualTo("richardinho");
        assertThat(userList.get(0).getEmail()).isEqualTo("hii@ho.com");
        assertThat(userList.get(0).getPassword()).isEqualTo("789**hi?");
        assertThat(userList.get(0).getRole()).isSameAs(Role.ADMIN);

        //Try to update a user with the same data as in DB already
        exchange = restTemplate.exchange(urlUser, HttpMethod.PUT,
                new HttpEntity<User>(updatedUser),
                String.class);
        assertThat(exchange.getStatusCode()).isSameAs(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(Objects.requireNonNull(exchange.getBody())).contains("\"message\":\"Nothing to update\"");

        //Update user which is not existing
        updatedUser.setId(1_000_000_000L);
        ResponseEntity<String> exchange2 = restTemplate.exchange(urlUser, HttpMethod.PUT,
                new HttpEntity<User>(updatedUser),
                String.class);

        assertThat(exchange2.getStatusCode()).isSameAs(HttpStatus.NOT_FOUND);
        assertThat(Objects.requireNonNull(exchange2.getBody())).contains("\"message\":\"A user with this id does not exist\"");
    }
}