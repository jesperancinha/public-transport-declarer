package com.steelzack.biscaje.game.objects;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

/**
 * Created by joaofilipesabinoesperancinha on 24-04-16.
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        format = { "pretty", "html:target/cucumber" },
        features = "classpath:cucumber/card.feature"
)
public class CardImplTest {
}