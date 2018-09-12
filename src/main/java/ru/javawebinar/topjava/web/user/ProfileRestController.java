package ru.javawebinar.topjava.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.to.UserTo;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

import static ru.javawebinar.topjava.util.UserUtil.createNewFromTo;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@RestController
@RequestMapping(ProfileRestController.REST_URL)
public class ProfileRestController extends AbstractUserController {
    static final String REST_URL = "/rest/profile";

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get() {
        return super.get(authUserId());
    }

    @DeleteMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete() {
        super.delete(authUserId());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@Valid @RequestBody UserTo userTo) {
        super.update(userTo, SecurityUtil.authUserId());
    }

    @GetMapping(value = "/text")
    public String testUTF() {
        return "Русский текст";
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createWithLocation(@Valid @RequestBody UserTo userTo) {
        User userToRegister = createNewFromTo(userTo);
        userToRegister.setRoles(Collections.singletonList(Role.ROLE_USER));
        User created = super.create(userToRegister);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}