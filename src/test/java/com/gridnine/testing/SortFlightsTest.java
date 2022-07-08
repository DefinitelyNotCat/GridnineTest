package com.gridnine.testing;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SortFlightsTest {

    @Test
    void testSortingEmptyList() {
        //given\expected
        List<Flight> flightList = new LinkedList<>();

        //test
        assertEquals(flightList, SortFlights.excludeArrivalBeforeDeparture(flightList));
        assertEquals(flightList, SortFlights.excludeDepartureBeforeNow(flightList));
        assertEquals(flightList, SortFlights.excludeOverTwoHoursGroundTime(flightList));
    }

    @Test
    void testSortingNormalOneSegmentFlights() {
        //given\expected
        List<Flight> flightList = new LinkedList<>();

        flightList.add(new Flight(Arrays.asList(new Segment(LocalDateTime.now().plusDays(1).plusHours(1), LocalDateTime.now().plusDays(1).plusHours(2)))));
        flightList.add(new Flight(Arrays.asList(new Segment(LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(4)))));
        flightList.add(new Flight(Arrays.asList(new Segment(LocalDateTime.now().plusDays(1).plusHours(1), LocalDateTime.now().plusDays(1).plusHours(1).plusSeconds(3)))));

        //test
        assertEquals(flightList, SortFlights.excludeArrivalBeforeDeparture(flightList));
        assertEquals(flightList, SortFlights.excludeDepartureBeforeNow(flightList));
        assertEquals(flightList, SortFlights.excludeOverTwoHoursGroundTime(flightList));
    }

    @Test
    void testSortingNormalMultiSegmentFlights() {
        //given
        List<Flight> flightList = new LinkedList<>();

        flightList.add(new Flight(Arrays.asList(
                new Segment(LocalDateTime.now().plusDays(1).plusHours(1), LocalDateTime.now().plusDays(1).plusHours(2)),
                new Segment(LocalDateTime.now().plusDays(1).plusHours(3), LocalDateTime.now().plusDays(1).plusHours(4))
        )));
        flightList.add(new Flight(Arrays.asList(
                new Segment(LocalDateTime.now().plusDays(3).plusDays(2), LocalDateTime.now().plusDays(3).plusDays(3)),
                new Segment(LocalDateTime.now().plusDays(5).plusDays(2), LocalDateTime.now().plusDays(5).plusDays(3))
        )));
        flightList.add(new Flight(Arrays.asList(
                new Segment(LocalDateTime.now().plusMinutes(30), LocalDateTime.now().plusHours(3)),
                new Segment(LocalDateTime.now().plusHours(4), LocalDateTime.now().plusHours(5)),
                new Segment(LocalDateTime.now().plusHours(6), LocalDateTime.now().plusHours(9))
        )));
        flightList.add(new Flight(Arrays.asList(
                new Segment(LocalDateTime.now().plusDays(1).plusHours(1), LocalDateTime.now().plusDays(1).plusHours(1).plusSeconds(3)),
                new Segment(LocalDateTime.now().plusDays(2).plusHours(1), LocalDateTime.now().plusDays(2).plusHours(1).plusSeconds(3))
        )));

        //expected
        List<Flight> expectedFlightListForBeforeNowSort = new LinkedList<>(flightList);
        expectedFlightListForBeforeNowSort.remove(3);
        expectedFlightListForBeforeNowSort.remove(1);

        //test
        assertEquals(flightList, SortFlights.excludeArrivalBeforeDeparture(flightList));
        assertEquals(flightList, SortFlights.excludeDepartureBeforeNow(flightList));
        assertEquals(expectedFlightListForBeforeNowSort, SortFlights.excludeOverTwoHoursGroundTime(flightList));
    }

    @Test
    void testSortingFlightsDepartingInThePast() {
        //given
        List<Flight> flightList = new LinkedList<>();

        flightList.add(new Flight(Arrays.asList(new Segment(LocalDateTime.now().minusDays(1).plusHours(1), LocalDateTime.now().minusDays(1).plusHours(2)))));
        flightList.add(new Flight(Arrays.asList(new Segment(LocalDateTime.now().minusDays(3).plusDays(2), LocalDateTime.now().minusDays(3).plusDays(3)))));
        flightList.add(new Flight(Arrays.asList(
                new Segment(LocalDateTime.now().minusDays(3), LocalDateTime.now().minusDays(3).plusMinutes(30)),
                new Segment(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1).plusHours(2))
        )));

        //expected
        List<Flight> expectedFlightListForBeforeNowSort = new LinkedList<>();
        List<Flight> expectedFlightListForTwoHoursSort = new LinkedList<>(flightList);

        expectedFlightListForTwoHoursSort.remove(2);

        //test
        assertEquals(flightList, SortFlights.excludeArrivalBeforeDeparture(flightList));
        assertEquals(expectedFlightListForBeforeNowSort, SortFlights.excludeDepartureBeforeNow(flightList));
        assertEquals(expectedFlightListForTwoHoursSort, SortFlights.excludeOverTwoHoursGroundTime(flightList));
    }

    @Test
    void testSortingFlightsDepartsBeforeArrival() {
        //given
        List<Flight> flightList = new LinkedList<>();

        flightList.add(new Flight(Arrays.asList(new Segment(LocalDateTime.now().minusDays(1).plusHours(3), LocalDateTime.now().minusDays(1).plusHours(2)))));
        flightList.add(new Flight(Arrays.asList(new Segment(LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(2)))));
        flightList.add(new Flight(Arrays.asList(
                new Segment(LocalDateTime.now().plusMinutes(30), LocalDateTime.now().plusHours(3)),
                new Segment(LocalDateTime.now().plusHours(5), LocalDateTime.now().plusHours(4)),
                new Segment(LocalDateTime.now().plusHours(6), LocalDateTime.now().plusHours(9))
        )));
        flightList.add(new Flight(Arrays.asList(
                new Segment(LocalDateTime.now().plusDays(3), LocalDateTime.now().plusDays(3).minusSeconds(30)),
                new Segment(LocalDateTime.now().plusDays(4), LocalDateTime.now().plusDays(4).minusHours(2))
        )));

        //expected
        List<Flight> expectedFlightListForBeforeDepartureSort = new LinkedList<>();
        List<Flight> expectedFlightListForBeforeNowSort = new LinkedList<>(flightList);
        List<Flight> expectedFlightListForTwoHoursSort = new LinkedList<>(flightList);

        expectedFlightListForBeforeNowSort.remove(0);

        expectedFlightListForTwoHoursSort.remove(3);
        expectedFlightListForTwoHoursSort.remove(2);

        //test
        assertEquals(expectedFlightListForBeforeDepartureSort, SortFlights.excludeArrivalBeforeDeparture(flightList));
        assertEquals(expectedFlightListForBeforeNowSort, SortFlights.excludeDepartureBeforeNow(flightList));
        assertEquals(expectedFlightListForTwoHoursSort, SortFlights.excludeOverTwoHoursGroundTime(flightList));
    }
}
