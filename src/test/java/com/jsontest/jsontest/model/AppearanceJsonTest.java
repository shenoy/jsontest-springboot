package com.jsontest.jsontest.model;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.Assert.*;


@JsonTest
class AppearanceJsonTest {

    @Autowired
    private ApplicationContext context;

    @Autowired
    JacksonTester<Appearance> jacksonTester;

    @Autowired
    JacksonTester<List<Appearance>> jacksonTesterList;

    @Value("classpath:appearance.json")
    private Resource appearanceJsonContent;

    @BeforeEach
    void setUp() {
        Arrays.stream(context.getBeanDefinitionNames()).forEach(System.out::println);
    }

    @Test
    void contextLoads(){
        assertThat(context).isNot(null);
        assertThat(jacksonTester).isNot(null);
    }

    @Test
    void testSerializationWithJsonPath() throws IOException {

        var id = UUID.randomUUID().toString();
        var appearance = new Appearance(id,
                "Spring One Tour Chicago",
                "Getting started with GraphQL for Spring",
                LocalDate.of(2022, 04, 26),
                LocalDate.of(2022, 04, 27),
                Type.CONFERENCE,
                List.of("Spring Boot", "GraphQL"),
                "https://tanzu.vmware.com/developer/spring-one-tour/"
                );



        JsonContent<Appearance> json = jacksonTester.write(appearance);

        System.out.println(json);

        assertThat(json).extractingJsonPathStringValue("$.id").isEqualTo(id);
        assertThat(json).extractingJsonPathStringValue("$.title").isEqualTo("Spring One Tour Chicago");
        assertThat(json).extractingJsonPathStringValue("$.description").isEqualTo("Getting started with GraphQL for Spring");
        assertThat(json).extractingJsonPathStringValue("$.startDate").isEqualTo("2022-04-26");
        assertThat(json).extractingJsonPathStringValue("$.endDate").isEqualTo("2022-04-27");
        assertThat(json).extractingJsonPathStringValue("$.type").isEqualTo(Type.CONFERENCE.toString());
        assertThat(json).extractingJsonPathArrayValue("$.tags").isEqualTo(List.of("Spring Boot", "GraphQL"));
        assertThat(json).extractingJsonPathStringValue("$.url").isEqualTo("https://tanzu.vmware.com/developer/spring-one-tour/");

    }

    @Test
    void testListAppearancesSerialization() throws IOException {

        var chicago = new Appearance("272a6c0e-ecef-11ec-8ea0-0242ac120002",
                "Spring One Tour Chicago",
                "Getting started with GraphQL for Spring",
                LocalDate.of(2022, 04, 26),
                LocalDate.of(2022, 04, 27),
                Type.CONFERENCE,
                List.of("Spring Boot", "GraphQL"),
                "https://tanzu.vmware.com/developer/spring-one-tour/"
        );

        var toronto = new Appearance("3b2528c0-ecef-11ec-8ea0-0242ac120002" ,
                "Spring One Tour Toronto",
                "Getting started with GraphQL for Spring",
                LocalDate.of(2022, 04, 26),
                LocalDate.of(2022, 04, 27),
                Type.CONFERENCE,
                List.of("Spring Boot", "GraphQL"),
                "https://tanzu.vmware.com/developer/spring-one-tour/"
        );

        List<Appearance> appearances = List.of(chicago, toronto);
        JsonContent<List<Appearance>> jsonContent = jacksonTesterList.write(appearances);
        assertThat(jsonContent).isEqualToJson(new ClassPathResource("appearances.json"));
    }



    @Test
    void testSerializationWithJsonFile() throws IOException {

        var id ="6adddfce-bd5b-44e7-a026-b4612afe46de";
        var appearance = new Appearance(id,
                "Spring One Tour Chicago",
                "Getting started with GraphQL for Spring",
                LocalDate.of(2022, 04, 26),
                LocalDate.of(2022, 04, 27),
                Type.CONFERENCE,
                List.of("Spring Boot", "GraphQL"),
                "https://tanzu.vmware.com/developer/spring-one-tour/"
        );

        JsonContent<Appearance> json = jacksonTester.write(appearance);

        assertThat(json).isEqualToJson(new ClassPathResource("appearance.json"));


    }

    @Test
    void testDeserialization() throws IOException {

        var appearance = jacksonTester.read(appearanceJsonContent).getObject();
        assertThat(appearance.getId()).isEqualTo("6adddfce-bd5b-44e7-a026-b4612afe46de");
        assertThat(appearance.getTitle()).isEqualTo("Spring One Tour Chicago");
        assertThat(appearance.getDescription()).isEqualTo("Getting started with GraphQL for Spring");
        
    }


}