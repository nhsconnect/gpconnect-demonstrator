package uk.gov.hscic.auth;


import ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.servlet.http.HttpServletRequest;

import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;

/**
 * <p>This class authenticates requests by ensuring the certificate provided is
 * in the trusted jks.</p>
 */
public final class CertificateValidator {
	
	@Autowired
    private Environment env;
	
    @Value("${logCertsToConsole:false}")
    private boolean logCertsToConsole;
    
    private final String domainName = "msg.dev.spine2.ncrs.nhs.uk";    
    private final String certificateAuthority = "VNIS03_SUBCA";
    private final List<X509Certificate> storeCertificates = new ArrayList<>();

    public CertificateValidator(KeyStore keyStore) throws KeyStoreException { 
        for (String alias : Collections.list(keyStore.aliases())) {
            if (keyStore.isCertificateEntry(alias)) {
                storeCertificates.add((X509Certificate) keyStore.getCertificate(alias));
            }
        }
    }
            
    public void validateRequest(HttpServletRequest request) {
        try {        
        	if (request.isSecure() && !HttpMethod.OPTIONS.name().equals(request.getMethod()) 
        			&& (env.getProperty("clientAuth") == null || !env.getProperty("clientAuth").equals("false"))) {
                
                X509Certificate[] certificates = (X509Certificate[]) request.getAttribute("javax.servlet.request.X509Certificate");

                if (certificates == null) {
                    throw new CertificateException("No certificate found!", 496);
                }

                X509Certificate certificate = certificates[0];

                if(logCertsToConsole){
                    System.out.println("Known Certs: ");
                    for(X509Certificate knownCert : storeCertificates){
                        System.out.println(knownCert.toString());
                    }
                    System.out.println("Recieved Cert: ");
                    System.out.println(certificate.toString());
                }
                
                if (!storeCertificates.contains(certificate)) {
                    String message = getCertificateError(certificate);                   
                    
                    throw new CertificateException(message, 495);
                }               
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
                    SystemCode.BAD_REQUEST, IssueType.INVALID);
        } catch (InvalidNameException ex) {
            java.util.logging.Logger.getLogger(CertificateValidator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String getSubjectCommonName(X509Certificate x509Certificate) throws InvalidNameException {
        String value = x509Certificate.getSubjectX500Principal().getName();
        LdapName ldapDN = new LdapName(value);
        
        for(Rdn rdn: ldapDN.getRdns()) {
            if (rdn.getType().equals("CN")){
                return rdn.getValue().toString();
            }
        }
        
        return "";       
    }
    
    private String getIssuerCommonName(X509Certificate x509Certificate) throws InvalidNameException {
        String value = x509Certificate.getIssuerX500Principal().getName();
        LdapName ldapDN = new LdapName(value);
        
        for(Rdn rdn: ldapDN.getRdns()) {
            if (rdn.getType().equals("CN")){
                return rdn.getValue().toString();
            }
        }
        
        return "";       
    }
    
    private String getCertificateError(X509Certificate certificate) throws InvalidNameException {
        if (certificate.getNotAfter().before(new Date())) {
            return "Provided certificate is expired!";
        } else {
            String subjectCommonName = getSubjectCommonName(certificate);

            if (!subjectCommonName.equals(domainName)) {
                return "The certificate provided has an invalid FQDN.";
            }

            String issuerCommonName = getIssuerCommonName(certificate);

            if (!issuerCommonName.equals(certificateAuthority)) {
                return "The certificate provided has an invalid Certificate Authority.";
            }
        }

        return  "Provided certificate is not in trusted list!";
    }
}
