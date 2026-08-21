package com.wow_libre.register_discord.application.register;

import java.util.Locale;
import java.util.regex.Pattern;

public final class AccountCommandFactory {

    private static final Pattern USERNAME = Pattern.compile("^[A-Za-z0-9]{3,16}$");
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private AccountCommandFactory() {
    }

    public static void validate(String username, String password, String email) {
        if (username == null || !USERNAME.matcher(username).matches()) {
            throw new IllegalArgumentException(
                    "El usuario debe tener entre 3 y 16 caracteres alfanuméricos, sin espacios.");
        }
        if (password == null || password.length() < 6 || password.length() > 16 || password.contains(" ")) {
            throw new IllegalArgumentException(
                    "La contraseña debe tener entre 6 y 16 caracteres y no puede contener espacios.");
        }
        if (email == null || !EMAIL.matcher(email).matches()) {
            throw new IllegalArgumentException("El correo electrónico no es válido.");
        }
        if (looksLikeCommandInjection(username) || looksLikeCommandInjection(password) || looksLikeCommandInjection(email)) {
            throw new IllegalArgumentException("Los datos contienen caracteres no permitidos.");
        }
    }

    public static String createAccountCommand(String username, String password, String email) {
        return "account create %s %s %s".formatted(
                username.toLowerCase(Locale.ROOT),
                password,
                email.toLowerCase(Locale.ROOT));
    }

    private static boolean looksLikeCommandInjection(String value) {
        return value.contains("\n") || value.contains("\r") || value.contains("\t") || value.contains("\"");
    }
}
