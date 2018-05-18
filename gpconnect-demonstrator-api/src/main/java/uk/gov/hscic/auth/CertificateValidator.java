package uk.gov.hscic.auth;

import ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXParameters;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;

/**
 * <p>
 * This class authenticates requests by ensuring the certificate provided is in
 * the trusted jks.</p>
 */
public final class CertificateValidator {

    @Autowired
    private Environment env;

    @Value("${logCertsToConsole:false}")
    private boolean logCertsToConsole;

    private final KeyStore keyStore;

    private final List<X509Certificate> storeCertificates = new ArrayList<>();

    public CertificateValidator(KeyStore p_keyStore) throws KeyStoreException {
        keyStore = p_keyStore;
        for (String alias : Collections.list(p_keyStore.aliases())) {
            if (p_keyStore.isCertificateEntry(alias)) {
                storeCertificates.add((X509Certificate) p_keyStore.getCertificate(alias));
            }
        }
    }

    public void validateRequest(HttpServletRequest request) {
        try {
            // Check that we have asked for a client certificate and that this is not an OPTIONS call which indicates a CORS request, some browsers don't send client cert on CORS request.
            if (request.isSecure() && !HttpMethod.OPTIONS.name().equals(request.getMethod())
                    && (env.getProperty("clientAuth") == null || !env.getProperty("clientAuth").equals("false"))) {

                X509Certificate[] certificates = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");

                // Check a client certificate was recieved as part of the connection
                if (null == certificates) {
                    throw new CertificateException("No certificate found!", 496);
                }

                // Output certificate if advanced logging is requested
                if (logCertsToConsole) {
                    System.out.println("Recieved certs");
                    for (X509Certificate recievedCerts : certificates) {
                        System.out.println(recievedCerts.toString());
                    }
                }

                // Use the default certificate checks to check that the certificate date is valid
                try {
                    for (X509Certificate certificate : certificates) {
                        certificate.checkValidity();
                    }
                } catch (Exception ex) {
                    throw new CertificateException("Certificate date is not valid", 495);
                }

                String certErrorMessage = null;
                //Check certificate chain
                try {
                    CertificateFactory certFactory = CertificateFactory.getInstance("X.509");

                    List<X509Certificate> sentCerts = new ArrayList<X509Certificate>();
                    for (X509Certificate cert : certificates) {
                        sentCerts.add(cert);
                    }

                    CertPath cp = certFactory.generateCertPath(sentCerts);
                    PKIXParameters params = new PKIXParameters(keyStore);
                    params.setRevocationEnabled(true);
                    CertPathValidator cpv = CertPathValidator.getInstance(CertPathValidator.getDefaultType());
                    PKIXCertPathValidatorResult pkixCertPathValidatorResult = (PKIXCertPathValidatorResult) cpv.validate(cp, params);
                } catch (Exception ex) {
                    certErrorMessage = ex.getMessage();
                    if (logCertsToConsole) {
                        System.out.println("Error with certificate chain validation: " + certErrorMessage);
                    }
                }

                // If the certificate chain validation fails check if any cert sent is in the keystore as a last resort
                if (null != certErrorMessage) {
                    for (X509Certificate cert : certificates) {
                        if (storeCertificates.contains(cert)) {
                            certErrorMessage = null;
                        }
                    }
                }
                
                // If the cert was not found return the error
                if(null != certErrorMessage){
                    throw new CertificateException(certErrorMessage, 495);
                }

            }
        } catch (CertificateException certificateException) {
            StringBuilder requestURL = new StringBuilder(request.getRequestURL());
            String queryString = request.getQueryString();

            if (null != queryString) {
                requestURL.append('?').append(queryString);
            }

            throw new UnclassifiedServerFailureException(certificateException.getStatusCode(),
                    "Bad signature detected for " + request.getMethod() + " to " + requestURL + ": " + certificateException.getMessage());
        }
    }
}
