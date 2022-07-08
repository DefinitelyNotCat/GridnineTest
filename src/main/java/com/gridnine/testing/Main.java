package com.gridnine.testing;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Flight> flightList = FlightBuilder.createFlights();

        System.out.println("Все полеты: ");
        SortFlights.printFlights(flightList);

        System.out.println("Исключаем полеты с вылетом до текущего момента времени: ");
        SortFlights.printFlights(SortFlights.excludeDepartureBeforeNow(flightList));

        System.out.println("Исключаем полеты с сегментами, где дата прилета раньше даты вылета:");
        SortFlights.printFlights(SortFlights.excludeArrivalBeforeDeparture(flightList));

        System.out.println("Исключаем полеты, где общее время, проведенное на земле, превышает два часа:");
        SortFlights.printFlights(SortFlights.excludeOverTwoHoursGroundTime(flightList));

    }
}
