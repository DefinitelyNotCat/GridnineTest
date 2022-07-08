package com.gridnine.testing;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;

public class SortFlights {

    private static final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yyyy' 'HH:mm");

    public static List<Flight> excludeDepartureBeforeNow(List<Flight> flightList) {
        LocalDateTime now = LocalDateTime.now();
        List<Flight> sortedFlightList = new LinkedList<>();

        for (Flight flight : flightList) {
            if (flight.getSegments().get(0).getDepartureDate().isBefore(now))
                continue;
            sortedFlightList.add(flight);
        }
        return sortedFlightList;
    }

    public static List<Flight> excludeArrivalBeforeDeparture(List<Flight> flightList) {
        List<Flight> sortedFlightList = new LinkedList<>();

        M1:
        for (Flight flight : flightList) {
            for (Segment segment : flight.getSegments()) {
                if (segment.getArrivalDate().isBefore(segment.getDepartureDate()))
                    continue M1;
            }
            sortedFlightList.add(flight);
        }
        return sortedFlightList;
    }

    public static List<Flight> excludeOverTwoHoursGroundTime(List<Flight> flightList) {
        List<Flight> sortedFlightList = new LinkedList<>();

        for (Flight flight : flightList) {
            List<Segment> segmentList = flight.getSegments();
            Long totalTime = 0L;

            if (segmentList.size() > 1) {
                for (int i = 0; i < segmentList.size() - 1; i++) {
                    totalTime += ChronoUnit.SECONDS.between(segmentList.get(i).getArrivalDate(), segmentList.get(i + 1).getDepartureDate());
                }
                if (totalTime / 3600 > 2)
                    continue;
            }
            sortedFlightList.add(flight);
        }
        return sortedFlightList;
    }

    public static void printFlights(List<Flight> flights) {
        int flightCounter = 0;

        for (Flight flight : flights) {
            System.out.println("\nПолет: " + flight + ".");
            int segmentCounter = 1;

            for (Segment segment : flight.getSegments()) {
                System.out.println("\tСегмент " + segmentCounter + ".\t\tДата вылета: " + segment.getDepartureDate().format(fmt)
                        + "\tДата прилета: " + segment.getArrivalDate().format(fmt));
                segmentCounter++;
            }
            flightCounter++;
        }
        System.out.println("\nВсего полетов: " + flightCounter + "\n" + dashedLine());
    }

    private static String dashedLine() {
        return "-".repeat(60) +
                System.lineSeparator();
    }

}
