package org.rippleosi.common.exception;

/**
 */
public class ConfigurationException extends RuntimeException {

    public ConfigurationException(String message) {
        super(message);
    }

    public static ConfigurationException unimplementedTransaction(Class clazz) {
        return new ConfigurationException("Unable to find a configured " + clazz.getSimpleName() + " instance");
    }
}
