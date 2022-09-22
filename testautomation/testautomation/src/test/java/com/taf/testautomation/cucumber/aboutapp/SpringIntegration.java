package com.taf.testautomation.cucumber.aboutapp;

import com.taf.testautomation.TestautomationApplication;
import com.taf.testautomation.uitests.AboutAppTestSuite;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {TestautomationApplication.class})
@ContextConfiguration(classes = TestautomationApplication.class)
public class SpringIntegration extends AboutAppTestSuite {

}
