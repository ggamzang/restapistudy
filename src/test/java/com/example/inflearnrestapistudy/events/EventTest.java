package com.example.inflearnrestapistudy.events;


import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(JUnitParamsRunner.class)
public class EventTest {

    @Test
    public void builder(){
        Event event = Event.builder()
                .name("API Test")
                .description("REST API Development with Spring")
                .build();
        assertThat(event).isNotNull();
    }

    @Test
    public void javaBean(){
        Event event = new Event();
        String name = "Event";
        event.setName(name);
        String description = "Spring";
        event.setDescription(description);

        assertThat(event.getName()).isEqualTo(name);
        assertThat(event.getDescription()).isEqualTo(description);
    }

    @Test
    @Parameters//(method = "paramsForTestFree")
    public void testFree(int basePrice, int maxPrice, boolean isFree){
        // Given
        Event event = Event.builder()
                .basePrice(basePrice)
                .maxPrice(maxPrice)
                .build();
        // When
        event.update();

        // Then
        assertThat(event.isFree()).isEqualTo(isFree);

//        // Given
//        event = Event.builder()
//                .basePrice(0)
//                .maxPrice(100)
//                .build();
//        // When
//        event.update();
//
//        // Then
//        assertThat(event.isFree()).isFalse();
    }

    private Object[] parametersForTestFree(){
        return new Object[]{
            new Object[] {0, 0, true},
            new Object[] {100, 0, false},
            new Object[] {100, 200, false}
        };
    }

    @Test
    @Parameters
    public void testOffline(String location, boolean isOffline){
        // Given
        Event event = Event.builder()
                .location(location)
                .build();

        // When
        event.update();

        // Then
        assertThat(event.isOffline()).isEqualTo(isOffline);
    }

    private Object[] parametersForTestOffline(){
        return new Object[]{
            new Object[] {"강남", true},
            new Object[] {null, false},
            new Object[] {"   ", false}
        };
    }
}