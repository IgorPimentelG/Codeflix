package com.fullcycle.admin.catalog.domain.errors;

import com.fullcycle.admin.catalog.domain.validation.handler.Notification;
import lombok.Getter;

@Getter
public class NotificationException extends DomainException {

    public NotificationException(final Notification notification) {
        super("", notification.getErrors());
    }
}
