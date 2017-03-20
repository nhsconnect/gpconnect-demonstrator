package uk.gov.hscic.selenium;

import io.ddavison.conductor.Browser;
import io.ddavison.conductor.Config;
import io.ddavison.conductor.Locomotive;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.Test;
import org.openqa.selenium.By;

@Config(browser = Browser.CHROME, url = "http://ec2-54-194-109-184.eu-west-1.compute.amazonaws.com/#/patients/9476719931/patients-summary")
public class SeleniumTest extends Locomotive {

    @Test
    public void seleniumTest() {
        assertThat(getText(By.xpath("(//div[contains(@class,'section-main')]//tbody//td)[2]"))).isEqualTo("Type II diabetes mellitus");
    }
}
