package com.garisure.vehiclestore.controller;

import com.garisure.vehiclestore.dto.LoginDto;
import com.garisure.vehiclestore.dto.NewSignUp;
import com.garisure.vehiclestore.dto.SignUpDTO;
import com.garisure.vehiclestore.model.User;
import com.garisure.vehiclestore.repository.UserRepository;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.gson.JsonObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;


@RestController
@RequestMapping("/api")
@Slf4j
public class VehicleResource {


    @Value("${vechiclestore.firebase.apikey}")
    private String API_KEY;
    @Value("${vechiclestore.firebase.signupurl}")
    private String URL_SIGNUP;
    @Value("${vechiclestore.firebase.signinurl}")
    private String URL_SIGNIN;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    RestTemplate restTemplate;

    // Pages
    /*
     * HOME PAGE (default page)
     * Access by ALL Users
     */

    @GetMapping("/")
    @ApiOperation(value = "Home Page",
            notes = "Only authenticated will get access to this page"
    )
    public ModelAndView getHomePage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index.html");
        return modelAndView;
    }
    /*
     * USER MANAGEMENT PAGE
     * Access by SUPER ADMIN ONLY
     */

    @GetMapping("/users/user-management")
    @ApiOperation(value = "User Management Page",
            notes = "Only SUPER ADMIN can get access to this page. This is where SUPER ADMIN manages other users"
    )
    public ModelAndView getUserManagementPage(){
        List<User> userList = userRepository.findAll();
        ModelAndView userManagementView = new ModelAndView("user_management");
        userManagementView.addObject("userList",userList);
        return userManagementView;
    }


    @PostMapping(value = "/auth/signup", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Sign-up a new user",
            notes = "Provide valid email and firebase confidential API Key as part of request body"
    )
    public ResponseEntity<?> registerNewUser(
            @ApiParam(value = "Email-id for registration", required = true)
            @RequestBody SignUpDTO signUpDTO
    ) {


        // ONE CAN CHANGE THE PASSWORD HERE
        final String USER_PASSWORD = "secretPassword";

        /*
         1) CHECK IF USER EXISTS LOCALLY
             YES: RETURN WITH MESSAGE USER WITH THIS EMAIL ALREADY EXISTS
             NO: CREATE A NEW USER AT BOTH PLACES i.e. FIREBASE AND LOCALLY
        */

        // check for username exists in a DB
        if(userRepository.existsByUsername(signUpDTO.getEmail())){
            // USERNAME ALREADY TAKEN
            return new ResponseEntity<>("Username \'" +signUpDTO.getEmail()+ "\' already taken!", HttpStatus.BAD_REQUEST);
        }

        String url = URL_SIGNIN+API_KEY;
        // create headers
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create a map for post parameters
        Map<String, String> map = new HashMap<>();
        map.put("email", signUpDTO.getEmail());
        map.put("password", USER_PASSWORD);

        // build the request
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(map, headers);

        try{
            // CREATE A NEW USER AT FIREBASE
            ResponseEntity<?> response = this.restTemplate.postForEntity(url, entity, String.class);
            if(response.getStatusCode() == HttpStatus.OK) {

                JSONObject jsonObject = new JSONObject(new String(String.valueOf(response.getBody())));
                String idToken = jsonObject.getString("idToken");
                String uid = jsonObject.getString("localId");

                // ALSO STORE USER LOCALLY
                User user = new User();
                user.setUsername(signUpDTO.getEmail());
                user.setPassword(passwordEncoder.encode(USER_PASSWORD));
                user.setActive(0);
                user.setUid(uid);
                user.setRole("ADMIN");
                user.setIdToken(idToken);
                userRepository.save(user);
                return new ResponseEntity<>(response.getBody(),HttpStatus.OK);

            } else {
                return new ResponseEntity<>(response.getStatusCode(),HttpStatus.FORBIDDEN);
            }
        }catch (HttpClientErrorException e){
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }catch (HttpServerErrorException e){
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }catch (UnknownHttpStatusCodeException e){
            return new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_IMPLEMENTED);
        }
    }


    @PostMapping(value = "/auth/signin", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Sign-in as a user",
            notes = "Provide valid email id and firebase confidential API Key as part of request body",
            response = User.class
    )
    public ResponseEntity<?> authenticateUser(
            @ApiParam(value = "Email-id for sign-in", required = true)
            @RequestBody LoginDto loginDto) {

        // ONE CAN CHANGE THE PASSWORD HERE
        final String USER_PASSWORD = "secretPassword";

        String url = URL_SIGNIN+API_KEY;
        // create headers
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create a map for post parameters
        Map<String, String> map = new HashMap<>();
        map.put("email", loginDto.getEmail());
        map.put("password",USER_PASSWORD);

        // build the request
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(map, headers);


        try{
            // SIGN IN WITH A USER AT FIREBASE
            ResponseEntity<?> response = this.restTemplate.postForEntity(url, entity, String.class);
            if(response.getStatusCode() == HttpStatus.OK) {

                // FETCH LOCAL USER
                try{
                    User user = userRepository.getUserByUsername(loginDto.getEmail());
                    // check for username exists in a DB
                    if(user != null){
                        JSONObject jsonObject = new JSONObject(new String(String.valueOf(response.getBody())));
                        if(userRepository.updateUserToken(jsonObject.getString("idToken"),user.getUsername()) == 1){

                            Map<String, String> myUser = new HashMap<>();
                            myUser.put("email", loginDto.getEmail());
                            myUser.put("uid", user.getUid());
                            myUser.put("role", user.getRole());
                            myUser.put("idToken", jsonObject.getString("idToken"));

                            return new ResponseEntity<>(myUser, HttpStatus.OK);
                        }else {
                            return new ResponseEntity<>("Oops! Something went wrong!", HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    }else{
                        return new ResponseEntity<>("User \'" +loginDto.getEmail()+ "\' Not Exists", HttpStatus.INTERNAL_SERVER_ERROR);
                    }

                }catch (Exception e){
                    return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }

            }else {
                return new ResponseEntity<>(response.getStatusCode(),HttpStatus.FORBIDDEN);
            }
        }catch (HttpClientErrorException e){
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }catch (HttpServerErrorException e){
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }catch (UnknownHttpStatusCodeException e){
            return new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_IMPLEMENTED);
        }
    }
}


