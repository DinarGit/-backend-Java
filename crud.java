package com.geekbrains.lesson6;

import com.geekbrains.builder.User;

import java.util.ArrayList;
import java.util.List;

public class crud {

    p@Service
    public class CreateUserService {
        @Autowired
        UserRepository repository;
        public User createNewUser(User user) {
            return repository.save(user);
        }
    }
    @Service
    public class DeleteUserService {
        @Autowired
        UserRepository repository;
        public void deleteUser(Long id) {
            repository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));
            repository.deleteById(id);
        }
    }
    @Service
    public class DetailUserService {
        @Autowired
        UserRepository repository;
        public User listUser(Long id) {
            return repository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));
        }
    }
    @Service
    public class ListUserService {
        @Autowired
        UserRepository repository;
        public List<User> listAllUsers() {
            return repository.findAll();
        }
    }
    @Service
    public class UpdateUserService {
        @Autowired
        UserRepository repository;
        public User updateUser(Long id, User user) {
            repository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException(id));
            user.setId(id);
            return repository.save(user);
        }
    }
    Create a new user service
    Starting with our CreateUserService class, we’ll create a test class named CreateUserServiceTest.
    src/test/java/com/usersapi/endpoints/unit/service/CreateUserServiceTest.java
    @RunWith(MockitoJUnitRunner.class)
    public class CreateUserServiceTest {
        @Mock
        private UserRepository userRepository;
        @InjectMocks
        private CreateUserService createUserService;
        @Test
        public void whenSaveUser_shouldReturnUser() {
            User user = new User();
            user.setName(“Test Name”);
            when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);
            User created = createUserService.createNewUser(user);
            assertThat(created.getName()).isSameAs(user.getName());
            verify(userRepository).save(user);
        }
    }

}

