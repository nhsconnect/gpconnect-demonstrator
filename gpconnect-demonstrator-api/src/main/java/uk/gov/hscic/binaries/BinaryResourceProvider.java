package uk.gov.hscic.binaries;

import ca.uhn.fhir.rest.annotation.*;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import org.hl7.fhir.dstu3.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;

@Component
public class BinaryResourceProvider implements IResourceProvider {

    @Value("${config.path}")
    private String configPath;

    @Override
    public Class<Binary> getResourceType() {
        return Binary.class;
    }

    @Read(version = true)
    public Binary getBinaryById(@IdParam IdType binaryId) throws IOException {
        String id = binaryId.getIdPart();
        if (id.equals("1")) {
            Binary binary = new Binary();
            binary.setId(id);
            binary.setContentType("application/msword");
            byte[] content = Files.readAllBytes(new File(configPath + "/responses/empty.docx").toPath());
            binary.setContent(content);
            return binary;
        } else {
            // return a 404
            String msg = String.format("No document found for ID: %s", id);
            throw OperationOutcomeFactory.buildOperationOutcomeException(new ResourceNotFoundException(msg),
                    SystemCode.REFERENCE_NOT_FOUND, OperationOutcome.IssueType.INVALID);
        }
    }
}
