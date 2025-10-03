package com.especlub.match.exceptions;

public record ValidationErrorDto(String field, String message, Object rejectedValue) {}

