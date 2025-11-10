package ru.practicum.shareit.request;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.request.dto.NewItemRequestDto;

import static ru.practicum.shareit.ShareitConstants.SERVER_URL_PARAMETER_NAME;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value(SERVER_URL_PARAMETER_NAME) String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }


    public ResponseEntity<Object> create(NewItemRequestDto itemRequest, long userId) {
        return post("",userId,itemRequest);
    }

    public ResponseEntity<Object> getAllWithAnswers(long userId) {
        return get("",userId);
    }

    public ResponseEntity<Object> getAllWithoutCurrentUser(long userId) {
        return get("/all",userId);
    }

    public ResponseEntity<Object> getItemRequest(@PathVariable(name = "requestId") Long requestId) {
        return get("/"+requestId);
    }
}
