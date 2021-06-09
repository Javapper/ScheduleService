package com.company.service.impl;

import com.company.dto.TaskDTO;
import com.company.mapper.TaskMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.patterns.AnyTypePattern;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.company.service.api.TelegramNotifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@Service
public class TelegramNotifierImpl implements TelegramNotifier {

    private final TaskMapper taskMapper;
    private final ModelMapper modelMapper;
    private final HttpClient client;

    private final String pathToAuthorizationService = "http://authorization-service:8092";

    @Autowired
    public TelegramNotifierImpl(TaskMapper taskMapper, ModelMapper modelMapper, HttpClient client) {
        this.taskMapper = taskMapper;
        this.modelMapper = modelMapper;
        this.client = client;
    }

    Timer timer = new Timer();

    @Override
    public ResponseEntity<AnyTypePattern> stopSendMessages() {
        log.info("Вызов метода stopSendMessages");
        timer.cancel();
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<AnyTypePattern> startSendMessages() {
        log.info("Вызов метода startSendMessages");
        long periodOfSending = 1000 * 60 * 60 * 24; //1 раз в день;
        String tasks = selectAllTaskForDay();
        timer.schedule(new SenderMessages(tasks), 0 /*getCountOfMillisecondsTo8()*/, periodOfSending);
        return ResponseEntity.ok().build();
    }

    private String selectAllTaskForDay() {
        log.info("Вызов метода selectAllTaskForDay");
        LocalDate currentDate = LocalDate.now();
        final String[] tasks = {""};
        taskMapper.showAllTasksAtDay(currentDate).stream()
                .map(taskEntity -> modelMapper.map(taskEntity, TaskDTO.class))
                .forEach(taskDTO -> tasks[0] += (taskDTO.getTask() + "\r\n"));
        log.info("Незавершённые дела из БД: " + tasks[0]);
        if (tasks[0].equals("")) {
            return "Сегодня нет никаких незавершённых дел";
        } else {
            return "Ваши задания на сегодня: \r\n" + tasks[0];
        }
    }

    private long getCountOfMillisecondsTo8() {
        log.info("Рассчёт количества миллисекунд до 8 утра");
        Calendar calendar = Calendar.getInstance();

        if (calendar.get(Calendar.HOUR_OF_DAY) > 8) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTimeInMillis() - System.currentTimeMillis();
    }

    public boolean isAllowedRequest(String token) throws IOException, InterruptedException {
        log.info("Запрос на сервер авторизации для проверки токена");
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(token))
                .timeout(Duration.ofSeconds(5))
                .uri(URI.create(pathToAuthorizationService + "/tokens/check-token"))
                .build();
        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());
        log.info("Получен результат от сервера аторизации: " + response.statusCode());
        return response.statusCode() == 200;
    }
}

@Slf4j
class SenderMessages extends TimerTask {

    private static final String CHAT_ID = "1030875369";
    private static final String TOKEN = "1741029277:AAEfLwNAPnweQsjd6I0AHBL-DLJmvOk1YCg";

    String message;

    public SenderMessages(String tasks) {
        this.message = tasks;
    }

    @Override
    public void run() {

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .version(HttpClient.Version.HTTP_2)
                .build();
        log.info("Создание HttpClient: " + client.toString());

        UriBuilder builder = UriBuilder
                .fromUri("https://api.telegram.org")
                .path("/{token}/sendMessage")
                .queryParam("chat_id", CHAT_ID)
                .queryParam("text", message);
        log.info("Создание UriBuilder: " + builder.toString());

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(builder.build("bot" + TOKEN))
                .timeout(Duration.ofSeconds(5))
                .build();
        log.info("Создание HttpRequest: " + request.toString());

        try {
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
