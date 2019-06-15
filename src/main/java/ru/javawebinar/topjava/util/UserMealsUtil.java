package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static ru.javawebinar.topjava.util.TimeUtil.isBetween;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> sumCalForPerDay = new HashMap<>();
        LocalDate date;

        for (UserMeal element : mealList) {
            date = element.getDateTime().toLocalDate(); //получаем время каждого элемента
            sumCalForPerDay.put(date, sumCalForPerDay.getOrDefault(date, 0) + element.getCalories()); //складываем калории
        }


        List<UserMealWithExceed> resultSelUsersAtTime = new ArrayList<>();
        LocalDate dayOfDescription;
        LocalTime hourOfDescription;

        for (UserMeal meal : mealList) {
            dayOfDescription = meal.getDateTime().toLocalDate(); //получаем дату каждого элемента
            hourOfDescription = meal.getDateTime().toLocalTime(); //получаем время

            if (isBetween(hourOfDescription, startTime, endTime))//если час больше старта и час меньше конца
                resultSelUsersAtTime.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        getExcessCalories(sumCalForPerDay, dayOfDescription, caloriesPerDay))); //добавим в лист
        }
        return resultSelUsersAtTime;
    }

    private static boolean getExcessCalories(Map<LocalDate, Integer> sumCalForPerDay, LocalDate date, int calories) {
        return sumCalForPerDay.get(date) > calories;
    }
}