@RunWith(MockitoJUnitRunner.class)
public class ListUserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private crud.ListUserService listUserService;
    @Test
    public void shouldReturnAllUsers() {
        List<User> users = new ArrayList();
        users.add(new User());
        given(userRepository.findAll()).willReturn(users);
        List<User> expected = listUserService.listAllUsers();
        assertEquals(expected, users);
        verify(userRepository).findAll();
    }

    @RunWith(MockitoJUnitRunner.class)
    public class DeleteUserServiceTest {
        @Mock
        private UserRepository userRepository;
        @InjectMocks
        private DeleteUserService deleteUserService;
        @Test
        public void whenGivenId_shouldDeleteUser_ifFound(){
            User user = new User();
            user.setName(“Test Name”);
            user.setId(1L);
            when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
            deleteUserService.deleteUser(user.getId());
            verify(userRepository).deleteById(user.getId());
        }
        @Test(expected = RuntimeException.class)
        public void should_throw_exception_when_user_doesnt_exist() {
            User user = new User();
            user.setId(89L);
            user.setName(“Test Name”);
            given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
            deleteUserService.deleteUser(user.getId());
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public class UpdateUserServiceTest {
        @Mock
        private UserRepository userRepository;
        @InjectMocks
        private UpdateUserService updateUserService;
        @Test
        public void whenGivenId_shouldUpdateUser_ifFound() {
            User user = new User();
            user.setId(89L);
            user.setName(“Test Name”);
            User newUser = new User();
            user.setName(“New Test Name”);
            given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
            updateUserService.updateUser(user.getId(), newUser);
            verify(userRepository).save(newUser);
            verify(userRepository).findById(user.getId());
        }
        @Test(expected = RuntimeException.class)
        public void should_throw_exception_when_user_doesnt_exist() {
            User user = new User();
            user.setId(89L);
            user.setName(“Test Name”);
            User newUser = new User();
            newUser.setId(90L);
            user.setName(“New Test Name”);
            given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
            updateUserService.updateUser(user.getId(), newUser);
        }
    }

    @RunWith(MockitoJUnitRunner.class)
    public class DetailUserServiceTest {
        @Mock
        private UserRepository userRepository;
        @InjectMocks
        private DetailUserService detailUserService;
        @Test
        public void whenGivenId_shouldReturnUser_ifFound() {
            User user = new User();
            user.setId(89L);
            when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
            User expected = detailUserService.listUser(user.getId());
            assertThat(expected).isSameAs(user);
            verify(userRepository).findById(user.getId());
        }
        @Test(expected = UserNotFoundException.class)
        public void should_throw_exception_when_user_doesnt_exist() {
            User user = new User();
            user.setId(89L);
            user.setName(“Test Name”);
            given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(null));
            detailUserService.listUser(user.getId());
        }
    }

    @RestController
    @RequestMapping(“/users”)
    public class CreateUserController {
        @Autowired
        CreateUserService service;
        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        public ResponseEntity<User> createNewUser_whenPostUser(@RequestBody User user) {
            User createdUser = service.createNewUser(user);
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path(“/{id}”)
.buildAndExpand(createdUser.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(createdUser);
        }
    }
    @RestController
    @RequestMapping(“/users/{id}”)
    public class DeleteUserController {
        @Autowired
        DeleteUserService service;
        @DeleteMapping
        @ResponseStatus(HttpStatus.NO_CONTENT)
        public void deleteUser_whenDeleteUser(@PathVariable Long id) {
            service.deleteUser(id);
        }
    }
    @RestController
    @RequestMapping(“/users/{id}”)
    public class DetailUserController {
        @Autowired
        DetailUserService service;
        @GetMapping
        @ResponseStatus(HttpStatus.OK)
        public ResponseEntity<User> list(@PathVariable Long id) {
            return ResponseEntity.ok().body(service.listUser(id));
        }
    }
    @RestController
    @RequestMapping(“/users”)
    public class ListUserController {
        @Autowired
        ListUserService service;
        @GetMapping
        @ResponseStatus(HttpStatus.OK)
        public ResponseEntity<List<User>> listAllUsers_whenGetUsers() {
            return ResponseEntity.ok().body(service.listAllUsers());
        }
    }
    @RestController
    @RequestMapping(“/users/{id}”)
    public class UpdateUserController {
        @Autowired
        UpdateUserService service;
        @PutMapping
        @ResponseStatus(HttpStatus.OK)
        public ResponseEntity<User> updateUser_whenPutUser(@RequestBody User user, @PathVariable Long id) {
            return ResponseEntity.ok().body(service.updateUser(id, user));
        }
    }

    @RunWith(SpringRunner.class)
    @WebMvcTest(CreateUserController.class)
    public class CreateUserControllerTest {
        @Autowired
        private MockMvc mockMvc;
        @MockBean
        private CreateUserService service;
        @Test
        public void createUser_whenPostMethod() throws Exception {
            User user = new User();
            user.setName(“Test Name”);
            given(service.createNewUser(user)).willReturn(user);
            mockMvc.perform(post(“/users”)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(JsonUtil.toJson(user)))
.andExpect(status().isCreated())
                    .andExpect(jsonPath(“$.name”, is(user.getName())));
        }
    }

    public class JsonUtil {
        public static byte[] toJson(Object object) throws IOException {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            return mapper.writeValueAsBytes(object);
        }
    }

    @RunWith(SpringRunner.class)
    @WebMvcTest(ListUserController.class)
    public class ListUserControllerTest {
        @Autowired
        private MockMvc mvc;
        @MockBean
        private ListUserService listUserService;
        @Test
        public void listAllUsers_whenGetMethod()
                throws Exception {
            User user = new User();
            user.setName(“Test name”);
            List<User> allUsers = Arrays.asList(user);
            given(listUserService
                    .listAllUsers())
                    .willReturn(allUsers);
            mvc.perform(get(“/users”)
                    .contentType(MediaType.APPLICATION_JSON))
.andExpect(status().isOk())
                    .andExpect(jsonPath(“$”, hasSize(1)))
                    .andExpect(jsonPath(“$[0].name”, is(user.getName())));
        }
    }

    @RunWith(SpringRunner.class)
    @WebMvcTest(DeleteUserController.class)
    public class DeleteUserControllerTest {
        @Autowired
        private MockMvc mvc;
        @MockBean
        private DeleteUserService deleteUserService;
        @Test
        public void removeUserById_whenDeleteMethod() throws Exception {
            User user = new User();
            user.setName(“Test Name”);
            user.setId(89L);
            doNothing().when(deleteUserService).deleteUser(user.getId());
            mvc.perform(delete(“/users/” + user.getId().toString())
.contentType(MediaType.APPLICATION_JSON))
.andExpect(status().isNoContent());
        }
        @Test
        public void should_throw_exception_when_user_doesnt_exist() throws Exception {
            User user = new User();
            user.setId(89L);
            user.setName(“Test Name”);
            Mockito.doThrow(new UserNotFoundException(user.getId())).when(deleteUserService).deleteUser(user.getId());
            mvc.perform(delete(“/users/” + user.getId().toString())
.contentType(MediaType.APPLICATION_JSON))
.andExpect(status().isNotFound());
        }
    }

    @RunWith(SpringRunner.class)
    @WebMvcTest(DetailUserController.class)
    public class DetailUserControllerTest {
        @Autowired
        private MockMvc mvc;
        @MockBean
        private DetailUserService detailUserService;
        @Test
        public void listUserById_whenGetMethod() throws Exception {
            User user = new User();
            user.setName(“Test Name”);
            user.setId(89L);
            given(detailUserService.listUser(user.getId())).willReturn(user);
            mvc.perform(get(“/users/” + user.getId().toString())
.contentType(MediaType.APPLICATION_JSON))
.andExpect(status().isOk())
                    .andExpect(jsonPath(“name”, is(user.getName())));
        }
        @Test
        public void should_throw_exception_when_user_doesnt_exist() throws Exception {
            User user = new User();
            user.setId(89L);
            user.setName(“Test Name”);
            Mockito.doThrow(new UserNotFoundException(user.getId())).when(detailUserService).listUser(user.getId());
            mvc.perform(get(“/users/” + user.getId().toString())
.contentType(MediaType.APPLICATION_JSON))
.andExpect(status().isNotFound());
        }
    }

    @RunWith(SpringRunner.class)
    @WebMvcTest(UpdateUserController.class)
    public class UpdateUserControllerTest {
        @Autowired
        private MockMvc mvc;
        @MockBean
        private UpdateUserService updateUserService;
        @Test
        public void updateUser_whenPutUser() throws Exception {
            User user = new User();
            user.setName(“Test Name”);
            user.setId(89L);
            given(updateUserService.updateUser(user.getId(), user)).willReturn(user);
            ObjectMapper mapper = new ObjectMapper();
            mvc.perform(put(“/users/” + user.getId().toString())
.content(mapper.writeValueAsString(user))
                    .contentType(MediaType.APPLICATION_JSON))
.andExpect(status().isOk())
                    .andExpect(jsonPath(“name”, is(user.getName())));
        }
        @Test
        public void should_throw_exception_when_user_doesnt_exist() throws Exception {
            User user = new User();
            user.setId(89L);
            user.setName(“Test Name”);
            Mockito.doThrow(new UserNotFoundException(user.getId())).when(updateUserService).updateUser(user.getId(), user);
            ObjectMapper mapper = new ObjectMapper();
            mvc.perform(put(“/users/” + user.getId().toString())
.content(mapper.writeValueAsString(user))
                    .contentType(MediaType.APPLICATION_JSON))
.andExpect(status().isNotFound());
        }
    }


}

