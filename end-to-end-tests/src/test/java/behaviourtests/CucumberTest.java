package behaviourtests;

import org.junit.runner.OrderWith;
import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.CucumberOptions.SnippetType;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = "summary",
        snippets = SnippetType.CAMELCASE,
        features = "src/features", // This points to the 'features' directory path relative to the project root
        glue = "behaviourtests" // This should be the package where your step definitions are located
)

public class CucumberTest {

}