package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    private final Comparator<TimeOfDay> timeComparator = (t1, t2) -> {
        if (t1.getHours() != t2.getHours()) {
            return Integer.compare(t1.getHours(), t2.getHours());
        }
        return Integer.compare(t1.getMinutes(), t2.getMinutes());
    };

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        if (!timetable.containsKey(day)) {
            timetable.put(day, new TreeMap<>(timeComparator));
        }

        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);

        if (!daySchedule.containsKey(time)) {
            daySchedule.put(time, new ArrayList<>());
        }
        daySchedule.get(time).add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySessions = timetable.get(dayOfWeek);

        if (daySessions == null) {
            return Collections.emptyList();
        }

        List<TrainingSession> result = new ArrayList<>();
        for (List<TrainingSession> list : daySessions.values()) {
            result.addAll(list);
        }
        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySessions = timetable.get(dayOfWeek);

        if (daySessions == null) {
            return Collections.emptyList();
        }

        return daySessions.getOrDefault(timeOfDay, Collections.emptyList());
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> counts = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> daySchedule : timetable.values()) {
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    counts.put(coach, counts.getOrDefault(coach, 0) + 1);
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : counts.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        // Сортируем по убыванию (от большего количества тренировок к меньшему)
        result.sort((c1, c2) -> Integer.compare(c2.getCount(), c1.getCount()));

        return result;
    }
}
