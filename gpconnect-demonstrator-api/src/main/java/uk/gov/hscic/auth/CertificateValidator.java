package uk.gov.hscic.auth;

import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.http.HttpMethod;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;

/**
 * <p>This class authenticates requests by ensuring the certificate provided is
 * in the trusted jks.</p>
 */
public final class CertificateValidator {
    private static final Logger LOG = Logger.getLogger(CertificateValidator.class);

    private final List<X509Certificate> knownCerts = new ArrayList<>();

    public CertificateValidator(KeyStore keyStore) throws KeyStoreException {
        for (String alias : Collections.list(keyStore.aliases())) {
            if (keyStore.isCertificateEntry(alias)) {
                knownCerts.add((X509Certificate) keyStore.getCertificate(alias));
            }
        }
    }

    public void validateRequest(HttpServletRequest request) {
        try {
            
            if (request.isSecure() && !HttpMethod.OPTIONS.name().equals(request.getMethod())) {
                
                X509Certificate[] certs = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");
                
                for(X509Certificate cert : knownCerts){
                    LOG.info("Cert in certificate store: " + cert.toString());
                }
                if (null != certs){
                    for(X509Certificate cert : certs){
                        LOG.info("Client certificate: " + cert.toString());
                    }
                }

//                if (null == certs) {
//                    throw new CertificateException("No certificate found!", 496);
//                }
//
//                X509Certificate x509Certificate = certs[0];
//
//                if (!knownCerts.contains(x509Certificate)) {
//                    throw new CertificateException("Provided certificate is not in trusted list!", 495);
//                } else { // Otherwise, check the expiry
//                    knownCerts.stream()
//                            .filter(x509Certificate::equals)
//                            .peek(cert -> LOG.info("Certificate valid until: " + cert.getNotAfter()))
//                            .filter(cert -> new Date().before(cert.getNotAfter()))
//                            .findAny()
//                            .orElseThrow(() -> new CertificateException("Provided certificate has expired!", 495));
//                }
            }
        } catch (CertificateException certificateException) {
            StringBuilder requestURL = new StringBuilder(request.getRequestURL());
            String queryString = request.getQueryString();

            if (null != queryString) {
                requestURL.append('?').append(queryString);
            }

            String warningMsg = "Bad signature detected for " + request.getMethod() + " to " + requestURL + ": " + certificateException.getMessage();

            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnclassifiedServerFailureException(certificateException.getStatusCode(), warningMsg),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.FORBIDDEN);
        }
    }
}
