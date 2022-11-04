//package com.garisure.vehiclestore.controller;
//
//import com.garisure.vehiclestore.dto.SignUpDTO;
//import com.garisure.vehiclestore.model.User;
//import com.garisure.vehiclestore.repository.UserRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.HttpClientErrorException;
//import org.springframework.web.client.HttpServerErrorException;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.client.UnknownHttpStatusCodeException;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@Slf4j
//public class OldVehicleController {
//
//
//    @Value("${vechiclestore.firebase.apikey}")
//    private String API_KEY;
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    RestTemplate restTemplate;
//
//    // USER RELATED API END POINTS
//
//    //    1) Create
//    //    2) Edit
//
//
////    @RequestMapping("/api/users/edit/{Id}")
////public ModelAndView showEditUserForm(@PathVariable(name = "Id") Long Id) {
////    ModelAndView mav = new ModelAndView("update_user");
////    User user = userRepository.findUserById(Id);
////    mav.addObject("user", user);
////    return mav;
////}
//////    3) Delete
////
////    @RequestMapping(value = "/api/users/update", method = RequestMethod.POST)
////    public ResponseEntity<String> updateUser(@RequestBody User user) {
////        System.out.println(user.toString());
////            int isUpdated = userRepository.updateUser(user.getDisplayName(),user.getPassword(), user.getUsername());
////            if(isUpdated == 1){
////                // User updated successfully
////                return new ResponseEntity<>("Successfully Updated", HttpStatus.OK);
////            }
////        return new ResponseEntity<>("Fucked Up", HttpStatus.NOT_IMPLEMENTED);
////    }
//
////    @RequestMapping(value = "/api/users/update", method = RequestMethod.POST)
////    public void updateUser(@ModelAttribute("user") User user, HttpServletResponse response) throws IOException {
////        int isUpdated = userRepository.updateUser(user.getDisplayName(),user.getPassword(), user.getUsername());
////        if(isUpdated == 1){
////            // User updated successfully
////            response.sendRedirect("/");
////        }
////    }
//
//
////    @RequestMapping("/delete/{id}")
////    public String deleteProduct(@PathVariable(name = "id") Long id) {
////        service.delete(id);
////
////        return "redirect:/list";
////    }
//
//
//    // DONE
//
////    @RequestMapping(method = RequestMethod.POST, value = "api/auth/signin")
////    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto) {
////        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
////                loginDto.getUsername(), loginDto.getPassword()));
////
////        SecurityContextHolder.getContext().setAuthentication(authentication);
////        return new ResponseEntity<>("User signed-in successfully!.", HttpStatus.OK);
////    }
////    @PostMapping("api/auth/signup")
////    public ResponseEntity<?> registerUser(@RequestBody NewSignUp signUpDto){
////
////        // add check for username exists in a DB
////        if(userRepository.existsByUsername(signUpDto.getUsername())){
////            return new ResponseEntity<>("Username is already taken!", HttpStatus.BAD_REQUEST);
////        }
////
////        String userDisplayName = signUpDto.getDisplayname().split("@")[0].substring(0,1).toUpperCase() + signUpDto.getDisplayname().split("@")[0].substring(1).toLowerCase();
////        // create user object
////        User user = new User();
////        user.setDisplayName( signUpDto.getDisplayname() == "" ? userDisplayName : signUpDto.getDisplayname());
////        user.setUsername(signUpDto.getUsername());
////        user.setPassword(signUpDto.getPassword());
////        user.setActive(0);
////        user.setUid(signUpDto.getUid());
////        user.setRole("ADMIN");
////        userRepository.save(user);
////        return new ResponseEntity<>(user, HttpStatus.OK);
////
////    }
//
////    @PostMapping("api/new/signup")
////    public ResponseEntity<?> registerNewUser(@RequestBody NewSignUpDto newSignUpDto) throws FirebaseAuthException {
////
////        // Check if user email exists in firebase?
////        try {
////            UserRecord userRecord = FirebaseAuth.getInstance().getUserByEmail(newSignUpDto.getUsername());
////            if(userRepository.existsByUsername(userRecord.getEmail())){
////                return new ResponseEntity<>("Username is already taken!", HttpStatus.FORBIDDEN);
////            }else{
////                // create user object
////                User user = new User();
////                user.setDisplayName( userRecord.getDisplayName() == "" ?
////                        userRecord.getEmail().split("@")[0].substring(0,1).toUpperCase() + userRecord.getEmail().split("@")[0].substring(1).toLowerCase()
////                        : userRecord.getDisplayName());
////                user.setUsername(userRecord.getEmail());
////                user.setPassword("secretPassword");
////                user.setActive(0);
////                user.setRole("ADMIN");
////                userRepository.save(user);
////                return new ResponseEntity<>("Username already taken!", HttpStatus.FORBIDDEN);
////            }
////
////        } catch (FirebaseAuthException e) {
////
////            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
////                    .setEmail(newSignUpDto.getUsername())
////                    .setEmailVerified(false)
////                    .setPassword("secretPassword")
////                    .setDisplayName(newSignUpDto.getDisplayname())
////                    .setDisabled(false);
////
////            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
////            // add check for username exists in a DB
////            if(userRepository.existsByUsername(newSignUpDto.getUsername())){
////                return new ResponseEntity<>(userRecord, HttpStatus.OK);
////            }
////            // create user object
////            User user = new User();
////            user.setDisplayName( userRecord.getDisplayName() == "" ?
////                    userRecord.getEmail().split("@")[0].substring(0,1).toUpperCase() + userRecord.getEmail().split("@")[0].substring(1).toLowerCase()
////                    : userRecord.getDisplayName());
////            user.setUsername(userRecord.getEmail());
////            user.setPassword("secretPassword");
////            user.setActive(0);
////            user.setRole("ADMIN");
////            userRepository.save(user);
////            return new ResponseEntity<>(user, HttpStatus.OK);
////        }
////    }
//
//
//
//    @PostMapping(value = "api/auth/signup", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> registerNewUser(@RequestBody SignUpDTO signUpDTO) {
//
//        // ONE CAN CHANGE THE PASSWORD HERE
//        final String USER_PASSWORD = "secretPassword";
//
//        /*
//         1) CHECK IF USER EXISTS LOCALLY
//             YES: RETURN WITH MESSAGE USER WITH THIS EMAIL ALREADY EXISTS
//             NO: CREATE A NEW USER AT BOTH PLACES i.e. FIREBASE AND LOCALLY
//        */
//
//        // check for username exists in a DB
//        if(userRepository.existsByUsername(signUpDTO.getEmail())){
//            // USERNAME ALREADY TAKEN
//            return new ResponseEntity<>("Username \'" +signUpDTO.getEmail()+ "\' already taken!", HttpStatus.BAD_REQUEST);
//        }
//
//        String url = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key="+API_KEY;
//        // create headers
//        HttpHeaders headers = new HttpHeaders();
//
//        headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_JSON_VALUE));
//        // set `accept` header
//        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
//
//        // create a map for post parameters
//        Map<String, String> map = new HashMap<>();
//        map.put("email", signUpDTO.getEmail());
//        map.put("password", USER_PASSWORD);
//
//        // build the request
//        HttpEntity<Map<String, String>> entity = new HttpEntity<>(map, headers);
//
//        try{
//            // CREATE A NEW USER AT FIREBASE
//            ResponseEntity<?> response = this.restTemplate.postForEntity(url, entity, String.class);
//            if(response.getStatusCode() == HttpStatus.OK) {
//
//                JSONObject jsonObject = new JSONObject(new String(String.valueOf(response.getBody())));
//                String idToken = jsonObject.getString("idToken");
//                String uid = jsonObject.getString("localId");
//
//                // ALSO STORE USER LOCALLY
//                User user = new User();
//                user.setUsername(signUpDTO.getEmail());
//                user.setPassword(USER_PASSWORD);
//                user.setActive(0);
//                user.setUid(uid);
//                user.setRole("ADMIN");
//                user.setIdToken(idToken);
//                userRepository.save(user);
//                return new ResponseEntity<>(response.getBody(),HttpStatus.OK);
//
//            } else {
//                return new ResponseEntity<>(response.getStatusCode(),HttpStatus.FORBIDDEN);
//            }
//        }catch (HttpClientErrorException e){
//            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
//        }catch (HttpServerErrorException e){
//            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
//        }catch (UnknownHttpStatusCodeException e){
//            return new ResponseEntity<>(e.getResponseBodyAsString(), HttpStatus.NOT_IMPLEMENTED);
//        }
//    }
//
//
//
//
//
//
//    // Pages
//    /*
//     * HOME PAGE (default page)
//     * Access by ALL Users
//     */
////    @RequestMapping("/")
////
////    public ModelAndView getHomePage(){
////        ModelAndView modelAndView = new ModelAndView();
////        modelAndView.setViewName("index.html");
////        return modelAndView;
////    }
////    @RequestMapping("/infoPage")
////    public ModelAndView infoPage(){
////        return new ModelAndView("infoPage");
////    }
////    @RequestMapping("/loginPage")
////    public ModelAndView loginPage(){
////        return new ModelAndView("loginPage");
////    }
////    @RequestMapping("/signupPage")
////    public ModelAndView signupPage(){
////        return new ModelAndView("signupPage");
////    }
//    /*
//     * USER MANAGEMENT PAGE
//     * Access by SUPER ADMIN ONLY
//     */
////    @RequestMapping("/api/users/user-management")
////    public ModelAndView getUserManagementPage(){
////        List<User> userList = userRepository.findAll();
////        ModelAndView userManagementView = new ModelAndView("user_management");
////        userManagementView.addObject("userList",userList);
////        return userManagementView;
////    }
//}
