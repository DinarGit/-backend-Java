public class RetrofitClientFactory {
    private final int port;

    PersonAPIClient authenticatedClient(String username, String password) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().authenticator(
                (route, response) -> response.request().newBuilder().header("Authorization", Credentials.basic(username, password))
                        .build()).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(String.format("http://localhost:%d/", port)).
                addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit.create(PersonAPIClient.class);
    }
}
@Test
public void EntityLifeCycleHappyFlow() throws IOException {

        executeCall(adminClient.createPerson(Person.builder().id("42").name("Jane").build()));
        executeCall(adminClient.createPerson(Person.builder().id("43").name("Jack").build()));
        assertThat(executeCall(userClient.getPersonById("42", null)).body().getName()).isEqualTo("Jane");
        assertThat(executeCall(userClient.getPersonById("43", null)).body().getName()).isEqualTo("Jack");

        Response<List<Person>> response = executeCall(userClient.getAll(null));
        assertThat(response.body()).hasSize(2);

        executeCall(adminClient.upsertPerson(Person.builder().id("42").name("Jane").address("London").build()));
        Person jane = executeCall(userClient.getPersonById("42", "address,dateofBirth")).body();
        assertThat(jane.getAddress()).isEqualTo("London");
        executeCall(adminClient.deletePerson("42"));
        assertThat(userClient.getAll(null).execute().body()).hasSize(1);
        }

private <T> Response<T> executeCall(Call<T> call) throws IOException {
        Response<T> response = call.execute();
        if (!response.isSuccessful()) {
        fail("response returned " + response.errorBody().string());
        }
        return response;
        }

        // Пользователю с такой ролью не разрешено выполнение запросов POST
        assertThat(userClient.createPerson(Person.builder().id("42").name("Jane").build()).execute().code()).isEqualTo(403);

// Уже имеется пользователь с id 42.
        Response<Void> personExists = adminClient.createPerson(Person.builder().id("42").name("Jane").build()).execute();
        assertThat(personExists.code()).isEqualTo(400);
        assertThat(personExists.errorBody().string()).isEqualTo("Person with ID already exists: 42");

// Имя пользователя не может быть пустым
        Response<Void> incompletePost = adminClient.createPerson(Person.builder().id("44").name(null).build()).execute();
        assertThat(incompletePost.code()).isEqualTo(400);
        assertThat(incompletePost.errorBody().string()).isEqualTo("Person name cannot be null");