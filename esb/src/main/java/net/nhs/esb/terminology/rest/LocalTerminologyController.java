package net.nhs.esb.terminology.rest;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import net.nhs.esb.terminology.model.Terminology;
import org.apache.velocity.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;

/**
 */
@Controller
@Path("local")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@PropertySource("classpath:terminology.properties")
public class LocalTerminologyController {

    @Autowired
    private Environment localTerminologyResources;

    @GET
    @Path("list/{type}")
    public List<Terminology> getTerms(@PathParam("type") String type) {

        String[] keyList = StringUtils.split(localTerminologyResources.getProperty(type + ".type"), ",");

        List<Terminology> terminologyList = new ArrayList<>();

        for (String key : keyList) {
            String code = localTerminologyResources.getProperty(key + ".code");
            String text = localTerminologyResources.getProperty(key + ".text");

            terminologyList.add(new Terminology(code, text));
        }

        return terminologyList;
    }
}
