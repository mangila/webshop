package com.github.mangila.webshop.event;

import com.github.mangila.webshop.common.util.ValidatorService;
import com.github.mangila.webshop.event.model.EventCommand;
import org.springframework.stereotype.Service;

@Service
public class EventValidator {

    private final ValidatorService validatorService;

    public EventValidator(ValidatorService validatorService) {
        this.validatorService = validatorService;
    }

    public void validate(EventCommand command) {
        validatorService.validateField(command, "commandType");
    }
}
