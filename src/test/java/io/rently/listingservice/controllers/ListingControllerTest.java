//package io.rently.listingservice.controllers;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.rently.listingservice.components.UserService;
//import io.rently.listingservice.configs.ListingControllerTestConfigs;
//import io.rently.listingservice.dtos.Listing;
//import io.rently.listingservice.interfaces.ListingsRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
//import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import javax.crypto.spec.SecretKeySpec;
//import java.util.Date;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.mockito.Mockito.doReturn;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//@ContextConfiguration(classes = ListingControllerTestConfigs.class)
//@AutoConfigureMockMvc
//@EnableAutoConfiguration(exclude = {
//        DataSourceAutoConfiguration.class,
//        DataSourceTransactionManagerAutoConfiguration.class,
//        HibernateJpaAutoConfiguration.class})
//class ListingControllerTest {
//
//    @Autowired
//    public MockMvc mvc;
//    @Autowired
//    public ListingsRepository repository;
//    public static final String SECRET = "HelloDarknessMyOldFriend";
//    public static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS384;
//    public static final SecretKeySpec SECRET_KEY_SPEC = new SecretKeySpec(SECRET.getBytes(), ALGORITHM.getJcaName());
//    public String token;
//    public String validLeaserId = UUID.randomUUID().toString();
//    public Listing validListingData;
//    @Autowired
//    public UserService service;
//
//    @BeforeEach
//    public void generateValidJwt() {
//        Date expiredDate = new Date(System.currentTimeMillis() + 60000L);
//        token = Jwts.builder()
//                .setIssuedAt(expiredDate)
//                .setExpiration(expiredDate)
//                .setSubject(validLeaserId)
//                .signWith(ALGORITHM, SECRET_KEY_SPEC)
//                .compact();
//
//        getValidGenericListing();
//    }
//
//    void getValidGenericListing() {
//        Listing.Address address = new Listing.Address();
//        address.setFormattedAddress("Street number, Street name, City, Zip, Country");
//        address.setCity("City");
//        address.setCountry("Country");
//        address.setStreet("Street");
//        address.setZip("Zip");
//        address.setLocation(new GeoJsonPoint(0.0, 0.0));
//        validListingData = new Listing();
//        validListingData.setId(UUID.randomUUID().toString());
//        validListingData.setImage("imageURL");
//        validListingData.setLeaser(validLeaserId);
//        validListingData.setName("My listing");
//        validListingData.setDesc("A description");
//        validListingData.setPhone("+1 07 12 23 34 45");
//        validListingData.setPrice("123");
//        validListingData.setStartDate(String.valueOf(new Date().getTime()));
//        validListingData.setEndDate(String.valueOf(new Date().getTime()));
//        validListingData.setUpdatedAt(String.valueOf(new Date().getTime()));
//        validListingData.setCreatedAt(String.valueOf(new Date().getTime()));
//        validListingData.setAddress(address);
//    }
//
//    @Test
//    void handleGetRequest_validLeaserId_returnListing() throws Exception {
//        doReturn(Optional.of(validListingData)).when(repository).findById(validLeaserId);
//
//        ResultActions response = mvc.perform(get("/api/v1/{id}", validLeaserId));
//
//        String responseJson = response.andReturn().getResponse().getContentAsString();
//        response.andExpect(MockMvcResultMatchers.status().isOk());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
//        assert responseJson.contains(String.valueOf(HttpStatus.OK.value()));
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").doesNotExist());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").isNotEmpty());
//        assert responseJson.contains(validLeaserId);
//    }
//
//    @Test
//    void handlePostRequest_mismatchedDataHolderIds_throwUnauthorized() throws Exception {
//        validListingData.setLeaser("invalidId");
//        String jsonBody = new ObjectMapper().writeValueAsString(validListingData);
//
//        ResultActions response = mvc.perform(post("/api/v1/")
//                .content(jsonBody).contentType(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));
//
//        String responseJson = response.andReturn().getResponse().getContentAsString();
//        response.andExpect(MockMvcResultMatchers.status().isUnauthorized());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
//        assert responseJson.contains(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
//    }
//
//    @Test
//    void handlePostRequest_validListingData_successMsg() throws Exception {
//        String jsonBody = new ObjectMapper().writeValueAsString(validListingData);
//
//        ResultActions response = mvc.perform(post("/api/v1/")
//                .content(jsonBody).contentType(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));
//
//        String responseJson = response.andReturn().getResponse().getContentAsString();
//        response.andExpect(MockMvcResultMatchers.status().isCreated());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
//        assert responseJson.contains(String.valueOf(HttpStatus.CREATED.value()));
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
//    }
//
//    @Test
//    void handlePutRequest_mismatchedDataHolderIds_throwUnauthorized() throws Exception {
//        validListingData.setLeaser("invalidId");
//        String jsonBody = new ObjectMapper().writeValueAsString(validListingData);
//
//        ResultActions response = mvc.perform(put("/api/v1/{id}", validLeaserId)
//                .content(jsonBody).contentType(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));
//
//        String responseJson = response.andReturn().getResponse().getContentAsString();
//        response.andExpect(MockMvcResultMatchers.status().isUnauthorized());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
//        assert responseJson.contains(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
//    }
//
//    @Test
//    void handlePutRequest_validListingData_successMsg() throws Exception {
//        String jsonBody = new ObjectMapper().writeValueAsString(validListingData);
//
//        doReturn(Optional.of(validListingData)).when(repository).findById(validLeaserId);
//
//        ResultActions response = mvc.perform(put("/api/v1/{id}", validLeaserId)
//                .content(jsonBody).contentType(MediaType.APPLICATION_JSON)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));
//
//        String responseJson = response.andReturn().getResponse().getContentAsString();
//        response.andExpect(MockMvcResultMatchers.status().isOk());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
//        assert responseJson.contains(String.valueOf(HttpStatus.OK.value()));
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
//    }
//
//    @Test
//    void handleDeleteRequest_mismatchedDataHolderIds_throwUnauthorized() throws Exception {
//        ResultActions response = mvc.perform(delete("/api/v1/{id}", "invalidId")
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));
//
//        String responseJson = response.andReturn().getResponse().getContentAsString();
//        response.andExpect(MockMvcResultMatchers.status().isUnauthorized());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
//        assert responseJson.contains(String.valueOf(HttpStatus.UNAUTHORIZED.value()));
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
//    }
//
//    @Test
//    void handleDeleteRequest_validLeaserId_successMsg() throws Exception {
//        doReturn(Optional.of(validListingData)).when(repository).findById(validLeaserId);
//
//        ResultActions response = mvc.perform(delete("/api/v1/{id}", validLeaserId)
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token));
//
//        String responseJson = response.andReturn().getResponse().getContentAsString();
//        response.andExpect(MockMvcResultMatchers.status().isOk());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.status").isNotEmpty());
//        assert responseJson.contains(String.valueOf(HttpStatus.OK.value()));
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.timestamp").isNotEmpty());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.message").isNotEmpty());
//        response.andExpect(MockMvcResultMatchers.jsonPath("$.content").doesNotExist());
//    }
//}