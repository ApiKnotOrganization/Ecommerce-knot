package com.knot.gateway.util;

import java.util.regex.Pattern;

public class ValidationUtil {
    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        String trimmedEmail = email.trim();

        // Check for basic structure requirements
        if (!trimmedEmail.contains("@") ||
                trimmedEmail.startsWith("@") ||
                trimmedEmail.endsWith("@") ||
                trimmedEmail.indexOf("@") != trimmedEmail.lastIndexOf("@")) {
            return false;
        }

        String[] parts = trimmedEmail.split("@");
        if (parts.length != 2) {
            return false;
        }

        String localPart = parts[0];
        String domainPart = parts[1];

        // Validate local part
        if (!isValidLocalPart(localPart)) {
            return false;
        }

        // Validate domain part
        if (!isValidDomainPart(domainPart)) {
            return false;
        }

        return true;
    }

    private static boolean isValidLocalPart(String localPart) {
        if (localPart.isEmpty() ||
                localPart.startsWith(".") ||
                localPart.endsWith(".") ||
                localPart.contains("..")) {
            return false;
        }

        // Local part can only contain alphanumeric characters and ._%+-
        return localPart.matches("[a-zA-Z0-9._%+-]+");
    }

    private static boolean isValidDomainPart(String domainPart) {
        if (domainPart.isEmpty() ||
                domainPart.startsWith(".") ||
                domainPart.endsWith(".") ||
                domainPart.startsWith("-") ||
                domainPart.endsWith("-") ||
                domainPart.contains("..") ||
                !domainPart.contains(".")) {
            return false;
        }

        // Split domain into labels (parts separated by dots)
        String[] labels = domainPart.split("\\.");

        for (String label : labels) {
            if (!isValidDomainLabel(label)) {
                return false;
            }
        }

        // Check if last label (TLD) is valid
        String tld = labels[labels.length - 1];
        return tld.length() >= 2 && tld.matches("[a-zA-Z]+");
    }

    private static boolean isValidDomainLabel(String label) {
        if (label.isEmpty() ||
                label.length() > 63 || // RFC limit for domain labels
                label.startsWith("-") ||
                label.endsWith("-") ||
                label.contains("--")) {
            return false;
        }

        // Domain labels can only contain alphanumeric characters and hyphens
        return label.matches("[a-zA-Z0-9-]+");
    }
}
