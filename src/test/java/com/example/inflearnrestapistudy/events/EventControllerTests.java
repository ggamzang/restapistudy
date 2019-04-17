package com.example.inflearnrestapistudy.events;

import com.example.inflearnrestapistudy.common.RestDocsConfiguration;
import com.example.inflearnrestapistudy.common.TestDescription;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest
@SpringBootTest
@AutoConfigureMockMvc // to use mockmvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class EventControllerTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

//    @MockBean
//    EventRepository eventRepository;

    @Test
    @TestDescription("정상적으로 이벤트를 생성하는 테스트")
    public void createEvent() throws Exception {
        EventDto event = EventDto.builder()
                .name("Spring")
                .description("REST API with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 4, 11, 16, 20))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 4, 12, 16, 20))
                .beginEventDateTime(LocalDateTime.of(2019, 4, 13, 16, 20))
                .endEventDateTime(LocalDateTime.of(2019, 4, 14, 16, 20))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();
        //Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .accept(MediaTypes.HAL_JSON)
                    .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
                .andExpect(jsonPath("free").value(false))
                .andExpect(jsonPath("offline").value(true))
                .andExpect(jsonPath("eventStatus").value(EventStatus.DRAFT.name()))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.query-events").exists())
                .andExpect(jsonPath("_links.update-events").exists())
                .andDo(document("create-events",
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("query-events").description("link to query"),
                                linkWithRel("update-events").description("link to update")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("accept header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
                        ),
                        requestFields(
                                fieldWithPath("name").description("Name of new Event"),
                                fieldWithPath("description").description("Name of new Event"),
                                fieldWithPath("beginEnrollmentDateTime").description("Name of new Event"),
                                fieldWithPath("closeEnrollmentDateTime").description("Name of new Event"),
                                fieldWithPath("beginEventDateTime").description("Name of new Event"),
                                fieldWithPath("endEventDateTime").description("Name of new Event"),
                                fieldWithPath("location").description("Name of new Event"),
                                fieldWithPath("basePrice").description("Name of new Event"),
                                fieldWithPath("maxPrice").description("Name of new Event"),
                                fieldWithPath("limitOfEnrollment").description("Name of new Event")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("location header"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("content type")
                        ),
                        responseFields(        // 응답 오는 모든 정보를 포함해야 테스트 성공
                        //relaxedResponseFields(  // 부가적인 정보를 빼고 문서화 가능
                                fieldWithPath("id").description("Name of new Event"),
                                fieldWithPath("name").description("Name of new Event"),
                                fieldWithPath("description").description("Name of new Event"),
                                fieldWithPath("beginEnrollmentDateTime").description("Name of new Event"),
                                fieldWithPath("closeEnrollmentDateTime").description("Name of new Event"),
                                fieldWithPath("beginEventDateTime").description("Name of new Event"),
                                fieldWithPath("endEventDateTime").description("Name of new Event"),
                                fieldWithPath("location").description("Name of new Event"),
                                fieldWithPath("basePrice").description("Name of new Event"),
                                fieldWithPath("maxPrice").description("Name of new Event"),
                                fieldWithPath("limitOfEnrollment").description("Name of new Event"),
                                fieldWithPath("free").description("Name of new Event"),
                                fieldWithPath("offline").description("Name of new Event"),
                                fieldWithPath("eventStatus").description("Name of new Event"),
                                fieldWithPath("_links.self.href").description("Name of new Event"),
                                fieldWithPath("_links.query-events.href").description("Name of new Event"),
                                fieldWithPath("_links.update-events.href").description("Name of new Event")
                        )
                ))
        ;
    }

    @Test
    @TestDescription("입력 받을 수 없는 값을 사용한 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request() throws Exception {
        Event event = Event.builder()
                .id(100)
                .name("Spring")
                .description("REST API with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 4, 11, 16, 20))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 4, 12, 16, 20))
                .beginEventDateTime(LocalDateTime.of(2019, 4, 13, 16, 20))
                .endEventDateTime(LocalDateTime.of(2019, 4, 14, 16, 20))
                .basePrice(100)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .free(true)
                .offline(false)
                .eventStatus(EventStatus.PUBLISHED)
                .build();
        //Mockito.when(eventRepository.save(event)).thenReturn(event);

        mockMvc.perform(post("/api/events/")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaTypes.HAL_JSON)
                .content(objectMapper.writeValueAsString(event)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Empty_Input() throws Exception {
        EventDto eventDto = EventDto.builder().build();

        this.mockMvc.perform(post("/api/events")
                    .contentType(MediaType.APPLICATION_JSON_UTF8)
                    .content(this.objectMapper.writeValueAsString(eventDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @TestDescription("입력 값이 비어있는 경우에 에러가 발생하는 테스트")
    public void createEvent_Bad_Request_Wrong_Input() throws Exception {
        EventDto eventDto = EventDto.builder()
                .name("Spring")
                .description("REST API with Spring")
                .beginEnrollmentDateTime(LocalDateTime.of(2019, 4, 11, 16, 20))
                .closeEnrollmentDateTime(LocalDateTime.of(2019, 4, 10, 16, 20))
                .beginEventDateTime(LocalDateTime.of(2019, 4, 9, 16, 20))
                .endEventDateTime(LocalDateTime.of(2019, 4, 8, 16, 20))
                .basePrice(10000)
                .maxPrice(200)
                .limitOfEnrollment(100)
                .location("강남역")
                .build();

        this.mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(this.objectMapper.writeValueAsString(eventDto)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].objectName").exists())
//                .andExpect(jsonPath("$[0].field").exists())
                .andExpect(jsonPath("$[0].defaultMessage").exists())
                .andExpect(jsonPath("$[0].code").exists());
//                .andExpect(jsonPath("$[0].rejectedValue").exists());
    }


}
