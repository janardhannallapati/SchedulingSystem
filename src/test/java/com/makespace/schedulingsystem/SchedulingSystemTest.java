package com.makespace.schedulingsystem;

import com.makespace.schedulingsystem.service.SchedulingSystem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class SchedulingSystemTest {
    public final static String INCORRECT_INPUT ="INCORRECT_INPUT";
    public final static String NO_VACANT_ROOM = "NO_VACANT_ROOM";
    public final static String CCAVE = "C-Cave";
    public final static String DTOWER = "D-Tower";
    public final static String GMANSION = "G-Mansion";

    @Test
    void bookMeetingRoom_should_not_return_meetingroom_with_no_start_or_end_times(){
        SchedulingSystem system = new SchedulingSystem();
        String name=system.bookMeetingRoom("12:15","",10 );
        assertThat(name).isEqualTo(INCORRECT_INPUT);
    }

    @Test
    void bookMeetingRoom_should_book_room_only_when_action_is_book(){
        SchedulingSystem system = new SchedulingSystem();
        String name=system.bookMeetingRoom("14:00" , "15:00",10 );
        assertThat(name).isNotEqualTo(INCORRECT_INPUT);
    }

    @DisplayName("Should validate that times are in 24 hrs format, end time is greater than start time and start and end times are in 15 minute intervals")
    @ParameterizedTest(name = "{index} => startTime={0}, endTime={1}")
    @MethodSource("bookMeetingRoomOnlyWhenTimesAreIn24hrFormatProvider")
    void bookMeetingRoomOnlyWhenTimesAreIn24hrFormat(String startTime, String endTime){
        SchedulingSystem system = new SchedulingSystem();
        String name=system.bookMeetingRoom(startTime, endTime,12 );
        assertThat(name).isEqualTo(INCORRECT_INPUT);

    }

    private static Stream<Arguments> bookMeetingRoomOnlyWhenTimesAreIn24hrFormatProvider() {
        return Stream.of(
                Arguments.of( "32:15", "12:00"),
                Arguments.of( "09:15", "e2:00"),
                Arguments.of( "119:15", "12:00"),
                Arguments.of( "09:15", "112:00"),
                Arguments.of( "09:65", "12:00"),
                Arguments.of( "09:5", "02:00"),
                Arguments.of( "09:50", "2:00"),
                Arguments.of( "09:-0", "2:00"),
                Arguments.of("09:00","08:45"),
                Arguments.of("13:00","12:45"),
                Arguments.of("14:01","14:16")
        );
    }


    @DisplayName("Should show NO_VACANT_ROOM if booking with a overlapping buffer time")
    @ParameterizedTest(name = "{index} => startTime={0}, endTime={1}")
    @MethodSource("bookMeetingRoomFailsWhenTimeOverlapsBufferTimeProvider")
    void bookMeetingRoomFailsWhenTimeOverlapsBufferTime(String startTime, String endTime){
        SchedulingSystem system = new SchedulingSystem();
        String name=system.bookMeetingRoom(startTime, endTime,15 );
        assertThat(name).isEqualTo(NO_VACANT_ROOM);
    }

    private static Stream<Arguments> bookMeetingRoomFailsWhenTimeOverlapsBufferTimeProvider(){
        return Stream.of (
          Arguments.of("09:00","09:15"),
          Arguments.of("13:15","13:45"),
          Arguments.of("18:45","19:00"),
          Arguments.of("09:00","09:45"),
          Arguments.of("12:00","16:00")
        );
    }

    @Test
    void bookMeetingRoomFailsIfNotBookedAsPerCapacityLimit(){
        SchedulingSystem system = new SchedulingSystem();
        String name=system.bookMeetingRoom("12:00", "13:00",25);
        assertThat(name).isEqualTo(NO_VACANT_ROOM);
        name=system.bookMeetingRoom("12:00", "13:00",1);
        assertThat(name).isEqualTo(NO_VACANT_ROOM);
    }

    @DisplayName("should book a meeting room as per capacity and availability")
    @ParameterizedTest(name = "{index} => startTime={0}, endTime={1}")
    @MethodSource("bookMeetingRoomShouldReturnRoomAsPerCapacityAndAvailabilityProvider")
    void bookMeetingRoomShouldReturnRoomAsPerCapacityAndAvailability(String startTime, String endTime, int attendeeCount, String expectedMsg, SchedulingSystem system){
        String name=system.bookMeetingRoom( startTime, endTime, attendeeCount);
        assertThat(name).isEqualTo(expectedMsg);
    }

    private static Stream<Arguments> bookMeetingRoomShouldReturnRoomAsPerCapacityAndAvailabilityProvider(){
        SchedulingSystem system = new SchedulingSystem();
        return Stream.of(
               Arguments.of("11:00", "11:45", 2, CCAVE, system),
               Arguments.of("11:30", "13:00", 35, NO_VACANT_ROOM, system),
               Arguments.of("11:30", "13:00", 15, GMANSION, system),
               Arguments.of("14:00", "15:30", 3, CCAVE, system),
               Arguments.of("15:00", "16:30", 2, DTOWER, system),
               Arguments.of("15:15", "12:15", 12, INCORRECT_INPUT, system),
               Arguments.of("15:30", "16:30", 2, CCAVE, system),
               Arguments.of("16:00", "17:00", 5, GMANSION, system),
               Arguments.of("18:00", "19:00", 3, NO_VACANT_ROOM, system)
        );

    }

    @Test
    void vacancyReturnsAvailableRooms(){
        SchedulingSystem system = new SchedulingSystem();
        //String name=system.bookMeetingRoom("12:00", "13:00",25);
        String rooms = system.vacancy("10:00", "12:00");
        assertThat(rooms).isEqualTo("C-Cave D-Tower G-Mansion");
        system.bookMeetingRoom("10:00","12:00", 20);
        rooms = system.vacancy("10:00", "12:00");
        assertThat(rooms).isEqualTo("C-Cave D-Tower");
        system.bookMeetingRoom("10:00","12:00", 2);
        rooms = system.vacancy("10:00", "12:00");
        assertThat(rooms).isEqualTo("D-Tower");
        system.bookMeetingRoom("10:00","12:00", 7);
        rooms = system.vacancy("10:00", "12:00");
        assertThat(rooms).isEqualTo(NO_VACANT_ROOM);
    }
}