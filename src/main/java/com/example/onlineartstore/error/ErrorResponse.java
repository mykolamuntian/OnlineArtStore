package com.example.onlineartstore.error;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ErrorResponse {
    private final String message;
    private final String status = "error";
}
