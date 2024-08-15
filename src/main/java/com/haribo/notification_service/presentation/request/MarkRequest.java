package com.haribo.notification_service.presentation.request;

import jakarta.validation.constraints.NotBlank;

public record MarkRequest(@NotBlank String notiId) { }
