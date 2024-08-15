package com.haribo.notification_service.presentation.request;

import jakarta.validation.constraints.NotBlank;

public record NotificationRequest(@NotBlank String receiver,
                                  @NotBlank String add,
                                  String content,
                                  @NotBlank String typeId) { }