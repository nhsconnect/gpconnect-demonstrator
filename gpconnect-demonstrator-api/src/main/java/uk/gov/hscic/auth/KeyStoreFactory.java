package uk.gov.hscic.auth;

import java.io.InputStream;
import java.security.KeyStore;
import org.springframework.core.io.FileSystemResource;

/**
 *
 * @author Kris
 */
public final class KeyStoreFactory {

    private KeyStoreFactory() { }

    public static KeyStore getKeyStore(String path, String keystorePassword) throws Exception {
        try (InputStream is = new FileSystemResource(path + ".jks").getInputStream()) {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(is, keystorePassword.toCharArray());
            return keyStore;
        }
    }
}
